package unicorn.arubis.controller;

import unicorn.arubis.model.Base;
import unicorn.arubis.exceptions.FileException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase genérica para manejar operaciones de lectura y escritura de archivos.
 * Permite guardar, cargar, agregar datos y verificar la existencia de archivos.
 * 
 * @param <T> Tipo genérico que extiende de la clase Base, utilizado para definir el formato de los datos.
 * 
 * @description Funcionalidades principales:
 *                  - Guardar una lista de datos en un archivo.
 *                  - Cargar datos desde un archivo.
 *                  - Agregar un dato al final de un archivo existente.
 *                  - Verificar si un archivo existe.
 *                  - Crear un archivo si no existe.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see Base
 * @see IFile
 * @see FileException
 */
public class FileHandler<T extends Base<T>> implements IFile<T> {
    
    private final T prototype;
    
    public FileHandler(T prototype) {
        this.prototype = prototype;
    }
     /**
     * Guarda una lista de objetos en un archivo, usando el formato generado por el método toFile() de cada objeto.
     * @throws FileException Si ocurre algún error de IO (permisos, ruta inválida, etc)
     * 
     * @see FileWriter
     * @see BufferedWriter
     * @see FileException#writeError()
     */
    @Override
    public void saveData(List<T> data, String filePath) throws FileException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                writer.write(item.toFile());// Serializa cada objeto
                writer.newLine(); // Agrega salto de línea
            }
        } catch (IOException e) {
            throw FileException.writeError();// Encapsula error específico
        }
    }
     /**
     * Carga una lista de objetos desde un archivo, usando el método fromFile() del prototipo.
     * @return Lista de objetos cargados (lista vacía si no existe el archivo)
     * @throws FileException Si ocurre error de lectura (permisos, formato inválido, etc)
     * 
     * @see #fileExists(String) Verificación previa de existencia
     * @see Prototype#fromFile(String) Método de deserialización
     */
    @Override
    public List<T> loadData(String filePath) throws FileException {
        List<T> data = new ArrayList<>();
        
        if (!fileExists(filePath)) return data;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) data.add(prototype.fromFile(line));
            }
        } catch (IOException e) {
            throw FileException.readError();
        }
        
        return data;
    }
     /**
     * Agrega un nuevo registro al final de un archivo existente en formato de anexo (append).
     * @throws FileException Si ocurren errores de IO (permisos, ruta inválida, etc)
     * 
     * @see FileWriter#FileWriter(String, boolean) Constructor con modo append
     * @see BufferedWriter Para escritura eficiente
     */
    @Override
    public void appendData(T data, String filePath) throws FileException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data.toFile());
            writer.newLine();
        } catch (IOException e) {
            throw FileException.writeError();
        }
    }
     /**
     * Verifica la existencia de un archivo en el sistema de archivos.
     * @return true si el archivo existe, false en caso contrario
     * 
     * @see Files#exists(Path, LinkOption...)
     * @see Paths#get(String, String...)
     */
    @Override
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
     /**
     * Crea un archivo nuevo incluyendo su estructura de directorios si no existe previamente.
     * @throws FileException Si:
     *                      - No se tienen permisos adecuados
     *                      - La ruta contiene caracteres inválidos
     *                      - Ocurren errores de IO durante la creación
     * 
     * @see Files#createDirectories() Creación recursiva de directorios
     * @see Files#createFile() Creación segura de archivos
     */
    @Override
    public void createFileIfNotExists(String filePath) throws FileException {
        if (!fileExists(filePath)) {
            try {
                Path path = Paths.get(filePath);
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            } catch (IOException e) {
                throw new FileException("No se pudo crear el archivo: " + e.getMessage());
            }
        }
    }
}