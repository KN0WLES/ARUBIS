package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.TipoCuenta;
import unicorn.util.TipoRoom;
import java.util.List;

public class RoomMenuController extends BaseMenuController {
    private final RoomController roomController;
    private final Account account;

    public RoomMenuController(Account account) throws RoomException {
        this.account = account;
        IFile<Room> fileHandler = new FileHandler<>(new Room());
        this.roomController = new RoomController(fileHandler);
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
            mostrarMensajeCentrado(" GESTIÓN DE SALAS (ADMIN) ");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Agregar nueva sala");
            System.out.println("3. Modificar sala existente");
            System.out.println("4. Eliminar sala");
            System.out.println("5. Poner en mantenimiento");
            System.out.println("6. Quitar de mantenimiento");
            System.out.println("7. Listar salas disponibles");
            System.out.println("8. Listar salas por tipo");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listAllRooms();
                    case 2 -> addRoom();
                    case 3 -> updateRoom();
                    case 4 -> deleteRoom();
                    case 5 -> setMaintenance(true);
                    case 6 -> setMaintenance(false);
                    case 7 -> listAvailableRooms();
                    case 8 -> listRoomsByType();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (RoomException e) {
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
            mostrarMensajeCentrado(" GESTIÓN DE SALAS (PROFESOR) ");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Listar salas disponibles");
            System.out.println("3. Listar salas por tipo");
            System.out.println("4. Ocupar sala");
            System.out.println("5. Liberar sala");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listAllRooms();
                    case 2 -> listAvailableRooms();
                    case 3 -> listRoomsByType();
                    case 4 -> occupyRoom(true);
                    case 5 -> occupyRoom(false);
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (RoomException e) {
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
            mostrarMensajeCentrado(" CONSULTA DE SALAS (ESTUDIANTE) ");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Listar salas disponibles");
            System.out.println("3. Listar salas por tipo");
            System.out.println("0. Volver al menú principal");

            option = readIntOption("Seleccione una opción: ");

            try {
                switch (option) {
                    case 1 -> listAllRooms();
                    case 2 -> listAvailableRooms();
                    case 3 -> listRoomsByType();
                    case 0 -> {}
                    default -> System.out.println("❌ Opción inválida.");
                }
            } catch (RoomException e) {
                System.out.println("❌ Error: " + e.getMessage());
            }
            if (option != 0) readLine("\nPresione Enter para continuar...");
        } while (option != 0);
    }

    // ==================== MÉTODOS COMUNES ====================
    private void listAllRooms() throws RoomException {
        mostrarMensajeCentrado(" LISTA DE TODAS LAS SALAS ");
        List<Room> rooms = roomController.getRoomsByType("FISICA");
        rooms.addAll(roomController.getRoomsByType("VIRTUAL"));
        if (rooms.isEmpty()) {
            System.out.println("No hay salas registradas.");
        } else {
            rooms.forEach(room -> System.out.println(room.getInfo()));
        }
    }

    private void listAvailableRooms() throws RoomException {
        mostrarMensajeCentrado(" SALAS DISPONIBLES ");
        List<Room> rooms = roomController.getAvailableRooms();
        if (rooms.isEmpty()) {
            System.out.println("No hay salas disponibles.");
        } else {
            rooms.forEach(room -> System.out.println(room.getInfo()));
        }
    }

    private void listRoomsByType() throws RoomException {
        mostrarMensajeCentrado(" FILTRAR POR TIPO DE SALA ");
        System.out.println("1. Aula física");
        System.out.println("2. Aula virtual");
        System.out.println("3. Laboratorio");
        System.out.println("4. Auditorio");
        int typeOption = readIntOption("Seleccione el tipo: ");

        TipoRoom tipo;
        switch (typeOption) {
            case 1 -> tipo = TipoRoom.AULA;
            case 2 -> tipo = TipoRoom.VIRTUAL;
            case 3 -> tipo = TipoRoom.LABORATORIO;
            case 4 -> tipo = TipoRoom.AUDITORIO;
            default -> {
                System.out.println("❌ Tipo inválido.");
                return;
            }
        }

        List<Room> rooms = roomController.getRoomsByType(String.valueOf(tipo));
        if (rooms.isEmpty()) {
            System.out.println("No hay salas de este tipo.");
        } else {
            rooms.forEach(room -> System.out.println(room.getInfo()));
        }
    }

    // ==================== MÉTODOS DE ADMINISTRADOR ====================
    private void addRoom() throws RoomException {
        mostrarMensajeCentrado(" AGREGAR NUEVA SALA ");
        String name = readLine("Nombre de la sala: ");
        int capacity = readIntOption("Capacidad: ");
        boolean hasProjector = readLine("¿Tiene proyector? (S/N): ").equalsIgnoreCase("S");

        System.out.println("Tipo de sala:");
        System.out.println("1. Aula física");
        System.out.println("2. Aula virtual");
        System.out.println("3. Laboratorio");
        System.out.println("4. Auditorio");
        int typeOption = readIntOption("Seleccione el tipo: ");

        TipoRoom tipo;
        switch (typeOption) {
            case 1 -> tipo = TipoRoom.AULA;
            case 2 -> tipo = TipoRoom.VIRTUAL;
            case 3 -> tipo = TipoRoom.LABORATORIO;
            case 4 -> tipo = TipoRoom.AUDITORIO;
            default -> {
                System.out.println("❌ Tipo inválido.");
                return;
            }
        }

        Room newRoom = new Room(name, tipo, capacity, hasProjector);
        roomController.addRoom(newRoom);
        System.out.println("✅ Sala agregada exitosamente. ID: " + newRoom.getId());
    }

    private void updateRoom() throws RoomException {
        mostrarMensajeCentrado(" MODIFICAR SALA ");
        String id = readLine("ID de la sala a modificar: ");
        Room room = roomController.getRoomById(id);

        System.out.println("Datos actuales:");
        System.out.println(room.getInfo());

        String newName = readLine("Nuevo nombre (dejar vacío para no cambiar): ");
        if (!newName.isEmpty()) room.setNombre(newName);

        String capacityInput = readLine("Nueva capacidad (dejar vacío para no cambiar): ");
        if (!capacityInput.isEmpty()) room.setCapacidad(Integer.parseInt(capacityInput));

        if (room.getTipo() != TipoRoom.VIRTUAL) {
            String projectorInput = readLine("¿Tiene proyector? (S/N, dejar vacío para no cambiar): ");
            if (!projectorInput.isEmpty()) room.setTieneProyector(projectorInput.equalsIgnoreCase("S"));
        }

        roomController.updateRoom(room);
        System.out.println("✅ Sala modificada exitosamente.");
    }

    private void deleteRoom() throws RoomException {
        mostrarMensajeCentrado(" ELIMINAR SALA ");
        String id = readLine("ID de la sala a eliminar: ");
        System.out.println("¿Está seguro? (S/N): ");
        if (readLine("").equalsIgnoreCase("S")) {
            roomController.deleteRoom(id);
            System.out.println("✅ Sala eliminada exitosamente.");
        }
    }

    private void setMaintenance(boolean inMaintenance) throws RoomException {
        String action = inMaintenance ? "PONER EN MANTENIMIENTO" : "QUITAR DE MANTENIMIENTO";
        mostrarMensajeCentrado(" " + action + " ");
        String id = readLine("ID de la sala: ");
        roomController.setRoomMaintenance(id, inMaintenance);
        System.out.println("✅ Operación realizada exitosamente.");
    }

    // ==================== MÉTODOS DE PROFESOR ====================
    private void occupyRoom(boolean occupy) throws RoomException {
        String action = occupy ? "OCUPAR" : "LIBERAR";
        mostrarMensajeCentrado(" " + action + " SALA ");
        String id = readLine("ID de la sala: ");
        roomController.setRoomOccupied(id, occupy);
        System.out.println("✅ Sala " + (occupy ? "ocupada" : "liberada") + " exitosamente.");
    }
}