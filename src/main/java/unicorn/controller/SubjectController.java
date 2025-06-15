package unicorn.controller;

import unicorn.model.Subject;
import unicorn.model.Schedule;
import unicorn.exceptions.SubjectException;
import unicorn.interfaces.IFile;
import unicorn.interfaces.ISchedule;
import unicorn.interfaces.ISubject;
import unicorn.exceptions.ScheduleException;
import java.util.*;
import java.util.stream.Collectors;

public class SubjectController implements ISubject {
    private final IFile<Subject> fileHandler;
    private final String filePath = "src/main/java/unicorn/dto/subjects.txt";
    private Map<String, Subject> subjects;
    private ISchedule scheduleController;

    public SubjectController(IFile<Subject> fileHandler) throws SubjectException {
        this.fileHandler = fileHandler;
        this.scheduleController = scheduleController;
        this.subjects = new HashMap<>();
        loadData();
    }

    private void loadData() throws SubjectException {
        try {
            fileHandler.createFileIfNotExists(filePath);
            List<Subject> loadedSubjects = fileHandler.loadData(filePath);
            if (loadedSubjects != null) {
                loadedSubjects.forEach(s -> subjects.put(s.getId(), s));
            }
        } catch (Exception e) {
            throw new SubjectException("Error al cargar materias: " + e.getMessage());
        }
    }

    private void saveData() throws SubjectException {
        try {
            fileHandler.saveData(new ArrayList<>(subjects.values()), filePath);
        } catch (Exception e) {
            throw new SubjectException("Error al guardar materias: " + e.getMessage());
        }
    }

    @Override
    public void addSubject(Subject subject) throws SubjectException {
        if (subjects.values().stream().anyMatch(s -> s.getNombre().equalsIgnoreCase(subject.getNombre()))) {
            throw SubjectException.alreadyExists();
        }
        subjects.put(subject.getId(), subject);
        saveData();
    }

    @Override
    public Subject getSubjectById(String id) throws SubjectException {
        Subject subject = subjects.get(id);
        if (subject == null) throw SubjectException.notFound();
        return subject;
    }

    @Override
    public Subject getSubjectByName(String name) throws SubjectException {
        return subjects.values().stream()
                .filter(s -> s.getNombre().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(SubjectException::notFound);
    }

    @Override
    public void updateSubject(Subject subject) throws SubjectException {
        if (!subjects.containsKey(subject.getId())) {
            throw SubjectException.notFound();
        }
        subjects.put(subject.getId(), subject);
        saveData();
    }

    @Override
    public void deleteSubject(String id) throws SubjectException {
        if (!subjects.containsKey(id)) {
            throw SubjectException.notFound();
        }
        subjects.remove(id);
        saveData();
    }

    @Override
    public List<Subject> getSubjectsByProfessor(String professorId) throws SubjectException {
        return subjects.values().stream()
                .filter(s -> s.getProfesorId().equals(professorId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Subject> getAllSubjects() throws SubjectException {
        return new ArrayList<>(subjects.values());
    }

    @Override
    public void addScheduleToSubject(String subjectId, Schedule schedule) throws SubjectException, ScheduleException {
        Subject subject = getSubjectById(subjectId);
        scheduleController.addSchedule(schedule);
        subject.addSchedule(schedule.getId(), schedule);
        saveData();
    }

    @Override
    public void removeScheduleFromSubject(String subjectId, String scheduleId) throws SubjectException, ScheduleException {
        Subject subject = getSubjectById(subjectId);
        scheduleController.deleteSchedule(scheduleId);
        subject.removeSchedule(scheduleId);
        saveData();
    }
}