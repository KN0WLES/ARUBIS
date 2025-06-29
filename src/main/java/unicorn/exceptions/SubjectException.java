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

    // Sugerencia: por si necesitas validar el tipo de materia
    public static SubjectException invalidType() {
        return new SubjectException("Tipo de materia inválido");
    }

    // Sugerencia: por si el nombre está vacío o nulo
    public static SubjectException invalidName() {
        return new SubjectException("El nombre de la materia no puede estar vacío");
    }
}