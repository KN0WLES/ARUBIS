package unicorn.arubis.exceptions;

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
     /**
     * Excepción especializada para errores en la gestión de habitaciones.
     * 
     * @param message Descripción detallada del error (usar factory methods para mensajes estándar)
     * @see #notFound()
     * @see #invalidType()
     * @see #isOccupied()
     */
    public RoomException(String message) {
        super(message);
    }
     /**
     * Excepción estándar para cuando no se encuentra una habitación.
     * @return RoomException con mensaje estandarizado
     * @see RoomService#findById() Ejemplo de uso
     */
    public static RoomException notFound() {
        return new RoomException("Habitación no encontrada");
    }
     /**
     * Excepción para tipos de habitación no válidos.
     * @return RoomException con mensaje que especifica los tipos válidos
     * @see RoomValidator#validateType() Validación asociada
     */
    public static RoomException invalidType() {
        return new RoomException("Tipo de habitación inválido (use I, P, D o M)");
    }
     /**
     * Excepción para operaciones no permitidas en habitaciones ocupadas.
     * @return RoomException con mensaje descriptivo
     * @see RoomService#checkIn() Ejemplo de uso
     */
    public static RoomException isOccupied() {
        return new RoomException("La habitación está ocupada");
    }
}