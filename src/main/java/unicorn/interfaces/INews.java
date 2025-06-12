package unicorn.interfaces;

import unicorn.model.News;
import unicorn.exceptions.NewsException;
import unicorn.util.TipoNews;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz para la gestión de notificaciones del sistema.
 * Define los métodos necesarios para enviar, gestionar y consultar notificaciones,
 * tanto globales como específicas por usuario o materia.
 *
 * @description Funcionalidades principales:
 *                   - Envío de notificaciones globales y específicas
 *                   - Consulta de notificaciones por diversos criterios
 *                   - Gestión del estado de lectura
 *                   - Eliminación de notificaciones
 *
 * @author KNOWLES
 * @version 2.0
 * @since 2025-06-10
 */
public interface INews {
    /**
     * Envía una notificación global a todos los usuarios.
     * @param message Contenido de la notificación
     * @param tipo Tipo de notificación (usar enum TipoNews)
     * @throws NewsException Si el mensaje está vacío o hay errores de persistencia
     */
    void sendGlobalNews(String message, TipoNews tipo) throws NewsException;

    /**
     * Envía una notificación específica a un usuario.
     * @param message Contenido de la notificación
     * @param tipo Tipo de notificación (usar enum TipoNews)
     * @param recipientId ID del usuario destinatario
     * @throws NewsException Si el mensaje está vacío, el ID es inválido o se intenta auto-envío
     */
    void sendUserNews(String message, TipoNews tipo, String recipientId) throws NewsException;

    /**
     * Envía una notificación asociada a una materia.
     * @param message Contenido de la notificación
     * @param materiaId ID de la materia destinataria
     * @throws NewsException Si el mensaje está vacío o hay errores de persistencia
     */
    void sendNewsForMateria(String message, String materiaId) throws NewsException;

    /**
     * Obtiene una notificación por su ID único.
     * @param id ID de la notificación
     * @return Objeto News correspondiente
     * @throws NewsException Si la notificación no existe
     */
    News getNewsById(String id) throws NewsException;

    /**
     * Obtiene las notificaciones no leídas de un usuario.
     * @param userId ID del usuario
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getUnreadNewsByUser(String userId);

    /**
     * Obtiene todas las notificaciones de un usuario (leídas y no leídas).
     * @param userId ID del usuario
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getAllNewsByUser(String userId);

    /**
     * Obtiene todas las notificaciones globales.
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getGlobalNews();

    /**
     * Marca una notificación como leída.
     * @param newsId ID de la notificación
     * @throws NewsException Si la notificación no existe
     */
    void markAsRead(String newsId) throws NewsException;

    /**
     * Elimina una notificación del sistema.
     * @param newsId ID de la notificación
     * @throws NewsException Si la notificación no existe
     */
    void deleteNews(String newsId) throws NewsException;

    /**
     * Filtra notificaciones por tipo.
     * @param type Tipo de notificación (enum TipoNews)
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getNewsByType(TipoNews type);

    /**
     * Filtra notificaciones por rango de fechas.
     * @param start Fecha de inicio
     * @param end Fecha de fin
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end);

    /**
     * Filtra notificaciones por materia.
     * @param materiaId ID de la materia
     * @return Lista de notificaciones ordenadas por fecha (más reciente primero)
     */
    List<News> getNewsByMateria(String materiaId);
}