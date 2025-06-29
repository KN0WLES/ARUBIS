package unicorn.model;

import unicorn.util.TipoRoom;
import java.util.UUID;
/**
 * Clase que representa el modelo de un aula en el sistema.
 * Gestiona información completa de aulas incluyendo nombre (que contiene ubicación),
 * tipo (física/virtual), capacidad y estado de disponibilidad.
 * 
 * @description Funcionalidades principales:
 *                   - Crear aulas con validación de formato de nombre (EBPA: Edificio-Bloque-Piso-Aula).
 *                   - Gestionar estados de disponibilidad (L: Libre, O: Ocupado, M: Mantenimiento).
 *                   - Manejar aulas físicas y virtuales, con casos especiales para laboratorios y auditorios.
 *                   - Serializar y deserializar aulas para almacenamiento.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see Base
 */

public class Room extends Base<Room> {
    private String id;
    private String nombre;      // Formato: "692A" (Bloque 6, Edificio 9, Piso 2, Aula A)
    private char disponible;    // 'L': Libre, 'O': Ocupado, 'M': Mantenimiento
    private int capacidad;
    private boolean tieneProyector;
    private TipoRoom tipo; // Tipo de aula: Virtual, Física, Laboratorio, Auditorio

    /**
     * Constructor vacío para serialización.
     */
    public Room(){}
    
    /**
     * Constructor principal para crear un aula.
     *
     * @param nombre Nombre del aula (ej. "E1-B2-P3-AulaA", "Laboratorio", "Auditorio", "Virtual-Sala1").
     * @param esFisica Indica si el aula es física (true) o virtual (false).
     * @param capacidad Cantidad máxima de estudiantes
     * @param tieneProyector Si cuenta con proyector
     */
    public Room(String nombre, TipoRoom tipo, int capacidad, boolean tieneProyector) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.tieneProyector = tieneProyector;
        this.disponible = 'L';
        
        // Auditorios y laboratorios siempre tienen proyector
        if (tipo == TipoRoom.AUDITORIO || tipo == TipoRoom.LABORATORIO) {
            this.tieneProyector = true;
        }
    }

    /**
     * Constructor especial para laboratorios.
     *
     * @param bloque Número de bloque
     * @param edificio Número de edificio
     * @param piso Número de piso
     * @param numLab Número de laboratorio
     * @param capacidad Cantidad máxima de estudiantes
     * @return 
     */
    public static Room crearLaboratorio(int bloque, int edificio, int piso, int numLab, int capacidad) {
        String nombre = String.format("%d%d%dL%d", bloque, edificio, piso, numLab);
        return new Room(nombre, TipoRoom.LABORATORIO, capacidad, true);
    }

    /**
     * Constructor especial para auditorios.
     *
     * @param edificio Número de edificio
     * @param capacidad Cantidad máxima de estudiantes
     * @return 
     */
    public static Room crearAuditorio(int edificio, int capacidad) {
        String nombre = String.format("AUD-%d", edificio);
        return new Room(nombre, TipoRoom.AUDITORIO, capacidad, true);
    }

    /**
     * Obtiene el identificador único de la habitación.
     * @return ID de la habitación
     */
    @Override
    public String getId() { return id; }
    
    /**
     * Obtiene el nombre de la habitación.
     * @return Nombre de la habitación
     */
    public String getNombre() { return nombre; }
    
    /**
     * Obtiene el estado de disponibilidad de la habitación.
     * @return Estado: 'L' (Libre), 'O' (Ocupado), 'M' (Mantenimiento)
     */
    public char getDisponible() { return disponible; }

    /**
     * Obtiene la capacidad máxima de estudiantes del aulas.
     * @return Capacidad máxima de estudiantes
     */
    public int getCapacidad() { return capacidad; }

    /**
     * Verifica si el aula cuenta con proyector.
     * @return true si tiene proyector, false en caso contrario
     */
    public boolean isTieneProyector() { return tieneProyector; }

    /**
     * Obtiene el tipo de aula.
     * Los tipos pueden ser: AULA, VIRTUAL, LABORATORIO o AUDITORIO.
     * 
     * @return El tipo de aula como enum TipoRoom
     * @see TipoRoom Para los tipos disponibles
     */
    public TipoRoom getTipo() { return tipo; }

    /**
     * Establece la capacidad máxima de estudiantes del aula.
     * @param capacidad Nueva capacidad máxima de estudiantes
     * @throws IllegalArgumentException Si la capacidad es menor o igual a 0
     */
    public void setCapacidad(int capacidad) {
        if (capacidad <= 0)
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        this.capacidad = capacidad;
    }
    public void setNombre(String nombre){
         if(nombre == null || nombre.isEmpty()){
            throw new IllegalArgumentException("El nombre del aula no puede ser nula o vacio");

         }
    }

    /**
     * Establece si el aula cuenta con proyector.
     * @param tieneProyector true si se añade proyector, false si se retira
     */
    public void setTieneProyector(boolean tieneProyector) {
        this.tieneProyector = tieneProyector;
    }

    /**
     * Modifica el tipo de aula.
     * Permite adaptar un aula normal a diferentes usos:
     * 'N': Aula Normal
     * 'L': Laboratorio
     * 'A': Auditorio
     * 
     * @param tipo Nuevo tipo de aula ('N', 'L' o 'A')
     * @throws IllegalArgumentException Si el tipo no es válido
     */
    public void setTipo(TipoRoom tipo) {
        if (tipo == null)
            throw new IllegalArgumentException("El tipo de aula no puede ser null");
        
        this.tipo = tipo;
        
        // Actualizar el nombre según el nuevo tipo
        switch (tipo) {
            case LABORATORIO -> {
                // Convertir a formato de laboratorio
                String numeros = nombre.replaceAll("[^0-9]", "");
                if (numeros.length() >= 3) {
                    this.nombre = numeros + "L1"; // Asigna como primer laboratorio por defecto
                }
            }
            case AUDITORIO -> {
                // Convertir a formato de auditorio
                String edificio = nombre.substring(1, 2); // Toma el número de edificio
                this.nombre = "AUD-" + edificio;
            }
            default -> {} // Para AULA y VIRTUAL mantiene el nombre original
        }
        
        // Asegurar que tenga proyector si es laboratorio o auditorio
        if (tipo == TipoRoom.LABORATORIO || tipo == TipoRoom.AUDITORIO) {
            this.tieneProyector = true;
        }
    }

    /**
     * Modifica el estado de disponibilidad del aula.
     * Estados válidos:
     * 'L': Libre - El aula está disponible para su uso
     * 'O': Ocupado - El aula está en uso
     * 'M': Mantenimiento - El aula está en mantenimiento o reparación
     *
     * @param estado Nuevo estado de disponibilidad ('L', 'O' o 'M')
     * @throws IllegalArgumentException Si el estado no es válido
     */
    public void setDisponible(char estado) {
        if (!"LOM".contains(String.valueOf(estado)))
            throw new IllegalArgumentException("Estado inválido. Use L (Libre), O (Ocupado) o M (Mantenimiento)");
        this.disponible = estado;
    }

    @Override
    public String toFile() {
        return String.join("|",
            id, nombre, tipo.name(),
            String.valueOf(capacidad), String.valueOf(disponible),
            String.valueOf(tieneProyector)
        );
    }

    @Override
    public Room fromFile(String line) {
        String[] parts = line.split("\\|");
        Room room = new Room(
            parts[1], 
            TipoRoom.valueOf(parts[2]),
            Integer.parseInt(parts[3]), 
            Boolean.parseBoolean(parts[5])
        );
        room.id = parts[0];
        room.disponible = parts[4].charAt(0);
        return room;
    }

    @Override
    public String getInfo() {
        String estadoStr = switch(disponible) {
            case 'L' -> "✅ Libre";
            case 'O' -> "🔴 Ocupado";
            case 'M' -> "🔧 En Mantenimiento";
            default -> "❓ Desconocido";
        };

        return String.format("""
                            Aula: %s
                            Tipo: %s
                            Capacidad: %d estudiantes
                            Proyector: %s
                            Estado: %s""",
            nombre,
            tipo.getDescripcion(),
            capacidad,
            tieneProyector ? "✅" : "❌",
            estadoStr
        );
    }
}