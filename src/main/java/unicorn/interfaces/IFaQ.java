package unicorn.interfaces;

import unicorn.model.FaQ;
import unicorn.exceptions.FaQException;
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
public interface IFaQ {
    
    /**
     * Agrega una nueva pregunta frecuente.
     *                      La pregunta debe contener:
     *                          -ID único
     *                          -La pregunta y su respuesta.
     *                      Las preguntas nuevas pueden ser marcadas como pendientes de revisión por un administrador.
     *
     * @param faq Pregunta frecuente a agregar.
     * @throws FaQException Si ocurre un error al agregar la FAQ.
     */
    void addFaq(FaQ faq) throws FaQException;
    
    /**
     * Obtiene una pregunta frecuente por su ID.
     * 
     * @param id Identificador de la FAQ.
     * @return La pregunta frecuente correspondiente.
     * @throws FaQException Si la FAQ no existe.
     */
    FaQ getFaqById(String id) throws FaQException;

    /**
     * Obtiene una lista de preguntas pendientes.
     *                      Las preguntas pendientes son aquellas que han sido enviadas por usuarios
     *                      y que requieren revisión por parte de un administrador antes de ser publicadas.
     * 
     * @return La pregunta frecuente correspondiente.
     * @throws FaQException Si la FAQ no existe.
     */
    List<FaQ> getFaqByPending() throws FaQException;

    /**
     * Verificca que no existan preguntas frecuentes pendientes.
     *                      Este método es útil para notificar a los administradores sobre preguntas
     *                      que requieren su atención.
     * 
     * @return true si tenemos FaQ pendientes, false si no.
     * @throws FaQException si tenemos un error al verificar pending questions
     */
    boolean areFaqByPending() throws FaQException;

    /**
     * Actualiza una pregunta frecuente existente.
     *                      Permite modificar:
     *                          -Pregunta
     *                          -Respuesta
     *                          -Categoría o estado (pendiente/publicada)
     *                      de una FAQ existente.
     *
     * @param faq Pregunta frecuente con los datos actualizados.
     * @throws FaQException Si la FAQ no existe o los datos son inválidos.
     */
    void updateFaq(FaQ faq,String userResponse) throws FaQException;
    
    /**
     * Elimina una pregunta frecuente.
     *                      Solo los administradores pueden eliminar preguntas frecuentes del sistema.
     *
     * @param id Identificador de la FAQ a eliminar.
     * @throws FaQException Si la FAQ no existe.
     */
    void deleteFaq(String id) throws FaQException;
    
    /**
     * Obtiene todas las preguntas frecuentes registradas.
     *                      Incluye tanto las preguntas publicadas como las pendientes de revisión.
     *
     * @return Lista de preguntas frecuentes.
     * @throws FaQException Si ocurre un error al obtener las FAQ.
     */
    List<FaQ> getAllFaqs() throws FaQException;
}