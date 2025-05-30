package unicorn.arubis.exceptions;

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
     /**
     * Excepción especializada para errores en operaciones de archivos.
     * @param message Descripción detallada del error (usar factory methods para mensajes estándar)
     * @see #readError()
     * @see #writeError() 
     */
    public FileException(String message) {
        super(message);
    }
     /**
     * Excepción estándar para fallos en operaciones de lectura de archivos.
     * @return FileException con mensaje estandarizado
     * @see FileHandler#loadData() Ejemplo de uso
     */
    public static FileException readError() {
        return new FileException("Error al leer el archivo");
    }
     /**
     * Excepción estándar para fallos en operaciones de escritura de archivos.
     * @return FileException con mensaje estandarizado
     * @see FileHandler#saveData() Ejemplo de uso
     */
    public static FileException writeError() {
        return new FileException("Error al escribir en el archivo");
    }
}