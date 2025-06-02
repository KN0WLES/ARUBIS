package unicorn.model;

import java.util.UUID;

/**
 * Clase que representa el modelo de preguntas frecuentes en el sistema.
 * Permite gestionar las preguntas de los usuarios con sus respectivas respuestas,
 * así como manejar su estado (pendiente o respondida).
 * 
 * @description Funcionalidades principales:
 *                   - Crear preguntas con o sin respuesta inmediata.
 *                   - Gestionar el estado de pendiente de las preguntas.
 *                   - Serializar y deserializar FaQs para almacenamiento.
 *                   - Obtener información formateada de preguntas y respuestas.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see Base
 */
public class FaQ extends Base<FaQ> {
    private String id;
    private String pregunta;
    private String respuesta;
    private boolean pendiente;

    /**
     * Constructor vacío para serialización.
     */
    public FaQ(){}
    
    /**
     * Constructor para crear una pregunta con respuesta.
     * 
     * @param pregunta Texto de la pregunta
     * @param respuesta Texto de la respuesta
     * @throws IllegalArgumentException Si la pregunta o respuesta están vacías
     */
    public FaQ(String pregunta, String respuesta) {
        if (pregunta == null || pregunta.trim().isEmpty())
            throw new IllegalArgumentException("La pregunta no puede estar vacía");
        if (respuesta == null || respuesta.trim().isEmpty())
            throw new IllegalArgumentException("La respuesta no puede estar vacía");

        this.id = UUID.randomUUID().toString();
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.pendiente = false;
    }
    
    /**
     * Constructor para crear una pregunta pendiente sin respuesta.
     * Inicializa con un mensaje predeterminado y estado pendiente.
     * 
     * @param pregunta Texto de la pregunta
     * @throws IllegalArgumentException Si la pregunta está vacía
     */
    public FaQ(String pregunta) {
        if (pregunta == null || pregunta.trim().isEmpty()){
            throw new IllegalArgumentException("La pregunta no puede estar vacía");
        }
        this.respuesta = "Solicitud pendiente de respuesta";
        this.pendiente = true;
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Obtiene el identificador único de la pregunta.
     * @return ID de la pregunta
     */
    public String getId() { return id; }
    
    /**
     * Obtiene el texto de la pregunta.
     * @return Texto de la pregunta
     */
    public String getPregunta() { return pregunta; }
    
    /**
     * Obtiene el texto de la respuesta.
     * @return Texto de la respuesta
     */
    public String getRespuesta() { return respuesta; }
    
    /**
     * Verifica si la pregunta está pendiente de respuesta.
     * @return true si está pendiente, false si ya tiene respuesta
     */
    public boolean isPendiente() { return pendiente; }

    /**
     * Modifica el texto de la pregunta.
     * 
     * @param pregunta Nuevo texto de la pregunta
     * @throws IllegalArgumentException Si la pregunta está vacía
     */
    public void setPregunta(String pregunta) {
        if (pregunta == null || pregunta.trim().isEmpty())
            throw new IllegalArgumentException("La pregunta no puede estar vacía");
        this.pregunta = pregunta;
    }

    /**
     * Modifica el texto de la respuesta.
     * 
     * @param respuesta Nuevo texto de la respuesta
     * @throws IllegalArgumentException Si la respuesta está vacía
     */
    public void setRespuesta(String respuesta) {
        if (respuesta == null || respuesta.trim().isEmpty())
            throw new IllegalArgumentException("La respuesta no puede estar vacía");
        this.respuesta = respuesta;
    }

    /**
     * Modifica el estado de pendiente de la pregunta.
     * 
     * @param estado Nuevo estado: true para pendiente, false para respondida
     */
    public void setPendiente(boolean estado) { this.pendiente = estado; }

    @Override
    public String toFile() {
        return String.join("|", id, pregunta, respuesta, String.valueOf(pendiente));
    }

    @Override
    public FaQ fromFile(String line) {
        String[] parts = line.split("\\|");
        FaQ faq = new FaQ(parts[1], parts[2]);
        faq.id = parts[0];
        faq.pendiente = Boolean.parseBoolean(parts[3]);
        return faq;
    }

    @Override
    public String getInfo() {
        return String.format("P: %s\nR: %s", pregunta, respuesta);
    }
}