package unicorn.arubis.exceptions;

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
     /**
     * Excepción personalizada para el manejo de errores en el módulo de Preguntas Frecuentes (FAQ).
     * 
     * @param message Mensaje descriptivo del error (se recomienda usar los métodos factory)
     * @see #notFound() Factory method recomendado
     */
    public FaQException(String message) {
        super(message);
    }
     /**
     * Excepción estándar para cuando no se encuentra una pregunta en el FAQ.
     * @return FaQException preconfigurada con mensaje estándar 
     * @see FaQService#getQuestionById() Ejemplo de uso
     */
    public static FaQException notFound() {
        return new FaQException("Pregunta no encontrada");
    }
}