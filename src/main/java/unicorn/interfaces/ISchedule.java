package unicorn.interfaces;

import unicorn.model.Schedule;
import unicorn.exceptions.ScheduleException;
import java.util.List;

public interface ISchedule {
    void addSchedule(Schedule schedule) throws ScheduleException;
    Schedule getScheduleById(String id) throws ScheduleException;
    List<Schedule> getSchedulesByProfesor(String profesorId) throws ScheduleException;
    List<Schedule> getSchedulesBySubject(String subjectId) throws ScheduleException;
    void updateSchedule(Schedule schedule) throws ScheduleException;
    void deleteSchedule(String id) throws ScheduleException;
    List<Schedule> getAllSchedules() throws ScheduleException;
}
