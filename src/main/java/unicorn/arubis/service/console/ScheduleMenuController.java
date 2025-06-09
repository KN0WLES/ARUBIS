package unicorn.arubis.service.console;

import unicorn.arubis.controller.FileHandler;
import unicorn.arubis.controller.ScheduleController;
import unicorn.arubis.model.Schedule;
import unicorn.arubis.exceptions.ScheduleException;
import unicorn.arubis.interfaces.IFile;

import java.util.List;
import java.util.Scanner;

/**
 * Clase principal para la gestión de horarios con control de acceso por roles
 */
public class ScheduleMenuController {
    private final ScheduleController scheduleController;
    private final Scanner scanner;
    private UserRole currentRole;

    // Roles del sistema
    private enum UserRole {
        ADMINISTRADOR,
        PROFESOR,
        ALUMNO
    }

    public ScheduleMenuController() throws ScheduleException {
        IFile<Schedule> fileHandler = new FileHandler<>(new Schedule());
        this.scheduleController = new ScheduleController(fileHandler);
        this.scanner = new Scanner(System.in);
        this.currentRole = null;
    }

    public static void main(String[] args) {
        try {
            ScheduleMenuController menu = new ScheduleMenuController();
            menu.authenticateUser();
            menu.run();
        } catch (ScheduleException e) {
            System.err.println("Error al inicializar el sistema: " + e.getMessage());
        }
    }

    /**
     * Autenticación de usuario y asignación de rol
     */
    private void authenticateUser() {
        System.out.println("=== SISTEMA DE GESTIÓN DE HORARIOS ===");
        System.out.println("1. Administrador");
        System.out.println("2. Profesor");
        System.out.println("3. Alumno");
        System.out.print("Seleccione su rol: ");
        
        int roleChoice = readIntInput("");
        
        switch (roleChoice) {
            case 1:
                currentRole = UserRole.ADMINISTRADOR;
                System.out.println("\nBienvenido Administrador");
                break;
            case 2:
                currentRole = UserRole.PROFESOR;
                System.out.println("\nBienvenido Profesor");
                break;
            case 3:
                currentRole = UserRole.ALUMNO;
                System.out.println("\nBienvenido Alumno");
                break;
            default:
                System.out.println("Opción inválida. Saliendo del sistema.");
                System.exit(0);
        }
    }

    /**
     * Método principal que muestra el menú según el rol
     */
    public void run() {
        boolean running = true;
        
        while (running) {
            showRoleSpecificMenu();
            int option = readIntInput("Seleccione una opción: ");
            
            try {
                switch (currentRole) {
                    case ADMINISTRADOR:
                        running = handleAdminOptions(option);
                        break;
                    case PROFESOR:
                        running = handleTeacherOptions(option);
                        break;
                    case ALUMNO:
                        running = handleStudentOptions(option);
                        break;
                }
            } catch (ScheduleException e) {
                System.err.println("Error: " + e.getMessage());
            }
            
            if (running) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
                scanner.nextLine(); // Doble lectura para limpiar buffer
            }
        }
        
        scanner.close();
    }

    private void showRoleSpecificMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        switch (currentRole) {
            case ADMINISTRADOR:
                System.out.println("1. Agregar nuevo horario");
                System.out.println("2. Consultar horario por ID");
                System.out.println("3. Actualizar horario");
                System.out.println("4. Eliminar horario");
                System.out.println("5. Listar horarios por día");
                System.out.println("6. Listar horarios por aula");
                System.out.println("7. Listar horarios por materia");
                System.out.println("8. Cambiar rol");
                System.out.println("9. Salir");
                break;
            case PROFESOR:
                System.out.println("1. Consultar mis horarios");
                System.out.println("2. Consultar horario por ID");
                System.out.println("3. Listar horarios por día");
                System.out.println("4. Listar horarios por aula");
                System.out.println("5. Cambiar rol");
                System.out.println("6. Salir");
                break;
            case ALUMNO:
                System.out.println("1. Consultar mis horarios");
                System.out.println("2. Listar horarios por día");
                System.out.println("3. Listar horarios por materia");
                System.out.println("4. Cambiar rol");
                System.out.println("5. Salir");
                break;
        }
    }

    private boolean handleAdminOptions(int option) throws ScheduleException {
        switch (option) {
            case 1:
                addSchedule();
                break;
            case 2:
                viewSchedule();
                break;
            case 3:
                updateSchedule();
                break;
            case 4:
                deleteSchedule();
                break;
            case 5:
                listSchedulesByDay();
                break;
            case 6:
                listSchedulesByClassroom();
                break;
            case 7:
                listSchedulesBySubject();
                break;
            case 8:
                authenticateUser();
                break;
            case 9:
                return false;
            default:
                System.out.println("Opción no válida");
        }
        return true;
    }

    private boolean handleTeacherOptions(int option) throws ScheduleException {
        switch (option) {
            case 1:
                // Suponiendo que el profesor tiene un ID asociado
                String teacherId = readStringInput("Ingrese su ID de profesor: ");
                List<Schedule> teacherSchedules = scheduleController.getSchedulesBySubject(teacherId);
                printScheduleList(teacherSchedules, "Mis horarios");
                break;
            case 2:
                viewSchedule();
                break;
            case 3:
                listSchedulesByDay();
                break;
            case 4:
                listSchedulesByClassroom();
                break;
            case 5:
                authenticateUser();
                break;
            case 6:
                return false;
            default:
                System.out.println("Opción no válida");
        }
        return true;
    }

    private boolean handleStudentOptions(int option) throws ScheduleException {
        switch (option) {
            case 1:
                // Suponiendo que el alumno tiene un ID de grupo/materia
                String studentGroup = readStringInput("Ingrese su ID de grupo/materia: ");
                List<Schedule> studentSchedules = scheduleController.getSchedulesBySubject(studentGroup);
                printScheduleList(studentSchedules, "Mis horarios");
                break;
            case 2:
                listSchedulesByDay();
                break;
            case 3:
                listSchedulesBySubject();
                break;
            case 4:
                authenticateUser();
                break;
            case 5:
                return false;
            default:
                System.out.println("Opción no válida");
        }
        return true;
    }

    private void addSchedule() throws ScheduleException {
        System.out.println("\n--- AGREGAR NUEVO HORARIO ---");
        
        String diaSemana = readStringInput("Día de la semana (ej. Lunes): ");
        String horaInicio = readStringInput("Hora de inicio (HH:mm): ");
        String horaFin = readStringInput("Hora de fin (HH:mm): ");
        String materiaId = readStringInput("ID de la materia: ");
        String classroomId = readStringInput("ID del aula: ");
        
        Schedule newSchedule = new Schedule(diaSemana, horaInicio, horaFin, materiaId, classroomId);
        scheduleController.addSchedule(newSchedule);
        
        System.out.println("Horario agregado exitosamente. ID: " + newSchedule.getId());
    }

    private void viewSchedule() throws ScheduleException {
        System.out.println("\n--- CONSULTAR HORARIO ---");
        String id = readStringInput("Ingrese el ID del horario: ");
        
        Schedule schedule = scheduleController.getScheduleById(id);
        System.out.println("\nInformación del horario:");
        System.out.println(schedule.getInfo());
    }

    private void updateSchedule() throws ScheduleException {
        System.out.println("\n--- ACTUALIZAR HORARIO ---");
        String id = readStringInput("Ingrese el ID del horario a actualizar: ");
        
        Schedule existing = scheduleController.getScheduleById(id);
        System.out.println("Horario actual:");
        System.out.println(existing.getInfo());
        
        System.out.println("\nIngrese los nuevos datos (deje en blanco para mantener el valor actual):");
        
        String diaSemana = readStringInput("Día de la semana [" + existing.getDiaSemana() + "]: ");
        String horaInicio = readStringInput("Hora de inicio [" + existing.getHoraInicio() + "]: ");
        String horaFin = readStringInput("Hora de fin [" + existing.getHoraFin() + "]: ");
        String materiaId = readStringInput("ID de la materia [" + existing.getMateriaId() + "]: ");
        String classroomId = readStringInput("ID del aula [" + existing.getClassroomId() + "]: ");
        
        Schedule updated = new Schedule(
            diaSemana.isEmpty() ? existing.getDiaSemana() : diaSemana,
            horaInicio.isEmpty() ? existing.getHoraInicio() : horaInicio,
            horaFin.isEmpty() ? existing.getHoraFin() : horaFin,
            materiaId.isEmpty() ? existing.getMateriaId() : materiaId,
            classroomId.isEmpty() ? existing.getClassroomId() : classroomId
        );
  
        
        scheduleController.updateSchedule(updated);
        System.out.println("Horario actualizado exitosamente.");
    }

    private void deleteSchedule() throws ScheduleException {
        System.out.println("\n--- ELIMINAR HORARIO ---");
        String id = readStringInput("Ingrese el ID del horario a eliminar: ");
        
        scheduleController.deleteSchedule(id);
        System.out.println("Horario eliminado exitosamente.");
    }

    private void listSchedulesByDay() throws ScheduleException {
        System.out.println("\n--- HORARIOS POR DÍA ---");
        String day = readStringInput("Ingrese el día a consultar: ");
        
        List<Schedule> schedules = scheduleController.getSchedulesByDay(day);
        printScheduleList(schedules, "Horarios para el día " + day);
    }

    private void listSchedulesByClassroom() throws ScheduleException {
        System.out.println("\n--- HORARIOS POR AULA ---");
        String classroomId = readStringInput("Ingrese el ID del aula: ");
        
        List<Schedule> schedules = scheduleController.getSchedulesByClassroom(classroomId);
        printScheduleList(schedules, "Horarios para el aula " + classroomId);
    }

    private void listSchedulesBySubject() throws ScheduleException {
        System.out.println("\n--- HORARIOS POR MATERIA ---");
        String subjectId = readStringInput("Ingrese el ID de la materia: ");
        
        List<Schedule> schedules = scheduleController.getSchedulesBySubject(subjectId);
        printScheduleList(schedules, "Horarios para la materia " + subjectId);
    }

    private void printScheduleList(List<Schedule> schedules, String title) {
        System.out.println("\n" + title + ":");
        if (schedules.isEmpty()) {
            System.out.println("No se encontraron horarios.");
        } else {
            schedules.forEach(s -> System.out.println(s.getInfo() + "\n---"));
        }
    }

    // Métodos auxiliares para entrada de datos
    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor ingrese un número.");
            }
        }
    }
}