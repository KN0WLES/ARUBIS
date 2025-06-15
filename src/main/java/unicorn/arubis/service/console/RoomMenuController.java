package unicorn.arubis.service.console;

import unicorn.arubis.controller.FileHandler;
import unicorn.arubis.controller.RoomController;
import unicorn.arubis.exceptions.FileException;
import unicorn.arubis.exceptions.RoomException;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.model.Room;
import unicorn.arubis.util.TipoCuenta;
import unicorn.arubis.util.TipoRoom;

import java.util.List;
import java.util.Scanner;

public class RoomMenuController {
    private static RoomController roomController;
    private static TipoCuenta tipoUsuario;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Inicializar el controlador de salas
            IFile<Room> fileHandler = new FileHandler<>(new Room()) {
                @Override
                public void createFileIfNotExists(String filePath) throws FileException {
                    // Implementación simplificada para el ejemplo
                }

                @Override
                public List<Room> loadData(String filePath) throws FileException {
                    // Implementación simplificada para el ejemplo
                    return null;
                }

                @Override
                public void saveData(List<Room> data, String filePath) throws FileException {
                    // Implementación simplificada para el ejemplo
                }
            };
            
            roomController = new RoomController(fileHandler);
            
            // Menú de inicio de sesión
            loginMenu();
            
            // Menú principal según tipo de usuario
            switch (tipoUsuario) {
                case ADMIN:
                    menuAdministrador();
                    break;
                case PROFESOR:
                    menuDocente();
                    break;
                case ESTUDIANTE:
                    menuEstudiante();
                    break;
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    private static void loginMenu() {
        System.out.println("=== SISTEMA DE GESTIÓN DE SALAS ===");
        System.out.println("1. Ingresar como Administrador");
        System.out.println("2. Ingresar como Docente");
        System.out.println("3. Ingresar como Estudiante");
        System.out.print("Seleccione su tipo de usuario: ");
        
        int opcion = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        
        switch (opcion) {
            case 1:
                tipoUsuario = TipoCuenta.ADMIN;
                break;
            case 2:
                tipoUsuario = TipoCuenta.PROFESOR;
                break;
            case 3:
                tipoUsuario = TipoCuenta.ESTUDIANTE;
                break;
            default:
                System.out.println("Opción inválida. Saliendo...");
                System.exit(0);
        }
    }

    private static void menuAdministrador() throws RoomException {
        while (true) {
            System.out.println("\n=== MENÚ ADMINISTRADOR ===");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Agregar nueva sala");
            System.out.println("3. Modificar sala existente");
            System.out.println("4. Eliminar sala");
            System.out.println("5. Poner sala en mantenimiento");
            System.out.println("6. Quitar sala de mantenimiento");
            System.out.println("7. Listar salas disponibles");
            System.out.println("8. Listar salas por tipo");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    listarTodasLasSalas();
                    break;
                case 2:
                    agregarSala();
                    break;
                case 3:
                    modificarSala();
                    break;
                case 4:
                    eliminarSala();
                    break;
                case 5:
                    ponerEnMantenimiento();
                    break;
                case 6:
                    quitarDeMantenimiento();
                    break;
                case 7:
                    listarSalasDisponibles();
                    break;
                case 8:
                    listarSalasPorTipo();
                    break;
                case 9:
                    System.out.println("Saliendo del sistema...");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static void menuDocente() throws RoomException {
        while (true) {
            System.out.println("\n=== MENÚ DOCENTE ===");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Listar salas disponibles");
            System.out.println("3. Listar salas por tipo");
            System.out.println("4. Ocupar sala");
            System.out.println("5. Liberar sala");
            System.out.println("6. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    listarTodasLasSalas();
                    break;
                case 2:
                    listarSalasDisponibles();
                    break;
                case 3:
                    listarSalasPorTipo();
                    break;
                case 4:
                    ocuparSala();
                    break;
                case 5:
                    liberarSala();
                    break;
                case 6:
                    System.out.println("Saliendo del sistema...");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static void menuEstudiante() throws RoomException {
        while (true) {
            System.out.println("\n=== MENÚ ESTUDIANTE ===");
            System.out.println("1. Listar todas las salas");
            System.out.println("2. Listar salas disponibles");
            System.out.println("3. Listar salas por tipo");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
            
            switch (opcion) {
                case 1:
                    listarTodasLasSalas();
                    break;
                case 2:
                    listarSalasDisponibles();
                    break;
                case 3:
                    listarSalasPorTipo();
                    break;
                case 4:
                    System.out.println("Saliendo del sistema...");
                    System.exit(0);
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    // Métodos comunes para las operaciones
    private static void listarTodasLasSalas() throws RoomException {
        System.out.println("\n=== LISTADO DE TODAS LAS SALAS ===");
        List<Room> salas = roomController.getRoomsByType("FISICA");
        salas.addAll(roomController.getRoomsByType("VIRTUAL"));
        
        if (salas.isEmpty()) {
            System.out.println("No hay salas registradas.");
        } else {
            for (Room sala : salas) {
                System.out.println(sala.getInfo());
                System.out.println("-----------------------------");
            }
        }
    }

    private static void listarSalasDisponibles() throws RoomException {
        System.out.println("\n=== LISTADO DE SALAS DISPONIBLES ===");
        List<Room> salas = roomController.getAvailableRooms();
        
        if (salas.isEmpty()) {
            System.out.println("No hay salas disponibles en este momento.");
        } else {
            for (Room sala : salas) {
                System.out.println(sala.getInfo());
                System.out.println("-----------------------------");
            }
        }
    }

    private static void listarSalasPorTipo() throws RoomException {
        System.out.println("\n=== LISTADO POR TIPO DE SALA ===");
        System.out.println("1. Aulas físicas");
        System.out.println("2. Aulas virtuales");
        System.out.println("3. Laboratorios");
        System.out.println("4. Auditorios");
        System.out.print("Seleccione el tipo de sala: ");
        
        int tipo = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        
        List<Room> salas;
        switch (tipo) {
            case 1:
                salas = roomController.getRoomsByType("FISICA", "AULA");
                break;
            case 2:
                salas = roomController.getRoomsByType("VIRTUAL");
                break;
            case 3:
                salas = roomController.getRoomsByType("FISICA", "LABORATORIO");
                break;
            case 4:
                salas = roomController.getRoomsByType("FISICA", "AUDITORIO");
                break;
            default:
                System.out.println("Opción inválida.");
                return;
        }
        
        if (salas.isEmpty()) {
            System.out.println("No hay salas de este tipo registradas.");
        } else {
            for (Room sala : salas) {
                System.out.println(sala.getInfo());
                System.out.println("-----------------------------");
            }
        }
    }

    // Métodos específicos para administrador
    private static void agregarSala() throws RoomException {
        System.out.println("\n=== AGREGAR NUEVA SALA ===");
        System.out.print("Nombre de la sala: ");
        String nombre = scanner.nextLine();
        
        System.out.println("Tipo de sala:");
        System.out.println("1. Aula física");
        System.out.println("2. Aula virtual");
        System.out.println("3. Laboratorio");
        System.out.println("4. Auditorio");
        System.out.print("Seleccione el tipo: ");
        int tipo = scanner.nextInt();
        
        System.out.print("Capacidad: ");
        int capacidad = scanner.nextInt();
        
        System.out.print("¿Tiene proyector? (1. Sí / 2. No): ");
        int proyector = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
        
        TipoRoom tipoRoom;
        switch (tipo) {
            case 1:
                tipoRoom = TipoRoom.AULA;
                break;
            case 2:
                tipoRoom = TipoRoom.VIRTUAL;
                break;
            case 3:
                tipoRoom = TipoRoom.LABORATORIO;
                break;
            case 4:
                tipoRoom = TipoRoom.AUDITORIO;
                break;
            default:
                System.out.println("Tipo inválido.");
                return;
        }
        
        boolean tieneProyector = proyector == 1;
        Room nuevaSala = new Room(nombre, tipoRoom, capacidad, tieneProyector);
        roomController.addRoom(nuevaSala);
        
        System.out.println("Sala agregada exitosamente.");
    }

    private static void modificarSala() throws RoomException {
        System.out.println("\n=== MODIFICAR SALA ===");
        System.out.print("Ingrese el ID de la sala a modificar: ");
        String id = scanner.nextLine();
        
        Room sala = roomController.getRoomById(id);
        System.out.println("Datos actuales de la sala:");
        System.out.println(sala.getInfo());
        
        System.out.print("Nuevo nombre (dejar vacío para no cambiar): ");
        String nuevoNombre = scanner.nextLine();
        if (!nuevoNombre.isEmpty()) {
            sala.setNombre(nuevoNombre);
        }
        
        System.out.print("Nueva capacidad (0 para no cambiar): ");
        int nuevaCapacidad = scanner.nextInt();
        if (nuevaCapacidad > 0) {
            sala.setCapacidad(nuevaCapacidad);
        }
        
        if (sala.getTipo() != TipoRoom.VIRTUAL) {
            System.out.print("¿Tiene proyector? (1. Sí / 2. No / 3. No cambiar): ");
            int proyector = scanner.nextInt();
            if (proyector == 1 || proyector == 2) {
                sala.setTieneProyector(proyector == 1);
            }
        }
        
        roomController.updateRoom(sala);
        System.out.println("Sala modificada exitosamente.");
    }

    private static void eliminarSala() throws RoomException {
        System.out.println("\n=== ELIMINAR SALA ===");
        System.out.print("Ingrese el ID de la sala a eliminar: ");
        String id = scanner.nextLine();
        
        roomController.deleteRoom(id);
        System.out.println("Sala eliminada exitosamente.");
    }

    private static void ponerEnMantenimiento() throws RoomException {
        System.out.println("\n=== PONER SALA EN MANTENIMIENTO ===");
        System.out.print("Ingrese el ID de la sala: ");
        String id = scanner.nextLine();
        
        roomController.setRoomMaintenance(id, true);
        System.out.println("Sala puesta en mantenimiento.");
    }

    private static void quitarDeMantenimiento() throws RoomException {
        System.out.println("\n=== QUITAR SALA DE MANTENIMIENTO ===");
        System.out.print("Ingrese el ID de la sala: ");
        String id = scanner.nextLine();
        
        roomController.setRoomMaintenance(id, false);
        System.out.println("Sala quitada de mantenimiento.");
    }

    // Métodos específicos para docente
    private static void ocuparSala() throws RoomException {
        System.out.println("\n=== OCUPAR SALA ===");
        System.out.print("Ingrese el ID de la sala a ocupar: ");
        String id = scanner.nextLine();
        
        roomController.setRoomOccupied(id, true);
        System.out.println("Sala ocupada exitosamente.");
    }

    private static void liberarSala() throws RoomException {
        System.out.println("\n=== LIBERAR SALA ===");
        System.out.print("Ingrese el ID de la sala a liberar: ");
        String id = scanner.nextLine();
        
        roomController.setRoomOccupied(id, false);
        System.out.println("Sala liberada exitosamente.");
    }
}