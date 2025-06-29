package unicorn.service.console;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
//import unicorn.util.*;
import java.util.List;

public class ScheduleMenuController extends BaseMenuController {
    private final ScheduleController scheduleController;
    private final PeriodController periodController;
    private final Account account;

    public ScheduleMenuController(Account account) throws ScheduleException, PeriodException, RoomException, FileException {
        this.account = account;
        IFile<Schedule> scheduleFileHandler = new FileHandler<>(new Schedule());
        IFile<Period> periodFileHandler = new FileHandler<>(new Period.Builder().build());
        this.periodController = new PeriodController(new RoomController(new FileHandler<>(new Room())), periodFileHandler);
        this.scheduleController = new ScheduleController(scheduleFileHandler,periodController);
    }

    @Override
    public void showMenu() {
        switch (account.getTipoCuenta()) {
            case ADMIN -> showAdminMenu();
            case PROFESOR -> showTeacherMenu();
            case ESTUDIANTE -> showStudentMenu();
            default -> System.out.println("❌ Rol no reconocido.");
        }
    }

    // ==================== MENÚ ADMINISTRADOR ====================
    private void showAdminMenu() {
        int option;
        do {
            clearScreen();
            mostrarMensajeCentrado(" GESTIÓN DE HORARIOS (ADMIN) ");
            System.out.println("1. Listar todos los horarios");
            System.out.println("2. Crear nuevo horario");
            System.out.println("3. Modificar horario existente");
            System.out.println("4. Eliminar horario");
            System.out.println("5. Buscar horarios por profesor");
            System.out.println("6. Buscar horarios por materia");
            System.out.println("7. Gestionar periodos de clase");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listAllSchedules();
                    case 2 -> createSchedule();
                    case 3 -> updateSchedule();
                    case 4 -> deleteSchedule();
                    case 5 -> searchSchedulesByProfessor();
                    case 6 -> searchSchedulesBySubject();
                    case 7 -> managePeriods();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (ScheduleException | PeriodException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    // ==================== MENÚ PROFESOR ====================
    private void showTeacherMenu() {
        int option;
        do {
            clearScreen();
            mostrarMensajeCentrado(" MIS HORARIOS (PROFESOR) ");
            System.out.println("1. Ver mis horarios");
            System.out.println("2. Ver detalles de horario");
            System.out.println("3. Gestionar periodos de clase");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listMySchedules();
                    case 2 -> viewScheduleDetails();
                    case 3 -> managePeriods();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (ScheduleException | PeriodException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    // ==================== MENÚ ESTUDIANTE ====================
    private void showStudentMenu() {
        int option;
        do {
            clearScreen();
            mostrarMensajeCentrado(" MIS HORARIOS (ESTUDIANTE) ");
            System.out.println("1. Ver mis horarios");
            System.out.println("2. Ver detalles de horario");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listMySchedules();
                    case 2 -> viewScheduleDetails();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (ScheduleException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    // ==================== MÉTODOS COMUNES ====================
    private void listAllSchedules() throws ScheduleException {
        mostrarMensajeCentrado(" LISTA DE TODOS LOS HORARIOS ");
        List<Schedule> schedules = scheduleController.getAllSchedules();
        if (schedules.isEmpty()) {
            System.out.println("No hay horarios registrados.");
        } else {
            schedules.forEach(schedule -> System.out.println(schedule.getInfo()));
        }
    }

    private void viewScheduleDetails() throws ScheduleException {
        String id = readLine("Ingrese el ID del horario: ");
        Schedule schedule = scheduleController.getScheduleById(id);
        System.out.println("\nDetalles del horario:");
        System.out.println(schedule.getInfo());
        System.out.println("\nPeriodos:");
        schedule.getPeriods().forEach(period -> System.out.println(formatPeriodInfo(period)));
    }

    private String formatPeriodInfo(Period period) {
        return String.format("- %s: %s a %s en %s",
            period.getDay(),
            period.getStart(),
            period.getEnd(),
            period.getRoomId());
    }

    private void listMySchedules() throws ScheduleException {
        mostrarMensajeCentrado(" MIS HORARIOS ");
        List<Schedule> schedules = scheduleController.getSchedulesByProfesor(account.getId());
        if (schedules.isEmpty()) {
            System.out.println("No tienes horarios asignados.");
        } else {
            schedules.forEach(schedule -> {
                System.out.println(schedule.getInfo());
                System.out.println("Periodos:");
                schedule.getPeriods().forEach(period -> System.out.println(formatPeriodInfo(period)));
                System.out.println();
            });
        }
    }

    private void searchSchedulesByProfessor() throws ScheduleException {
        String professorId = readLine("Ingrese el ID del profesor: ");
        List<Schedule> schedules = scheduleController.getSchedulesByProfesor(professorId);
        if (schedules.isEmpty()) {
            System.out.println("El profesor no tiene horarios asignados.");
        } else {
            schedules.forEach(schedule -> System.out.println(schedule.getInfo()));
        }
    }

    private void searchSchedulesBySubject() throws ScheduleException {
        String subjectId = readLine("Ingrese el ID de la materia: ");
        List<Schedule> schedules = scheduleController.getSchedulesBySubject(subjectId);
        if (schedules.isEmpty()) {
            System.out.println("La materia no tiene horarios asignados.");
        } else {
            schedules.forEach(schedule -> System.out.println(schedule.getInfo()));
        }
    }

    // ==================== MÉTODOS DE ADMINISTRADOR ====================
    private void createSchedule() throws ScheduleException {
        mostrarMensajeCentrado(" CREAR NUEVO HORARIO ");
        String professorId = readLine("ID del profesor: ");
        String subjectId = readLine("ID de la materia: ");
        String group = readLine("Grupo: ");
        String classType = readLine("Tipo de clase (T, P, TP): ").toUpperCase();

        Schedule newSchedule = new Schedule(professorId, subjectId, group, classType, new ArrayList<>());
        scheduleController.addSchedule(newSchedule);
        System.out.println("✅ Horario creado exitosamente. ID: " + newSchedule.getId());
    }

    private void updateSchedule() throws ScheduleException {
        mostrarMensajeCentrado(" MODIFICAR HORARIO ");
        String id = readLine("ID del horario a modificar: ");
        Schedule schedule = scheduleController.getScheduleById(id);

        System.out.println("Datos actuales:");
        System.out.println(schedule.getInfo());

        String newProfessorId = readLine("Nuevo ID de profesor (dejar vacío para no cambiar): ");
        if (!newProfessorId.isEmpty()) schedule.setProfesorId(newProfessorId);

        String newSubjectId = readLine("Nuevo ID de materia (dejar vacío para no cambiar): ");
        if (!newSubjectId.isEmpty()) schedule.setSubjectId(newSubjectId);

        String newGroup = readLine("Nuevo grupo (dejar vacío para no cambiar): ");
        if (!newGroup.isEmpty()) schedule.setGrupo(newGroup);

        scheduleController.updateSchedule(schedule);
        System.out.println("✅ Horario modificado exitosamente.");
    }

    private void deleteSchedule() throws ScheduleException {
        mostrarMensajeCentrado(" ELIMINAR HORARIO ");
        String id = readLine("ID del horario a eliminar: ");
        System.out.println("¿Está seguro? (S/N): ");
        if (readLine("").equalsIgnoreCase("S")) {
            scheduleController.deleteSchedule(id);
            System.out.println("✅ Horario eliminado exitosamente.");
        }
    }

    // ==================== MÉTODOS PARA PERIODOS ====================
    private void managePeriods() throws PeriodException, ScheduleException {
        int option;
        do {
            clearScreen();
            mostrarMensajeCentrado(" GESTIÓN DE PERIODOS ");
            System.out.println("1. Agregar periodo a horario");
            System.out.println("2. Modificar periodo");
            System.out.println("3. Eliminar periodo");
            System.out.println("4. Ver disponibilidad de aula");
            System.out.println("0. Volver");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> addPeriodToSchedule();
                    case 2 -> updatePeriod();
                    case 3 -> deletePeriod();
                    case 4 -> checkRoomAvailability();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (PeriodException | ScheduleException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    private void addPeriodToSchedule() throws PeriodException, ScheduleException {
        mostrarMensajeCentrado(" AGREGAR PERIODO A HORARIO ");
        String scheduleId = readLine("ID del horario: ");
        Schedule schedule = scheduleController.getScheduleById(scheduleId);

        System.out.println("Día de la semana (LUNES, MARTES, ...): ");
        String day = readLine("").toUpperCase();
        
        System.out.println("Hora de inicio (HH:MM): ");
        String startTime = readLine("");
        
        System.out.println("Hora de fin (HH:MM): ");
        String endTime = readLine("");
        
        String roomId = readLine("ID del aula: ");

        Period newPeriod = periodController.createPeriod(
            schedule.getSubjectId(), 
            roomId, 
            DayOfWeek.valueOf(day),
            LocalTime.parse(startTime),
            LocalTime.parse(endTime)
        );

        List<Period> periods = new ArrayList<>(schedule.getPeriods());
        periods.add(newPeriod);
        schedule.setPeriods(periods);
        scheduleController.updateSchedule(schedule);
        System.out.println("✅ Periodo agregado exitosamente.");
    }

    private void updatePeriod() throws PeriodException {
        mostrarMensajeCentrado(" MODIFICAR PERIODO ");
        String periodId = readLine("ID del periodo a modificar: ");
        
        System.out.println("Nuevo día de la semana (LUNES, MARTES, ...): ");
        String newDay = readLine("").toUpperCase();
        
        System.out.println("Nueva hora de inicio (HH:MM): ");
        String newStartTime = readLine("");
        
        System.out.println("Nueva hora de fin (HH:MM): ");
        String newEndTime = readLine("");
        
        String newRoomId = readLine("Nuevo ID del aula: ");

        Period updated = periodController.editPeriod(
            periodId,
            "", // subjectId no cambia
            newRoomId,
            DayOfWeek.valueOf(newDay),
            LocalTime.parse(newStartTime),
            LocalTime.parse(newEndTime)
        );
        
        System.out.println("✅ Periodo modificado exitosamente.");
    }

    private void deletePeriod() throws PeriodException {
        mostrarMensajeCentrado(" ELIMINAR PERIODO ");
        String periodId = readLine("ID del periodo a eliminar: ");
        System.out.println("¿Está seguro? (S/N): ");
        if (readLine("").equalsIgnoreCase("S")) {
            periodController.deletePeriod(periodId);
            System.out.println("✅ Periodo eliminado exitosamente.");
        }
    }

    private void checkRoomAvailability() throws PeriodException {
        mostrarMensajeCentrado(" VERIFICAR DISPONIBILIDAD DE AULA ");
        String roomId = readLine("ID del aula: ");
        System.out.println("Día de la semana (LUNES, MARTES, ...): ");
        String day = readLine("").toUpperCase();
        
        List<Period> scheduledPeriods = periodController.getScheduledPeriodsForRoom(
            roomId, 
            DayOfWeek.valueOf(day)
        );
        
        if (scheduledPeriods.isEmpty()) {
            System.out.println("El aula está disponible todo el día.");
        } else {
            System.out.println("Periodos ocupados:");
            scheduledPeriods.forEach(period -> System.out.println(formatPeriodInfo(period)));
        }
    }
}