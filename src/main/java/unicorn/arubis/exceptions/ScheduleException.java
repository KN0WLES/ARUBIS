package unicorn.arubis.exceptions;

public class ScheduleException extends Exception {
    public ScheduleException(String message) {
        super(message);
    }

    public static ScheduleException notFound() {
        return new ScheduleException("Horario no encontrado");
    }

    public static ScheduleException timeConflict() {
        return new ScheduleException("Conflicto de horario detectado");
    }

    public static ScheduleException invalidTimeFormat() {
        return new ScheduleException("Formato de hora inválido (use HH:mm)");
    }

    public static ScheduleException invalidDay() {
        return new ScheduleException("Día de la semana inválido");
    }
}