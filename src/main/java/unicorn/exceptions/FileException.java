package unicorn.exceptions;

/**
 * Clase que representa excepciones específicas relacionadas con operaciones de archivos.
 * Proporciona mensajes de error predefinidos para situaciones comunes, como errores de lectura o escritura.
 * 
 * @description Funcionalidades principales:
 *                   - Lanzar una excepción cuando ocurre un error al leer un archivo.
 *                   - Lanzar una excepción cuando ocurre un error al escribir en un archivo.
 *  
 * @autor KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public class FileException extends Exception {
    public FileException(String message) {
        super(message);
    }
    
    public static FileException readError() {
        return new FileException("Error al leer el archivo");
    }
    public static FileException writeError() {
        return new FileException("Error al escribir en el archivo");
    }
}