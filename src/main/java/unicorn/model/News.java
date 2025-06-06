package unicorn.model;

import unicorn.util.*;
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
    private TipoNews tipoNotificacion; // SISTEMA, HORARIO, AULA, etc.
    private String destinatarioId; // ID del usuario destinatario
    private String senderId;      // ID del usuario que envía la notificación

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

    public News(String mensaje, TipoNews tipoNotificacion, String destinatarioId, String senderId) {
        if (mensaje == null || mensaje.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje no puede estar vacío");
        }
        if (tipoNotificacion == null) {
            throw new IllegalArgumentException("El tipo de notificación es requerido");
        }

        this.id = UUID.randomUUID().toString();
        this.mensaje = mensaje;
        this.fecha = LocalDateTime.now();
        this.leida = false;
        this.tipoNotificacion = tipoNotificacion;
        this.destinatarioId = destinatarioId;
        this.senderId = senderId;
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
    public TipoNews getTipoNotificacion() { return tipoNotificacion; }

      /** @return Para quién es (null = para todos) */
    public String getDestinatarioId() { return destinatarioId; }

    public String getSenderId() { return senderId; }

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

    public void setTipoNotificacion(TipoNews tipoNotificacion) {
        if (tipoNotificacion == null) {
            throw new IllegalArgumentException("El tipo de notificación es requerido");
        }
        this.tipoNotificacion = tipoNotificacion;
    }

    @Override
    public String toFile() {
        return String.join("|",
                id,
                mensaje,
                fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                String.valueOf(leida),
                tipoNotificacion.name(),
                destinatarioId != null ? destinatarioId : "ALL",
                senderId != null ? senderId : "SYSTEM"
        );
    }

    @Override
    public News fromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Formato de línea inválido. Se esperaban 7 partes, se encontraron: " + parts.length);
        }

        News notification = new News();
        notification.id = parts[0];
        notification.mensaje = parts[1];
        notification.fecha = LocalDateTime.parse(parts[2], DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        notification.leida = Boolean.parseBoolean(parts[3]);
        notification.tipoNotificacion = TipoNews.valueOf(parts[4]);
        notification.destinatarioId = parts[5].equals("ALL") ? null : parts[5];
        notification.senderId = parts[6].equals("SYSTEM") ? null : parts[6];

        return notification;
    }

    @Override
    public String getInfo() {
        return String.format(
                "[%s] De: %s\nPara: %s\nMensaje: %s\nFecha: %s\nEstado: %s",
                tipoNotificacion,
                senderId != null ? senderId : "Sistema",
                destinatarioId != null ? destinatarioId : "Todos",
                mensaje,
                fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                leida ? "Leída" : "No leída"
        );
    }
}