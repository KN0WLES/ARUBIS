package unicorn.arubis.controller;

import unicorn.arubis.model.Account;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.util.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * Clase que actúa como controlador para la gestión de cuentas de usuario.
 * Proporciona la lógica de negocio necesaria para registrar, iniciar sesión, actualizar, eliminar cuentas
 * y gestionar privilegios de administrador.
 * Los datos de las cuentas se almacenan y recuperan desde archivos separados por tipo de cuenta.
 *
 * @description Funcionalidades principales:
 * - Registrar nuevas cuentas de usuario.
 * - Iniciar sesión con credenciales de usuario.
 * - Actualizar información de las cuentas (nombre, apellido, teléfono, correo electrónico, contraseña).
 * - Eliminar cuentas de usuario.
 * - Gestionar privilegios de administrador.
 * - Recuperar cuentas por nombre de usuario, correo electrónico o ID.
 * - Listar todas las cuentas registradas.
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
    //private final String filePath = "src/main/java/data/accounts.txt";
    //private List<Account> accounts;
    private final Map<TipoCuenta, String> filePaths;
    private Map<String, Account> accounts;

    public AccountController(IFile<Account> fileHandler) throws AccountException {
        this.fileHandler = fileHandler;
        this.filePaths = new HashMap<>();
        // Define los nombres de archivo para cada tipo de cuenta 
        this.filePaths.put(TipoCuenta.ADMIN,      "src/main/java/unicorn/arubis/dto/accounts_admin.txt");
        this.filePaths.put(TipoCuenta.ESTUDIANTE, "src/main/java/unicorn/arubis/dto/accounts_estudiante.txt");
        this.filePaths.put(TipoCuenta.PROFESOR,   "src/main/java/unicorn/arubis/dto/accounts_profesor.txt");

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
        } catch (FileException e) {
            this.accounts = new HashMap<>(); // En caso de error, inicializamos un mapa vacío
            System.err.println("Error al cargar cuentas, iniciando con lista vacía: " + e.getMessage());
        }
    }
     /**
     * Guarda todas las cuentas en sus archivos correspondientes según su tipo.
     * 
     * <p>Organiza las cuentas por tipo (ADMIN, PROFESOR, ESTUDIANTE) y las guarda
     * en archivos separados usando el FileHandler.</p>
     * 
     * @throws AccountException Si ocurre un error al escribir en los archivos. El mensaje
     *                         incluye el tipo de cuenta que falló y el error original.
     * @see FileHandler#saveData(List, String)
     * @see AccountException
     */
    private void saveChanges() throws AccountException {
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
     /**
     * Registra una nueva cuenta de usuario validando la unicidad del username y email.
     * @throws AccountException Si:
     *                         - El username ya existe (error: "El usuario ya existe")
     *                         - El email ya existe (error: "El correo electrónico ya está registrado")
     *                         - Fallo al guardar cambios (error específico del sistema)
     *                         - Fallo al crear archivo de horarios (error específico del sistema)
     *
     * @see #saveChanges()
     * @see #createUserScheduleFile(Account) 
     * @see AccountException#duplicateUser()
     */
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
     /**
     * Crea un archivo de horarios personal para el usuario en el sistema de archivos.
     * @throws AccountException Si ocurre un error durante la creación del archivo. El mensaje
     *                         incluye detalles del error original.
     * 
     * @see FileHandler#createFileIfNotExists(String)
     * @see Account#getUser()
     */
    private void createUserScheduleFile(Account account) throws AccountException {
        String userFilePath = "src/main/java/unicorn/arubis/dto/schedules/" + account.getUser() + "_schedule.txt";
        try {
            fileHandler.createFileIfNotExists(userFilePath);
        } catch (FileException e) {
            throw new AccountException("Error al crear archivo de horarios para el usuario: " + e.getMessage());
        }
    }
     /**
     * Autentica un usuario en el sistema validando sus credenciales.
     * @throws AccountException Si:
     *                         - Las credenciales son incorrectas (error genérico "Usuario o contraseña incorrectos")
     *                         - La cuenta no existe
     * 
     * @see #getByUsername(String)
     * @see Account#verifyPassword(String)
     * @see AccountException#invalidCredentials()
     */
    @Override
    public Account login(String username, String password) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.invalidCredentials();
        
        if (!account.verifyPassword(password)) throw AccountException.invalidCredentials();
        
        return account;
    }
     /**
     * Actualiza el nombre de un usuario registrado en el sistema.
     * @throws AccountException Si:
     *                         - El usuario no existe (error: "Cuenta no encontrada")
     *                         - Fallo al guardar cambios (error específico del sistema)
     * 
     * @see #getByUsername(String)
     * @see #saveChanges()
     * @see AccountException#userNotFound()
     */
    @Override
    public void updateName(String username, String newName) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        account.setNombre(newName);
        saveChanges();
    }
     /**
     * Actualiza el apellido de un usuario registrado con validación de datos.
     * @throws AccountException Si:
     *                         - Datos de entrada inválidos (código específico)
     *                         - Usuario no existe (error: "Cuenta no encontrada")
     *                         - Error al guardar cambios
     * 
     * @see #getByUsername(String)
     * @see #saveChanges() 
     */
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
     /**
     * Actualiza el número de teléfono de un usuario registrado.
     * @throws AccountException Si:
     *                         - El usuario no existe (error: "Cuenta no encontrada")
     *                         - Error al guardar cambios
     * 
     * @see #getByUsername(String)
     * @see #saveChanges()
     * @see AccountException#userNotFound()
     */
    @Override
    public void updatePhone(String username, String newPhone) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        account.setPhone(newPhone);
        saveChanges();
    }
     /**
     * Actualiza el email de un usuario validando unicidad y existencia.
     * @throws AccountException Si:
     *                         - Usuario no existe (error: "Cuenta no encontrada")
     *                         - Email ya está registrado (error específico)
     *                         - Error al persistir cambios
     * 
     * @see #getByUsername(String)
     * @see #saveChanges()
     */
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
     /**
     * Actualiza la contraseña de un usuario con validación de seguridad.
     * @throws AccountException Si:
     *                         - Usuario no existe (error: "Cuenta no encontrada")
     *                         - Contraseña actual no coincide (error específico)
     *                         - Error al guardar cambios
     * 
     * @see Account#verifyPassword(String)
     * @see Account#setPassword(String)
     */
    @Override
    public void updatePassword(String username, String currentPassword, String newPassword) throws AccountException {
        // Validación de existencia
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        // Verificación de contraseña actual
        if (!account.verifyPassword(currentPassword)) {
            throw new AccountException("La contraseña actual es incorrecta");
        }
        // Actualización segura (el setPassword debería hacer hashing)
        account.setPassword(newPassword);
        saveChanges();
    }
     /**
     * Restablece la contraseña de un usuario sin validar la contraseña actual (solo para administradores).
     * @throws AccountException Si:
     *                         - El usuario no existe (error: "Cuenta no encontrada")
     *                         - Error al persistir cambios
     * 
     * @see #updatePassword() Versión segura para cambios de usuarios normales
     * @see Account#setPassword() Debería incluir hashing automático
     */
    public void resetUserPassword(String username, String newPassword) throws AccountException {
        // Validación básica de existencia
        Account account = getByUsername(username);

        if (account == null) throw AccountException.userNotFound();
        // Actualización directa (sin verificación previa)
        account.setPassword(newPassword);
        saveChanges();
    }
     /**
     * Cambia el tipo de cuenta de un usuario (ej: promueve a administrador).
     * @throws AccountException Si:
     *                         - El usuario no existe (error: "Cuenta no encontrada")
     *                         - Error al guardar cambios
     * 
     * @see TipoCuenta
     * @see Account#setTipoCuenta()
     */
    @Override
    public void promoteToAccount(String username, TipoCuenta newType) throws AccountException {
         // Validación de existencia
        Account account = getByUsername(username);
        if (account == null) throw AccountException.userNotFound();
        // Cambio de rol y persistencia
        account.setTipoCuenta(newType);
        saveChanges();
    }
     /**
     * Busca una cuenta de usuario por su nombre de usuario exacto (case-sensitive).
     * @return La cuenta encontrada o null si no existe
     * 
     * @see #accounts Mapa interno donde se realiza la búsqueda
     * @see Account#getUser() Método usado para comparación
     */
    @Override
    public Account getByUsername(String username) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getUser().equals(username))// Comparación exacta
                .findFirst()// Toma la primera coincidencia
                .orElse(null); // Null si no hay resultados
    }
     /**
     * Busca una cuenta de usuario por su dirección de email exacta (case-sensitive).
     * @return La cuenta asociada al email o null si no existe
     * @throws AccountException (Actualmente no se usa, se mantiene por consistencia con la interfaz)
     * 
     * @see #getByUsername() Versión para búsqueda por nombre de usuario
     * @see Account#getEmail() Campo utilizado para la búsqueda
     */
    @Override
    public Account getByEmail(String email) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getEmail().equals(email))// Comparación exacta
                .findFirst()// Primera coincidencia
                .orElse(null);// Null si no hay resultados
    }
     /**
     * Busca una cuenta de usuario por su ID único.
     * @return La cuenta encontrada o null si no existe
     * @throws AccountException (Reservado para futuras validaciones)
     */
    @Override
    public Account getById(String id) throws AccountException {
        return accounts.values().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
     /**
     * Elimina una cuenta de usuario del sistema con validaciones de seguridad.
     * @throws AccountException Si:
     *                         - El usuario no existe (error: "Cuenta no encontrada")
     *                         - Es el último admin (error: "No se puede modificar/eliminar una cuenta admin")
     *                         - Error al guardar cambios
     * 
     * @see #getByUsername(String)
     * @see #saveChanges()
     * @see AccountException#adminRestriction()
     */
    @Override
    public void deleteAccount(String username) throws AccountException {
        // 1. Validar existencia
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        // 2. Proteger último admin
        long adminCount = accounts.values().stream().filter(a -> a.getTipoCuenta() == TipoCuenta.ADMIN).count(); //
        if (account.getTipoCuenta() == TipoCuenta.ADMIN && adminCount <= 1) { //
            throw AccountException.adminRestriction(); // No se puede eliminar el último admin
        }
         // 3. Eliminación segura
        accounts.remove(account.getId());
        saveChanges();
    }
     /**
     * Inicializa una cuenta administradora por defecto si no existe ninguna.
     * @throws AccountException Si falla la creación o persistencia de la cuenta
     * 
     * @see Account#Account() Constructor de cuentas
     * @see #saveChanges() Persistencia de datos
     */
    private void initializeDefaultAdmin() throws AccountException {
        // Verificar si ya existe un administrador por defecto en la lista total de cuentas
        boolean adminExists = accounts.values().stream().anyMatch(a -> a.getUser().equals("admin") && a.getTipoCuenta() == TipoCuenta.ADMIN); //

        if (!adminExists) { //
            try {
                 // Crear admin con credenciales predeterminadas
                Account defaultAdmin = new Account("admin", "admin", "00000000", "admin@admin.com", "admin", "Hola1234"); //
                defaultAdmin.setTipoCuenta(TipoCuenta.ADMIN); 
                // Registrar y persistir
                this.accounts.put(defaultAdmin.getId(), defaultAdmin); // Agregamos al HashMap
                saveChanges(); // Guardar el nuevo admin en su archivo correspondiente
            } catch (IllegalArgumentException e) {
                throw new AccountException("Error creando cuenta admin por defecto: " + e.getMessage()); //
            }
        }
    }
     /**
 * Obtiene una lista de todas las cuentas registradas en el sistema.
 * @return Lista inmodificable de cuentas ({@code List<Account>})
 * @throws AccountException Si ocurre un error al acceder a los datos
 * 
 * @see Collections#unmodifiableList Mejor práctica para inmutabilidad
 * @see Account Clase modelo de cuentas
 */
    @Override
    public List<Account> getAllAccounts() throws AccountException {
        return new ArrayList<>(accounts.values());
    }
}