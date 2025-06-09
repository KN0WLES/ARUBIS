package unicorn.arubis.controller;

import unicorn.arubis.model.FaQ;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.IFaQ;
import unicorn.arubis.interfaces.IFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que actúa como controlador para la gestión de preguntas frecuentes (FAQs).
 * Proporciona la lógica de negocio necesaria para agregar, actualizar, eliminar y consultar FAQs.
 * También permite listar FAQs pendientes de respuesta y gestionar su contenido.
 * Los datos de las FAQs se almacenan y recuperan desde un archivo utilizando un manejador de archivos genérico.
 * 
 * @description Funcionalidades principales:
 *                   - Agregar nuevas FAQs.
 *                   - Actualizar preguntas y respuestas de FAQs existentes.
 *                   - Eliminar FAQs.
 *                   - Consultar FAQs por ID.
 *                   - Listar todas las FAQs registradas.
 *                   - Listar FAQs pendientes de respuesta.
 *                   - Verificar si existen FAQs pendientes.
 * 
 *             Al inicializar, crea FAQs predeterminadas si no existen datos previos.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see IFaQ
 * @see FaQ
 * @see IFile
 * @see FileException
 * @see FaQException
 */
public class FaQController implements IFaQ {
    
    private final IFile<FaQ> fileHandler;
    private final String filePath = "src/main/java/unicorn/arubis/dto/faqs.txt";
    private List<FaQ> faqs;
     /**
     * Constructor que inicializa el controlador y carga las FAQs desde el archivo.
     * Si el archivo no existe o está vacío, crea dos FAQs predeterminadas.
     * 
     * @param fileHandler Manejador de archivos para operaciones de lectura/escritura.
     * @throws FaQException Si ocurre un error al cargar o inicializar las FAQs.
     * @see #saveChanges() Para el guardado inicial de FAQs predeterminadas.
     */
    public FaQController(IFile<FaQ> fileHandler) throws FaQException{
        this.fileHandler = fileHandler;
        try {
            this.fileHandler.createFileIfNotExists(filePath);
            this.faqs = this.fileHandler.loadData(filePath);

            if (this.faqs == null || this.faqs.isEmpty()) {
                FaQ default1FaQ = new FaQ("Donde se encuentran ubicados","Calle Almagro 11, Madrid - CIF B65739856");
                FaQ default2FaQ = new FaQ("Contactos","Teléfono: 4258813 Whatsapp: 65486014 Correo: info@unicorn.com.bo");
                this.faqs = new ArrayList<>();
                this.faqs.add(default1FaQ);
                this.faqs.add(default2FaQ);
                saveChanges();
            }
        } catch (FileException e) {
            this.faqs = new ArrayList<>();
            System.err.println("Error al cargar FAQs: " + e.getMessage());
        }
    }
     /**
     * Guarda todas las FAQs actuales en el archivo especificado.
     * 
     * @throws FaQException Si falla la escritura del archivo.
     * @see IFile#saveData(List, String) Para detalles de implementación del guardado.
     */
    private void saveChanges() throws FaQException {
        try {
            fileHandler.saveData(faqs, filePath);
        } catch (FileException e) {
            throw new FaQException("Error al guardar los cambios: " + e.getMessage());
        }
    }
     /**
     * Agrega una nueva FAQ al sistema, verificando que la pregunta no exista previamente.
     * 
     * @param faq FAQ a agregar (no nula, con pregunta/respuesta válidas).
     * @throws FaQException Si:
     *   - La pregunta ya existe (comparación case-insensitive).
     *   - Fallo al guardar los cambios ({@link #saveChanges()}).
     */
    @Override
    public void addFaq(FaQ faq) throws FaQException {
        if (faqs.stream().anyMatch(f -> f.getPregunta().equalsIgnoreCase(faq.getPregunta()))) {
            throw new FaQException("Esta pregunta ya existe en el sistema");
        }
        
        faqs.add(faq);
        saveChanges();
    }
     /**
     * Busca una FAQ por su ID único.
     * 
     * @param id Identificador de la FAQ (UUID o similar).
     * @return La FAQ encontrada.
     * @throws FaQException Si no se encuentra la FAQ ({@link FaQException#notFound()}).
     */
    @Override
    public FaQ getFaqById(String id) throws FaQException {
        return faqs.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(FaQException::notFound);
    }
     /**
     * Actualiza una FAQ existente (pregunta, respuesta y estado "pendiente").
     * 
     * @param faq FAQ con los nuevos datos (debe incluir ID válido).
     * @throws FaQException Si:
     *   - La FAQ no existe ({@link #getFaqById(String)}).
     *   - Fallo al guardar los cambios ({@link #saveChanges()}).
     */
    @Override
    public void updateFaq(FaQ faq) throws FaQException {
        FaQ existingFaq = getFaqById(faq.getId());
        
        existingFaq.setPregunta(faq.getPregunta());
        existingFaq.setRespuesta(faq.getRespuesta());
        existingFaq.setPendiente(false);
        
        saveChanges();
    }
     /**
     * Verifica si hay FAQs marcadas como pendientes.
     * 
     * @return `true` si existe al menos una FAQ pendiente, `false` en caso contrario.
     * @throws FaQException Si hay errores al acceder a la lista de FAQs.
     */
    public boolean areFaqByPending() throws FaQException {
        return faqs.stream().anyMatch(FaQ::isPendiente);
    }
     /**
     * Obtiene todas las FAQs marcadas como pendientes.
     * 
     * @return Lista de FAQs pendientes (puede estar vacía).
     * @throws FaQException Si hay errores al filtrar las FAQs.
     */
    public List<FaQ> getFaqByPending() throws FaQException {
        return faqs.stream()
                .filter(FaQ::isPendiente)
                .toList();
    }
     /**
     * Elimina una FAQ del sistema por su ID.
     * 
     * @param id Identificador de la FAQ a eliminar.
     * @throws FaQException Si:
     *   - La FAQ no existe ({@link #getFaqById(String)}).
     *   - Fallo al eliminar o guardar los cambios.
     */
    @Override
    public void deleteFaq(String id) throws FaQException {
        FaQ existingFaq = getFaqById(id);
        
        if (!faqs.remove(existingFaq)) throw new FaQException("No se pudo eliminar el FAQ");

        saveChanges();
    }
     /**
     * Retorna una copia de todas las FAQs registradas.
     * 
     * @return Lista inmutable de FAQs.
     * @throws FaQException Si hay errores al acceder a los datos.
     */
    @Override
    public List<FaQ> getAllFaqs() throws FaQException {
        return new ArrayList<>(faqs);
    }
}