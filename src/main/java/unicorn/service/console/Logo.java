package unicorn.service.console;

import java.util.concurrent.TimeUnit;

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
public class Logo {
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
