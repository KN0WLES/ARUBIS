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
public class FaQException extends Exception {
    public FaQException(String message) {
        super(message);
    }

    public static FaQException notFound() {
        return new FaQException("Pregunta no encontrada");
    }

    public static FaQException duplicateQuestion() {
        return new FaQException("La pregunta ya existe");
    }

    public static FaQException invalidCategory() {
        return new FaQException("Categoría de pregunta inválida");
    }

    public static FaQException emptyField() {
        return new FaQException("Los campos no pueden estar vacíos");
    }
}