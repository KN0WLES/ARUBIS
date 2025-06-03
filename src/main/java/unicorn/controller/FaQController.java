package unicorn.controller;

import unicorn.model.FaQ;
import unicorn.exceptions.*;
import unicorn.interfaces.IFaQ;
import unicorn.interfaces.IFile;

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

    private void saveChanges() throws FaQException {
        try {
            fileHandler.saveData(faqs, filePath);
        } catch (FileException e) {
            throw new FaQException("Error al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public void addFaq(FaQ faq) throws FaQException {
        if (faqs.stream().anyMatch(f -> f.getPregunta().equalsIgnoreCase(faq.getPregunta()))) {
            throw new FaQException("Esta pregunta ya existe en el sistema");
        }
        
        faqs.add(faq);
        saveChanges();
    }

    @Override
    public FaQ getFaqById(String id) throws FaQException {
        return faqs.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(FaQException::notFound);
    }

    @Override
    public void updateFaq(FaQ faq) throws FaQException {
        FaQ existingFaq = getFaqById(faq.getId());
        
        existingFaq.setPregunta(faq.getPregunta());
        existingFaq.setRespuesta(faq.getRespuesta());
        existingFaq.setPendiente(false);
        
        saveChanges();
    }

    public boolean areFaqByPending() throws FaQException {
        return faqs.stream().anyMatch(FaQ::isPendiente);
    }

    public List<FaQ> getFaqByPending() throws FaQException {
        return faqs.stream()
                .filter(FaQ::isPendiente)
                .toList();
    }

    @Override
    public void deleteFaq(String id) throws FaQException {
        FaQ existingFaq = getFaqById(id);
        
        if (!faqs.remove(existingFaq)) throw new FaQException("No se pudo eliminar el FAQ");

        saveChanges();
    }

    @Override
    public List<FaQ> getAllFaqs() throws FaQException {
        return new ArrayList<>(faqs);
    }
}