package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;

import unicorn.util.*;
import java.util.List;
import java.util.Scanner;
import java.io.Console;
import java.util.stream.Collectors;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Clase que representa el controlador de menú para gestión de cuentas.
 * Gestiona la interacción del usuario con el sistema de cuentas, permitiendo
 * registrar, actualizar, eliminar y consultar cuentas de usuario.
 * {@code @description} Funcionalidades principales:
 *                   - Registrar nuevas cuentas de usuario.
 *                   - Iniciar sesión con credenciales.
 *                   - Actualizar información de cuentas existentes.
 *                   - Eliminar cuentas de usuario.
 *                   - Gestionar privilegios de administrador.
 *                   - Consultar información de cuentas.
 *                   - Validar entradas y manejar excepciones.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-05-28
 * @see AccountController
 * @see Account
 * @see IAccount
 */
public class AccountMenuController {
    private final AccountController accountController;
    private Account account;
    private final Scanner scanner;
    private final Console console;

    public AccountMenuController(Account account) throws AccountException {
        Account prototype = new Account();
        IFile<Account> fileHandler = new FileHandler<>(prototype);
        this.accountController = new AccountController(fileHandler);
        this.scanner = new Scanner(System.in);
        this.console = System.console();
        this.account = account;
    }

    public Account login(String username, String password) throws AccountException {
        account = accountController.login(username, password);

        System.out.print("\033[H\033[2J");
        System.out.flush();

        mostrarMensajeCentrado("==== SISTEMA DE GESTIÓN LEVELING UP LIFE ====");
        System.out.println("Usuario actual: " + username + " \t\t\t\t modo: " +
                (account.isAdmin() ? "ADMIN" : "USER"));

        return account;
    }

    public void registerAccount(String name, String lastName, String phone, String email, String username, String password) throws AccountException {
        Account newAccount = new Account(name, lastName, phone, email, username, password);
        accountController.registerAccount(newAccount);
    }

    public void showMyAccount(Account account) {
        mostrarMensajeCentrado(" MI CUENTA ");
        displayAccountDetails(account);
        showUserMenu();
    }

    private void displayAccountDetails(Account account) {
        mostrarMensajeCentrado(" Detalles de la cuenta ");
        System.out.println("Usuario: " + account.getUser());
        System.out.println("Nombre: " + account.getNombre());
        System.out.println("Apellido: " + account.getApellido());
        System.out.println("Email: " + account.getEmail());
        System.out.println("Teléfono: " + account.getPhone());
        System.out.println("Tipo: " +
                (account.getTipoCuenta() != null ? account.getTipoCuenta().getDescripcion() : "No definido"));
    }

    public void showUserMenu() {
        int option;
        do {
            mostrarMensajeCentrado("==== MENÚ DE USUARIO ====");
            System.out.println("1. Actualizar información de cuenta");
            System.out.println("2. Cambiar contraseña");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> updateAccountMenu();
                    case 2 -> changePasswordMenu();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void updateAccountMenu() {
        mostrarMensajeCentrado("==== ACTUALIZAR INFORMACIÓN DE CUENTA ====");
        System.out.println("1. Actualizar nombre");
        System.out.println("2. Actualizar apellido");
        System.out.println("3. Actualizar teléfono");
        System.out.println("4. Actualizar correo electrónico");
        System.out.println("0. Volver");

        int option = readIntOption("Seleccione una opción: ");

        try {
            switch (option) {
                case 1 -> {
                    System.out.print("Nuevo nombre: ");
                    String newName = scanner.nextLine();
                    accountController.updateName(account.getUser(), newName);
                    System.out.println("Nombre actualizado exitosamente.");
                }
                case 2 -> {
                    System.out.print("Nuevo nombre: ");
                    String newLast = scanner.nextLine();
                    accountController.updateLast(account.getUser(), newLast);
                    System.out.println("Nombre actualizado exitosamente.");
                }
                case 3 -> {
                    System.out.print("Nuevo teléfono: ");
                    String newPhone = scanner.nextLine();
                    accountController.updatePhone(account.getUser(), newPhone);
                    System.out.println("Teléfono actualizado exitosamente.");
                }
                case 4 -> {
                    System.out.print("Nuevo correo electrónico: ");
                    String newEmail = scanner.nextLine();
                    accountController.updateEmail(account.getUser(), newEmail);
                    System.out.println("Correo electrónico actualizado exitosamente.");
                }
                case 0 -> { }
                //case 0 -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        } catch (AccountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changePasswordMenu() {
        mostrarMensajeCentrado("==== CAMBIAR CONTRASEÑA ====");

        String currentPassword;
        String newPassword;

        if (console != null) {
            currentPassword = new String(console.readPassword("Contraseña actual: "));
            newPassword = new String(console.readPassword("Nueva contraseña: "));
        } else {
            System.out.print("Contraseña actual: ");
            currentPassword = scanner.nextLine();
            System.out.print("Nueva contraseña: ");
            newPassword = scanner.nextLine();
        }

        try {
            accountController.updatePassword(account.getUser(), currentPassword, newPassword);
            System.out.println("Contraseña actualizada exitosamente.");
        } catch (AccountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showAdmMenu() {
        int option;
        do {
            mostrarMensajeCentrado("==== MENÚ DE ADMINISTRACIÓN DE CUENTAS ====");
            System.out.println("1. Ver todas las cuentas (paginado)");
            System.out.println("2. Buscar cuentas (avanzado)");
            System.out.println("3. Filtrar cuentas por rol");
            System.out.println("4. Modificar estado de administrador");
            System.out.println("5. Eliminar cuenta");
            System.out.println("6. Resetear contraseña");
            System.out.println("7. Ver estadísticas");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> showAllAccountsMenu();
                    case 2 -> searchAccountsMenu();
                    case 3 -> filterAccountsByRole();
                    case 4 -> modifyAdminStatusMenu();
                    case 5 -> deleteAccountMenu();
                    case 6 -> resetPasswordMenu();
                    case 7 -> showAccountStats();
                    case 0 -> { }
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    private void showAllAccountsMenu() throws AccountException {
        int pageSize = 10; // Número de cuentas por página
        int currentPage = 0;
        List<Account> allAccounts = accountController.getAllAccounts();
        int totalPages = (int) Math.ceil((double) allAccounts.size() / pageSize);

        do {
            mostrarMensajeCentrado("==== TODAS LAS CUENTAS (Página " + (currentPage + 1) + "/" + totalPages + ") ====");

            // Mostrar cuentas de la página actual
            List<Account> pageAccounts = allAccounts.stream()
                    .skip((long) currentPage * pageSize)
                    .limit(pageSize)
                    .toList();

            for (Account acc : pageAccounts) {
                System.out.println("Usuario: " + acc.getUser() +
                        " | Nombre: " + acc.getFullName() +
                        " | Rol: " + acc.getTipoCuenta().getDescripcion());
            }

            System.out.println("\n1. Página anterior");
            System.out.println("2. Página siguiente");
            System.out.println("3. Filtrar por rol");
            System.out.println("0. Volver");

            int option = readIntOption("Seleccione una opción: ");

            switch (option) {
                case 1 -> {
                    if (currentPage > 0) currentPage--;
                }
                case 2 -> {
                    if (currentPage < totalPages - 1) currentPage++;
                }
                case 3 -> filterAccountsByRole();
                case 0 -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        } while (true);
    }

    private void filterAccountsByRole() throws AccountException {
        System.out.println("Filtrar por rol:");
        System.out.println("1. Estudiantes");
        System.out.println("2. Profesores");
        System.out.println("3. Administradores");
        System.out.println("0. Cancelar");

        int option = readIntOption("Seleccione una opción: ");
        TipoCuenta tipo;

        switch (option) {
            case 1 -> tipo = TipoCuenta.ESTUDIANTE;
            case 2 -> tipo = TipoCuenta.PROFESOR;
            case 3 -> tipo = TipoCuenta.ADMIN;
            case 0 -> { return; }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        TipoCuenta finalTipo = tipo;
        List<Account> filteredAccounts = accountController.getAllAccounts().stream()
                .filter(acc -> finalTipo.equals(acc.getTipoCuenta())) // Usar equals() en lugar de ==
                .toList();

        mostrarMensajeCentrado("==== CUENTAS FILTRADAS (" + tipo.getDescripcion() + ") ====");
        filteredAccounts.forEach(acc ->
                System.out.println(
                        "Usuario: " + acc.getUser() +
                                " | Nombre: " + acc.getFullName() +
                                " | Email: " + acc.getEmail()
                )
        );
        System.out.println("\nTotal: " + filteredAccounts.size() + " cuentas.");
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void searchAccountsMenu() throws AccountException {
        mostrarMensajeCentrado("==== BUSCAR CUENTAS ====");
        System.out.println("1. Buscar por nombre");
        System.out.println("2. Buscar por email");
        System.out.println("3. Buscar por usuario");
        System.out.println("0. Volver");

        int option = readIntOption("Seleccione una opción: ");
        String searchTerm;

        switch (option) {
            case 1 -> {
                System.out.print("Ingrese el nombre a buscar: ");
                searchTerm = scanner.nextLine();
                searchAccountsByName(searchTerm);
            }
            case 2 -> {
                System.out.print("Ingrese el email a buscar: ");
                searchTerm = scanner.nextLine();
                searchAccountsByEmail(searchTerm);
            }
            case 3 -> {
                System.out.print("Ingrese el usuario a buscar: ");
                searchTerm = scanner.nextLine();
                searchAccountsByUsername(searchTerm);
            }
            case 0 -> { }
            //case 0 -> { return; }
            default -> System.out.println("Opción inválida.");
        }
    }

    private void searchAccountsByName(String name) throws AccountException {
        List<Account> results = accountController.getAllAccounts().stream()
                .filter(acc -> acc.getNombre().toLowerCase().contains(name.toLowerCase()) ||
                        acc.getApellido().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());

        displaySearchResults(results, "nombre", name);
    }

    private void searchAccountsByEmail(String email) throws AccountException {
        List<Account> results = accountController.getAllAccounts().stream()
                .filter(acc -> acc.getEmail().toLowerCase().contains(email.toLowerCase()))
                .collect(Collectors.toList());

        displaySearchResults(results, "email", email);
    }

    private void searchAccountsByUsername(String username) throws AccountException {
        List<Account> results = accountController.getAllAccounts().stream()
                .filter(acc -> acc.getUser().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());

        displaySearchResults(results, "usuario", username);
    }

    private void displaySearchResults(List<Account> results, String filterType, String term) {
        mostrarMensajeCentrado("==== RESULTADOS DE BÚSQUEDA (" + filterType + ": " + term + ") ====");
        if (results.isEmpty()) {
            System.out.println("No se encontraron coincidencias.");
        } else {
            results.forEach(acc -> System.out.println(
                    "Usuario: " + acc.getUser() +
                            " | Nombre: " + acc.getFullName() +
                            " | Rol: " + acc.getTipoCuenta().getDescripcion()
            ));
        }
        System.out.println("\nTotal: " + results.size() + " resultados.");
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private void resetPasswordMenu() throws AccountException {
        mostrarMensajeCentrado("==== RESETEAR CONTRASEÑA ====");
        System.out.print("Ingrese el usuario de la cuenta: ");
        String username = scanner.nextLine();

        Account targetAccount = accountController.getByUsername(username);
        if (targetAccount == null) {
            System.out.println("Usuario no encontrado.");
            return;
        }

        String newPassword = "Temp1234"; // Contraseña temporal
        accountController.resetUserPassword(username, newPassword);
        System.out.println("Contraseña reseteada a: " + newPassword);
    }

    private void showAccountStats() throws AccountException {
        List<Account> allAccounts = accountController.getAllAccounts();

        long totalStudents = allAccounts.stream().filter(Account::isEstudiante).count();
        long totalTeachers = allAccounts.stream().filter(Account::isProfesor).count();
        long totalAdmins = allAccounts.stream().filter(Account::isAdmin).count();

        mostrarMensajeCentrado("==== ESTADÍSTICAS DE CUENTAS ====");
        System.out.println("Total de cuentas: " + allAccounts.size());
        System.out.println("Estudiantes: " + totalStudents);
        System.out.println("Profesores: " + totalTeachers);
        System.out.println("Administradores: " + totalAdmins);
        System.out.print("Presione Enter para continuar...");
        scanner.nextLine();
    }
    
    private void modifyAdminStatusMenu() throws AccountException {
        mostrarMensajeCentrado("==== MODIFICAR ROL DE CUENTA ====");
        System.out.print("Ingrese el nombre de usuario de la cuenta a modificar: ");
        String username = scanner.nextLine();

        Account targetAccount = accountController.getByUsername(username);
        if (targetAccount == null) {
            System.out.println("Error: Usuario no encontrado.");
            return;
        }

        // Mostrar información actual de la cuenta
        System.out.println("\nCuenta seleccionada:");
        System.out.println("Usuario: " + targetAccount.getUser());
        System.out.println("Nombre: " + targetAccount.getFullName());
        System.out.println("Rol actual: " + targetAccount.getTipoCuenta().getDescripcion());

        // Menú para seleccionar nuevo rol
        System.out.println("\nSeleccione el nuevo rol:");
        System.out.println("1. Administrador");
        System.out.println("2. Profesor");
        System.out.println("3. Estudiante");
        System.out.println("0. Cancelar");

        int option = readIntOption("Opción: ");
        TipoCuenta newRole;

        switch (option) {
            case 1 -> newRole = TipoCuenta.ADMIN;
            case 2 -> newRole = TipoCuenta.PROFESOR;
            case 3 -> newRole = TipoCuenta.ESTUDIANTE;
            case 0 -> {
                System.out.println("Operación cancelada.");
                return;
            }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        // Validar si el nuevo rol es diferente al actual
        if (targetAccount.getTipoCuenta() == newRole) {
            System.out.println("La cuenta ya tiene este rol.");
            return;
        }

        // Confirmar acción
        System.out.print("¿Confirmar cambio de rol a " + newRole.getDescripcion() + "? (S/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("S")) {
            accountController.promoteToAccount(targetAccount.getUser(), newRole);
            System.out.println("Rol actualizado exitosamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private void deleteAccountMenu() throws AccountException {
        mostrarMensajeCentrado("==== ELIMINAR CUENTA ====");
        System.out.print("Ingrese el nombre de usuario de la cuenta a eliminar: ");
        String user = scanner.nextLine();

        Account targetAccount = accountController.getByUsername(user);
        if (targetAccount == null) {
            System.out.println("Error: Usuario no encontrado.");
            return;
        }

        // Mostrar información de la cuenta
        System.out.println("\nCuenta seleccionada:");
        System.out.println("Usuario: " + targetAccount.getUser());
        System.out.println("Nombre: " + targetAccount.getFullName());
        System.out.println("Rol: " + targetAccount.getTipoCuenta().getDescripcion());

        // Validar si es el último admin
        if (targetAccount.isAdmin()) {
            long adminCount = accountController.getAllAccounts().stream()
                    .filter(Account::isAdmin)
                    .count();
            if (adminCount <= 1) {
                System.out.println("Error: No se puede eliminar el último administrador.");
                return;
            }
        }

        // Confirmar eliminación
        System.out.print("¿Está seguro de eliminar esta cuenta? (S/N): ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if (confirm.equals("S")) {
            accountController.deleteAccount(user);
            System.out.println("Cuenta eliminada exitosamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    public void mostrarMensajeCentrado(String mensaje) {
        int longitudMaxima = 73;
        int longitudMensaje = mensaje.length();
        int espaciosIzquierda = (longitudMaxima - longitudMensaje) / 2;
        int espaciosDerecha = longitudMaxima - longitudMensaje - espaciosIzquierda;
        String lineaCentrada = "=".repeat(espaciosIzquierda) + mensaje + "=".repeat(espaciosDerecha);
        System.out.println(lineaCentrada);
    }

    private int readIntOption(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Crear una instancia del controlador con una cuenta nula inicialmente
            AccountMenuController controller = new AccountMenuController(null);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                controller.mostrarMensajeCentrado("==== SISTEMA DE GESTIÓN DE CUENTAS ====");
                System.out.println("1. Iniciar sesión");
                System.out.println("2. Registrarse");
                System.out.println("0. Salir");

                int opcion = controller.readIntOption("Seleccione una opción: ");

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Usuario: ");
                        String username = scanner.nextLine();
                        System.out.print("Contraseña: ");
                        String password = scanner.nextLine();

                        try {
                            Account cuenta = controller.login(username, password);
                            if (cuenta.isAdmin()) {
                                controller.showAdmMenu();
                            } else {
                                controller.showUserMenu();
                            }
                        } catch (AccountException e) {
                            System.out.println("Error al iniciar sesión: " + e.getMessage());
                        }
                    }

                    case 2 -> {
                        System.out.print("Nombre: ");
                        String nombre = scanner.nextLine();
                        System.out.print("Apellido: ");
                        String apellido = scanner.nextLine();
                        System.out.print("Teléfono: ");
                        String telefono = scanner.nextLine();
                        System.out.print("Email: ");
                        String email = scanner.nextLine();
                        System.out.print("Usuario: ");
                        String usuario = scanner.nextLine();
                        System.out.print("Contraseña: ");
                        String contrasena = scanner.nextLine();

                        try {
                            controller.registerAccount(nombre, apellido, telefono, email, usuario, contrasena);
                            System.out.println("¡Cuenta creada exitosamente!");
                        } catch (AccountException e) {
                            System.out.println("Error al registrar: " + e.getMessage());
                        }
                    }

                    case 0 -> {
                        System.out.println("Saliendo del sistema...");
                        scanner.close();
                        return;
                    }

                    default -> System.out.println("Opción inválida");
                }
            }
        } catch (Exception e) {
            System.err.println("Error crítico: " + e.getMessage());
            Logger.getLogger(AccountMenuController.class.getName()).log(Level.SEVERE, "Error crítico", e);
        }
    }
}