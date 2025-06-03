package unicorn.controller;

import unicorn.model.News;
import unicorn.exceptions.NewsException;
import unicorn.interfaces.IFile;
import unicorn.interfaces.INews;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class NewsController implements INews {
    private final IFile<News> fileHandler;
    private final String filePath = "src/main/java/unicorn/arubis/dto/news.txt";
    private Map<String, News> newsMap;
    private String currentUserId; // Usuario actual (para evitar notificaciones propias)

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

    @Override
    public void sendGlobalNews(String message, String notificationType) throws NewsException {
        if (message == null || message.trim().isEmpty()) {
            throw NewsException.emptyMessage();
        }
        
        News news = new News(message, notificationType, null); // null = para todos
        newsMap.put(news.getId(), news);
        saveData();
    }

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

    @Override
    public News getNewsById(String id) throws NewsException {
        News news = newsMap.get(id);
        if (news == null) throw NewsException.notFound();
        return news;
    }

    @Override
    public List<News> getUnreadNewsByUser(String userId) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> !n.isLeida())
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getAllNewsByUser(String userId) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> isForUser(n, userId))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getGlobalNews() throws NewsException {
        return newsMap.values().stream()
                .filter(n -> n.getDestinatarioId() == null)
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

    @Override
    public List<News> getNewsByType(String type) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> n.getTipoNotificacion().equalsIgnoreCase(type))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) throws NewsException {
        return newsMap.values().stream()
                .filter(n -> !n.getFecha().isBefore(start) && !n.getFecha().isAfter(end))
                .sorted(Comparator.comparing(News::getFecha).reversed())
                .collect(Collectors.toList());
    }

    private boolean isForUser(News news, String userId) {
        return news.getDestinatarioId() == null ||  // Notificación global
               news.getDestinatarioId().equals(userId); // Notificación específica
    }
}