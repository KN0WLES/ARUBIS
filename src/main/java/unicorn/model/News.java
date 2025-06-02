package unicorn.model;

import java.util.UUID;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa el modelo de notificaciones en el sistema.
 * Permite gestionar las notificaciones del sistema con su mensaje y fecha,
 * así como manejar su estado (leída o no leída).
 *
 * @description Funcionalidades principales:
 *                   - Crear notificaciones con mensaje y fecha.
 *                   - Gestionar el estado de lectura de las notificaciones.
 *                   - Serializar y deserializar notificaciones para almacenamiento.
 *                   - Obtener información formateada de las notificaciones.
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see Base
 */
public class News extends Base<News> {
    private String id;
    private String mensaje;
    private LocalDateTime fecha;
    private boolean leida;
    private String tipoNotificacion; // SISTEMA, HORARIO, AULA, etc.
    private String destinatarioId; // ID del usuario destinatario

    /**
     * Constructor vacío para serialización.
     */
    public News(){}

    /**
     * Crea una nueva notificación.
     * @param mensaje El texto de la notificación
     * @param tipoNotificacion El tipo (SISTEMA, HORARIO, etc)
     * @param destinatarioId Para quién es (null si es para todos)
     */

    public News(String mensaje, String tipoNotificacion, String destinatarioId) {
        if (mensaje == null || mensaje.trim().isEmpty())
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        if (tipoNotificacion == null || tipoNotificacion.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de notificación no puede estar vacío");

        this.id = UUID.randomUUID().toString();
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
        this.tipoNotificacion = tipoNotificacion;
        this.destinatarioId = destinatarioId;
    }
    // Métodos para obtener información
    /** @return El ID único de la notificación */
    @Override
    public String getId() { return id; }

     /** @return El texto de la notificación */
    public String getMensaje() { return mensaje; }

     /** @return Cuándo se creó la notificación */
    public LocalDateTime getFecha() { return fecha; }

     /** @return Si ya fue leída (true) o no (false) */
    public boolean isLeida() { return leida; }

     /** @return El tipo de notificación */
    public String getTipoNotificacion() { return tipoNotificacion; }

      /** @return Para quién es (null = para todos) */
    public String getDestinatarioId() { return destinatarioId; }


    // Métodos para modificar la notificación
    /** Cambia el texto de la notificación
     * @param mensaje */
    public void setMensaje(String mensaje) {
        if (mensaje == null || mensaje.trim().isEmpty())
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        this.mensaje = mensaje;
    }

    /** Marca como leída o no leída
     * @param leida */
    public void setLeida(boolean leida) {
        this.leida = leida;
    }

    public void setTipoNotificacion(String tipoNotificacion) {
        if (tipoNotificacion == null || tipoNotificacion.trim().isEmpty())
            throw new IllegalArgumentException("El tipo de notificación no puede estar vacío");
        this.tipoNotificacion = tipoNotificacion;
    }

    @Override
    public String toFile() {
        return String.join("|", 
            id, 
            mensaje, 
            fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            String.valueOf(leida),
            tipoNotificacion,
            destinatarioId != null ? destinatarioId : "ALL"
        );
    }

    @Override
    public News fromFile(String line) {
        String[] parts = line.split("\\|");
        News notification = new News(parts[1], parts[4], parts[5].equals("ALL") ? null : parts[5]);
        notification.id = parts[0];
        notification.fecha = LocalDateTime.parse(parts[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        notification.leida = Boolean.parseBoolean(parts[3]);
        return notification;
    }

    @Override
    public String getInfo() {
        return String.format(
            "[%s] %s\nFecha: %s\nEstado: %s",
            tipoNotificacion,
            mensaje,
            fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            leida ? "Leída" : "No leída"
        );
    }
}