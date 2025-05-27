package controller;

import interfaces.*;
import model.FaQ;
import exceptions.*;

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
 * @see News
 * @see IFile
 * @see FileException
 * @see FaQException
 */
public class FaQController implements IFaQ {
    
    private final IFile<News> fileHandler;
    private final String filePath = "src/main/java/data/faqs.txt";
    private List<News> faqs;

    public FaQController(IFile<News> fileHandler) throws FaQException{
        this.fileHandler = fileHandler;
        try {
            this.fileHandler.createFileIfNotExists(filePath);
            this.faqs = this.fileHandler.loadData(filePath);

            if (this.faqs == null || this.faqs.isEmpty()) {
                News default1FaQ = new News("Donde se encuentran ubicados","Calle Almagro 11, Madrid - CIF B65739856");
                News default2FaQ = new News("Contactos","Teléfono: 4258813 Whatsapp: 65486014 Correo: info@unicorn.com.bo");
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
    public void addFaq(News faq) throws FaQException {
        if (faqs.stream().anyMatch(f -> f.getPregunta().equalsIgnoreCase(faq.getPregunta()))) {
            throw new FaQException("Esta pregunta ya existe en el sistema");
        }
        
        faqs.add(faq);
        saveChanges();
    }

    @Override
    public News getFaqById(String id) throws FaQException {
        return faqs.stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElseThrow(FaQException::notFound);
    }

    @Override
    public void updateFaq(News faq) throws FaQException {
        News existingFaq = getFaqById(faq.getId());
        
        existingFaq.setPregunta(faq.getPregunta());
        existingFaq.setRespuesta(faq.getRespuesta());
        existingFaq.setPendiente(false);
        
        saveChanges();
    }

    public boolean areFaqByPending() throws FaQException {
        return faqs.stream().anyMatch(News::isPendiente);
    }

    public List<News> getFaqByPending() throws FaQException {
        return faqs.stream()
                .filter(News::isPendiente)
                .toList();
    }

    @Override
    public void deleteFaq(String id) throws FaQException {
        News existingFaq = getFaqById(id);
        
        if (!faqs.remove(existingFaq)) throw new FaQException("No se pudo eliminar el FAQ");

        saveChanges();
    }

    @Override
    public List<News> getAllFaqs() throws FaQException {
        return new ArrayList<>(faqs);
    }
}