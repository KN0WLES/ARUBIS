package unicorn.arubis.model;

import java.util.UUID;

/**
 * Clase que representa el modelo de un horario en el sistema.
 * Gestiona información completa de horarios incluyendo día de la semana,
 * hora de inicio, hora de fin y la materia asociada.
 * 
 * @description Funcionalidades principales:
 *                   - Crear horarios con validación de formato de tiempo.
 *                   - Asociar horarios a materias específicas.
 *                   - Serializar y deserializar horarios para almacenamiento.
 * 
 * @see Base
 */
public class Schedule extends Base<Schedule> {
    private String id;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private String materiaId;
    private String classroomId;

    /**
     * Constructor vacío para serialización.
     */
    public Schedule(){}
    
    /**
     * Constructor principal para crear un horario.
     *
     * @param diaSemana Día de la semana
     * @param horaInicio Hora de inicio
     * @param horaFin Hora de fin
     * @param materiaId ID de la materia asociada
     * @param classroomId
     */
    public Schedule(String diaSemana, String horaInicio, String horaFin, String materiaId, String classroomId) {
        this.id = UUID.randomUUID().toString();
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.materiaId = materiaId;
        this.classroomId = classroomId;
    }

    // Métodos para obtener información
    @Override
    public String getId() { return id; }
    public String getDiaSemana() { return diaSemana; }
    public String getHoraInicio() { return horaInicio; }
    public String getHoraFin() { return horaFin; }
    public String getMateriaId() { return materiaId; }
    public String getClassroomId() { return classroomId; }

    // Métodos para modificar el horario
    public void setDiaSemana(String diaSemana) { this.diaSemana = diaSemana; }
    public void setHoraInicio(String horaInicio) { this.horaInicio = horaInicio; }
    public void setHoraFin(String horaFin) { this.horaFin = horaFin; }
    public void setMateriaId(String materiaId) { this.materiaId = materiaId; }
    public void setClassroomId(String classroomId) { this.classroomId = classroomId; }

    @Override
    public String toFile() {
        return String.join("|", id, diaSemana, horaInicio, horaFin, materiaId, classroomId);
    }

    @Override
    public Schedule fromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Formato de línea incorrecto para deserializar Schedule");
        }
        Schedule schedule = new Schedule(parts[1], parts[2], parts[3], parts[4], parts[5]);
        schedule.id = parts[0];
        return schedule;
    }

    @Override
    public String getInfo() {
        return String.format("Horario: %s %s-%s\nMateria ID: %s\nClassroom ID: %s", diaSemana, horaInicio, horaFin, materiaId, classroomId);
    }
}