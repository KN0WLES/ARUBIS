package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.TipoCuenta;

import java.time.LocalDate;
import java.util.*;
import java.io.Console;

public class AccountMenuController extends BaseMenuController {
    private final AccountController accountController;
    private final Account account;
    private final Console console;

    public AccountMenuController(AccountController accountController, Account account) {
        this.accountController = accountController;
        this.account = account;
        this.console = System.console();
    }

    @Override
    public void showMenu() {
        switch (account.getTipoCuenta()) {
            case ADMIN -> showAdminMenu();
            case PROFESOR -> showTeacherMenu();
            case ESTUDIANTE -> showStudentMenu();
            default -> System.out.println("Rol no reconocido");
        }
    }

    // ==================== MENÚ PARA TODOS ====================
    public void showCommonMenu() {
        int option;
        do {
            mostrarMensajeCentrado("==== MENÚ DE CUENTA ====");
            displayAccountDetails(account);
            System.out.println("1. Actualizar información de cuenta");
            System.out.println("2. Cambiar contraseña");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> updateAccountMenu(account);
                    case 2 -> changePasswordMenu(account);
                    case 0 -> {}
                    default -> System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (option != 0);
    }

    // ==================== MENÚ ADMIN ====================
    private void showAdminMenu() {
        while (true) {
            System.out.println("\n=== GESTIÓN DE CUENTAS (ADMIN) ===");
            System.out.println("1. Ver todas las cuentas (paginado)");
            System.out.println("2. Buscar cuentas (avanzado)");
            System.out.println("3. Filtrar cuentas por rol");
            System.out.println("4. Modificar estado de administrador");
            System.out.println("5. Eliminar cuenta");
            System.out.println("6. Resetear contraseña");
            System.out.println("7. Ver estadísticas");
            System.out.println("8. Crear cuenta docente/administrador");
            System.out.println("9. Gestión de sustituciones");
            System.out.println("0. Volver al menú principal");
            switch (readIntOption("Selección: ")) {
                case 1 -> verTodasCuentasPaginado();
                case 2 -> buscarCuentasAvanzado();
                case 3 -> filtrarPorRol();
                case 4 -> modificarEstadoAdmin();
                case 5 -> eliminarCuenta();
                case 6 -> resetearContrasena();
                case 7 -> verEstadisticas();
                case 8 -> crearCuentaDocenteAdmin();
                case 9 -> gestionSustituciones();
                case 0 -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    // ==================== MENÚ PROFESOR ====================
    private void showTeacherMenu() {
        showCommonMenu();
        // Puedes agregar funciones específicas para profesores aquí si lo requieres
    }

    // ==================== MENÚ ESTUDIANTE ====================
    private void showStudentMenu() {
        showCommonMenu();
        // Puedes agregar funciones específicas para estudiantes aquí si lo requieres
    }

    // ==================== FUNCIONES COMUNES ====================
    
    private void displayAccountDetails(Account account) {
        mostrarMensajeCentrado(" Detalles de la cuenta ");
        System.out.println("Usuario: " + account.getUser());
        System.out.println("Nombre: " + account.getNombre());
        System.out.println("Apellido: " + account.getApellido());
        System.out.println("Email: " + account.getEmail());
        System.out.println("Teléfono: " + account.getPhone());
        System.out.println("Tipo: " +
                (account.getTipoCuenta() != null ? account.getTipoCuenta().getDescripcion() : "No definido"));
        System.out.println(account.getStatus() != null ? "Estado: " + account.getStatus().getDescripcion() : "Estado: No definido");        
    }

    private void updateAccountMenu(Account account) {
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
                    System.out.print("Nuevo apellido: ");
                    String newLast = scanner.nextLine();
                    accountController.updateLast(account.getUser(), newLast);
                    System.out.println("Apellido actualizado exitosamente.");
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
                case 0 -> {}
                default -> System.out.println("Opción inválida.");
            }
        } catch (AccountException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void changePasswordMenu(Account account) {
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

    public Account login(String username, String password) throws AccountException {
        Account account2 = accountController.login(username, password);

        System.out.print("\033[H\033[2J");
        System.out.flush();

        mostrarMensajeCentrado("==== SISTEMA DE GESTIÓN DE HORARIOS ====");
        System.out.println("Usuario actual: " + username + " \t\t\t\t modo: " +
                (account2.getTipoCuenta() != null ? account2.getTipoCuenta().getDescripcion() : "No definido"));

        return account2;
    }

    public void registerAccount(Account newAccount) throws AccountException {
        accountController.registerAccount(newAccount);
    }

    public Account collectRegistrationData(Scanner scanner, Console console) {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Apellido: ");
        String apellido = scanner.nextLine();

        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Usuario: ");
        String username = scanner.nextLine();

        String password;
        if (console != null) {
            password = new String(console.readPassword("Contraseña: "));
        } else {
            System.out.print("Contraseña: ");
            password = scanner.nextLine();
        }

        return new Account(nombre, apellido, phone, email, username, password, TipoCuenta.ESTUDIANTE);
    }

    // ==================== FUNCIONES ADMIN ====================
    private void verTodasCuentasPaginado() {
        clearScreen();
        int pagina = 1;
        int porPagina = 10;
        List<Account> todasCuentas;
        
        try {
            todasCuentas = accountController.getAllAccounts();
        } catch (AccountException e) {
            System.err.println("Error al obtener cuentas: " + e.getMessage());
            return;
        }

        while (true) {
            clearScreen();
            int inicio = (pagina - 1) * porPagina;
            int fin = Math.min(inicio + porPagina, todasCuentas.size());
            
            System.out.println("\n=== TODAS LAS CUENTAS (" + pagina + "/" + 
                (int) Math.ceil((double)todasCuentas.size()/porPagina) + ") ===");
            
            for (int i = inicio; i < fin; i++) {
                Account a = todasCuentas.get(i);
                System.out.println(String.format("%d. %s %s (%s) - %s - %s", 
                    i+1, a.getNombre(), a.getApellido(), a.getUser(), 
                    a.getEmail(), a.getTipoCuenta()));
            }
            
            System.out.println("\n1. Siguiente | 2. Anterior | 3. Ir a página | 0. Volver");
            int opcion = readIntOption("Selección: ");
            
            switch (opcion) {
                case 1:
                    if (fin < todasCuentas.size()) pagina++;
                    break;
                case 2:
                    if (pagina > 1) pagina--;
                    break;
                case 3:
                    int nuevaPag = readIntOption("Ir a página: ");
                    if (nuevaPag > 0 && nuevaPag <= Math.ceil((double)todasCuentas.size()/porPagina)) {
                        pagina = nuevaPag;
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void buscarCuentasAvanzado() {
        clearScreen();
        System.out.println("\n=== BÚSQUEDA AVANZADA ===");
        String nombre = readLine("Nombre (dejar vacío para omitir): ");
        String apellido = readLine("Apellido (dejar vacío para omitir): ");
        String email = readLine("Email (dejar vacío para omitir): ");
        String usuario = readLine("Usuario (dejar vacío para omitir): ");
        
        try {
            List<Account> resultados = accountController.getAllAccounts().stream()
                    .filter(a -> nombre.isEmpty() || a.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                    .filter(a -> apellido.isEmpty() || a.getApellido().toLowerCase().contains(apellido.toLowerCase()))
                    .filter(a -> email.isEmpty() || a.getEmail().toLowerCase().contains(email.toLowerCase()))
                    .filter(a -> usuario.isEmpty() || a.getUser().toLowerCase().contains(usuario.toLowerCase()))
                    .toList();
            
            if (resultados.isEmpty()) {
                System.out.println("\nNo se encontraron cuentas con esos criterios.");
            } else {
                System.out.println("\n=== RESULTADOS ===");
                resultados.forEach(a -> System.out.println(
                    String.format("%s %s (%s) - %s - %s", 
                    a.getNombre(), a.getApellido(), a.getUser(), a.getEmail(), a.getTipoCuenta())
                ));
            }
        } catch (AccountException e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void filtrarPorRol() {
        clearScreen();
        System.out.println("\n=== FILTRAR POR ROL ===");
        System.out.println("1. Administradores");
        System.out.println("2. Profesores");
        System.out.println("3. Estudiantes");
        System.out.println("0. Volver");
        
        int opcion = readIntOption("Selección: ");
        TipoCuenta rol;
        
        switch (opcion) {
            case 1: rol = TipoCuenta.ADMIN; break;
            case 2: rol = TipoCuenta.PROFESOR; break;
            case 3: rol = TipoCuenta.ESTUDIANTE; break;
            case 0: return;
            default: 
                System.out.println("Opción inválida.");
                return;
        }
        
        try {
            List<Account> resultados = accountController.getAllAccounts().stream()
                    .filter(a -> a.getTipoCuenta() == rol)
                    .toList();
            
            if (resultados.isEmpty()) {
                System.out.println("\nNo se encontraron cuentas con ese rol.");
            } else {
                System.out.println("\n=== RESULTADOS ===");
                resultados.forEach(a -> System.out.println(
                    String.format("%s %s (%s) - %s", 
                    a.getNombre(), a.getApellido(), a.getUser(), a.getEmail())
                ));
            }
        } catch (AccountException e) {
            System.err.println("Error al filtrar: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void modificarEstadoAdmin() {
        clearScreen();
        String usuario = readLine("Ingrese el nombre de usuario a modificar: ");
        
        try {
            Account cuenta = accountController.getByUsername(usuario);
            if (cuenta == null) {
                System.out.println("Usuario no encontrado.");
                return;
            }
            
            // Mostrar información actual de la cuenta
            System.out.println("\nCuenta seleccionada:");
            System.out.println("Usuario: " + cuenta.getUser());
            System.out.println("Nombre: " + cuenta.getNombre() + " " + cuenta.getApellido());
            System.out.println("Rol actual: " + cuenta.getTipoCuenta().getDescripcion());
            
            // Solo permitir cambiar entre profesor y admin
            if (cuenta.getTipoCuenta() != TipoCuenta.PROFESOR && cuenta.getTipoCuenta() != TipoCuenta.ADMIN) {
                System.out.println("Esta función solo aplica a cuentas de profesor o administrador.");
                return;
            }
            
            TipoCuenta nuevoRol = (cuenta.getTipoCuenta() == TipoCuenta.PROFESOR) ? 
                TipoCuenta.ADMIN : TipoCuenta.PROFESOR;
            
            System.out.println("\nAcción a realizar:");
            System.out.println("1. Convertir a " + nuevoRol.getDescripcion());
            System.out.println("0. Cancelar");
            
            int opcion = readIntOption("Selección: ");
            
            if (opcion == 1) {
                if (nuevoRol == TipoCuenta.ADMIN) {
                    // Si estamos promoviendo a admin, necesitamos un sustituto
                    promoverAAdmin(cuenta);
                } else {
                    // Si estamos revirtiendo a profesor
                    revertirAProfesor(cuenta);
                }
            }
        } catch (AccountException e) {
            System.err.println("Error al modificar: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void promoverAAdmin(Account profesor) throws AccountException {
        clearScreen();
        mostrarMensajeCentrado("==== ASIGNAR SUSTITUTO ====");
        
        try {
            // Obtener lista de profesores disponibles (excluyendo al que se promueve)
            List<Account> profesores = accountController.getAllAccounts().stream()
                    .filter(a -> a.getTipoCuenta() == TipoCuenta.PROFESOR)
                    .filter(a -> !a.getId().equals(profesor.getId()))
                    .toList();
            
            if (profesores.isEmpty()) {
                System.out.println("No hay otros profesores disponibles para asignar como sustitutos.");
                return;
            }
            
            System.out.println("\nProfesores disponibles como sustitutos:");
            for (int i = 0; i < profesores.size(); i++) {
                Account p = profesores.get(i);
                System.out.printf("%d. %s %s (%s)%n", i+1, p.getNombre(), p.getApellido(), p.getUser());
            }
            
            int seleccion = readIntOption("\nSeleccione el sustituto (0 para cancelar): ") - 1;
            if (seleccion < 0 || seleccion >= profesores.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            
            Account sustituto = profesores.get(seleccion);
            
            // Solicitar fechas de sustitución
            System.out.print("\nFecha de inicio (AAAA-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Fecha de fin (dejar vacío para indefinido): ");
            String endDateStr = scanner.nextLine();
            LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);
            
            // Confirmar operación
            System.out.println("\nResumen de la operación:");
            System.out.println("Profesor a promover: " + profesor.getNombre() + " " + profesor.getApellido());
            System.out.println("Sustituto asignado: " + sustituto.getNombre() + " " + sustituto.getApellido());
            System.out.println("Período: " + startDate + " hasta " + (endDate != null ? endDate : "indefinido"));
            
            String confirmacion = readLine("\n¿Confirmar la operación? (S/N): ").toUpperCase();
            if (confirmacion.equals("S")) {
                accountController.promoteToAccount(profesor.getUser(), sustituto.getId(), startDate, endDate);
                System.out.println("Profesor promovido a administrador exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            throw new AccountException("Error al promover a administrador: " + e.getMessage());
        }
    }

    private void revertirAProfesor(Account admin) throws AccountException {
        try {
            // Verificar que el admin tenga un sustituto asignado
            if (!admin.hasSubstitute()) {
                System.out.println("Este administrador no tiene un sustituto asignado.");
                return;
            }
            
            // Confirmar operación
            System.out.println("\nResumen de la operación:");
            System.out.println("Administrador a revertir: " + admin.getNombre() + " " + admin.getApellido());
            
            String confirmacion = readLine("\n¿Confirmar la reversión a profesor? (S/N): ").toUpperCase();
            if (confirmacion.equals("S")) {
                accountController.revertToProfessor(admin.getUser());
                System.out.println("Administrador revertido a profesor exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (SubstituteException e) {
            throw new AccountException("Error al revertir a profesor: " + e.getMessage());
        }
    }

    private void eliminarCuenta() {
        clearScreen();
        String usuario = readLine("Ingrese el nombre de usuario a eliminar: ");
        
        try {
            System.out.println("\n¿Está seguro que desea eliminar la cuenta " + usuario + "?");
            System.out.println("1. Confirmar eliminación");
            System.out.println("0. Cancelar");
            
            int opcion = readIntOption("Selección: ");
            if (opcion == 1) {
                accountController.deleteAccount(usuario);
                System.out.println("Cuenta eliminada exitosamente.");
            }
        } catch (AccountException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void resetearContrasena() {
        clearScreen();
        String usuario = readLine("Ingrese el nombre de usuario: ");
        String nuevaContrasena = readLine("Ingrese la nueva contraseña: ");
        
        try {
            accountController.resetUserPassword(usuario, nuevaContrasena);
            System.out.println("Contraseña actualizada exitosamente.");
        } catch (AccountException e) {
            System.err.println("Error al resetear contraseña: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void verEstadisticas() {
        clearScreen();
        try {
            List<Account> cuentas = accountController.getAllAccounts();
            long total = cuentas.size();
            long admins = cuentas.stream().filter(a -> a.getTipoCuenta() == TipoCuenta.ADMIN).count();
            long profesores = cuentas.stream().filter(a -> a.getTipoCuenta() == TipoCuenta.PROFESOR).count();
            long estudiantes = cuentas.stream().filter(a -> a.getTipoCuenta() == TipoCuenta.ESTUDIANTE).count();
            
            System.out.println("\n=== ESTADÍSTICAS DE CUENTAS ===");
            System.out.println("Total de cuentas: " + total);
            System.out.println("Administradores: " + admins + " (" + (admins*100/total) + "%)");
            System.out.println("Profesores: " + profesores + " (" + (profesores*100/total) + "%)");
            System.out.println("Estudiantes: " + estudiantes + " (" + (estudiantes*100/total) + "%)");
        } catch (AccountException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void crearCuentaDocenteAdmin() {
        clearScreen();
        System.out.println("\n=== CREAR CUENTA DOCENTE/ADMIN ===");
        
        String nombre = readLine("Nombre: ");
        String apellido = readLine("Apellido: ");
        String telefono = readLine("Teléfono: ");
        String email = readLine("Email: ");
        String usuario = readLine("Usuario: ");
        String contrasena = readLine("Contraseña: ");
        
        System.out.println("\nTipo de cuenta:");
        System.out.println("1. Docente");
        System.out.println("2. Administrador");
        int tipo = readIntOption("Selección: ");
        
        if (tipo < 1 || tipo > 2) {
            System.out.println("Opción inválida.");
            return;
        }
        
        try {
            TipoCuenta tipoCuenta = (tipo == 1) ? TipoCuenta.PROFESOR : TipoCuenta.ADMIN;
            Account nuevaCuenta = new Account(nombre, apellido, telefono, email, usuario, contrasena, tipoCuenta);
            accountController.registerAccount(nuevaCuenta);
            System.out.println("Cuenta creada exitosamente.");
        } catch (IllegalArgumentException | AccountException e) {
            System.err.println("Error al crear cuenta: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void gestionSustituciones() {
    clearScreen();
    System.out.println("\n=== GESTIÓN DE SUSTITUCIONES ===");
    
    try {
        Substitute prototype = new Substitute();
        IFile<Substitute> fileHandler = new FileHandler<>(prototype);
        SubstituteController subController = new SubstituteController(fileHandler);
        
        while (true) {
            System.out.println("\n1. Promover profesor a administrador");
            System.out.println("2. Revertir administrador a profesor");
            System.out.println("3. Ver sustituciones activas");
            System.out.println("4. Finalizar sustitución");
            System.out.println("0. Volver al menú anterior");
            
            int opcion = readIntOption("Selección: ");
            
            switch (opcion) {
                case 1 -> promoverProfesorAAdmin(subController);
                case 2 -> revertirAdminAProfesor(subController);
                case 3 -> mostrarSustitucionesActivas(subController);
                case 4 -> finalizarSustitucion(subController);
                case 0 -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    } catch (Exception e) {
        System.err.println("Error en gestión de sustituciones: " + e.getMessage());
        readLine("\nPresione Enter para continuar...");
    }
}

    private void promoverProfesorAAdmin(SubstituteController subController) {
        clearScreen();
        System.out.println("\n=== PROMOVER PROFESOR A ADMINISTRADOR ===");
        
        try {
            // Obtener lista de profesores
            List<Account> profesores = accountController.getAllAccounts().stream()
                    .filter(a -> a.getTipoCuenta() == TipoCuenta.PROFESOR)
                    .toList();
            
            if (profesores.isEmpty()) {
                System.out.println("No hay profesores disponibles para promover.");
                return;
            }
            
            // Mostrar lista de profesores
            System.out.println("\nProfesores disponibles:");
            for (int i = 0; i < profesores.size(); i++) {
                Account prof = profesores.get(i);
                System.out.printf("%d. %s %s (%s)%n", i+1, prof.getNombre(), prof.getApellido(), prof.getUser());
            }
            
            int seleccion = readIntOption("\nSeleccione el profesor a promover (0 para cancelar): ") - 1;
            if (seleccion < 0 || seleccion >= profesores.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            
            Account profesor = profesores.get(seleccion);
            
            // Seleccionar sustituto
            List<Account> posiblesSustitutos = profesores.stream()
                    .filter(p -> !p.getId().equals(profesor.getId()))
                    .toList();
            
            if (posiblesSustitutos.isEmpty()) {
                System.out.println("No hay otros profesores disponibles como sustitutos.");
                return;
            }
            
            System.out.println("\nSeleccione el sustituto:");
            for (int i = 0; i < posiblesSustitutos.size(); i++) {
                Account sub = posiblesSustitutos.get(i);
                System.out.printf("%d. %s %s (%s)%n", i+1, sub.getNombre(), sub.getApellido(), sub.getUser());
            }
            
            int seleccionSub = readIntOption("\nSeleccione el sustituto (0 para cancelar): ") - 1;
            if (seleccionSub < 0 || seleccionSub >= posiblesSustitutos.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            
            Account sustituto = posiblesSustitutos.get(seleccionSub);
            
            // Fechas de sustitución
            System.out.print("\nFecha de inicio (AAAA-MM-DD): ");
            LocalDate startDate = LocalDate.parse(scanner.nextLine());
            
            System.out.print("Fecha de fin (dejar vacío para indefinido): ");
            String endDateStr = scanner.nextLine();
            LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);
            
            // Confirmar operación
            System.out.println("\nResumen de la operación:");
            System.out.println("Profesor a promover: " + profesor.getNombre() + " " + profesor.getApellido());
            System.out.println("Sustituto asignado: " + sustituto.getNombre() + " " + sustituto.getApellido());
            System.out.println("Período: " + startDate + " hasta " + (endDate != null ? endDate : "indefinido"));
            
            String confirmacion = readLine("\n¿Confirmar la operación? (S/N): ").toUpperCase();
            if (confirmacion.equals("S")) {
                accountController.promoteToAccount(profesor.getUser(), sustituto.getId(), startDate, endDate);
                System.out.println("Profesor promovido a administrador exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            System.err.println("Error al promover profesor: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void revertirAdminAProfesor(SubstituteController subController) {
        clearScreen();
        System.out.println("\n=== REVERTIR ADMINISTRADOR A PROFESOR ===");
        
        try {
            // Obtener administradores que fueron profesores
            List<Account> admins = accountController.getAllAccounts().stream()
                    .filter(a -> a.getTipoCuenta() == TipoCuenta.ADMIN && a.hasSubstitute())
                    .toList();
            
            if (admins.isEmpty()) {
                System.out.println("No hay administradores que puedan ser revertidos a profesores.");
                return;
            }
            
            // Mostrar lista de administradores
            System.out.println("\nAdministradores disponibles:");
            for (int i = 0; i < admins.size(); i++) {
                Account admin = admins.get(i);
                System.out.printf("%d. %s %s (%s)%n", i+1, admin.getNombre(), admin.getApellido(), admin.getUser());
            }
            
            int seleccion = readIntOption("\nSeleccione el administrador a revertir (0 para cancelar): ") - 1;
            if (seleccion < 0 || seleccion >= admins.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            
            Account admin = admins.get(seleccion);
            
            // Confirmar operación
            System.out.println("\nResumen de la operación:");
            System.out.println("Administrador a revertir: " + admin.getNombre() + " " + admin.getApellido());
            
            String confirmacion = readLine("\n¿Confirmar la reversión? (S/N): ").toUpperCase();
            if (confirmacion.equals("S")) {
                accountController.revertToProfessor(admin.getUser());
                System.out.println("Administrador revertido a profesor exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            System.err.println("Error al revertir administrador: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void mostrarSustitucionesActivas(SubstituteController subController) {
        clearScreen();
        System.out.println("\n=== SUSTITUCIONES ACTIVAS ===");
        
        try {
            List<Substitute> sustituciones = subController.getActiveSubstitutes();
            
            if (sustituciones.isEmpty()) {
                System.out.println("No hay sustituciones activas en este momento.");
            } else {
                for (Substitute sub : sustituciones) {
                    Account original = accountController.getById(sub.getOriginalTeacherId());
                    Account sustituto = accountController.getById(sub.getSubstituteTeacherId());
                    
                    System.out.println("\nProfesor original: " + 
                            (original != null ? original.getFullName() : "ID: " + sub.getOriginalTeacherId()));
                    System.out.println("Sustituto: " + 
                            (sustituto != null ? sustituto.getFullName() : "ID: " + sub.getSubstituteTeacherId()));
                    System.out.println("Período: " + sub.getStartDate() + " hasta " + 
                            (sub.getEndDate() != null ? sub.getEndDate() : "indefinido"));
                    System.out.println("Estado: " + (sub.isActive() ? "ACTIVO" : "INACTIVO"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener sustituciones: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }

    private void finalizarSustitucion(SubstituteController subController) {
        clearScreen();
        System.out.println("\n=== FINALIZAR SUSTITUCIÓN ===");
        
        try {
            List<Substitute> sustituciones = subController.getActiveSubstitutes();
            
            if (sustituciones.isEmpty()) {
                System.out.println("No hay sustituciones activas para finalizar.");
                return;
            }
            
            System.out.println("\nSustituciones activas:");
            for (int i = 0; i < sustituciones.size(); i++) {
                Substitute sub = sustituciones.get(i);
                Account original = accountController.getById(sub.getOriginalTeacherId());
                Account sustituto = accountController.getById(sub.getSubstituteTeacherId());
                
                System.out.printf("%d. %s -> %s (desde %s hasta %s)%n", 
                    i+1,
                    original != null ? original.getUser() : sub.getOriginalTeacherId(),
                    sustituto != null ? sustituto.getUser() : sub.getSubstituteTeacherId(),
                    sub.getStartDate(),
                    sub.getEndDate() != null ? sub.getEndDate() : "indefinido");
            }
            
            int seleccion = readIntOption("\nSeleccione la sustitución a finalizar (0 para cancelar): ") - 1;
            if (seleccion < 0 || seleccion >= sustituciones.size()) {
                System.out.println("Operación cancelada.");
                return;
            }
            
            Substitute sub = sustituciones.get(seleccion);
            
            String confirmacion = readLine("\n¿Confirmar finalización de la sustitución? (S/N): ").toUpperCase();
            if (confirmacion.equals("S")) {
                subController.endSubstitution(sub.getId());
                System.out.println("Sustitución finalizada exitosamente.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (Exception e) {
            System.err.println("Error al finalizar sustitución: " + e.getMessage());
        }
        readLine("\nPresione Enter para continuar...");
    }
}
