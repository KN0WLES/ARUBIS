package unicorn.exceptions;

public class ScheduleException extends Exception {
    public ScheduleException(String message) {
        super(message);
    }

    // Sugerencia: por si el horario no existe
    public static ScheduleException notFound() {
        return new ScheduleException("Horario no encontrado");
    }

    // Sugerencia: por si ya existe un horario igual
    public static ScheduleException alreadyExists() {
        return new ScheduleException("Ya existe un horario con estos datos");
    }

    // Sugerencia: por si la lista de periodos está vacía
    public static ScheduleException emptyPeriods() {
        return new ScheduleException("El horario debe tener al menos un periodo");
    }

    // Sugerencia: por si el grupo es inválido
    public static ScheduleException invalidGroup() {
        return new ScheduleException("El grupo es inválido o está vacío");
    }
}