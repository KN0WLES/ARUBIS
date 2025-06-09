package unicorn.arubis.controller;

import unicorn.arubis.model.News;
import unicorn.arubis.exceptions.NewsException;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.interfaces.INews;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
 /**
 * Controlador para gestionar notificaciones (News) en el sistema.
 * Permite enviar, recuperar, marcar como leídas y eliminar notificaciones,
 * tanto globales como dirigidas a usuarios específicos.
 * 
 * @implSpec Implementa la interfaz {@link INews} para operaciones básicas de notificaciones.
 * @see News Clase base que representa una notificación.
 * @see NewsException Excepciones específicas del módulo de notificaciones.
 */
public class NewsController implements INews {
    private final IFile<News> fileHandler;
    private final String filePath = "src/main/java/unicorn/arubis/dto/news.txt";
    private Map<String, News> newsMap;
    private String currentUserId; // Usuario actual (para evitar notificaciones propias)
     /**
     * Constructor del controlador de notificaciones.
     * 
     * @param fileHandler Manejador de archivos para persistencia (debe implementar {@link IFile<News>}).
     * @param currentUserId ID del usuario actual (no puede ser nulo/vacío).
     * @throws NewsException Si ocurre un error al cargar los datos iniciales.
     * @throws IllegalArgumentException Si `currentUserId` es nulo o vacío.
     */
    public NewsController(IFile<News> fileHandler, String currentUserId) throws NewsException {
        this.fileHandler = fileHandler;
        this.currentUserId = currentUserId;
        this.newsMap = new HashMap<>();
        loadData();
    }
     /**
     * Carga las notificaciones desde el archivo de persistencia.
     * 
     * @throws NewsException Si el archivo no puede leerse o contiene datos inválidos.
     * @see #saveData() Método complementario para guardar datos.
     */
    private void loadData() throws NewsException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<News> loadedNews = fileHandler.loadData(filePath);
            if (loadedNews != null) {
                loadedNews.forEach(n -> newsMap.put(n.getId(), n));
            }
        } catch (Exception e) {
            throw new NewsException("Error al cargar notificaciones: " + e.getMessage());
        }
    }
     /**
     * Guarda las notificaciones en el archivo de persistencia.
     * 
     * @throws NewsException Si falla la escritura en el archivo.
     * @see #loadData() Método complementario para cargar datos.
     */
    private void saveData() throws NewsException {
        try {
            fileHandler.saveData(new ArrayList<>(newsMap.values()), filePath);
        } catch (Exception e) {
            throw new NewsException("Error al guardar notificaciones: " + e.getMessage());
        }
    }
     /**
     * Envía una notificación global (para todos los usuarios).
     * 
     * @param message Texto de la notificación (no nulo/vacío, máximo 500 caracteres).
     * @param notificationType Tipo de notificación (ej: "ALERT", "UPDATE").
     * @throws NewsException Si el mensaje es inválido o falla el guardado.
     * @see #sendUserNews(String, String, String) Para notificaciones a usuarios específicos.
     */
    @Override
    public void sendGlobalNews(String message, String notificationType) throws NewsException {
        if (message == null || message.trim().isEmpty()) {
            throw NewsException.emptyMessage();
        }
        
        News news = new News(message, notificationType, null); // null = para todos
        newsMap.put(news.getId(), news);
        saveData();
    }
     /**
     * Envía una notificación a un usuario específico.
     * 
     * @param message Texto de la notificación (no nulo/vacío).
     * @param notificationType Tipo de notificación (ej: "MESSAGE", "REMINDER").
     * @param recipientId ID del destinatario (no puede ser el usuario actual).
     * @throws NewsException Si:
     * - El mensaje es inválido ({@link NewsException#emptyMessage()})
     * - El ID del destinatario es inválido
     * - Se intenta enviar una notificación a sí mismo
     * 
     * @see #sendGlobalNews(String, String) Para notificaciones globales.
     */
    @Override
    public void sendUserNews(String message, String notificationType, String recipientId) throws NewsException {
        if (message == null || message.trim().isEmpty()) {
            throw NewsException.emptyMessage();
        }
        if (recipientId == null || recipientId.trim().isEmpty()) {
            throw new NewsException("ID de destinatario inválido");
        }
        if (recipientId.equals(currentUserId)) {
            throw new NewsException("No puedes enviarte notificaciones a ti mismo");
        }
        
        News news = new News(message, notificationType, recipientId);
        newsMap.put(news.getId(), news);
        saveData();
    }
     /**
     * Obtiene una notificación por su ID.
     * 
     * @param id ID único de la notificación (formato UUID).
     * @return Instancia de {@link News}.
     * @throws NewsException Si la notificación no existe ({@link NewsException#notFound()}).
     */
    @Override
    public News getNewsById(String id) throws NewsException {
        News news = newsMap.get(id);
        if (news == null) throw NewsException.notFound();
        return news;
    }
     /**
     * Obtiene las notificaciones no leídas de un usuario.
     * 
     * @param userId ID del usuario (no nulo/vacío).
     * @return Lista ordenada por fecha (más reciente primero).
     * @throws NewsException Si el userId es inválido.
     * @see #getAllNewsByUser(String) Para obtener todas las notificaciones (leídas y no leídas).
     */
    @Override
    public List<News> getUnreadNewsByUser(String userId) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> !n.isLeida())
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }
     /**
     * Obtiene todas las notificaciones asociadas a un usuario específico, ordenadas por fecha (más reciente primero).
     * Incluye tanto notificaciones globales como dirigidas específicamente al usuario.
     * 
     * @param userId ID del usuario (no nulo, formato UUID).
     * @return Lista de notificaciones ordenadas cronológicamente descendente.
     * @throws NewsException Si el userId es inválido o hay errores de carga.
     * @see #getUnreadNewsByUser(String) Para obtener solo notificaciones no leídas.
     * @see #isForUser(News, String) Para la lógica de filtrado por usuario.
     */

    @Override
    public List<News> getAllNewsByUser(String userId) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }
     /**
     * Obtiene todas las notificaciones globales (sin destinatario específico), ordenadas por fecha.
     * 
     * @return Lista de notificaciones globales ordenadas cronológicamente descendente.
     * @throws NewsException Si hay errores de carga.
     * @see #sendGlobalNews(String, String) Para enviar notificaciones globales.
     */

    @Override
    public List<News> getGlobalNews() throws NewsException {
        return newsMap.values().stream()
                .filter(n -> n.getDestinatarioId() == null)
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }
     /**
     * Marca una notificación como leída.
     * 
     * @param newsId ID de la notificación a marcar (formato UUID).
     * @throws NewsException Si:
     * - La notificación no existe ({@link NewsException#notFound()})
     * - Fallo al guardar los cambios
     * 
     * @see News#setLeida(boolean) Para cambiar el estado de lectura.
     */

    @Override
    public void markAsRead(String newsId) throws NewsException {
        News news = getNewsById(newsId);
        news.setLeida(true);
        saveData();
    }
     /**
     * Elimina permanentemente una notificación del sistema.
     * 
     * @param newsId ID de la notificación a eliminar (formato UUID).
     * @throws NewsException Si la notificación no existe ({@link NewsException#notFound()}).
     * 
     * @implNote Esta operación no puede deshacerse.
     * @see #sendGlobalNews(String, String) Para crear nuevas notificaciones.
     */
    @Override
    public void deleteNews(String newsId) throws NewsException {
        if (!newsMap.containsKey(newsId)) {
            throw NewsException.notFound();
        }
        newsMap.remove(newsId);
        saveData();
    }
     /**
     * Filtra notificaciones por tipo específico, ordenadas por fecha.
     * 
     * @param type Tipo de notificación (case-insensitive, ej: "ALERTA", "RECORDATORIO").
     * @return Lista de notificaciones coincidentes ordenadas cronológicamente descendente.
     * @throws NewsException Si el tipo es nulo o hay errores de carga.
     * 
     * @see News#getTipoNotificacion() Para conocer los tipos disponibles.
     */

    @Override
    public List<News> getNewsByType(String type) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> n.getTipoNotificacion().equalsIgnoreCase(type))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }
     /**
     * Obtiene notificaciones dentro de un rango de fechas específico, ordenadas por fecha.
     * 
     * @param start Fecha/hora de inicio (inclusive, no nula).
     * @param end Fecha/hora de fin (inclusive, no nula).
     * @return Lista de notificaciones en el rango ordenadas cronológicamente descendente.
     * @throws NewsException Si:
     * - Las fechas son nulas
     * - La fecha de inicio es posterior a la de fin
     * - Hay errores de carga
     */
    @Override
    public List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> !n.getFecha().isBefore(start) && !n.getFecha().isAfter(end))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }
     /**
     * Filtra notificaciones para determinar si son relevantes para un usuario.
     * 
     * @param news Notificación a evaluar.
     * @param userId ID del usuario.
     * @return true si la notificación es global o está dirigida al usuario especificado.
     */
    private boolean isForUser(News news, String userId) {
        return news.getDestinatarioId() == null ||  // Notificación global
               news.getDestinatarioId().equals(userId); // Notificación específica
    }
}