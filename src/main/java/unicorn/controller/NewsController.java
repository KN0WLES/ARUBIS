package unicorn.controller;

import unicorn.model.News;
import unicorn.exceptions.NewsException;
import unicorn.interfaces.IFile;
import unicorn.interfaces.INews;
import unicorn.util.TipoNews;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class NewsController implements INews {
    private final IFile<News> fileHandler;
    private final String filePath = "src/main/java/unicorn/dto/news.txt";
    private Map<String, News> newsMap;
    private String currentUserId;

    public NewsController(IFile<News> fileHandler, String currentUserId) throws NewsException {
        this.fileHandler = fileHandler;
        this.currentUserId = currentUserId;
        this.newsMap = new HashMap<>();
        loadData();
    }

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

    private void saveData() throws NewsException {
        try {
            fileHandler.saveData(new ArrayList<>(newsMap.values()), filePath);
        } catch (Exception e) {
            throw new NewsException("Error al guardar notificaciones: " + e.getMessage());
        }
    }

    //@Override
    public void sendGlobalNews(String message, TipoNews tipo) throws NewsException {
        validateMessage(message);
        News news = new News(message, tipo, null, currentUserId); // Agregamos senderId
        newsMap.put(news.getId(), news);
        saveData();
    }

    //@Override
    public void sendNewsForMateria(String message, String materiaId) throws NewsException {
        validateMessage(message);
        News news = new News(message, TipoNews.MATERIA, materiaId, currentUserId);
        newsMap.put(news.getId(), news);
        saveData();
    }

    @Override
    public void sendUserNews(String message, TipoNews tipo, String recipientId) throws NewsException {
        validateMessage(message);
        validateRecipient(recipientId);

        News news = new News(message, tipo, recipientId, currentUserId);
        newsMap.put(news.getId(), news);
        saveData();
    }

    private void validateRecipient(String recipientId) throws NewsException {
        if (recipientId == null || recipientId.trim().isEmpty()) {
            throw new NewsException("ID de destinatario inválido");
        }
        if (recipientId.equals(currentUserId)) {
            throw new NewsException("No puedes enviarte notificaciones a ti mismo");
        }
    }

    private void validateMessage(String message) throws NewsException {
        if (message == null || message.trim().isEmpty()) {
            throw NewsException.emptyMessage();
        }
    }

    @Override
    public News getNewsById(String id) throws NewsException {
        News news = newsMap.get(id);
        if (news == null) throw NewsException.notFound();
        return news;
    }

    @Override
    public List<News> getUnreadNewsByUser(String userId) {
        return newsMap.values().stream()
                .filter(n -> !n.isLeida())
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getAllNewsByUser(String userId) {
        return newsMap.values().stream()
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getGlobalNews() {
        return newsMap.values().stream()
                .filter(n -> n.getDestinatarioId() == null)
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    public List<News> getNewsByMateria(String materiaId) {
        return newsMap.values().stream()
                .filter(n -> n.getTipoNotificacion() == TipoNews.MATERIA
                        && materiaId.equals(n.getDestinatarioId()))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getNewsByType(TipoNews type) {
        return newsMap.values().stream()
                .filter(n -> n.getTipoNotificacion() == type)
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) {
        return newsMap.values().stream()
                .filter(n -> !n.getFecha().isBefore(start) && !n.getFecha().isAfter(end))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    public List<News> getNewsBySender(String senderId) {
        return newsMap.values().stream()
                .filter(n -> senderId.equals(n.getSenderId()))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void markAsRead(String newsId) throws NewsException {
        News news = getNewsById(newsId);
        news.setLeida(true);
        saveData();
    }

    @Override
    public void deleteNews(String newsId) throws NewsException {
        if (!newsMap.containsKey(newsId)) {
            throw NewsException.notFound();
        }
        newsMap.remove(newsId);
        saveData();
    }

    private boolean isForUser(News news, String userId) {
        return news.getDestinatarioId() == null ||
                news.getDestinatarioId().equals(userId);
    }

    /**
     * Obtiene estadísticas de notificaciones por tipo
     */
    public Map<TipoNews, Long> getNewsStatistics() {
        return newsMap.values().stream()
                .collect(Collectors.groupingBy(
                        News::getTipoNotificacion,
                        Collectors.counting()
                ));
    }

    /**
     * Obtiene el conteo de notificaciones no leídas para un usuario
     */
    public long getUnreadCount(String userId) {
        return newsMap.values().stream()
                .filter(n -> !n.isLeida())
                .filter(n -> isForUser(n, userId))
                .count();
    }
}