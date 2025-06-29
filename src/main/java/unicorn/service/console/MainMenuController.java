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
    private ExcelHorarioLoader excelLoader;
    private Logo logo;
    private Scanner scanner;

    public MainMenuController() throws FaQException, AccountException, SubjectException, RoomException, NewsException, PeriodException {
        this.scanner = new Scanner(System.in);
        this.logo = new Logo();

        // Inicializar accountController primero
        initializeAccountController();
        run();
    }

    private void initializeAccountController() throws FaQException, AccountException {
        try {
            // Inicializar controladores base
            IFile<Account> accountFileHandler = new FileHandler<>(new Account());
            IFile<Substitute> substituteFileHandler = new FileHandler<>(new Substitute());
            IFile<FaQ> faqFileHandler = new FileHandler<>(new FaQ());
            INews newsController = new NewsController(new FileHandler<>(new News()), "system");

            this.accountController = new AccountController(accountFileHandler, substituteFileHandler, newsController, faqFileHandler);
        } catch (Exception e) {
            System.err.println("Error initializing account controller: " + e.getMessage());
            throw new AccountException("Failed to initialize system: " + e.getMessage());
        }
    }

    private void logo (){
        //Mostrar animación de inicio
        //logo.loadingEffect();
        //logo.mostrarLogo();
        //logo.details();
    }

    public void run() throws FaQException, AccountException, SubjectException, RoomException, NewsException, PeriodException {
        logo();
        waitForValidLogin(); // Fase 1
        initializeControllers(); // Fase 2
        showMainMenu();
    }

    private void waitForValidLogin() throws FaQException, AccountException, SubjectException, RoomException, NewsException, PeriodException {
        while (currentAccount == null) {
            showLoginMenu();
        }
    }


    private void initializeControllers() {
        try {
            // Inicializar controladores
            IFile<Account> accountFileHandler = new FileHandler<>(new Account());
            IFile<Substitute> substituteFileHandler = new FileHandler<>(new Substitute());
            IFile<FaQ> faqFileHandler = new FileHandler<>(new FaQ());
            IFile<Subject> subjectFileHandler = new FileHandler<>(new Subject());
            IFile<Room> roomFileHandler = new FileHandler<>(new Room());
            IFile<Schedule> scheduleFileHandler = new FileHandler<>(new Schedule());
            IFile<Period> periodFileHandler = new FileHandler<>(new Period());
            INews newsController = new NewsController(new FileHandler<>(new News()), currentAccount.getUser());

            this.accountController = new AccountController(accountFileHandler, substituteFileHandler, newsController, faqFileHandler);
            this.accountMenu = new AccountMenuController(accountController, currentAccount);

            RoomController roomController = new RoomController(roomFileHandler);
            SubjectController subjectController = new SubjectController(subjectFileHandler);
            ScheduleController scheduleController = new ScheduleController(scheduleFileHandler, new PeriodController(roomController, periodFileHandler));
 

            this.excelLoader = new ExcelHorarioLoader(
                accountController, 
                subjectController, 
                roomController, 
                scheduleController
            );
            excelLoader.cargarExcelSiCorresponde("src/main/java/unicorn/resources/resources/DATOS_ARUBIS.xlsx");
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
            } catch (AccountException | FaQException | NewsException | RoomException | PeriodException | SubjectException e) {
                System.out.println("Error al mostrar el menú de inicio de sesión: " + e.getMessage());
                readLine("Presione Enter para continuar...");
            }
        } else {
            showMainMenu();
        }
    }

    private void showLoginMenu()  throws AccountException, FaQException, NewsException, RoomException, PeriodException, SubjectException{
        while (currentAccount == null) {
            clearScreen();
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
        boolean exit = false;
        while (!exit && currentAccount != null) {
            clearScreen();
            mostrarMensajeCentrado(" MENU PRINCIPAL ");
            System.out.println("Usuario: " + currentAccount.getUser() +
                    " | Rol: " + currentAccount.getTipoCuenta().getDescripcion());
            mostrarMensajeCentrado("====================================");

            if (currentAccount.getTipoCuenta() == TipoCuenta.ADMIN) {
                System.out.println("1. Mi Cuenta");
                System.out.println("2. Gestión de Horarios");
                System.out.println("3. Gestión de Cuentas");
                System.out.println("4. Gestión de Aulas");
                System.out.println("5. Gestión de Comunicados");
                System.out.println("6. Ver todos los horarios");
                System.out.println("7. Preguntas Frecuentes");
                System.out.println("8. Cerrar sesión");
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
                System.out.println("4. Aulas asignadas");
                System.out.println("5. Notificaciones");
                System.out.println("6. Ayuda y soporte");
                System.out.println("7. Cerrar sesión");
                System.out.println("0. Salir");
            }

            int option = readIntOption("Seleccione una opción: ");
            handleMainMenuOption(option);
        }
    }

    private void handleMainMenuOption(int option) {
        try {
            if (currentAccount == null) {
                showMenu(); // Si no hay cuenta, muestra el menú de inicio
                return;
            }

            switch (currentAccount.getTipoCuenta()) {
                case ADMIN -> handleAdminMenu(option);
                case PROFESOR -> handleProfesorMenu(option);
                case ESTUDIANTE -> handleEstudianteMenu(option);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            readLine("Presione Enter para continuar...");
        }
    }

    private void handleAdminMenu(int option) {
        switch (option) {
            case 1 -> accountMenu.showCommonMenu();
            case 2 -> scheduleMenu.showMenu();
            case 3 -> accountMenu.showMenu();
            case 4 -> roomMenu.showMenu();
            case 5 -> newsMenu.showMenu();
            case 6 -> scheduleMenu.showMenu();
            case 7 -> faqMenu.showMenu();
            case 8 -> cargarDesdeExcel();
            case 9 -> logout();
            case 0 -> {
                System.out.println("¡Gracias por usar el sistema!");
                System.exit(0);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private void handleProfesorMenu(int option) {
        switch (option) {
            case 1 -> accountMenu.showCommonMenu();
            case 2 -> scheduleMenu.showMenu();
            case 4 -> scheduleMenu.showMenu();
            case 5 -> newsMenu.showMenu();
            case 6 -> faqMenu.showMenu();
            case 7 -> logout();
            case 0 -> {
                System.out.println("¡Gracias por usar el sistema!");
                System.exit(0);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private void handleEstudianteMenu(int option) {
        switch (option) {
            case 1 -> accountMenu.showCommonMenu();
            case 2 -> scheduleMenu.showMenu();
            case 3 -> subjectMenu.showMenu();
            case 4 -> roomMenu.showMenu();
            case 5 -> newsMenu.showMenu();
            case 6 -> faqMenu.showMenu();
            case 7 -> logout();
            case 0 -> {
                System.out.println("¡Gracias por usar el sistema!");
                System.exit(0);
            }
            default -> System.out.println("Opción inválida");
        }
    }

    private void login()  throws AccountException, FaQException, NewsException, RoomException, PeriodException, SubjectException{
        Console console = System.console();
        try {
            String username, password;

            if (console == null) {
                System.out.println("AVISO: Para mayor seguridad, ejecute desde una terminal real (no desde un IDE)");
                System.out.println("Continuando con entrada por Scanner...");
                
                System.out.print("Usuario: ");
                username = scanner.nextLine();
                System.out.print("Contraseña: ");
                password = scanner.nextLine();
            } else {
                System.out.print("Usuario: ");
                username = console.readLine();
                char[] pwdArray = console.readPassword("Contraseña: ");
                password = new String(pwdArray);
            }

            currentAccount = accountController.login(username, password);
            initializeUserMenus();
            System.out.println("¡Bienvenido " + currentAccount.getNombre() + "!");
            readLine("Presione Enter para continuar...");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            readLine("Presione Enter para continuar...");
        }
    }

    private void initializeUserMenus() throws AccountException, FaQException, NewsException, RoomException, PeriodException, SubjectException, ScheduleException, FileException {
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
            Console console = System.console();
            if (console != null) {
                char[] pwdArray = console.readPassword("Contraseña: ");
                password = new String(pwdArray);
            } else {
                System.out.print("Contraseña: ");
                password = scanner.nextLine();
            }

            Account newAccount = new Account(nombre, apellido, phone, email, username, password, TipoCuenta.ESTUDIANTE);

            if (!AccountValidation.validateRoleEmail(email, TipoCuenta.ESTUDIANTE)) {
                System.out.println("Error: Email debe terminar en @est.umss.edu");
                return;
            }

            accountController.registerAccount(newAccount);
            System.out.println("¡Registro exitoso! Por favor inicie sesión.");
        } catch (AccountException e) {
            System.out.println("Error: " + e.getMessage());
        }
        readLine("Presione Enter para continuar...");
    }

    private void logout() {
        currentAccount = null;
        System.out.println("Sesión cerrada exitosamente");
        readLine("Presione Enter para continuar...");
        showMenu();
    }

    private void cargarDesdeExcel() {
        String filePath = readLine("Ruta del archivo Excel: ");
        try {
            excelLoader.cargarExcelSiCorresponde(filePath);
            System.out.println("Datos cargados exitosamente desde Excel");
        } catch (Exception e) {
            System.out.println("Error al cargar desde Excel: " + e.getMessage());
        }
        readLine("Presione Enter para continuar...");
    }

    public static void main(String[] args) throws FaQException, AccountException, SubjectException, RoomException, NewsException, PeriodException {
        new MainMenuController();
    }
}