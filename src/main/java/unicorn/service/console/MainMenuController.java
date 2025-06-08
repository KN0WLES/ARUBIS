package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.model.*;
import unicorn.util.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.io.Console;

/**
 * Clase principal que controla el menú principal del sistema.
 * Coordina la navegación entre los diferentes módulos y gestiona
 * la sesión del usuario actual.
 *
 * @description Funcionalidades principales:
 *                   - Gestionar el inicio de sesión y registro de usuarios.
 *                   - Coordinar la navegación entre módulos del sistema.
 *                   - Mostrar menús específicos según el rol del usuario.
 *                   - Mantener el estado de la sesión actual.
 *                   - Inicializar y coordinar los controladores secundarios.
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see AccountMenuController
 * @see FaQController
 * @see NewsController
 * @see RoomController
 * @see ScheduleController
 * @see SubjectController
 * @see Account
 */
public class MainMenuController {
    private AccountMenuController accountMenu;
    private FaQMenuController faqMenu;
    private NewsMenuController NewsMenu;
    private RoomMenuController roomMenu;
    private ScheduleMenuController ScheduleMenu;
    private SubjectMenuController SubjectMenu;
    private Scanner scanner;
    private Console console;
    private Account currentAccount;
    private Logo logo;


    public MainMenuController() {
        try {
            this.currentAccount = null;
            this.accountMenu = new AccountMenuController(currentAccount);
            this.NewsMenu = new NewsMenuController(currentAccount);
            this.SubjectMenu = new SubjectMenuController(currentAccount);
            this.faqMenu = new FaQMenuController(currentAccount);
            this.ScheduleMenu = new ScheduleMenuController(currentAccount);
            this.roomMenu = new RoomMenuController(currentAccount);
            this.scanner = new Scanner(System.in);
            this.console = System.console();
            this.logo = new Logo();
        } catch (AccountException | FaQException | NewsException | RoomException | ScheduleException |
                 SubjectException | SubstituteException e) {
            System.err.println("Error initializing menus: " + e.getMessage());
        }
    }

    public MainMenuController(Account account) {
        try {
            this.currentAccount = account;
            this.accountMenu = new AccountMenuController(currentAccount);
            this.NewsMenu = new NewsMenuController(currentAccount);
            this.SubjectMenu = new SubjectMenuController(currentAccount);
            this.faqMenu = new FaQMenuController(currentAccount);
            this.ScheduleMenu = new ScheduleMenuController(currentAccount);
            this.roomMenu = new RoomMenuController(currentAccount);
            this.scanner = new Scanner(System.in);
            this.console = System.console();
            this.logo = new Logo();
        } catch (AccountException | FaQException | NewsException | RoomException | ScheduleException |
                 SubjectException | SubstituteException e) {
            System.err.println("Error initializing menus: " + e.getMessage());
        }
    }

    public void start() {
        int option = -1;
        logo.loadingEffect();
        logo.mostrarLogo();
        logo.details();
        do {
            if (currentAccount == null) {
                showLoginMenu();
            } else {
                showMainMenu();
                option = readIntOption("Seleccione una opción: ");
                handleMainMenuOption(option);
            }
        } while (option != 0);
    }

    public void go() {
        int option = -1;
        logo.loadingEffect();
        //logo.mostrarLogo();
        logo.details();
        do {
            if (currentAccount == null) {
                showLoginMenu();
            } else {
                showMainMenu();
                option = readIntOption("Seleccione una opción: ");
                handleMainMenuOption(option);
            }
        } while (option != 0);
    }

    private void showLoginMenu() {
        mostrarMensajeCentrado("=");
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

    private void showMainMenu() {
        mostrarMensajeCentrado(" MENU PRINCIPAL ");
        if (currentAccount.getTipoCuenta() == TipoCuenta.ADMIN){
            System.out.println("1. Gestión de Cuentas");
            System.out.println("2. Gestión de Horarios");
            System.out.println("3. Gestión de Aulas");
            System.out.println("4. Gestión de Comunicados");
            System.out.println("5. Preguntas Frecuentes");
            System.out.println("6. Mi Cuenta");
            System.out.println("7. Cerrar sesión");
            System.out.println("0. Salir");
        } else if (currentAccount.getTipoCuenta() == TipoCuenta.PROFESOR) {
            System.out.println("1. Mi Cuenta");
            System.out.println("2. Mi Horario");
            System.out.println("4. Cambio de horarios");
            System.out.println("5. Comunicados por materia");
            System.out.println("6. Ayuda y soporte");
            System.out.println("7. Cerrar sesión");
            System.out.println("0. Salir");
        }else if (currentAccount.getTipoCuenta() == TipoCuenta.ESTUDIANTE) {
            System.out.println("1. Mi Cuenta");
            System.out.println("2. Mis Horario");
            System.out.println("3. Mis Materias");
            System.out.println("4. Mis Profesores");
            System.out.println("5. Aulas asignadas");
            System.out.println("6. Notificaciones");
            System.out.println("7. Ayuda y soporte");
            System.out.println("8. Cerrar sesión");
            System.out.println("0. Salir");
        }
    }

    private void handleMainMenuOption(int option) {
        try {
            if (currentAccount.getTipoCuenta() == TipoCuenta.ADMIN) {
                switch (option) {
                    case 1 -> accountMenu.showAdmMenu();
                    case 2 -> ScheduleMenu.showAdmMenu();
                    case 3 -> roomMenu.showAdmMenu();
                    case 4 -> NewsMenu.showAdmMenu();
                    case 5 -> faqMenu.showAdmMenu();
                    case 6 -> accountMenu.showMyAccount(currentAccount);
                    case 7 -> logout();
                    case 0 -> {
                        System.out.println("¡Gracias por usar el sistema!");
                        System.exit(0);
                    }
                    default -> System.out.println("Opción inválida");
                }
            } else if (currentAccount.getTipoCuenta() == TipoCuenta.PROFESOR){
                switch (option) {
                    case 1 -> accountMenu.showMyAccount(currentAccount);
                    case 2 -> NewsMenu.showPrfMenu();
                    case 3 -> SubjectMenu.showPrfMenu();
                    case 4 -> roomMenu.showPrfMenu();
                    case 5 -> ScheduleMenu.showPrfMenu();
                    case 6 -> faqMenu.showPrfMenu();
                    case 7 -> logout();
                    case 0 -> {
                        System.out.println("¡Gracias por usar el sistema!");
                        System.exit(0);
                    }
                    default -> System.out.println("Opción inválida");
                }
            } else if (currentAccount.getTipoCuenta() == TipoCuenta.ESTUDIANTE) {
                switch (option) {
                    case 1 -> accountMenu.showMyAccount(currentAccount);
                    case 2 -> NewsMenu.showEstMenu();
                    case 3 -> SubjectMenu.showEstMenu();
                    case 4 -> roomMenu.showEstMenu();
                    case 5 -> ScheduleMenu.showEstMenu();
                    case 6 -> faqMenu.showEstMenu();
                    case 7 -> logout();
                    case 0 -> {
                        System.out.println("¡Gracias por usar el sistema!");
                        System.exit(0);
                    }
                    default -> System.out.println("Opción inválida");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void login() {
        if (console == null) {
            System.out.println("Error: la consola no está disponible. Ejecute desde una terminal.");
            return;
        }

        int intentos = 0;
        final int MAX_INTENTOS = 3;

        while (intentos < MAX_INTENTOS) {
            try {
                System.out.print("Usuario: ");
                String username = console.readLine();

                char[] pwdArray = console.readPassword("Contraseña: ");
                String password = new String(pwdArray);

                currentAccount = accountMenu.login(username, password);
                if (currentAccount != null) {
                    // ... inicialización exitosa ...
                    return;
                }
            } catch (AccountException e) {
                intentos++;
                System.out.println("\nError: " + e.getMessage());
                System.out.println("Intento " + intentos + " de " + MAX_INTENTOS);

                if (intentos >= MAX_INTENTOS) {
                    System.out.println("Demasiados intentos fallidos. Saliendo...");
                    System.exit(0);
                }
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                return;
            }
        }
    }

    private void register() {
        boolean success = false;
        do {
            try {
                Account newAccount = accountMenu.collectRegistrationData(scanner, console);

                // Validación adicional si es necesario
                if (!AccountValidation.validateRoleEmail(newAccount.getEmail(), TipoCuenta.ESTUDIANTE)) {
                    System.out.println("Error: Email debe terminar en @est.umss.edu");
                    continue;
                }

                accountMenu.registerAccount(newAccount);
                success = true;
                System.out.println("¡Registro exitoso!");
            } catch (AccountException e) {
                System.out.println("Error: " + e.getMessage());
                System.out.println("Por favor intente nuevamente");
            } catch (IllegalArgumentException e) {
                System.out.println("Error en los datos: " + e.getMessage());
            }
        } while (!success);
    }

    private void logout() {
        currentAccount = null;
        mostrarMensajeCentrado("Sesión cerrada exitosamente");
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

    public void mostrarMensajeCentrado(String mensaje) {
        int longitudMaxima = 73;
        int longitudMensaje = mensaje.length();
        int espaciosIzquierda = (longitudMaxima - longitudMensaje) / 2;
        int espaciosDerecha = longitudMaxima - longitudMensaje - espaciosIzquierda;
        String lineaCentrada = "=".repeat(espaciosIzquierda) + mensaje + "=".repeat(espaciosDerecha);
        System.out.println(lineaCentrada);
    }

    public static void main(String[] args) {
        MainMenuController mainMenu = new MainMenuController();
        mainMenu.start();
    }
}

/**
 * Clase que gestiona la presentación del logo y animación inicial.
 * Proporciona efectos visuales y animaciones para mejorar la experiencia
 * de usuario al iniciar el sistema.
 *
 * @description Funcionalidades principales:
 *                   - Mostrar el logo animado del sistema.
 *                   - Proporcionar efectos visuales de carga.
 *                   - Gestionar colores y estilos de texto en consola.
 *                   - Controlar tiempos de animación.
 *                   - Manejar excepciones de interrupción.
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
class Logo {
    public static final String RESET = "\u001B[0m";
    public static final String YELLOW = "\u001B[33m";
    public static final String CYAN = "\u001B[36m";
    public static final String GREEN = "\u001B[32m";
    public static final String PURPLE = "\u001B[35m";
    public static final String RED = "\u001B[31m";
    public static final String STAR = "\u2605";
    private static final String VERSION = "V1.4.0";
    private static final String BUILD_DATE = "29/04/2025";

    public void mostrarLogo() {
        String[] logo = {
                YELLOW + "╔═════════════════════════════════════════════════════════════════════════╗" + RESET,
                YELLOW + "║                                                                         ║" + RESET,
                YELLOW + "║" + CYAN + "  ██╗      ███████╗ ██╗   ██╗ ███████╗ ██╗      ██╗ ███╗   ██╗  ██████╗  " + YELLOW + "║" + RESET,
                YELLOW + "║" + CYAN + "  ██║      ██╔════╝ ██║   ██║ ██╔════╝ ██║      ██║ ████╗  ██║ ██╔════╝  " + YELLOW + "║" + RESET,
                YELLOW + "║" + CYAN + "  ██║      █████╗   ██║   ██║ █████╗   ██║      ██║ ██╔██╗ ██║ ██║  ███╗ " + YELLOW + "║" + RESET,
                YELLOW + "║" + CYAN + "  ██║      ██╔══╝   ╚██╗ ██╔╝ ██╔══╝   ██║      ██║ ██║╚██╗██║ ██║   ██║ " + YELLOW + "║" + RESET,
                YELLOW + "║" + CYAN + "  ███████╗ ███████╗  ╚████╔╝  ███████╗ ███████╗ ██║ ██║ ╚████║ ╚██████╔╝ " + YELLOW + "║" + RESET,
                YELLOW + "║" + CYAN + "  ╚══════╝ ╚══════╝   ╚═══╝   ╚══════╝ ╚══════╝ ╚═╝ ╚═╝  ╚═══╝  ╚═════╝  " + YELLOW + "║" + RESET,
                YELLOW + "║" + "                                                                         " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                           ██╗   ██╗ ██████╗                             " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                           ██║   ██║ ██╔══██╗                            " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                           ██║   ██║ ██████╔╝                            " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                           ██║   ██║ ██╔═══╝                             " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                           ╚██████╔╝ ██║                                 " + YELLOW + "║" + RESET,
                YELLOW + "║" + GREEN + "                            ╚═════╝  ╚═╝                                 " + YELLOW + "║" + RESET,
                YELLOW + "║" + "                                                                         " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ██╗      ██╗ ███████╗ ███████╗                       " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ██║      ██║ ██╔════╝ ██╔════╝                       " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ██║      ██║ █████╗   █████╗                         " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ██║      ██║ ██╔══╝   ██╔══╝                         " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ███████╗ ██║ ██║      ███████╗                       " + YELLOW + "║" + RESET,
                YELLOW + "║" + PURPLE + "                    ╚══════╝ ╚═╝ ╚═╝      ╚══════╝                       " + YELLOW + "║" + RESET,
                YELLOW + "║" + "                                                                         " + YELLOW + "║" + RESET,
                YELLOW + "╚═════════════════════════════════════════════════════════════════════════╝" + RESET,
                "",
        };

        for (String line : logo) {
            System.out.println(line);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void details(){
        String[] logo = {
                CYAN + "                          "+STAR+" ¡BIENVENIDO! "+STAR + RESET,
                PURPLE + "                    Version: " + VERSION + " - Build: " + BUILD_DATE + RESET,
                PURPLE + "                         Copyright © 2025 KNOWLES" + RESET
        };

        for (String line : logo) {
            System.out.println(line);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void loadingEffect() {
        System.out.println(RED + "Iniciando sistema..." + RESET);
        String loading = "█";

        System.out.print("Cargando: [          ] 0%");

        for (int i = 0; i <= 100; i += 10) {
            try {
                Thread.sleep(500);
                System.out.print("\rCargando: [");
                for (int j = 0; j < i/10; j++) {
                    System.out.print(GREEN + loading + RESET);
                }
                for (int j = 0; j < 10 - i/10; j++) {
                    System.out.print(" ");
                }
                System.out.print("] " + i + "%");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\n" + GREEN + "¡Sistema cargado con éxito!" + RESET);
        System.out.println();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}