package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.model.*;
import unicorn.arubis.exceptions.SubjectException;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.interfaces.ISchedule;
import java.util.*;
import java.util.Scanner;

public class SubjectMenuController {

    private final SubjectController subjectController;
    private final Scanner scanner;
    private final ISchedule scheduleController;
    private User currentUser;

    public static void main(String[] args) {
        try {
            IFile<Subject> fileHandler = new FileHandler<>(new Subject());
            ISchedule scheduleController = null; // Implementación de ISchedule
            SubjectMenuController app = new SubjectMenuController(fileHandler, scheduleController);
            app.start();
        } catch (SubjectException e) {
            System.err.println("🚨 Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public SubjectMenuController(IFile<Subject> fileHandler, ISchedule scheduleController) throws SubjectException {
        this.subjectController = new SubjectController(fileHandler, scheduleController);
        this.scanner = new Scanner(System.in);
        this.scheduleController = scheduleController;
    }

    public void start() {
        System.out.println("\n✨ BIENVENIDO AL SISTEMA DE GESTIÓN DE MATERIAS ✨");
        authenticateUser();
        
        if (currentUser != null) {
            int option;
            do {
                showMenu();
                option = readIntInput("➡️ Seleccione una opción: ");
                handleOption(option);
            } while (option != getExitOption());
        }
    }

    private void authenticateUser() {
        System.out.println("\n=== INICIO DE SESIÓN ===");
        String username = readInput("Usuario: ", false);
        String password = readInput("Contraseña: ", false);
        
        // Simulación de base de datos de usuarios
        Map<String, User> users = new HashMap<>();
        users.put("admin", new User("admin", "admin123", UserRole.ADMIN));
        users.put("profesor1", new User("profesor1", "prof123", UserRole.PROFESSOR, "PROF001"));
        users.put("alumno1", new User("alumno1", "alum123", UserRole.STUDENT, "ALUM001"));
        
        if (users.containsKey(username) && users.get(username).getPassword().equals(password)) {
            currentUser = users.get(username);
            System.out.println("\n✅ ¡Bienvenido, " + currentUser.getUsername() + "!");
        } else {
            System.out.println("❌ Usuario o contraseña incorrectos.");
        }
    }

    private void showMenu() {
        System.out.println("\n=== MENÚ PRINCIPAL ===");
        System.out.println("1. Buscar materia por ID");
        System.out.println("2. Buscar materia por nombre");
        System.out.println("3. Listar todas las materias");
        
        if (currentUser.getRole() == UserRole.ADMIN) {
            System.out.println("4. Agregar materia");
            System.out.println("5. Actualizar materia");
            System.out.println("6. Eliminar materia");
            System.out.println("7. Listar materias por profesor");
            System.out.println("8. Salir");
        } else if (currentUser.getRole() == UserRole.PROFESSOR) {
            System.out.println("4. Listar mis materias");
            System.out.println("5. Salir");
        } else { // STUDENT
            System.out.println("4. Listar materias disponibles");
            System.out.println("5. Salir");
        }
    }

    private int getExitOption() {
        if (currentUser.getRole() == UserRole.ADMIN) {
            return 8;
        } else {
            return 5;
        }
    }

    private void handleOption(int option) {
        switch (currentUser.getRole()) {
            case ADMIN -> handleAdminOptions(option);
            case PROFESSOR -> handleProfessorOptions(option);
            case STUDENT -> handleStudentOptions(option);
        }
    }

    private void handleAdminOptions(int option) {
        switch (option) {
            case 1 -> searchSubjectById();
            case 2 -> searchSubjectByName();
            case 3 -> listAllSubjects();
            case 4 -> addSubject();
            case 5 -> updateSubject();
            case 6 -> deleteSubject();
            case 7 -> listSubjectsByProfessor();
            case 8 -> System.out.println("\n👋 ¡Hasta pronto!");
            default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
        }
    }

    private void handleProfessorOptions(int option) {
        switch (option) {
            case 1 -> searchSubjectById();
            case 2 -> searchSubjectByName();
            case 3 -> listAllSubjects();
            case 4 -> listMySubjects();
            case 5 -> System.out.println("\n👋 ¡Hasta pronto!");
            default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
        }
    }

    private void handleStudentOptions(int option) {
        switch (option) {
            case 1 -> searchSubjectById();
            case 2 -> searchSubjectByName();
            case 3 -> listAllSubjects();
            case 4 -> listAvailableSubjects();
            case 5 -> System.out.println("\n👋 ¡Hasta pronto!");
            default -> System.out.println("❌ Opción inválida. Intente nuevamente.");
        }
    }

    private void addSubject() {
        if (currentUser.getRole() != UserRole.ADMIN) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n📝 AGREGAR NUEVA MATERIA");
        try {
            String nombre = readInput("Nombre: ", false);
            String descripcion = readInput("Descripción (opcional): ", true);
            int creditos = readIntInput("Créditos: ");
            String profesorId = readInput("ID del profesor: ", false);

            Subject subject = new Subject(nombre, descripcion, creditos, profesorId);
            subjectController.addSubject(subject);
            System.out.println("✅ Materia registrada exitosamente!");
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchSubjectById() {
        System.out.println("\n🔍 BUSCAR MATERIA POR ID");
        String id = readInput("Ingrese el ID de la materia: ", false);
        try {
            Subject subject = subjectController.getSubjectById(id);
            System.out.println("\n" + subject.getInfo());
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void searchSubjectByName() {
        System.out.println("\n🔍 BUSCAR MATERIA POR NOMBRE");
        String nombre = readInput("Ingrese el nombre de la materia: ", false);
        try {
            Subject subject = subjectController.getSubjectByName(nombre);
            System.out.println("\n" + subject.getInfo());
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void updateSubject() {
        if (currentUser.getRole() != UserRole.ADMIN) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n🔄 ACTUALIZAR MATERIA");
        String id = readInput("Ingrese el ID de la materia a actualizar: ", false);
        try {
            Subject subject = subjectController.getSubjectById(id);
            System.out.println("\nDatos actuales:\n" + subject.getInfo());

            String nuevoNombre = readInput("\nNuevo nombre (Enter para mantener actual): ", true);
            if (!nuevoNombre.isEmpty()) subject.setNombre(nuevoNombre);

            String nuevaDesc = readInput("Nueva descripción (Enter para mantener actual): ", true);
            if (!nuevaDesc.isEmpty()) subject.setDescripcion(nuevaDesc);

            String creditosStr = readInput("Nuevos créditos (Enter para mantener actual): ", true);
            if (!creditosStr.isEmpty()) subject.setCreditos(Integer.parseInt(creditosStr));

            String nuevoProfesorId = readInput("Nuevo ID de profesor (Enter para mantener actual): ", true);
            if (!nuevoProfesorId.isEmpty()) subject.setProfesorId(nuevoProfesorId);

            subjectController.updateSubject(subject);
            System.out.println("✅ Materia actualizada exitosamente!");
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("❌ Los créditos deben ser un número válido.");
        }
    }

    private void deleteSubject() {
        if (currentUser.getRole() != UserRole.ADMIN) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n🗑️ ELIMINAR MATERIA");
        String id = readInput("Ingrese el ID de la materia a eliminar: ", false);
        try {
            subjectController.deleteSubject(id);
            System.out.println("✅ Materia eliminada exitosamente!");
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void listAllSubjects() {
        System.out.println("\n📜 LISTA DE TODAS LAS MATERIAS");
        try {
            List<Subject> materias = subjectController.getAllSubjects();
            if (materias.isEmpty()) {
                System.out.println("No hay materias registradas.");
            } else {
                materias.forEach(subject -> System.out.println("\n" + subject.getInfo() + "\n――――――――――"));
            }
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void listSubjectsByProfessor() {
        if (currentUser.getRole() != UserRole.ADMIN) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n👨‍🏫 MATERIAS POR PROFESOR");
        String profesorId = readInput("Ingrese el ID del profesor: ", false);
        try {
            List<Subject> materias = subjectController.getSubjectsByProfessor(profesorId);
            if (materias.isEmpty()) {
                System.out.println("No hay materias asignadas a este profesor.");
            } else {
                materias.forEach(subject -> System.out.println("\n" + subject.getInfo() + "\n――――――――――"));
            }
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void listMySubjects() {
        if (currentUser.getRole() != UserRole.PROFESSOR) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n👨‍🏫 MIS MATERIAS");
        try {
            List<Subject> materias = subjectController.getSubjectsByProfessor(currentUser.getUserId());
            if (materias.isEmpty()) {
                System.out.println("No tienes materias asignadas.");
            } else {
                materias.forEach(subject -> System.out.println("\n" + subject.getInfo() + "\n――――――――――"));
            }
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private void listAvailableSubjects() {
        if (currentUser.getRole() != UserRole.STUDENT) {
            System.out.println("❌ No tiene permisos para realizar esta acción.");
            return;
        }
        
        System.out.println("\n📚 MATERIAS DISPONIBLES");
        try {
            List<Subject> materias = subjectController.getAllSubjects();
            if (materias.isEmpty()) {
                System.out.println("No hay materias disponibles.");
            } else {
                materias.forEach(subject -> System.out.println("\n" + subject.getInfo() + "\n――――――――――"));
            }
        } catch (SubjectException e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    private String readInput(String prompt, boolean optional) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        if (!optional && input.isEmpty()) {
            System.out.println("⚠️ Este campo es obligatorio.");
            return readInput(prompt, optional);
        }
        return input;
    }

    private int readIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número válido.");
            }
        }
    }
}

enum UserRole {
    ADMIN, PROFESSOR, STUDENT
}

class User {
    private String username;
    private String password;
    private UserRole role;
    private String userId;

    public User(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(String username, String password, UserRole role, String userId) {
        this(username, password, role);
        this.userId = userId;
    }

    // Getters
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public UserRole getRole() { return role; }
    public String getUserId() { return userId; }
}