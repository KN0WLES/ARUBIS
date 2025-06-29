package unicorn.util;

public enum AccountStatus {
    PENDIENTE("Pendiente de revisión"),
    ACTIVO("Activo"),
    RECHAZADO("Rechazado");

    private final String descripcion;

    AccountStatus(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
