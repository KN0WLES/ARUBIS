package unicorn.arubis.controller;

import unicorn.arubis.model.Account;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que actúa como controlador para la gestión de cuentas de usuario.
 * Proporciona la lógica de negocio necesaria para registrar, iniciar sesión, actualizar, eliminar cuentas
 * y gestionar privilegios de administrador.
 * Los datos de las cuentas se almacenan y recuperan desde un archivo utilizando un manejador de archivos genérico.
 * 
 * @description Funcionalidades principales:
 *                   - Registrar nuevas cuentas de usuario.
 *                   - Iniciar sesión con credenciales de usuario.
 *                   - Actualizar información de las cuentas (nombre, apellido, teléfono, correo electrónico, contraseña).
 *                   - Eliminar cuentas de usuario.
 *                   - Gestionar privilegios de administrador.
 *                   - Recuperar cuentas por nombre de usuario, correo electrónico o ID.
 *                   - Listar todas las cuentas registradas.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see IAccount
 * @see Account
 * @see IFile
 * @see FileException
 * @see AccountException
 */
public class AccountController implements IAccount {
    
    private final IFile<Account> fileHandler;
    private final String filePath = "src/main/java/data/accounts.txt";
    private List<Account> accounts;

    public AccountController(IFile<Account> fileHandler) throws AccountException {
        this.fileHandler = fileHandler;
        try {
            this.fileHandler.createFileIfNotExists(filePath);
            this.accounts = this.fileHandler.loadData(filePath);
            initializeDefaultAdmin();
        } catch (FileException e) {
            this.accounts = new ArrayList<>();
            System.err.println("Error al cargar cuentas, iniciando con lista vacía: " + e.getMessage());
        }
    }

    private void saveChanges() throws AccountException {
        try {
            fileHandler.saveData(accounts, filePath);
        } catch (FileException e) {
            throw new AccountException("Error al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public void registerAccount(Account account) throws AccountException {
        if (accounts.stream().anyMatch(a -> a.getUser().equals(account.getUser()))) {
            throw AccountException.duplicateUser();
        }
        
        if (accounts.stream().anyMatch(a -> a.getEmail().equals(account.getEmail()))) {
            throw new AccountException("El correo electrónico ya está registrado");
        }
        
        accounts.add(account);
        saveChanges();
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
        
        if (accounts.stream()
                .filter(a -> !a.getUser().equals(username))
                .anyMatch(a -> a.getEmail().equals(newEmail))) {
            throw new AccountException("El correo electrónico ya está registrado");
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
    public void setAdminStatus(String username, boolean isAdmin) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        if (isAdmin) {
            account.setTipoCuenta(TipoCuenta.ADMIN);
        } else {
            account.setTipoCuenta(TipoCuenta.ESTUDIANTE);
        }
        saveChanges();
    }

    @Override
    public Account getByUsername(String username) throws AccountException {
        return accounts.stream()
                .filter(a -> a.getUser().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account getByEmail(String email) throws AccountException {
        return accounts.stream()
                .filter(a -> a.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Account getById(String id) throws AccountException {
        return accounts.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public void deleteAccount(String username) throws AccountException {
        Account account = getByUsername(username);
        
        if (account == null) throw AccountException.userNotFound();
        
        if (account.isAdmin()) throw AccountException.adminRestriction();
        
        accounts.removeIf(a -> a.getUser().equals(username));
        saveChanges();
    }

    private void initializeDefaultAdmin() throws AccountException {
        if (this.accounts == null || this.accounts.isEmpty()) {
            this.accounts = new ArrayList<>();
            try {
                Account defaultAdmin = new Account("admin", "admin", "00000000", "admin@admin.com", "admin", "Admin123");
                defaultAdmin.setTipoCuenta(TipoCuenta.ADMIN);
                this.accounts.add(defaultAdmin);
                saveChanges();
            } catch (IllegalArgumentException e) {
                throw new AccountException("Error creando cuenta admin por defecto: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Account> getAllAccounts() throws AccountException {
        return new ArrayList<>(accounts);
    }
}