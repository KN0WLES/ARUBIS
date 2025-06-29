package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.*;
import java.util.*;

public class NewsMenuController extends BaseMenuController {
    private final NewsController newsController;
    private final Account account;

    public NewsMenuController(Account account) throws NewsException {
        if (account == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }
        this.account = account;
        IFile<News> newsFileHandler = new FileHandler<>(new News());
        this.newsController = new NewsController(newsFileHandler, account.getId());
    }

    @Override
    public void showMenu() {
        try {
            switch (account.getTipoCuenta()) {
                case ADMIN -> showAdminMenu();
                case PROFESOR -> showTeacherMenu();
                case ESTUDIANTE -> showStudentMenu();
                default -> System.out.println("Rol no reconocido");
            }
        } catch (Exception e) {
            showError("Error inesperado: " + e.getMessage());
        }
    }

    // ==================== ADMIN MENU ====================
    private void showAdminMenu() {
        while (true) {
            mostrarMensajeCentrado("ADMINISTRACIÓN DE COMUNICADOS");
            System.out.println("1. Enviar comunicado global");
            System.out.println("2. Ver todos los comunicados");
            System.out.println("3. Buscar por tipo");
            System.out.println("4. Estadísticas");
            System.out.println("5. Eliminar comunicado");
            System.out.println("0. Volver");

            switch (readIntOption("Selección: ")) {
                case 1 -> handleSendGlobalNews();
                case 2 -> displayNews(newsController.getAllNewsByUser(account.getId()), "No hay comunicados en el sistema");
                case 3 -> handleSearchByType();
                case 4 -> displayStatistics();
                case 5 -> handleDeleteNews();
                case 0 -> { return; }
                default -> showInvalidOption();
            }
        }
    }

    // ==================== TEACHER MENU ====================
    private void showTeacherMenu() {
        while (true) {
            mostrarMensajeCentrado("MENÚ DE COMUNICADOS (PROFESOR)");
            System.out.println("1. Enviar a materia");
            System.out.println("2. Enviar a estudiante");
            System.out.println("3. Mis comunicados enviados");
            System.out.println("4. Comunicados recibidos");
            System.out.println("0. Volver");

            switch (readIntOption("Selección: ")) {
                case 1 -> handleSendToSubject();
                case 2 -> handleSendToStudent();
                case 3 -> displayNews(newsController.getNewsBySender(account.getId()), "No has enviado comunicados");
                case 4 -> displayNews(newsController.getAllNewsByUser(account.getId()), "No tienes comunicados");
                case 0 -> { return; }
                default -> showInvalidOption();
            }
        }
    }

    // ==================== STUDENT MENU ====================
    private void showStudentMenu() {
        while (true) {
            mostrarMensajeCentrado("MIS NOTIFICACIONES");
            System.out.printf("1. Ver no leídas (%d)%n", newsController.getUnreadCount(account.getId()));
            System.out.println("2. Ver todas");
            System.out.println("3. Comunicados globales");
            System.out.println("4. Marcar como leída");
            System.out.println("0. Volver");

            switch (readIntOption("Selección: ")) {
                case 1 -> displayNews(newsController.getUnreadNewsByUser(account.getId()), "No hay notificaciones no leídas");
                case 2 -> displayNews(newsController.getAllNewsByUser(account.getId()), "No hay notificaciones");
                case 3 -> displayNews(newsController.getGlobalNews(), "No hay comunicados globales");
                case 4 -> handleMarkAsRead();
                case 0 -> { return; }
                default -> showInvalidOption();
            }
        }
    }

    // ==================== HANDLER METHODS ====================
    private void handleSendGlobalNews() {
        try {
            String message = readRequiredInput("Mensaje: ", "El mensaje no puede estar vacío");
            TipoNews type = readType();

            newsController.sendGlobalNews(message, type);
            showSuccess("Comunicado global enviado exitosamente a todos los usuarios");
        } catch (NewsException e) {
            showError("Error al enviar comunicado: " + e.getMessage());
        }
    }

    private void handleSendToSubject() {
        try {
            String subjectId = readRequiredInput("ID Materia: ", "El ID de materia es requerido");
            String message = readRequiredInput("Mensaje: ", "El mensaje no puede estar vacío");

            newsController.sendNewsForMateria(message, subjectId);
            showSuccess("Comunicado enviado exitosamente a la materia " + subjectId);
        } catch (NewsException e) {
            showError("Error al enviar a materia: " + e.getMessage());
        }
    }

    private void handleSendToStudent() {
        try {
            String recipientId = readRequiredInput("ID Estudiante: ", "El ID de estudiante es requerido");
            String message = readRequiredInput("Mensaje: ", "El mensaje no puede estar vacío");
            TipoNews type = readType();

            newsController.sendUserNews(message, type, recipientId);
            showSuccess("Comunicado enviado exitosamente al estudiante " + recipientId);
        } catch (NewsException e) {
            showError("Error al enviar a estudiante: " + e.getMessage());
        }
    }

    private void handleSearchByType() {
        try {
            TipoNews type = readType();
            List<News> newsList = newsController.getNewsByType(type);
            if (newsList.isEmpty()) {
                System.out.println("No se encontraron comunicados del tipo: " + type);
            } else {
                displayNews(newsList, "No hay comunicados de este tipo");
            }
        } catch (Exception e) {
            // Captura cualquier otra excepción no prevista
            showError("Se produjo un error inesperado: " + e.getMessage());
        }
    }

    private void handleMarkAsRead() {
        try {
            String newsId = readRequiredInput("ID de notificación: ", "ID requerido");
            newsController.markAsRead(newsId);
            showSuccess("Notificación marcada como leída exitosamente");
        } catch (NewsException e) {
            showError("Error al marcar como leída: " + e.getMessage());
        }
    }

    private void handleDeleteNews() {
        try {
            String newsId = readRequiredInput("ID de comunicado a eliminar: ", "ID requerido");
            System.out.println("¿Está seguro que desea eliminar este comunicado? (S/N)");
            String confirmation = readRequiredInput("Confirmación: ", "Debe ingresar S o N").toUpperCase();

            if (confirmation.equals("S")) {
                newsController.deleteNews(newsId);
                showSuccess("Comunicado eliminado exitosamente");
            } else {
                showSuccess("Operación cancelada");
            }
        } catch (NewsException e) {
            showError("Error al eliminar comunicado: " + e.getMessage());
        }
    }

    // ==================== UTILITY METHODS ====================
    private void displayNews(List<News> newsList, String emptyMessage) {
        if (newsList == null || newsList.isEmpty()) {
            System.out.println(emptyMessage);
            return;
        }

        mostrarMensajeCentrado("=== LISTA DE COMUNICADOS ===");
        String border = "+" + "-".repeat(78) + "+";

        for (News n : newsList) {
            // Mostrar cada comunicado con formato de caja
            System.out.println(border);
            System.out.printf("| %-76s |%n", "ID: " + n.getId());
            System.out.println(border);

            System.out.printf("| %-76s |%n", "TIPO: " + n.getTipoNotificacion().getDescripcion());
            System.out.println(border);

            System.out.printf("| %-76s |%n", "FECHA: " + n.getFecha());
            System.out.println(border);

            System.out.printf("| %-76s |%n", "MENSAJE:");
            String[] messageLines = wordWrap(n.getMensaje(), 76);
            for (String line : messageLines) {
                System.out.printf("| %-76s |%n", line);
            }
            System.out.println(border);

            System.out.printf("| %-76s |%n", "ESTADO: " + (n.isLeida() ? "LEÍDO" : "NO LEÍDO"));
            System.out.println(border);
            System.out.println(); // Espacio entre comunicados
        }
        System.out.println("Total: " + newsList.size() + " comunicados\n");
    }

    private String[] wordWrap(String text, int width) {
        if (text == null) return new String[]{"N/A"};

        String[] words = text.split("\\s+");
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + word.length() + 1 <= width) {
                if (currentLine.length() > 0) currentLine.append(" ");
                currentLine.append(word);
            } else {
                lines.add(currentLine.toString());
                currentLine = new StringBuilder(word);
            }
        }
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines.toArray(new String[0]);
    }

    private void displayStatistics() {
        try {
            Map<TipoNews, Long> stats = newsController.getNewsStatistics();
           mostrarMensajeCentrado("ESTADÍSTICAS DE COMUNICADOS");

            if (stats.isEmpty()) {
                System.out.println("No hay datos estadísticos disponibles");
                return;
            }

            stats.forEach((type, count) ->
                    System.out.printf("- %s: %d comunicados%n", type.getDescripcion(), count));

            long total = stats.values().stream().mapToLong(Long::longValue).sum();
            System.out.printf("\nTotal general: %d comunicados%n", total);
        } catch (Exception e) {
            showError("Error al obtener estadísticas: " + e.getMessage());
        }
    }

    private TipoNews readType() {
        System.out.println("\nTipos disponibles:");
        Arrays.stream(TipoNews.values())
                .forEach(t -> System.out.printf("- %s: %s%n", t.name(), t.getDescripcion()));

        while (true) {
            try {
                String input = readRequiredInput("\nSeleccione tipo: ", "Tipo requerido");
                return TipoNews.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException e) {
                showError("Tipo inválido. Intente nuevamente");
            }
        }
    }

    private String readRequiredInput(String prompt, String errorMessage) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) return input;
            showError(errorMessage);
        }
    }

    private void printMenuHeader(String title) {
        System.out.println("\n" + "=".repeat(title.length() + 6));
        System.out.println("== " + title + " ==");
        System.out.println("=".repeat(title.length() + 6));
    }

    private void showSuccess(String message) {
        System.out.println("\n✅ " + message + "\n");
    }

    private void showError(String message) {
        System.out.println("\n❌ Error: " + message + "\n");
    }

    private void showInvalidOption() {
        showError("Opción inválida. Por favor seleccione una opción válida.");
    }
}