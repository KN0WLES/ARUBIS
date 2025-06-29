package unicorn.model;

import java.util.*;

public class Schedule extends Base<Schedule> {
    private String id;
    private String profesorId;
    private String subjectId;
    private String grupo;
    private String clasType;
    private List<Period> periods;

    public Schedule() {
        this.id = UUID.randomUUID().toString();
        this.periods = new ArrayList<>();
    }

    public Schedule(String profesorId, String subjectId, String grupo, String clasType, List<Period> periods) {
        this();
        this.profesorId = profesorId;
        this.subjectId = subjectId;
        this.grupo = grupo;
        this.clasType = clasType;
        if (periods != null) this.periods = periods;
    }

    // Getters y setters
    @Override
    public String getId() { return id; }
    public String getProfesorId() { return profesorId; }
    public String getSubjectId() { return subjectId; }
    public String getGrupo() { return grupo; }
    public List<Period> getPeriods() { return periods; }
    public String getClasType() { return clasType; }

    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public void setGrupo(String grupo) { this.grupo = grupo; }
    public void setPeriods(List<Period> periods) { this.periods = periods; }
    public void setClasType(String clasType) { this.clasType = clasType; }

    // Serialización simple (ajusta según tu persistencia)
    @Override
    public String toFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("|", id, profesorId, subjectId, grupo));
        for (Period p : periods) {
            sb.append("|").append(p.toFile());
        }
        return sb.toString();
    }

    @Override
    public Schedule fromFile(String line) {
        String[] parts = line.split("\\|");
        Schedule schedule = new Schedule();
        schedule.id = parts[0];
        schedule.profesorId = parts[1];
        schedule.subjectId = parts[2];
        schedule.grupo = parts[3];
        schedule.periods = new ArrayList<>();
        for (int i = 4; i < parts.length; i++) {
            Period period = new Period.Builder().build().fromFile(parts[i]);
            schedule.periods.add(period);
        }
        return schedule;
    }

    @Override
    public String getInfo() {
        return String.format("Horario: %s | Profesor: %s | Materia: %s | Grupo: %s | Periodos: %d",
                id, profesorId, subjectId, grupo, periods.size());
    }
}