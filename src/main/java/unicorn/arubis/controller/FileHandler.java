package unicorn.arubis.controller;

import unicorn.arubis.model.Base;
import unicorn.arubis.exceptions.FileException;
import unicorn.arubis.interfaces.IFile;

import java.io.*;
import java.nio.file.Files;
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
     /**
     * Constructor que inicializa el manejador con un prototipo del objeto.
     * El prototipo se usa para deserializar líneas del archivo.
     * 
     * @param prototype Instancia de {@link T} con métodos de serialización/deserialización.
     */
    public FileHandler(T prototype) {
        this.prototype = prototype;
    }
     /**
     * Guarda una lista de objetos en un archivo, sobrescribiendo su contenido.
     * Cada objeto se serializa con {@link Base#toFile()} y se escribe en una línea.
     * 
     * @param data     Lista de objetos a guardar (no nula).
     * @param filePath Ruta del archivo (formato: sistema operativo dependiente).
     * @throws FileException Si falla la escritura ({@link FileException#writeError()}).
     */
    @Override
    public void saveData(List<T> data, String filePath) throws FileException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                writer.write(item.toFile());
                writer.newLine();
            }
        } catch (IOException e) {
            throw FileException.writeError();
        }
    }
     /**
     * Carga objetos desde un archivo, deserializando cada línea con {@link Base#fromFile(String)}.
     * - Ignora líneas vacías o con espacios.
     * - Retorna una lista vacía si el archivo no existe.
     * 
     * @param filePath Ruta del archivo.
     * @return Lista de objetos cargados (puede estar vacía).
     * @throws FileException Si falla la lectura ({@link FileException#readError()}).
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
     * Añade un objeto al final del archivo sin sobrescribir.
     * 
     * @param data     Objeto a añadir (no nulo).
     * @param filePath Ruta del archivo.
     * @throws FileException Si falla la escritura ({@link FileException#writeError()}).
     * @see #saveData(List, String) Para sobrescritura completa.
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
     * Verifica si un archivo existe en la ruta especificada.
     * 
     * @param filePath Ruta a verificar.
     * @return `true` si el archivo existe, `false` en caso contrario.
     */
    @Override
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
     /**
     * Crea un archivo y sus directorios padres si no existen.
     * 
     * @param filePath Ruta del archivo a crear.
     * @throws FileException Si falla la creación (ej: permisos insuficientes).
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