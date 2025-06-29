package unicorn.controller;

import unicorn.interfaces.*;
import unicorn.exceptions.*;
import unicorn.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class PeriodController implements IPeriod {
    private final RoomController roomService;
    private final IFile<Period> fileHandler;
    private final String filePath = "src/main/java/unicorn/dto/periods.txt";
    private List<Period> periods;

    public PeriodController(RoomController roomService, IFile<Period> fileHandler) throws FileException {
        this.roomService = roomService;
        this.fileHandler = fileHandler;
        this.fileHandler.createFileIfNotExists(filePath);
        this.periods = fileHandler.loadData(filePath);
        if (this.periods == null) this.periods = new ArrayList<>();
    }

    // Crea y guarda un periodo
    @Override
    public Period createPeriod(String subjectId, String roomId, DayOfWeek day,
                               LocalTime start, LocalTime end) throws PeriodException {
        Period period = new Period.Builder()
                .withId(UUID.randomUUID().toString())
                .withDay(day)
                .withTimeRange(start, end)
                .forSubject(subjectId)
                .inRoom(roomId)
                .build();

        if (!validatePeriod(period)) {
            throw new PeriodException("Invalid period configuration");
        }
        periods.add(period);
        try {
            fileHandler.saveData(periods, filePath);
        } catch (FileException e) {
            throw new PeriodException("Error al guardar el periodo: " + e.getMessage());
        }
        return period;
    }

    // Valida reglas de negocio de periodos
    @Override
    public boolean validatePeriod(Period period) throws PeriodException {
        if (period == null) throw new PeriodException("Period cannot be null");
        if (period.getStart() == null || period.getEnd() == null)
            throw new PeriodException("Start and end time must be specified");
        if (period.getStart().isAfter(period.getEnd()) || period.getStart().equals(period.getEnd()))
            throw new PeriodException("Start time must be before end time");
        if (period.getDay() == null)
            throw new PeriodException("Day of week must be specified");
        if (period.getRoomId() == null || period.getRoomId().isEmpty())
            throw new PeriodException("Room ID must be specified");
        if (period.getSubjectId() == null || period.getSubjectId().isEmpty())
            throw new PeriodException("Subject ID must be specified");

        LocalTime minTime = LocalTime.of(6, 45);
        LocalTime maxTime;
        switch (period.getDay()) {
            case SATURDAY:
                maxTime = LocalTime.of(15, 45);
                break;
            case SUNDAY:
                throw new PeriodException("No periods allowed on Sunday");
            default:
                maxTime = LocalTime.of(21, 45);
        }

        if (period.getStart().isBefore(minTime) || period.getEnd().isAfter(maxTime))
            throw new PeriodException("Period time out of allowed range for the day");

        return true;
    }

    // Verifica traslape de horarios en la sala
    @Override
    public boolean checkRoomAvailability(Period period) throws PeriodException {
        List<Period> scheduledPeriods = getScheduledPeriodsForRoom(period.getRoomId(), period.getDay());
        for (Period scheduled : scheduledPeriods) {
            if (periodsOverlap(period, scheduled)) {
                return false;
            }
        }
        return true;
    }

    // Devuelve los periodos agendados para una sala y día
    public List<Period> getScheduledPeriodsForRoom(String roomId, DayOfWeek day) {
        List<Period> result = new ArrayList<>();
        for (Period p : periods) {
            if (p.getRoomId().equals(roomId) && p.getDay() == day) {
                result.add(p);
            }
        }
        return result;
    }

    // Utilidad para traslape de periodos
    private boolean periodsOverlap(Period p1, Period p2) {
        if (!p1.getDay().equals(p2.getDay())) return false;
        return !p1.getEnd().isBefore(p2.getStart()) && !p1.getStart().isAfter(p2.getEnd());
    }

        /**
     * Edita un periodo existente por ID.
     * Retorna el periodo actualizado o lanza excepción si no existe.
     */
    public Period editPeriod(String periodId, String newSubjectId, String newRoomId, DayOfWeek newDay,
                             LocalTime newStart, LocalTime newEnd) throws PeriodException {
        Period toEdit = null;
        for (Period p : periods) {
            if (p.getId().equals(periodId)) {
                toEdit = p;
                break;
            }
        }
        if (toEdit == null) throw new PeriodException("Period not found");

        // Crea el nuevo periodo con los datos actualizados
        Period updated = new Period.Builder()
                .withId(periodId)
                .withDay(newDay)
                .withTimeRange(newStart, newEnd)
                .forSubject(newSubjectId)
                .inRoom(newRoomId)
                .build();

        if (!validatePeriod(updated)) {
            throw new PeriodException("Invalid updated period configuration");
        }

        // Reemplaza el periodo en la lista
        periods.remove(toEdit);
        periods.add(updated);

        try {
            fileHandler.saveData(periods, filePath);
        } catch (FileException e) {
            throw new PeriodException("Error al guardar el periodo editado: " + e.getMessage());
        }
        return updated;
    }

    /**
     * Elimina un periodo por ID.
     * Retorna true si se eliminó, false si no existe.
     */
    public boolean deletePeriod(String periodId) throws PeriodException {
        boolean removed = periods.removeIf(p -> p.getId().equals(periodId));
        if (removed) {
            try {
                fileHandler.saveData(periods, filePath);
            } catch (FileException e) {
                throw new PeriodException("Error al eliminar el periodo: " + e.getMessage());
            }
        }
        return removed;
    }

    /**
     * Crea horarios por defecto para todas las aulas (excepto auditorios) desde las 6:45 hasta las 21:45
     * de lunes a viernes, y hasta las 15:45 los sábados.
     * 
     * @param subjectId El ID de la materia a asignar a estos periodos (puede ser un ID genérico o nulo)
     * @throws PeriodException Si ocurre un error al crear los periodos
     */
    @SuppressWarnings("unlikely-arg-type")
    public void createDefaultPeriodsForAllRooms(String subjectId) throws PeriodException {
        // Obtener todas las aulas que no son auditorios
        List<Room> classrooms = roomService.getAllRooms().stream()
                .filter(room -> (!room.getTipo().equals("AUDITORIO") && 
                                !room.getTipo().equals("VIRTUAL")))
                .toList();

        // Días de la semana a programar (de lunes a sábado)
        DayOfWeek[] days = {
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY
        };

        // Crear periodos para cada aula y cada día
        for (Room room : classrooms) {
            for (DayOfWeek day : days) {
                LocalTime startTime = LocalTime.of(6, 45);
                LocalTime endTime = day == DayOfWeek.SATURDAY ? 
                        LocalTime.of(15, 45) : 
                        LocalTime.of(21, 45);

                try {
                    // Crear un periodo que abarque todo el día disponible
                    createPeriod(subjectId, room.getId(), day, startTime, endTime);
                } catch (PeriodException e) {
                    // Continuar con las demás aulas si hay error en una
                    System.err.println("Error al crear periodo para aula " + room.getId() + 
                                    ", día " + day + ": " + e.getMessage());
                }
            }
        }
    }
}