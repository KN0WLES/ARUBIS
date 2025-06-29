package unicorn.model;

import java.util.UUID;

/**
 * Clase que representa el modelo de una materia en el sistema.
 * Solo contiene información propia de la materia.
 */
public class Subject extends Base<Subject> {
    private String id;
    private String nombre;
    private String descripcion;
    private int creditos;
    private String tipo; // Ejemplo: "Teórico", "Laboratorio", "Ambos"

    public Subject() {}

    public Subject(String nombre, String descripcion, int creditos, String tipo) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creditos = creditos;
        this.tipo = tipo;
    }

    @Override
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getCreditos() { return creditos; }
    public String getTipo() { return tipo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCreditos(int creditos) { this.creditos = creditos; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    @Override
    public String toFile() {
        return String.join("|", id, nombre, descripcion, String.valueOf(creditos), tipo);
    }

    @Override
    public Subject fromFile(String line) {
        String[] parts = line.split("\\|");
        Subject subject = new Subject(parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
        subject.id = parts[0];
        return subject;
    }

    @Override
    public String getInfo() {
        return String.format("Materia: %s\nDescripción: %s\nCréditos: %d\nTipo: %s", nombre, descripcion, creditos, tipo);
    }
}