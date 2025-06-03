package unicorn.exceptions;

public class SubjectException extends Exception {
    public SubjectException(String message) {
        super(message);
    }

    public static SubjectException notFound() {
        return new SubjectException("Materia no encontrada");
    }

    public static SubjectException alreadyExists() {
        return new SubjectException("Ya existe una materia con este nombre");
    }

    public static SubjectException invalidCredits() {
        return new SubjectException("Número de créditos inválido (debe ser positivo)");
    }
}