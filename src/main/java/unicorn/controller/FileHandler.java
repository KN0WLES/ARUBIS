package unicorn.controller;

import unicorn.model.Base;
import unicorn.exceptions.FileException;
import unicorn.interfaces.IFile;

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
    
    public FileHandler(T prototype) {
        this.prototype = prototype;
    }

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

    @Override
    public void appendData(T data, String filePath) throws FileException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(data.toFile());
            writer.newLine();
        } catch (IOException e) {
            throw FileException.writeError();
        }
    }

    @Override
    public boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

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