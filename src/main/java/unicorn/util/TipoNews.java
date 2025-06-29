package unicorn.util;

public enum TipoNews {
    GLOBAL("Global"), // notificaciones virtual
    MATERIA("Materia"), 
    AULA("Aula"),
    SISTEMA("Sistema"); 

    private final String descripcion;

    TipoNews(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción legible del tipo de notificaciones
     *
     * @return String con la descripción del tipo de notificaciones
     */
    public String getDescripcion() {
        return descripcion;
    }
}