package unicorn.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
//import java.util.Objects;

public final class Period extends Base<Period>{
    private final String id;
    private final DayOfWeek day;
    private final LocalTime start;
    private final LocalTime end;
    private final String subjectId;
    private final String roomId;


    private Period(Builder builder) {
        this.id = builder.id;
        this.day = builder.day;
        this.start = builder.start;
        this.end = builder.end;
        this.subjectId = builder.subjectId;
        this.roomId = builder.roomId;
        validate();
    }

    public Period(){
        this.id = "default-id";
        this.day = DayOfWeek.MONDAY;
        this.start = LocalTime.of(8, 0);  // 08:00 AM
        this.end = LocalTime.of(9, 0);    // 09:00 AM
        this.subjectId = "default-subject";
        this.roomId = "default-room";
    }

    private void validate() {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
        if (day == null) {
            throw new IllegalArgumentException("Day of week cannot be null");
        }
    }

    // Pattern Builder
    public static class Builder {
        private String id = "default-id";
        private DayOfWeek day = DayOfWeek.MONDAY;
        private LocalTime start = LocalTime.of(8, 0);
        private LocalTime end = LocalTime.of(9, 0);
        private String subjectId = "default-subject";
        private String roomId = "default-room";

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withDay(DayOfWeek day) {
            this.day = day;
            return this;
        }

        public Builder withTimeRange(LocalTime start, LocalTime end) {
            this.start = start;
            this.end = end;
            return this;
        }

        public Builder forSubject(String subjectId) {
            this.subjectId = subjectId;
            return this;
        }

        public Builder inRoom(String roomId) {
            this.roomId = roomId;
            return this;
        }

        public Period build() {
            return new Period(this);
        }
    }

    // Getters
    public String getId() { return id; }
    public DayOfWeek getDay() { return day; }
    public LocalTime getStart() { return start; }
    public LocalTime getEnd() { return end; }
    public String getSubjectId() { return subjectId; }
    public String getRoomId() { return roomId; }

    public String toFile() {
        // id,day,start,end,subjectId,roomId
        return String.join(",", id, day.toString(), start.toString(), end.toString(), subjectId, roomId);
    }

    public Period fromFile(String line) {
        String[] parts = line.split(",");
        return new Period.Builder()
                .withId(parts[0])
                .withDay(DayOfWeek.valueOf(parts[1]))
                .withTimeRange(LocalTime.parse(parts[2]), LocalTime.parse(parts[3]))
                .forSubject(parts[4])
                .inRoom(parts[5])
                .build();
    }

    public String getInfo() {
        return String.format("Periodo: %s\nDía: %s\nHora Inicio: %s\nHora Fin: %s\nMateria: %s\nAula: %s",
                id, day, start, end, subjectId, roomId);
    }
}