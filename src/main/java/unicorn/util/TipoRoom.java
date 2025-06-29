package unicorn.util;

/**
 * Enumeración que define los tipos de cuenta disponibles en el sistema.
 * Incluye los diferentes roles de usuario con sus respectivas descripciones.
 * 
 * @description Funcionalidades principales:
 *                   - Define los tipos de cuenta permitidos en el sistema.
 *                   - Proporciona descripciones legibles para cada tipo de cuenta.
 *                   - Garantiza consistencia en la asignación de roles.
 * 
 * @note Los tipos de cuenta están limitados a ADMIN, PROFESOR y ESTUDIANTE.
 *       Para agregar nuevos tipos, se debe modificar esta enumeración.
 * 
 * @see AccountValidation
 * @see PasswordUtil
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public enum TipoRoom{
    VIRTUAL("Virtual"), // Aula virtual
    AULA("Fisica"), // Aula física
    LABORATORIO("Laboratorio"), // Laboratorio
    AUDITORIO("Auditorio"); // Auditorio

    private final String descripcion;

    TipoRoom(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible del tipo de aula
     * 
     * @return String con la descripción del tipo de aula
     */
    public String getDescripcion() {
        return descripcion;
    }
}