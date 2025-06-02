package unicorn.model;

import java.util.UUID;
import java.util.HashMap;

/**
 * Clase que representa el modelo de una materia en el sistema.
 * Gestiona información completa de materias incluyendo nombre,
 * descripción, créditos y profesor asociado.
 * 
 * @description Funcionalidades principales:
 *                   - Crear materias con validación de datos.
 *                   - Asociar materias a profesores específicos.
 *                   - Serializar y deserializar materias para almacenamiento.
 * 
 * @see Base
 */
public class Subject extends Base<Subject> {
    private String id;
    private String nombre;
    private String descripcion;
    private int creditos;
    private String profesorId;
    private HashMap<String, Schedule> horarios;

    /**
     * Constructor vacío para serialización.
     */
    public Subject(){
        this.horarios = new HashMap<>();
    }
    
    /**
     * Constructor principal para crear una materia.
     *
     * @param nombre Nombre de la materia
     * @param descripcion Descripción de la materia
     * @param creditos Número de créditos
     * @param profesorId ID del profesor asociado
     */
    public Subject(String nombre, String descripcion, int creditos, String profesorId) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.profesorId = profesorId;
        this.horarios = new HashMap<>();
    }

    // Métodos para obtener información
    @Override
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCreditos() { return creditos; }
    public String getProfesorId() { return profesorId; }
    public Schedule getSchedule(String id) { return horarios.get(id); }

    // Métodos para modificar la materia
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }
    public void addSchedule(String id, Schedule schedule) { horarios.put(id, schedule); }
    public void removeSchedule(String id) { horarios.remove(id); }
    

    @Override
    public String toFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("|", id, nombre, descripcion, String.valueOf(creditos), profesorId));
        for (Schedule schedule : horarios.values()) {
            sb.append("|").append(schedule.toFile());
        }
        return sb.toString();
    }

    @Override
    public Subject fromFile(String line) {
        String[] parts = line.split("\\|");
        Subject subject = new Subject(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
        subject.id = parts[0];
        
        // Deserializar los horarios
        for (int i = 5; i < parts.length; i++) {
            Schedule schedule = new Schedule();
            schedule = schedule.fromFile(parts[i]);
            subject.horarios.put(schedule.getId(), schedule);
        }
        
        return subject;
    }

    @Override
    public String getInfo() {
        return String.format("Materia: %s\nDescripción: %s\nCréditos: %d\nProfesor ID: %s", nombre, descripcion, creditos, profesorId);
    }
}