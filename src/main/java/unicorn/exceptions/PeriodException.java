package unicorn.exceptions;

public class PeriodException extends Exception {
    public PeriodException(String message) {
        super(message);
    }

    // Sugerencia: por si el periodo no existe
    public static PeriodException notFound() {
        return new PeriodException("Periodo no encontrado");
    }

    // Sugerencia: por si hay traslape de periodos
    public static PeriodException overlap() {
        return new PeriodException("El periodo traslapa con otro existente");
    }

    // Sugerencia: por si el horario está fuera de rango
    public static PeriodException outOfRange() {
        return new PeriodException("El periodo está fuera del rango permitido");
    }

    // Sugerencia: por si falta información
    public static PeriodException missingData() {
        return new PeriodException("Faltan datos obligatorios para el periodo");
    }
}