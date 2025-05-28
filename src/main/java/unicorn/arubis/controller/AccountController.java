package unicorn.arubis.controller;

import unicorn.arubis.model.Account;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.IAccount;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.util.*;

import java.util.ArrayList;
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
        String userFilePath = "src/main/java/unicorn/arubis/dto/schedules/" + account.getUser() + "_schedule.txt";
        try {
            fileHandler.createFileIfNotExists(userFilePath);
        } catch (FileException e) {
            throw new AccountException("Error al crear archivo de horarios para el usuario: " + e.getMessage());
        }
    }

    @Override
    public Account login(String username, String password) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.invalidCredentials();
        
        if (!account.verifyPassword(password)) throw AccountException.invalidCredentials();
        
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

    @Override
    public void promoteToAccount(String username, TipoCuenta newType) throws AccountException {
        Account account = getByUsername(username);
        if (account == null) throw AccountException.userNotFound();
        
        account.setTipoCuenta(newType);
        saveChanges();
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
                Account defaultAdmin = new Account("admin", "admin", "00000000", "admin@admin.com", "admin", "Hola1234"); //
                defaultAdmin.setTipoCuenta(TipoCuenta.ADMIN); //
                this.accounts.put(defaultAdmin.getId(), defaultAdmin); // Agregamos al HashMap
                saveChanges(); // Guardar el nuevo admin en su archivo correspondiente
            } catch (IllegalArgumentException e) {
                throw new AccountException("Error creando cuenta admin por defecto: " + e.getMessage()); //
            }
        }
    }

    @Override
    public List<Account> getAllAccounts() throws AccountException {
        return new ArrayList<>(accounts.values());
    }
}