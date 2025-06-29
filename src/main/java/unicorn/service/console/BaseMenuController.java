package unicorn.service.console;

import unicorn.interfaces.*;
import java.util.Scanner;

/**
 * Clase base para controladores de menús en consola.
 * Proporciona utilidades comunes para la interacción con el usuario.
 */
public abstract class BaseMenuController implements IMenu {
    protected final Scanner scanner;

    public BaseMenuController() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Muestra un mensaje centrado en la consola.
     * @param mensaje El mensaje a mostrar.
     */
    protected void mostrarMensajeCentrado(String mensaje) {
        int longitudMaxima = 73;
        int longitudMensaje = mensaje.length();
        int espaciosIzquierda = (longitudMaxima - longitudMensaje) / 2;
        int espaciosDerecha = longitudMaxima - longitudMensaje - espaciosIzquierda;
        String lineaCentrada = "=".repeat(espaciosIzquierda) + mensaje + "=".repeat(espaciosDerecha);
        System.out.println(lineaCentrada);
    }

    /**
     * Lee una opción numérica del usuario.
     * @param message Mensaje para solicitar la entrada.
     * @return La opción seleccionada.
     */
    protected int readIntOption(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            }
        }
    }

    /**
     * Lee una línea de texto del usuario.
     * @param message Mensaje para solicitar la entrada.
     * @return La línea ingresada.
     */
    protected String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine();
    }

    /**
     * Limpia la pantalla de la consola.
     */
    protected void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    @Override
    public abstract void showMenu();
}