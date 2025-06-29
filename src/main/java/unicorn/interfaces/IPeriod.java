package unicorn.interfaces;

import java.time.DayOfWeek;
import java.time.LocalTime;

import unicorn.exceptions.PeriodException;
import unicorn.model.Period;

public interface IPeriod {
    Period createPeriod(String subjectId, String roomId, DayOfWeek day, 
                          LocalTime start, LocalTime end) throws PeriodException;
    boolean validatePeriod(Period period) throws PeriodException;
    boolean checkRoomAvailability(Period period) throws PeriodException;
}