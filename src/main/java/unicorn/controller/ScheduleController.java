package unicorn.controller;

import unicorn.model.Schedule;
import unicorn.exceptions.ScheduleException;
import unicorn.interfaces.IFile;
import unicorn.interfaces.ISchedule;

import java.util.*;
import java.util.stream.Collectors;

public class ScheduleController implements ISchedule {
    private final IFile<Schedule> fileHandler;
    private final String filePath = "src/main/java/unicorn/dto/schedules.txt";
    private Map<String, Schedule> schedules;

    public ScheduleController(IFile<Schedule> fileHandler) throws ScheduleException {
        this.fileHandler = fileHandler;
        this.schedules = new HashMap<>();
        loadData();
    }

    private void loadData() throws ScheduleException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<Schedule> loadedSchedules = fileHandler.loadData(filePath);
            if (loadedSchedules != null) {
                loadedSchedules.forEach(s -> schedules.put(s.getId(), s));
            }
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
        if (checkTimeConflict(schedule)) {
            throw ScheduleException.timeConflict();
        }
        schedules.put(schedule.getId(), schedule);
        saveData();
    }

    @Override
    public Schedule getScheduleById(String id) throws ScheduleException {
        Schedule schedule = schedules.get(id);
        if (schedule == null) throw ScheduleException.notFound();
        return schedule;
    }

    @Override
    public void updateSchedule(Schedule schedule) throws ScheduleException {
        if (!schedules.containsKey(schedule.getId())) {
            throw ScheduleException.notFound();
        }
        if (checkTimeConflict(schedule)) {
            throw ScheduleException.timeConflict();
        }
        schedules.put(schedule.getId(), schedule);
        saveData();
    }

    @Override
    public void deleteSchedule(String id) throws ScheduleException {
        if (!schedules.containsKey(id)) {
            throw ScheduleException.notFound();
        }
        schedules.remove(id);
        saveData();
    }

    @Override
    public List<Schedule> getSchedulesByDay(String day) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getDiaSemana().equalsIgnoreCase(day))
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getSchedulesByClassroom(String classroomId) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getClassroomId().equals(classroomId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Schedule> getSchedulesBySubject(String subjectId) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getMateriaId().equals(subjectId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkTimeConflict(Schedule schedule) throws ScheduleException {
        return schedules.values().stream()
                .filter(s -> s.getClassroomId().equals(schedule.getClassroomId()))
                .filter(s -> s.getDiaSemana().equalsIgnoreCase(schedule.getDiaSemana()))
                .anyMatch(s -> timeOverlap(s, schedule));
    }

    private boolean timeOverlap(Schedule s1, Schedule s2) {
        // Implementación de lógica para detectar superposición de horarios
        // Comparar horaInicio y horaFin de ambos horarios
        // Retorna true si hay superposición
        return false; // Implementar lógica real
    }
}