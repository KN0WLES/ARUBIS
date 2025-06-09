package unicorn.controller;

import unicorn.model.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.interfaces.*;
import unicorn.util.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.io.File;

/**
 * Clase que actúa como controlador para la gestión de cuentas de usuario.
 * Proporciona la lógica de negocio necesaria para registrar, iniciar sesión, actualizar, eliminar cuentas
 * y gestionar privilegios de administrador.
 * Los datos de las cuentas se almacenan y recuperan desde archivos separados por tipo de cuenta.

 * &#064;description  Funcionalidades principales:
 *                  - Registrar nuevas cuentas de usuario.
 *                  - Iniciar sesión con credenciales de usuario.
 *                  - Actualizar información de las cuentas (nombre, apellido, teléfono, correo electrónico, contraseña).
 *                  - Eliminar cuentas de usuario.
 *                  - Gestionar privilegios de administrador.
 *                  - Recuperar cuentas por nombre de usuario, correo electrónico o ID.
 *                  - Listar todas las cuentas registradas.
 *
 * @author KNOWLES
 * @version 1.1
 * @since 2025-05-28
 * @see IAccount
 * @see Account
 * @see IFile
 * @see FileException
 * @see AccountException
 */
public class AccountController implements IAccount {
    
    private final IFile<Account> fileHandler;
    private final IFile<Substitute> substituteFile;
    private final Map<TipoCuenta, String> filePaths;
    private Map<String, Account> accounts;

    public AccountController(IFile<Account> fileHandler, IFile<Substitute> substituteFile) throws AccountException {
        this.fileHandler = fileHandler;
        this.substituteFile = substituteFile;
        this.filePaths = new HashMap<>();
        // Define los nombres de archivo para cada tipo de cuenta 
        this.filePaths.put(TipoCuenta.ADMIN,      "src/main/java/unicorn/dto/accounts_admin.txt");
        this.filePaths.put(TipoCuenta.ESTUDIANTE, "src/main/java/unicorn/dto/accounts_estudiante.txt");
        this.filePaths.put(TipoCuenta.PROFESOR,   "src/main/java/unicorn/dto/accounts_profesor.txt");

        this.accounts = new HashMap<>(); // Inicializamos el HashMap
        try {
            // Asegurarse de que todos los archivos existan y cargar datos de cada uno
            for (Map.Entry<TipoCuenta, String> entry : filePaths.entrySet()) {
                fileHandler.createFileIfNotExists(entry.getValue());
                // Cargamos la lista y la convertimos a un mapa usando el ID como clave
                List<Account> loadedAccounts = fileHandler.loadData(entry.getValue());
                for (Account acc : loadedAccounts) {
                    this.accounts.put(acc.getId(), acc);
                }
            }
            initializeDefaultAdmin();
            initializeDefaultPrf();
        } catch (FileException e) {
            this.accounts = new HashMap<>(); // En caso de error, inicializamos un mapa vacío
            System.err.println("Error al cargar cuentas, iniciando con lista vacía: " + e.getMessage());
        }
    }

    public  void saveChanges() throws AccountException {
        Map<TipoCuenta, List<Account>> accountsByType = accounts.values().stream() // Obtenemos la colección de cuentas
                .collect(Collectors.groupingBy(Account::getTipoCuenta));

        for (Map.Entry<TipoCuenta, String> entry : filePaths.entrySet()) {
            TipoCuenta type = entry.getKey();
            String path = entry.getValue();
            List<Account> accountsToSave = accountsByType.getOrDefault(type, new ArrayList<>());
            try {
                fileHandler.saveData(accountsToSave, path);
            } catch (FileException e) {
                throw new AccountException("Error al guardar los cambios para el tipo de cuenta " + type + ": " + e.getMessage());
            }
        }
    }

    @Override
    public void registerAccount(Account account) throws AccountException {
        // Verificamos si el nombre de usuario o el email ya existen
        if (accounts.values().stream().anyMatch(a -> a.getUser().equals(account.getUser()))) { //
            throw AccountException.duplicateUser(); //
        }

        if (accounts.values().stream().anyMatch(a -> a.getEmail().equals(account.getEmail()))) { //
            throw new AccountException("El correo electrónico ya está registrado"); //
        }

        accounts.put(account.getId(), account); // Agregamos la cuenta al HashMap usando su ID
        saveChanges(); //

        // Crear archivo de horarios para el usuario
        createUserScheduleFile(account); //
    }

    private void createUserScheduleFile(Account account) throws AccountException {
        String baseDir = "src/main/java/unicorn/dto/schedules/";
        String subDir;

        // Determinar la subcarpeta según el tipo de cuenta
        switch (account.getTipoCuenta()) {
            case PROFESOR:
                subDir = "profesor/";
                break;
            case ESTUDIANTE:
                subDir = "estudiante/";
                break;
            case ADMIN:
                // Los administradores no necesitan archivo de horario
                return;
            default:
                throw new AccountException("Tipo de cuenta no válido para crear horario");
        }

        String filePath = baseDir + subDir + account.getUser() + "_schedule.txt";

        try {
            // Crear el directorio si no existe (incluyendo subdirectorios)
            File directory = new File(baseDir + subDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Crear el archivo de horario
            fileHandler.createFileIfNotExists(filePath);
        } catch (Exception e) {
            throw new AccountException("Error al crear archivo de horarios para el usuario: " + e.getMessage());
        }
    }

    @Override
    public Account login(String username, String password) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw new AccountException("Usuario no encontrado");
        
        if (!account.verifyPassword(password)) throw new AccountException("Contraseña incorrecta");
        
        return account;
    }

    @Override
    public void updateName(String username, String newName) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        account.setNombre(newName);
        saveChanges();
    }

    @Override
    public void updateLast(String username, String newLast) throws AccountException {
        if (username == null || username.trim().isEmpty()) {
            throw new AccountException("El nombre de usuario no puede estar vacío");
        }
        if (newLast == null || newLast.trim().isEmpty()) {
            throw new AccountException("El nuevo apellido no puede estar vacío");
        }
        
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        account.setApellido(newLast.trim());
        saveChanges();
    }

    @Override
    public void updatePhone(String username, String newPhone) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        account.setPhone(newPhone);
        saveChanges();
    }

    @Override
    public void updateEmail(String username, String newEmail) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        if (accounts.values().stream() //
                .filter(a -> !a.getUser().equals(username)) //
                .anyMatch(a -> a.getEmail().equals(newEmail))) { //
            throw new AccountException("El correo electrónico ya está registrado"); //
        }
        
        account.setEmail(newEmail);
        saveChanges();
    }

    @Override
    public void updatePassword(String username, String currentPassword, String newPassword) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        if (!account.verifyPassword(currentPassword)) {
            throw new AccountException("La contraseña actual es incorrecta");
        }
        
        account.setPassword(newPassword);
        saveChanges();
    }

    public void resetUserPassword(String username, String newPassword) throws AccountException {
        Account account = getByUsername(username);

        if (account == null) throw AccountException.userNotFound();

        account.setPassword(newPassword);
        saveChanges();
    }

    public void promoteToAccount(String username, String substituteId, LocalDate startDate, LocalDate endDate) throws AccountException, SubstituteException {
        Account account = getByUsername(username);
        if (account == null) throw AccountException.userNotFound();

        if (!account.isProfesor()) {
            throw new AccountException("Solo los profesores pueden ser promovidos a administrador");
        }

        // Crear la sustitución
        Substitute substitution = account.promoteToAdmin(substituteId, startDate, endDate);

        // Guardar cambios en la cuenta
        saveChanges();

        // Guardar la sustitución
        SubstituteController subController = new SubstituteController(substituteFile);
        subController.createSubstitute(substitution);

        // Eliminar archivo de horario si existe
        deleteUserScheduleFile(account);
    }

    //@Override
    public void revertToProfessor(String username) throws AccountException, SubstituteException {
        Account account = getByUsername(username);
        if (account == null) throw AccountException.userNotFound();

        if (!account.isAdmin() || !account.hasSubstitute()) {
            throw new AccountException("Solo administradores que fueron profesores pueden ser revertidos");
        }

        // Revertir a profesor
        boolean reverted = account.revertToProfessor();
        if (!reverted) {
            throw new AccountException("No se pudo revertir la cuenta a profesor");
        }

        // Finalizar la sustitución
        SubstituteController subController = new SubstituteController(substituteFile);
        List<Substitute> activeSubstitutes = subController.getActiveSubstitutes();

        for (Substitute s : activeSubstitutes) {
            if (s.getOriginalTeacherId().equals(account.getId())) {
                subController.endSubstitution(s.getId());
                break;
            }
        }

        // Crear archivo de horario nuevamente
        createUserScheduleFile(account);
        saveChanges();
    }

    private void deleteUserScheduleFile(Account account) {
        String filePath = "src/main/java/unicorn/dto/schedules/profesor/" + account.getUser() + "_schedule.txt";
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar archivo de horario: " + e.getMessage());
        }
    }

    @Override
    public Account getByUsername(String username) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getUser().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account getByEmail(String email) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account getById(String id) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void deleteAccount(String username) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        long adminCount = accounts.values().stream().filter(a -> a.getTipoCuenta() == TipoCuenta.ADMIN).count(); //
        if (account.getTipoCuenta() == TipoCuenta.ADMIN && adminCount <= 1) { //
            throw AccountException.adminRestriction(); // No se puede eliminar el último admin
        }
        
        accounts.remove(account.getId());
        saveChanges();
    }

    private void initializeDefaultAdmin() throws AccountException {
        // Verificar si ya existe un administrador por defecto en la lista total de cuentas
        boolean adminExists = accounts.values().stream().anyMatch(a -> a.getUser().equals("admin") && a.getTipoCuenta() == TipoCuenta.ADMIN); //

        if (!adminExists) { //
            try {
                Account defaultAdmin = new Account("admin", "admin", "00000000", "admin@adm.umss.edu", "admin", "Hola1234", TipoCuenta.ADMIN);
                this.accounts.put(defaultAdmin.getId(), defaultAdmin); // Agregamos al HashMap
                saveChanges(); // Guardar el nuevo admin en su archivo correspondiente
            } catch (IllegalArgumentException e) {
                throw new AccountException("Error creando cuenta admin por defecto: " + e.getMessage()); //
            }
        }
    }

    private void initializeDefaultPrf() throws AccountException {
        //Verifica si ya existen profesores y de ser asi no hacer nada
        boolean profExist = accounts.values().stream()
                .anyMatch(a -> a.getTipoCuenta() == TipoCuenta.PROFESOR);
        if (profExist) return;

        // Datos de profesores por defecto
        String[][] profesoresData = {
            {"Samuel",            "",         "",        "Acha",         "Perez"},
            {"Luis"  ,     "Roberto",         "",      "Agreda",      "Corrales"},
            {"Marcelo",           "",         "",    "Antezana",       "Camacho"},
            {"Tatiana",           "",         "",    "Aparicio",          "Yuja"},
            {"Ligia",   "Jacqueline",         "",    "Aranibar",     "La Fuente"},
            {"Jose",       "Richard",         "",      "Ayoroa",       "Cardozo"},
            {"Leticia",           "",         "",      "Blanco",          "Coca"},
            {"Alex",       "Isrrael",         "",   "Bustillos",        "Vargas"},
            {"Boris",             "",         "",    "Calancha",         "Navia"},
            {"Indira",            "",         "",     "Camacho",  "Del Castillo"},
            {"Cecilia",    "Beatriz",         "",      "Castro",       "Lazarte"},
            {"Raul",              "",         "",      "Catari",          "Rios"},
            {"Maria",       "Benita",         "",    "Cespedes",       "Guizada"},
            {"Alex",     "Danchgelo",         "",      "Choque",        "Flores"},
            {"Carlos",      "Javier",  "Alfredo",       "Cosio",   "Papadopolis"},
            {"Vladimir",      "Abel",         "",      "Costas",      "Jauregui"},
            {"Grover",    "Humberto",         "",       "Cussi",       "Nicolas"},
            {"Jorge",             "",         "",     "Davalos",      "Brozovic"},
            {"David",      "Alfredo",         "",  "Delgadillo",        "Cossio"},
            {"David",             "",         "",    "Escalera",     "Fernandez"},
            {"David",             "",         "",   "Fernandez",         "Ramos"},
            {"Americo",           "",         "",     "Fiorilo",        "Lozada"},
            {"Freddy",            "",         "",      "Flores",        "Flores"},
            {"Hernan",            "",         "",      "Flores",        "Garcia"},
            {"Juan",       "Marcelo",         "",      "Flores",         "Soliz"},
            {"Corina",            "",         "",      "Flores",    "Villarroel"},
            {"Ivan",              "",         "",     "Fuentes",       "Miranda"},
            {"Juan",         "Ruben",         "",      "Garcia",        "Molina"},
            {"Carmen",        "Rosa",         "",      "Garcia",         "Perez"},
            {"Maria",       "Estela",         "",       "Grilo",   "Salvatierra"},
            {"Osvaldo",     "Walter",         "",   "Gutierrez",       "Andrade"},
            {"Gonzalo",    "Enrique",  "Antonio",      "Guzman",      "Orellana"},
            {"Rocio",             "",         "",      "Guzman",      "Saavedra"},
            {"K.",         "Rolando",         "",      "Jaldin",       "Rosales"},
            {"Demetrio",          "",         "",     "Juchani",      "Bazualdo"},
            {"Valentin",          "",         "",       "Laime",        "Zapata"},
            {"Gualberto",         "",         "",        "Leon",        "Romero"},
            {"Tito",        "Anibal",         "",        "Lima",      "Vacaflor"},
            {"Roberto",       "Juan",         "",    "Manchego",     "Castellon"},
            {"Carlos",          "B.",         "",      "Manzur",         "Soria"},
            {"Vidal",             "",         "",      "Matias",         "Marca"},
            {"Julio",             "",         "",      "Medina",        "Gamboa"},
            {"Victor",      "Ramiro",         "",       "Mejia",      "Urquieta"},
            {"Victor",        "Hugo",         "",     "Montano",       "Quiroga"},
            {"Marco",      "Antonio",         "",  "Montecinos",        "Choque"},
            {"Yony",       "Richard",         "",     "Montoya",        "Burgos"},
            {"Rene",              "",         "",     "Moreira",      "Calizaya"},
            {"Jose",           "Gil",         "",      "Omonte",        "Ojalvo"},
            {"Jose",       "Roberto",         "",      "Omonte",        "Ojalvo"},
            {"Miguel",       "Angel",         "",     "Ordonez",   "Salvatierra"},
            {"Jorge",       "Walter",         "",    "Orellana",         "Araoz"},
            {"Ronald",       "Edgar",         "",      "Patino",          "Tito"},
            {"Magda",         "Lena",         "",     "Peeters",        "Ilonaa"},
            {"Alfredo",           "",         "",     "Pericon",    "Balderrama"},
            {"Abdon",             "",         "",      "Quiroz",        "Chavez"},
            {"Santiago",          "",         "",       "Relos",          "Paco"},
            {"Luz",           "Maya",         "",     "Revollo",         "Teran"},
            {"Erika",     "Patricia",         "",   "Rodriguez",        "Bilbao"},
            {"Juan",       "Antonio",         "",   "Rodriguez",         "Sejas"},
            {"Ramiro",            "",         "",       "Rojas",        "Zurita"},
            {"Patricia",          "",         "",      "Romero",     "Rodriguez"},
            {"Carla",             "",         "",     "Salazar",       "Serrudo"},
            {"Ariel",      "Antonio",         "",   "Sarmiento",        "Franco"},
            {"Galina",            "",         "",    "Shitikov",      "Gagarina"},
            {"Maria",        "Ritha",   "Carola",       "Siles",       "Marzana"},
            {"Jose",       "Antonio",         "",      "Soruco",         "Maita"},
            {"Fidel",             "",         "",     "Taborga",          "Acha"},
            {"Darlong",     "Howard",         "",      "Taylor",      "Terrazas"},
            {"Juan",              "",         "",    "Terrazas",          "Lobo"},
            {"Juan",        "Carlos",         "",    "Terrazas",        "Vargas"},
            {"Rosemary",          "",         "",     "Torrico",       "Bascope"},
            {"Felix",             "",         "",      "Ugarte",         "Cejas"},
            {"Hernan",            "",         "",     "Ustariz",        "Vargas"},
            {"Tania",       "Andrea",         "",      "Valero",        "Malele"},
            {"Marco",      "Antonio",         "",     "Vallejo",       "Camacho"},
            {"Ademar",     "Marcelo",         "",      "Vargas",      "Antezana"},
            {"Emir",         "Felix",         "",      "Vargas",        "Peredo"},
            {"Michael",    "Huascar",         "",     "Vasquez",      "Carrillo"},
            {"Gustavo",     "Adolfo",         "",     "Veizaga",              ""},
            {"Jimmy",             "",         "",  "Villarroel",       "Novillo"},
            {"Henry",        "Frank",         "",  "Villarroel",         "Tapia"},
            {"Oscar",           "A.",         "",    "Zabalaga",       "Montano"},
            {"Jhomil",      "Efrain",         "",    "Zambrana",        "Burgos"}
            //{"Por",               "",         "",    "Designar",              ""}
        };

        for (String[] profesor : profesoresData) {
            String nombre1 = profesor[0];
            String nombre2 = profesor[1];
            String nombre3 = profesor[2];
            String apellido1 = profesor[3];
            String apellido2 = profesor[4];

            // Construir nombre completo
            String nombreCompleto = Stream.of(nombre1, nombre2, nombre3)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" "));

            // Construir apellidos
            String apellidos = Stream.of(apellido1, apellido2)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(" "));

            // Generar username y email según nuevo formato
            String username = generateProfessorUsername(nombre1, nombre2, apellido1, apellido2);
            String email = generateProfessorEmail(nombre1, nombre2, apellido1, apellido2);

            if (!accounts.values().stream().anyMatch(a -> a.getUser().equals(username))) {
                try {
                    Account profesorAccount = new Account(
                            nombreCompleto,
                            apellidos,
                            "00000000",
                            email,
                            username,
                            "Hola1234", // Cumple con 1 mayúscula, 1 minúscula, 1 número
                            TipoCuenta.PROFESOR
                    );
                    this.accounts.put(profesorAccount.getId(), profesorAccount);
                    createUserScheduleFile(profesorAccount);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error creando cuenta profesor: " + e.getMessage());
                }
            }
        }
        saveChanges();
    }

    private String generateProfessorUsername(String nombre1, String nombre2, String apellido1, String apellido2) {
        StringBuilder sb = new StringBuilder();

        // Primera letra del primer nombre
        sb.append(nombre1.toLowerCase().charAt(0));

        // Si tiene segundo nombre, agregar primera letra
        if (!nombre2.isEmpty()) {
            sb.append(nombre2.toLowerCase().charAt(0));
        }

        // Apellido paterno completo en minúsculas, sin espacios ni caracteres especiales
        String apellido = apellido1.toLowerCase()
                .replace(" ", "")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n");

        sb.append(".").append(apellido);
        return sb.toString();
    }

    private String generateProfessorEmail(String nombre1, String nombre2, String apellido1, String apellido2) {
        return generateProfessorUsername(nombre1, nombre2, apellido1, apellido2) + "@prf.umss.edu";
    }

    @Override
    public List<Account> getAllAccounts() throws AccountException {
        return new ArrayList<>(accounts.values());
    }
}