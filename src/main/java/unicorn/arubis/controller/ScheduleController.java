package unicorn.arubis.controller;

import unicorn.arubis.model.Schedule;
import unicorn.arubis.exceptions.ScheduleException;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.interfaces.ISchedule;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Controlador para la gestión de horarios académicos.
 * Permite administrar la asignación de aulas, materias y horarios, evitando conflictos de tiempo.
 * 
 * @implSpec Implementa la interfaz {@link ISchedule} para operaciones básicas de horarios.
 * @see Schedule Modelo que representa un horario con sus propiedades.
 * @see ScheduleException Excepciones específicas del módulo de horarios.
 */

public class ScheduleController implements ISchedule {
    private final IFile<Schedule> fileHandler;
    private final String filePath = "src/main/java/unicorn/arubis/dto/schedules.txt";
    private Map<String, Schedule> schedules;
     /**
     * Constructor que inicializa el controlador cargando horarios desde persistencia.
     * 
     * @param fileHandler Manejador de archivos para persistencia (debe implementar {@link IFile<Schedule>}).
     * @throws ScheduleException Si ocurre un error al cargar los datos iniciales.
     */
    public ScheduleController(IFile<Schedule> fileHandler) throws ScheduleException {
        this.fileHandler = fileHandler;
        this.schedules = new HashMap<>();
        loadData();
    }
     /**
     * Carga los horarios desde el archivo de persistencia.
     * 
     * @throws ScheduleException Si el archivo no puede leerse o contiene datos inválidos.
     * @see #saveData() Método complementario para guardar datos.
     */
    private void loadData() throws ScheduleException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<Schedule> loadedSchedules = fileHandler.loadData(filePath);
            if (loadedSchedules != null) {
                loadedSchedules.forEach(s -> schedules.put(s.getId(), s));
            }
        } catch (Exception e) {
            throw new ScheduleException("Error al cargar horarios: " + e.getMessage());
        }
    }
     /**
     * Guarda los horarios en el archivo de persistencia.
     * 
     * @throws ScheduleException Si falla la escritura en el archivo.
     * @see #loadData() Método complementario para cargar datos.
     */
    private void saveData() throws ScheduleException {
        try {
            fileHandler.saveData(new ArrayList<>(schedules.values()), filePath);
        } catch (Exception e) {
            throw new ScheduleException("Error al guardar horarios: " + e.getMessage());
        }
    }
     /**
     * Agrega un nuevo horario al sistema, verificando conflictos de tiempo.
     * 
     * @param schedule Instancia de {@link Schedule} a registrar (no nula).
     * @throws ScheduleException Si:
     * - Hay un conflicto de horario ({@link ScheduleException#timeConflict()})
     * - Fallo al guardar los cambios
     * 
     * @see #checkTimeConflict(Schedule) Para la verificación de conflictos.
     */
    @Override
    public void addSchedule(Schedule schedule) throws ScheduleException {
        if (checkTimeConflict(schedule)) {
            throw ScheduleException.timeConflict();
        }
        schedules.put(schedule.getId(), schedule);
        saveData();
    }
     /**
     * Recupera un horario por su ID único.
     * 
     * @param id Identificador único del horario (formato UUID).
     * @return Instancia de {@link Schedule} correspondiente.
     * @throws ScheduleException Si no se encuentra el horario ({@link ScheduleException#notFound()}).
     */
    @Override
    public Schedule getScheduleById(String id) throws ScheduleException {
        Schedule schedule = schedules.get(id);
        if (schedule == null) throw ScheduleException.notFound();
        return schedule;
    }
     /**
     * Actualiza un horario existente, verificando conflictos de tiempo.
     * 
     * @param schedule Instancia de {@link Schedule} con los nuevos datos (debe contener ID existente).
     * @throws ScheduleException Si:
     * - El horario no existe ({@link ScheduleException#notFound()})
     * - Hay un conflicto de horario ({@link ScheduleException#timeConflict()})
     * 
     * @see #addSchedule(Schedule) Para crear nuevos horarios.
     */
    @Override
    public void updateSchedule(Schedule schedule) throws ScheduleException {
        if (!schedules.containsKey(schedule.getId())) {
            throw ScheduleException.notFound();
        }
        if (checkTimeConflict(schedule)) {
            throw ScheduleException.timeConflict();
        }
        schedules.put(schedule.getId(), schedule);
        saveData();
    }
     /**
     * Elimina un horario del sistema.
     * 
     * @param id Identificador único del horario a eliminar.
     * @throws ScheduleException Si no se encuentra el horario ({@link ScheduleException#notFound()}).
     */
    @Override
    public void deleteSchedule(String id) throws ScheduleException {
        if (!schedules.containsKey(id)) {
            throw ScheduleException.notFound();
        }
        schedules.remove(id);
        saveData();
    }
     /**
     * Obtiene todos los horarios para un día específico.
     * 
     * @param day Día de la semana (case-insensitive, formato esperado: "Lunes", "Martes", etc.).
     * @return Lista de horarios para el día especificado (vacía si no hay resultados).
     */
    @Override
    public List<Schedule> getSchedulesByDay(String day) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getDiaSemana().equalsIgnoreCase(day))
                .collect(Collectors.toList());
    }
     /**
     * Obtiene todos los horarios asignados a un aula específica.
     * 
     * @param classroomId ID del aula (formato UUID).
     * @return Lista de horarios para el aula especificada.
     * @see RoomController Para la gestión de aulas.
     */
    @Override
    public List<Schedule> getSchedulesByClassroom(String classroomId) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getClassroomId().equals(classroomId))
                .collect(Collectors.toList());
    }
     /**
     * Obtiene todos los horarios asignados a una materia específica.
     * 
     * @param subjectId ID de la materia (formato UUID).
     * @return Lista de horarios para la materia especificada.
     */
    @Override
    public List<Schedule> getSchedulesBySubject(String subjectId) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getMateriaId().equals(subjectId))
                .collect(Collectors.toList());
    }
     /**
     * Verifica si un horario propuesto entra en conflicto con horarios existentes.
     * 
     * @param schedule Horario a verificar.
     * @return true si existe conflicto con otro horario en la misma aula y día.
     * @throws ScheduleException Si hay errores en el procesamiento.
     * 
     * @see #timeOverlap(Schedule, Schedule) Para la lógica de detección de superposición.
     */
    @Override
    public boolean checkTimeConflict(Schedule schedule) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getClassroomId().equals(schedule.getClassroomId()))
                .filter(s -> s.getDiaSemana().equalsIgnoreCase(schedule.getDiaSemana()))
                .anyMatch(s -> timeOverlap(s, schedule));
    }
     /**
     * Determina si dos horarios se superponen en el tiempo.
     * 
     * @param s1 Primer horario a comparar.
     * @param s2 Segundo horario a comparar.
     * @return true si los intervalos de tiempo se superponen.
     * 
     * @implNote Compara las propiedades horaInicio y horaFin de ambos horarios.
     * La implementación actual siempre retorna false (debe ser implementada).
     */
    private boolean timeOverlap(Schedule s1, Schedule s2) {
        // Implementación de lógica para detectar superposición de horarios
        // Comparar horaInicio y horaFin de ambos horarios
        // Retorna true si hay superposición
        return false; // Implementar lógica real
    }
}