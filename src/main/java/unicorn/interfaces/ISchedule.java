package unicorn.interfaces;

import unicorn.model.Schedule;
import unicorn.exceptions.ScheduleException;
import java.util.List;

public interface ISchedule {
    void addSchedule(Schedule schedule) throws ScheduleException;
    Schedule getScheduleById(String id) throws ScheduleException;
    void updateSchedule(Schedule schedule) throws ScheduleException;
    void deleteSchedule(String id) throws ScheduleException;
    List<Schedule> getSchedulesByDay(String day) throws ScheduleException;
    List<Schedule> getSchedulesByClassroom(String classroomId) throws ScheduleException;
    List<Schedule> getSchedulesBySubject(String subjectId) throws ScheduleException;
    boolean checkTimeConflict(Schedule schedule) throws ScheduleException;
}