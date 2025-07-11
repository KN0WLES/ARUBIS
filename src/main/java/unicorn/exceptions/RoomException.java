package unicorn.exceptions;

/**
 * Clase que actúa como controlador para la gestión de cuentas de usuario.
 * Proporciona la lógica de negocio necesaria para registrar, iniciar sesión, actualizar, eliminar cuentas
 * y gestionar privilegios de administrador.
 * Los datos de las cuentas se almacenan y recuperan desde un archivo utilizando un manejador de archivos genérico.
 * 
 * @description Funcionalidades principales:
 *                   - Lanzar una excepción cuando una habitación no es encontrada.
 *                   - Lanzar una excepción cuando el tipo de habitación es inválido.
 *                   - Lanzar una excepción cuando la habitación está ocupada.
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public class RoomException extends Exception {
    public RoomException(String message) {
        super(message);
    }

    public static RoomException notFound() {
        return new RoomException("Aula no encontrada");
    }

    public static RoomException invalidType() {
        return new RoomException("Tipo de aula inválido");
    }

    public static RoomException occupied() {
        return new RoomException("El aula está ocupada");
    }

    public static RoomException capacityExceeded() {
        return new RoomException("Capacidad del aula excedida");
    }

}