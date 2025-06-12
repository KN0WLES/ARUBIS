package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.IFile;
import unicorn.interfaces.INews;
import unicorn.model.*;
import unicorn.util.*;
import java.util.Scanner;
import java.io.Console;

public class MainMenuController extends BaseMenuController {
    private AccountController accountController;
    private AccountMenuController accountMenu;
    private FaQMenuController faqMenu;
    private NewsMenuController newsMenu;
    private RoomMenuController roomMenu;
    private ScheduleMenuController scheduleMenu;
    private SubjectMenuController subjectMenu;
    private Account currentAccount;
    private Logo logo;
    private Scanner scanner;

    public MainMenuController() {
        this.scanner = new Scanner(System.in);
        this.logo = new Logo();
        initializeControllers();
        this.accountMenu = new AccountMenuController(accountController, null);
    }

    private void initializeControllers() {
        try {
            // Mostrar animación de inicio
            logo.loadingEffect();
            logo.mostrarLogo();
            logo.details();
            

            // Inicializar controladores
            IFile<Account> accountFileHandler = new FileHandler<>(new Account());
            IFile<Substitute> substituteFileHandler = new FileHandler<>(new Substitute());
            IFile<FaQ> faqFileHandler = new FileHandler<>(new FaQ());
            INews newsController = new NewsController(new FileHandler<>(new News()), null);

            this.accountController = new AccountController(accountFileHandler, substituteFileHandler, newsController, faqFileHandler);

            // Mostrar menú de login
            showLoginMenu();

        } catch (Exception e) {
            System.err.println("Error initializing system: " + e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void showMenu() {
        clearScreen();
        if (currentAccount == null) {
            try {
                showLoginMenu();
            } catch (AccountException | FaQException | NewsException | RoomException | ScheduleException | SubjectException e) {
                System.out.println("Error al mostrar el menú de inicio de sesión: " + e.getMessage());
                readLine("Presione Enter para continuar...");
            }
        } else {
            showMainMenu();
        }
    }

    private void showLoginMenu()  throws AccountException, FaQException, NewsException, RoomException, ScheduleException, SubjectException{
        while (currentAccount == null) {
            clearScreen();
            logo.mostrarLogo();
            mostrarMensajeCentrado(" SISTEMA DE GESTIÓN ACADÉMICA ");
            System.out.println("1. Iniciar sesión");
            System.out.println("2. Registrarse");
            System.out.println("0. Salir");

            int option = readIntOption("Seleccione una opción: ");
            switch (option) {
                case 1 -> login();
                case 2 -> register();
                case 0 -> System.exit(0);
                default -> System.out.println("Opción inválida");
            }
        }
    }

    private void showMainMenu() {
        while (currentAccount != null) {
            clearScreen();
            mostrarMensajeCentrado(" MENU PRINCIPAL ");
            System.out.println("Usuario: " + currentAccount.getUser() +
                    " | Rol: " + currentAccount.getTipoCuenta().getDescripcion());
            System.out.println("====================================");

            if (currentAccount.getTipoCuenta() == TipoCuenta.ADMIN) {
                System.out.println("1. Mi Cuenta");
                System.out.println("2. Gestión de Horarios");
                System.out.println("3. Gestión de Profesores");
                System.out.println("4. Gestión de Estudiantes");
                System.out.println("5. Gestión de Aulas");
                System.out.println("6. Gestión de Comunicados");
                System.out.println("7. Ver todos los horarios");
                System.out.println("8. Preguntas Frecuentes");
                System.out.println("9. Cerrar sesión");
                System.out.println("0. Salir");
            } else if (currentAccount.getTipoCuenta() == TipoCuenta.PROFESOR) {
                System.out.println("1. Mi Cuenta");
                System.out.println("2. Mi Horario");
                System.out.println("4. Cambio de horarios");
                System.out.println("5. Comunicados por materia");
                System.out.println("6. Ayuda y soporte");
                System.out.println("7. Cerrar sesión");
                System.out.println("0. Salir");
            } else if (currentAccount.getTipoCuenta() == TipoCuenta.ESTUDIANTE) {
                System.out.println("1. Mi Cuenta");
                System.out.println("2. Mis Horarios");
                System.out.println("3. Mis Materias");
                System.out.println("4. Mis Profesores");
                System.out.println("5. Aulas asignadas");
                System.out.println("6. Notificaciones");
                System.out.println("7. Ayuda y soporte");
                System.out.println("8. Cerrar sesión");
                System.out.println("0. Salir");
            }

            int option = readIntOption("Seleccione una opción: ");
            handleMainMenuOption(option);
        }
    }

    private void handleMainMenuOption(int option) {
        try {
            if (currentAccount == null) return;

            switch (option) {
                case 1 -> accountMenu.showCommonMenu();
                case 3 -> newsMenu.showMenu();
                case 6 -> faqMenu.showMenu();
                case 7, 8, 9 -> logout();
                case 0 -> {
                    System.out.println("¡Gracias por usar el sistema!");
                    System.exit(0);
                }
                default -> System.out.println("Opción inválida");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            readLine("Presione Enter para continuar...");
        }
    }

    private void login()  throws AccountException, FaQException, NewsException, RoomException, ScheduleException, SubjectException{
        Console console = System.console();
        if (console == null) {
            System.out.println("AVISO: Para mayor seguridad, ejecute desde una terminal real (no desde un IDE)");
            System.out.println("Continuando con entrada por Scanner...");

            System.out.print("Usuario: ");
            String username = scanner.nextLine();

            System.out.print("Contraseña: ");
            String password = scanner.nextLine();

            try {
                currentAccount = accountController.login(username, password);
                initializeUserMenus();
                System.out.println("¡Bienvenido " + currentAccount.getNombre() + "!");
                readLine("Presione Enter para continuar...");
            } catch (AccountException e) {
                System.out.println("Error: " + e.getMessage());
                readLine("Presione Enter para continuar...");
            }
        } else {
            try {
                System.out.print("Usuario: ");
                String username = console.readLine();

                char[] pwdArray = console.readPassword("Contraseña: ");
                String password = new String(pwdArray);

                currentAccount = accountController.login(username, password);
                initializeUserMenus();
                System.out.println("¡Bienvenido " + currentAccount.getNombre() + "!");
                readLine("Presione Enter para continuar...");
            } catch (AccountException e) {
                System.out.println("Error: " + e.getMessage());
                readLine("Presione Enter para continuar...");
            }
        }
    }

    private void initializeUserMenus() throws AccountException, FaQException, NewsException, RoomException, ScheduleException, SubjectException {
        if (currentAccount == null) return;

        this.accountMenu = new AccountMenuController(accountController, currentAccount);
        this.newsMenu = new NewsMenuController(currentAccount);
        this.subjectMenu = new SubjectMenuController(currentAccount);
        this.faqMenu = new FaQMenuController(currentAccount);
        this.scheduleMenu = new ScheduleMenuController(currentAccount);
        this.roomMenu = new RoomMenuController(currentAccount);
    }

    private void register() {
        try {
            Account newAccount = accountMenu.collectRegistrationData(scanner, System.console());

            if (!AccountValidation.validateRoleEmail(newAccount.getEmail(), TipoCuenta.ESTUDIANTE)) {
                System.out.println("Error: Email debe terminar en @est.umss.edu");
                return;
            }

            accountController.registerAccount(newAccount);
            System.out.println("¡Registro exitoso! Por favor inicie sesión.");
        } catch (AccountException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error en los datos: " + e.getMessage());
        }
        readLine("Presione Enter para continuar...");
    }

    private void logout() {
        currentAccount = null;
        System.out.println("Sesión cerrada exitosamente");
        readLine("Presione Enter para continuar...");
    }

    public static void main(String[] args) {
        new MainMenuController().showMenu();
    }
}