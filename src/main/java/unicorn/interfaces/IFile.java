package unicorn.interfaces;

import unicorn.exceptions.FileException;
import java.util.List;

/**
 * Interfaz genérica para la gestión de archivos.
 * Define los métodos necesarios para guardar, cargar y manipular datos en archivos,
 * proporcionando una capa de abstracción para las operaciones de entrada/salida.
 * 
 * @description Funcionalidades principales:
 *                   - Guardar colecciones de datos en archivos.
 *                   - Cargar datos desde archivos.
 *                   - Agregar datos a archivos existentes.
 *                   - Verificar existencia de archivos.
 *                   - Crear archivos si no existen.
 * 
 * Ejemplo de uso:
 * <pre>
 *     IFile<Account> fileHandler = new JsonFileHandler<>();
 *     List<Account> accounts = new ArrayList<>();
 *     accounts.add(new Account("user1", "password123", ...));
 *     fileHandler.saveData(accounts, "accounts.json");
 * </pre>
 * 
 * @param <T> Tipo de datos a manejar en el archivo.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public interface IFile<T> {
    
    /**
     * Guarda una lista de datos en un archivo.
     *                      El método sobrescribe el contenido del archivo si ya existe.
     *                      El formato de serialización depende de la implementación (JSON, XML, binario, etc.).
     *
     * @param data Lista de datos a guardar.
     * @param filePath Ruta del archivo.
     * @throws FileException Si ocurre un error al guardar los datos.
     */
    void saveData(List<T> data, String filePath) throws FileException;
    
    /**
     * Carga una lista de datos desde un archivo.
     *                      El formato de deserialización depende de la implementación (JSON, XML, binario, etc.).
     *
     * @param filePath Ruta del archivo.
     * @return Lista de datos cargados.
     * @throws FileException Si ocurre un error al leer el archivo.
     */
    List<T> loadData(String filePath) throws FileException;
    
    /**
     * Agrega un dato al final del archivo.
     *                      A diferencia de saveData, este método no sobrescribe el contenido existente,
     *                      sino que añade el nuevo dato al final del archivo.
     *
     * @param data Dato a agregar.
     * @param filePath Ruta del archivo.
     * @throws FileException Si ocurre un error al escribir en el archivo.
     */
    void appendData(T data, String filePath) throws FileException;
    
    /**
     * Verifica si un archivo existe.
     *                      Útil para validar la existencia del archivo antes de intentar operaciones de lectura.
     *
     * @param filePath Ruta del archivo.
     * @return true si el archivo existe, false en caso contrario.
     */
    boolean fileExists(String filePath);
    
    /**
     * Crea un archivo si no existe.
     *                      Este método es útil para inicializar archivos de datos vacíos.
     *                      Si el archivo ya existe, no realiza ninguna acción.
     *
     * @param filePath Ruta del archivo.
     * @throws FileException Si ocurre un error al crear el archivo.
     */
    void createFileIfNotExists(String filePath) throws FileException;
}