package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.TipoCuenta;
import java.util.List;

public class SubjectMenuController extends BaseMenuController {
    private final SubjectController subjectController;
    private final Account account;

    public SubjectMenuController(Account account) throws SubjectException {
        this.account = account;
        IFile<Subject> fileHandler = new FileHandler<>(new Subject());
        this.subjectController = new SubjectController(fileHandler);
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
            mostrarMensajeCentrado(" GESTIÓN DE MATERIAS (ADMIN) ");
            System.out.println("1. Listar todas las materias");
            System.out.println("2. Agregar nueva materia");
            System.out.println("3. Modificar materia existente");
            System.out.println("4. Eliminar materia");
            System.out.println("5. Buscar materia por nombre");
            System.out.println("6. Listar materias por profesor");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listAllSubjects();
                    case 2 -> addSubject();
                    case 3 -> updateSubject();
                    case 4 -> deleteSubject();
                    case 5 -> searchSubjectByName();
                    case 6 -> listSubjectsByProfessor();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (SubjectException e) {
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
            mostrarMensajeCentrado(" MIS MATERIAS (PROFESOR) ");
            System.out.println("1. Ver mis materias asignadas");
            System.out.println("2. Buscar materia por nombre");
            System.out.println("3. Ver detalles de materia");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listMySubjects();
                    case 2 -> searchSubjectByName();
                    case 3 -> viewSubjectDetails();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (SubjectException e) {
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
            mostrarMensajeCentrado(" MIS MATERIAS (ESTUDIANTE) ");
            System.out.println("1. Ver mis materias inscritas");
            System.out.println("2. Buscar materia por nombre");
            System.out.println("3. Ver detalles de materia");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listMySubjects();
                    case 2 -> searchSubjectByName();
                    case 3 -> viewSubjectDetails();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (SubjectException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    // ==================== MÉTODOS COMUNES ====================
    private void listAllSubjects() throws SubjectException {
        mostrarMensajeCentrado(" LISTA DE TODAS LAS MATERIAS ");
        List<Subject> subjects = subjectController.getAllSubjects();
        if (subjects.isEmpty()) {
            System.out.println("No hay materias registradas.");
        } else {
            subjects.forEach(subject -> System.out.println(subject.getInfo()));
        }
    }

    private void searchSubjectByName() throws SubjectException {
        String name = readLine("Ingrese el nombre de la materia a buscar: ");
        Subject subject = subjectController.getSubjectByName(name);
        System.out.println("\nResultado de búsqueda:");
        System.out.println(subject.getInfo());
    }

    private void viewSubjectDetails() throws SubjectException {
        String id = readLine("Ingrese el ID de la materia: ");
        Subject subject = subjectController.getSubjectById(id);
        System.out.println("\nDetalles de la materia:");
        System.out.println(subject.getInfo());
    }

    private void listMySubjects() throws SubjectException {
        mostrarMensajeCentrado(" MIS MATERIAS ");
        List<Subject> subjects = subjectController.getSubjectsByProfessor(account.getId());
        if (subjects.isEmpty()) {
            System.out.println("No tienes materias asignadas.");
        } else {
            subjects.forEach(subject -> System.out.println(subject.getInfo()));
        }
    }

    private void listSubjectsByProfessor() throws SubjectException {
        String professorId = readLine("Ingrese el ID del profesor: ");
        List<Subject> subjects = subjectController.getSubjectsByProfessor(professorId);
        if (subjects.isEmpty()) {
            System.out.println("El profesor no tiene materias asignadas.");
        } else {
            subjects.forEach(subject -> System.out.println(subject.getInfo()));
        }
    }

    // ==================== MÉTODOS DE ADMINISTRADOR ====================
    private void addSubject() throws SubjectException {
        mostrarMensajeCentrado(" AGREGAR NUEVA MATERIA ");
        String name = readLine("Nombre: ");
        String description = readLine("Descripción: ");
        int credits = readIntOption("Créditos: ");
        String type = readLine("Tipo (Teórico/Laboratorio/Ambos): ");

        Subject newSubject = new Subject(name, description, credits, type);
        subjectController.addSubject(newSubject);
        System.out.println("✅ Materia agregada exitosamente. ID: " + newSubject.getId());
    }

    private void updateSubject() throws SubjectException {
        mostrarMensajeCentrado(" MODIFICAR MATERIA ");
        String id = readLine("ID de la materia a modificar: ");
        Subject subject = subjectController.getSubjectById(id);

        System.out.println("Datos actuales:");
        System.out.println(subject.getInfo());

        String newName = readLine("Nuevo nombre (dejar vacío para no cambiar): ");
        if (!newName.isEmpty()) subject.setNombre(newName);

        String newDesc = readLine("Nueva descripción (dejar vacío para no cambiar): ");
        if (!newDesc.isEmpty()) subject.setDescripcion(newDesc);

        String creditsInput = readLine("Nuevos créditos (dejar vacío para no cambiar): ");
        if (!creditsInput.isEmpty()) subject.setCreditos(Integer.parseInt(creditsInput));

        String newType = readLine("Nuevo tipo (dejar vacío para no cambiar): ");
        if (!newType.isEmpty()) subject.setTipo(newType);

        subjectController.updateSubject(subject);
        System.out.println("✅ Materia modificada exitosamente.");
    }

    private void deleteSubject() throws SubjectException {
        mostrarMensajeCentrado(" ELIMINAR MATERIA ");
        String id = readLine("ID de la materia a eliminar: ");
        System.out.println("¿Está seguro? (S/N): ");
        if (readLine("").equalsIgnoreCase("S")) {
            subjectController.deleteSubject(id);
            System.out.println("✅ Materia eliminada exitosamente.");
        }
    }
}