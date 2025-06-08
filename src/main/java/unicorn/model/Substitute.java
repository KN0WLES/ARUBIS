package unicorn.model;

import java.time.LocalDate;
import java.util.UUID;
/**
 * Modelo que representa un suplente temporal para un docente promovido a administrador.
 * Extiende la clase Base para heredar la funcionalidad de serialización.
 *
 * @description Funcionalidades principales:
 *                   - Registro de sustituciones temporales
 *                   - Gestión de períodos de sustitución
 *                   - Control de estado activo/inactivo
 *                   - Serialización/deserialización a archivo
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-06-15
 */
public class Substitute extends Base<Substitute> {
    private String id;
    private String originalTeacherId;
    private String substituteTeacherId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String scheduleBackupPath;

    public Substitute() {
        this.id = UUID.randomUUID().toString();
    }

    public Substitute(String originalTeacherId, String substituteTeacherId,
                      LocalDate startDate, LocalDate endDate) {
        this();
        this.originalTeacherId = originalTeacherId;
        this.substituteTeacherId = substituteTeacherId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = true;
    }

    @Override
    public String getId() { return this.id; }
    public String getOriginalTeacherId() { return originalTeacherId; }
    public String getSubstituteTeacherId() { return substituteTeacherId; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public boolean isActive() { return active; }
    public String getScheduleBackupPath() { return scheduleBackupPath; }
    public boolean isForAccount(String accountId) {
        return this.originalTeacherId.equals(accountId);
    }

    public void setId(String id) { this.id = id; }
    public void setOriginalTeacherId(String originalTeacherId) { this.originalTeacherId = originalTeacherId; }
    public void setSubstituteTeacherId(String substituteTeacherId) { this.substituteTeacherId = substituteTeacherId; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setActive(boolean active) { this.active = active; }
    public void setScheduleBackupPath(String scheduleBackupPath) { this.scheduleBackupPath = scheduleBackupPath; }

    public boolean isCurrentlyActive() {
        LocalDate today = LocalDate.now();
        return active &&
                !today.isBefore(startDate) &&
                (endDate == null || !today.isAfter(endDate));
    }

    public void markAsCompleted() {
        this.active = false;
        this.endDate = LocalDate.now();
    }

    @Override
    public String toFile() {
        return String.join("|",
                id,
                originalTeacherId,
                substituteTeacherId,
                startDate.toString(),
                endDate != null ? endDate.toString() : "null",
                String.valueOf(active),
                scheduleBackupPath != null ? scheduleBackupPath : "null"
        );
    }

    @Override
    public Substitute fromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Formato de línea inválido para Substitute");
        }

        Substitute sub = new Substitute();
        sub.setId(parts[0]);
        sub.setOriginalTeacherId(parts[1]);
        sub.setSubstituteTeacherId(parts[2]);
        sub.setStartDate(LocalDate.parse(parts[3]));
        sub.setEndDate(!"null".equals(parts[4]) ? LocalDate.parse(parts[4]) : null);
        sub.setActive(Boolean.parseBoolean(parts[5]));
        sub.setScheduleBackupPath(!"null".equals(parts[6]) ? parts[6] : null);

        return sub;
    }

    @Override
    public String getInfo() {
        return String.format(
                "Sustitución [Profesor: %s -> Sustituto: %s] del %s al %s | Estado: %s",
                originalTeacherId,
                substituteTeacherId,
                startDate,
                endDate != null ? endDate : "Indefinido",
                active ? "ACTIVO" : "INACTIVO"
        );
    }
}