package unicorn.controller;


import unicorn.interfaces.*;
import unicorn.exceptions.*;
import unicorn.model.*;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Controlador para la gestión de materias académicas.
 * Permite administrar las asignaturas, sus profesores y horarios asociados.
 * 
 * @implSpec Implementa la interfaz {@link ISubject} para operaciones básicas de materias.
 * @see Subject Modelo que representa una materia con sus propiedades.
 * @see SubjectException Excepciones específicas del módulo de materias.
 * @see IPeriod Interfaz para gestión de horarios (dependencia).
 */

public class SubjectController implements ISubject {
    private final IFile<Subject> fileHandler;
    private final String filePath = "src/main/java/unicorn/dto/subjects.txt";
    private Map<String, Subject> subjects;
   /**
     * Constructor que inicializa el controlador cargando materias desde persistencia.
     * 
     * @param fileHandler Manejador de archivos para persistencia (implementa {@link IFile<Subject>}).
     * @param scheduleController Controlador de horarios (implementa {@link IPeriod}).
     * @throws SubjectException Si ocurre un error al cargar los datos iniciales.
     */
    public SubjectController(IFile<Subject> fileHandler) throws SubjectException {
        this.fileHandler = fileHandler;
        this.subjects = new HashMap<>();
        loadData();
    }
    /**
     * Carga las materias desde el archivo de persistencia.
     * 
     * @throws SubjectException Si el archivo no puede leerse o contiene datos inválidos.
     * @see #saveData() Método complementario para guardar datos.
     */
    private void loadData() throws SubjectException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<Subject> loadedSubjects = fileHandler.loadData(filePath);
            if (loadedSubjects != null) {
                loadedSubjects.forEach(s -> subjects.put(s.getId(), s));
            }
        } catch (Exception e) {
            throw new SubjectException("Error al cargar materias: " + e.getMessage());
        }
    }
    /**
     * Persiste los cambios actuales en el archivo de almacenamiento.
     * 
     * @throws SubjectException Si falla la escritura en el archivo.
     * @see #loadData() Método complementario para cargar datos.
     */
    private void saveData() throws SubjectException {
        try {
            fileHandler.saveData(new ArrayList<>(subjects.values()), filePath);
        } catch (Exception e) {
            throw new SubjectException("Error al guardar materias: " + e.getMessage());
        }
    }
    /**
     * Registra una nueva materia en el sistema.
     * 
     * @param subject Instancia de {@link Subject} a registrar (no nula).
     * @throws SubjectException Si:
     * - Ya existe una materia con el mismo nombre ({@link SubjectException#alreadyExists()})
     * - Fallo al guardar los cambios
     * 
     * @see #updateSubject(Subject) Para modificar materias existentes.
     */
    @Override
    public void addSubject(Subject subject) throws SubjectException {
        if (subjects.values().stream().anyMatch(s -> s.getNombre().equalsIgnoreCase(subject.getNombre()))) {
            throw SubjectException.alreadyExists();
        }
        subjects.put(subject.getId(), subject);
        saveData();
    }
    /**
     * Recupera una materia por su ID único.
     * 
     * @param id Identificador único de la materia (formato UUID).
     * @return Instancia de {@link Subject} correspondiente.
     * @throws SubjectException Si no se encuentra la materia ({@link SubjectException#notFound()}).
     */
    @Override
    public Subject getSubjectById(String id) throws SubjectException {
        Subject subject = subjects.get(id);
        if (subject == null) throw SubjectException.notFound();
        return subject;
    }
    /**
     * Recupera una materia por su nombre (case-insensitive).
     * 
     * @param name Nombre completo de la materia.
     * @return Instancia de {@link Subject} correspondiente.
     * @throws SubjectException Si no se encuentra la materia ({@link SubjectException#notFound()}).
     */
    @Override
    public Subject getSubjectByName(String name) throws SubjectException {
        return subjects.values().stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(SubjectException::notFound);
    }
    /**
     * Actualiza los datos de una materia existente.
     * 
     * @param subject Instancia de {@link Subject} con los nuevos datos (debe contener ID existente).
     * @throws SubjectException Si:
     * - La materia no existe ({@link SubjectException#notFound()})
     * - Fallo al guardar los cambios
     * 
     * @see #addSubject(Subject) Para crear nuevas materias.
     */
    @Override
    public void updateSubject(Subject subject) throws SubjectException {
        if (!subjects.containsKey(subject.getId())) {
            throw SubjectException.notFound();
        }
        subjects.put(subject.getId(), subject);
        saveData();
    }
    /**
     * Elimina una materia del sistema.
     * 
     * @param id Identificador único de la materia a eliminar.
     * @throws SubjectException Si no se encuentra la materia ({@link SubjectException#notFound()}).
     * 
     * @implNote También elimina los horarios asociados a través del ScheduleController.
     */
    @Override
    public void deleteSubject(String id) throws SubjectException {
        if (!subjects.containsKey(id)) {
            throw SubjectException.notFound();
        }
        subjects.remove(id);
        saveData();
    }
    /**
     * Obtiene todas las materias asignadas a un profesor.
     * 
     * @param professorId ID del profesor (formato UUID).
     * @return Lista de materias asignadas (vacía si no hay resultados).
     */
    @Override
    public List<Subject> getSubjectsByProfessor(String professorId) throws SubjectException {
        return subjects.values().stream()
                .filter(s -> s.getId().equals(professorId))
                .collect(Collectors.toList());
    }
    /**
     * Obtiene todas las materias registradas en el sistema.
     * 
     * @return Lista no modificable de todas las materias.
     */
    @Override
    public List<Subject> getAllSubjects() throws SubjectException {
        return new ArrayList<>(subjects.values());
    }
}