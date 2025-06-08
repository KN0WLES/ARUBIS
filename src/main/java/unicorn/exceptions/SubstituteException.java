package unicorn.exceptions;

public class SubstituteException extends Exception {
    public SubstituteException(String message) {
        super(message);
    }

    public static SubstituteException initializationError() {
        return new SubstituteException("Error al inicializar el controlador de suplentes");
    }

    public static SubstituteException activeSubstitutionExists() {
        return new SubstituteException("Ya existe una sustitución activa para este docente");
    }

    public static SubstituteException readError() {
        return new SubstituteException("Error al leer suplentes del archivo");
    }

    public static SubstituteException updateError() {
        return new SubstituteException("Error al actualizar suplente");
    }
}