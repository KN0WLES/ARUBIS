package unicorn.controller;


import unicorn.interfaces.*;
import unicorn.exceptions.*;
import unicorn.model.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class ScheduleController implements ISchedule {
    private final IFile<Schedule> fileHandler;
    private final PeriodController periodController;
    private final String filePath = "src/main/java/unicorn/dto/schedules.txt";
    private Map<String, Schedule> schedules;

     public ScheduleController(IFile<Schedule> fileHandler, PeriodController periodController) 
        throws ScheduleException {
        this.fileHandler = fileHandler;
        this.periodController = periodController;
        this.schedules = new HashMap<>();
        loadData();
    }

    private void loadData() throws ScheduleException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<Schedule> loaded = fileHandler.loadData(filePath);
            if (loaded != null) loaded.forEach(s -> schedules.put(s.getId(), s));
        } catch (Exception e) {
            throw new ScheduleException("Error al cargar horarios: " + e.getMessage());
        }
    }

    private void saveData() throws ScheduleException {
        try {
            fileHandler.saveData(new ArrayList<>(schedules.values()), filePath);
        } catch (Exception e) {
            throw new ScheduleException("Error al guardar horarios: " + e.getMessage());
        }
    }

    @Override
    public void addSchedule(Schedule schedule) throws ScheduleException {
        if (schedules.containsKey(schedule.getId()))
            throw new ScheduleException("Ya existe un horario con este ID");
        schedules.put(schedule.getId(), schedule);
        saveData();
    }

    @Override
    public Schedule getScheduleById(String id) throws ScheduleException {
        Schedule s = schedules.get(id);
        if (s == null) throw new ScheduleException("Horario no encontrado");
        return s;
    }

    @Override
    public List<Schedule> getSchedulesByProfesor(String profesorId) throws ScheduleException {
        List<Schedule> result = new ArrayList<>();
        for (Schedule s : schedules.values()) {
            if (s.getProfesorId().equals(profesorId)) result.add(s);
        }
        return result;
    }

    @Override
    public List<Schedule> getSchedulesBySubject(String subjectId) throws ScheduleException {
        List<Schedule> result = new ArrayList<>();
        for (Schedule s : schedules.values()) {
            if (s.getSubjectId().equals(subjectId)) result.add(s);
        }
        return result;
    }

    @Override
    public void updateSchedule(Schedule schedule) throws ScheduleException {
        if (!schedules.containsKey(schedule.getId()))
            throw new ScheduleException("Horario no encontrado");
        schedules.put(schedule.getId(), schedule);
        saveData();
    }

    @Override
    public void deleteSchedule(String id) throws ScheduleException {
        if (schedules.remove(id) != null) {
            saveData();
        } else {
            throw new ScheduleException("Horario no encontrado");
        }
    }

    @Override
    public List<Schedule> getAllSchedules() throws ScheduleException {
        return new ArrayList<>(schedules.values());
    }

    /**
     * Crea un horario completo para una materia con múltiples periodos
     * 
     * @param subjectId ID de la materia
     * @param group Grupo de la materia (ej: "10")
     * @param professorId ID del profesor
     * @param classType Tipo de clase (ej: "R" para Regular)
     * @param periods Lista de periodos (día, hora inicio, hora fin, aula)
     * @return El horario creado
     * @throws ScheduleException Si hay errores al crear el horario
     */
    public Schedule createCompleteSchedule(String subjectId, String group, String professorId, 
                                            String classType, List<PeriodInfo> periodsInfo) 
                                            throws ScheduleException {
        List<Period> periods = new ArrayList<>();
        for (PeriodInfo period : periodsInfo) {
            try {
                Period newPeriod = periodController.createPeriod(
                    subjectId,
                    period.roomId(),
                    period.day(),
                    period.startTime(),
                    period.endTime()
                );
                periods.add(newPeriod);
            } catch (PeriodException e) {
                throw new ScheduleException("Error al crear periodo: " + e.getMessage());
            }
        }

        Schedule schedule = new Schedule(
            professorId,
            subjectId,
            group,
            classType,
            periods
        );

        addSchedule(schedule);
        return schedule;
    }
    
    // Clase de apoyo para información de periodos
    public record PeriodInfo(DayOfWeek day, LocalTime startTime, LocalTime endTime, String roomId) {}

}
