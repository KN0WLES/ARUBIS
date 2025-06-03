package unicorn.exceptions;

/**
 * Clase que representa excepciones específicas relacionadas con la gestión de preguntas frecuentes (FAQs).
 * Proporciona mensajes de error predefinidos para situaciones comunes, como preguntas no encontradas.
 * 
 * @description Funcionalidades principales:
 *                   - Lanzar una excepción cuando una pregunta no es encontrada.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public class NewsException extends Exception {
    public NewsException(String message) {
        super(message);
    }

    public static NewsException notFound() {
        return new NewsException("Notificación no encontrada");
    }

    public static NewsException invalidType() {
        return new NewsException("Tipo de notificación inválido");
    }

    public static NewsException emptyMessage() {
        return new NewsException("El mensaje no puede estar vacío");
    }
}