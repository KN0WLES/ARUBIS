package unicorn.interfaces;

import unicorn.model.*;
import unicorn.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz para la gestión de preguntas frecuentes (FAQ).
 * Define los métodos necesarios para crear, obtener, actualizar y eliminar preguntas frecuentes,
 * así como para gestionar el estado de las preguntas pendientes de revisión.
 *
 * @description Funcionalidades principales:
 *                   - Agregar nuevas preguntas frecuentes.
 *                   - Obtener preguntas frecuentes por ID.
 *                   - Listar preguntas pendientes de revisión.
 *                   - Verificar existencia de preguntas pendientes.
 *                   - Actualizar preguntas existentes.
 *                   - Eliminar preguntas.
 *                   - Listar todas las preguntas registradas.
 *
 * Ejemplo de uso:
 * <pre>
 *     IFaQ faqManager = new FaQController(fileHandler);
 *     FaQ nuevaFaq = new FaQ("F001", "¿Cómo reservo una habitación?", "Puede realizar su reserva...");
 *     faqManager.addFaq(nuevaFaq);
 * </pre>
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public interface INews {
    // Notificaciones globales (para todos los usuarios)
    void sendGlobalNews(String message, String notificationType) throws NewsException;
    
    // Notificaciones específicas para un usuario
    void sendUserNews(String message, String notificationType, String recipientId) throws NewsException;
    
    // Métodos de consulta
    News getNewsById(String id) throws NewsException;
    List<News> getUnreadNewsByUser(String userId) throws NewsException;
    List<News> getAllNewsByUser(String userId) throws NewsException;
    List<News> getGlobalNews() throws NewsException;
    
    // Métodos de gestión
    void markAsRead(String newsId) throws NewsException;
    void deleteNews(String newsId) throws NewsException;
    
    // Métodos de filtrado
    List<News> getNewsByType(String type) throws NewsException;
    List<News> getNewsByDateRange(LocalDateTime start, LocalDateTime end) throws NewsException;
}