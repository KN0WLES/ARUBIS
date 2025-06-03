package unicorn.controller;

import unicorn.model.*;
import unicorn.exceptions.*;
import unicorn.interfaces.IFile;
import unicorn.interfaces.IRoom;
import unicorn.util.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que actúa como controlador para la gestión de habitaciones.
 * Proporciona la lógica de negocio necesaria para agregar, actualizar, eliminar y consultar habitaciones.
 * También permite gestionar la disponibilidad, el mantenimiento y la ocupación de las habitaciones.
 * Los datos de las habitaciones se almacenan y recuperan desde un archivo utilizando un manejador de archivos genérico.
 * 
 * @desciption Funcionalidades principales:
 *                   - Agregar nuevas habitaciones.
 *                   - Actualizar información de habitaciones existentes.
 *                   - Eliminar habitaciones.
 *                   - Consultar habitaciones por ID o tipo.
 *                   - Gestionar la disponibilidad de habitaciones.
 *                   - Establecer habitaciones en mantenimiento u ocupadas.
 *                   - Listar habitaciones disponibles o por tipo.
 *                   - Crear habitaciones predeterminadas si no existen.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 * @see IRoom
 * @see Room
 * @see IFile
 * @see FileException
 * @see RoomException
 */
public class RoomController implements IRoom {
    
    private final IFile<Room> fileHandler;
    private final String filePath = "src/main/java/unicorn/arubis/dto/rooms.txt";
    private Map<String, Room> rooms;

    public RoomController(IFile<Room> fileHandler)throws RoomException {
        this.fileHandler = fileHandler;
        this.rooms = new HashMap<>();
        
        try {
            this.fileHandler.createFileIfNotExists(filePath);
            List<Room> loadedRooms = this.fileHandler.loadData(filePath);

            if (loadedRooms == null || loadedRooms.isEmpty()) {
                createDefaultRooms();
            } else {
                loadedRooms.forEach(room -> rooms.put(room.getId(), room));
            }
            saveChanges();
        } catch (FileException e) {
            System.err.println("Error al cargar aulas: " + e.getMessage());
        }
    }

    private void saveChanges() throws RoomException {
        try {
            fileHandler.saveData(new ArrayList<>(rooms.values()), filePath);
        } catch (FileException e) {
            throw new RoomException("Error al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public void addRoom(Room room) throws RoomException {
        if (rooms.containsKey(room.getId())) {
            throw new RoomException("Ya existe un aula con este ID");
        }
        
        // Validar nombre único si es necesario
        boolean nameExists = rooms.values().stream()
            .anyMatch(r -> r.getNombre().equalsIgnoreCase(room.getNombre()));
        
        if (nameExists) {
            throw new RoomException("Ya existe un aula con este nombre");
        }
        
        rooms.put(room.getId(), room);
        saveChanges();
    }

    @Override
    public Room getRoomById(String id) throws RoomException {
        Room room = rooms.get(id);
        if (room == null) {
            throw new RoomException("Aula no encontrada");
        }
        return room;
    }

    @Override
    public void updateRoom(Room room) throws RoomException {
        if (!rooms.containsKey(room.getId())) {
            throw new RoomException("Aula no encontrada");
        }
        
        // Validar nombre único si es necesario
        boolean nameExists = rooms.values().stream()
            .filter(r -> !r.getId().equals(room.getId()))
            .anyMatch(r -> r.getNombre().equalsIgnoreCase(room.getNombre()));
        
        if (nameExists) {
            throw new RoomException("Ya existe un aula con este nombre");
        }
        
        rooms.put(room.getId(), room);
        saveChanges();
    }

    @Override
    public void deleteRoom(String id) throws RoomException {
        Room room = getRoomById(id);
        
        if (room.getDisponible() != 'L') {
            throw new RoomException("El aula está ocupada o en mantenimiento");
        }
        
        rooms.remove(id);
        saveChanges();
    }

    @Override
    public List<Room> getRoomsByType(String type) throws RoomException {
        if (!type.equalsIgnoreCase("FISICA") && !type.equalsIgnoreCase("VIRTUAL")) {
            throw new RoomException("Tipo de aula inválido. Use 'FISICA' o 'VIRTUAL'");
        }
        
        if (type.equalsIgnoreCase("VIRTUAL")) {
            return rooms.values().stream()
                    .filter(r -> r.getTipo() == TipoRoom.VIRTUAL)
                    .collect(Collectors.toList());
        } else {
            return rooms.values().stream()
                    .filter(r -> r.getTipo() != TipoRoom.VIRTUAL)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<Room> getRoomsByType(String type, String subType) throws RoomException{
        if (type.equalsIgnoreCase("VIRTUAL")) {
            return getRoomsByType("VIRTUAL");
        }
        
        if (!type.equalsIgnoreCase("FISICA")) {
            throw new RoomException("Tipo de aula inválido. Use 'FISICA' o 'VIRTUAL'");
        }

        try {
            TipoRoom tipoRoom = TipoRoom.valueOf(subType.toUpperCase());
            if (tipoRoom == TipoRoom.VIRTUAL) {
                throw new RoomException("Subtipo inválido para aulas físicas");
            }
            
            return rooms.values().stream()
                    .filter(r -> r.getTipo() == tipoRoom)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new RoomException("Subtipo inválido. Use 'AULA', 'LABORATORIO' o 'AUDITORIO'");
        }
    }
    @Override
    public List<Room> getAvailableRooms() throws RoomException {
        return rooms.values().stream()
            .filter(r -> r.getDisponible() == 'L')
            .collect(Collectors.toList());
    }

    @Override
    public void setRoomMaintenance(String roomId, boolean inMaintenance) throws RoomException {
        Room room = getRoomById(roomId);
        room.setDisponible(inMaintenance ? 'M' : 'L');
        saveChanges();
    }

    @Override
    public void setRoomOccupied(String roomId, boolean occupied) throws RoomException {
        Room room = getRoomById(roomId);
        room.setDisponible(occupied ? 'O' : 'L');
        saveChanges();
    }

    private void createDefaultRooms() throws RoomException {
        for (int edificio = 1; edificio <= 3; edificio++) {
            for (int piso = 1; piso <= 4; piso++) {
                for (int aula = 1; aula <= 10; aula++) {
                    String nombre = String.format("E%d-P%d-Aula%d", edificio, piso, aula);
                    boolean tieneProyector = piso >= 3; // Aulas de pisos altos tienen proyector
                    Room room = new Room(nombre, TipoRoom.AULA, 30, tieneProyector);
                    rooms.put(room.getId(), room);
                }
            }
        }
        
        // Aulas virtuales
        for (int i = 1; i <= 20; i++) {
            Room room = new Room("Virtual-Sala" + i, TipoRoom.VIRTUAL, 100, true);
            rooms.put(room.getId(), room);
        }
        
        // Laboratorios (5 por edificio)
        for (int i = 1; i <= 5; i++) {
            Room lab = Room.crearLaboratorio(1, 1, 2, i, 20);
            rooms.put(lab.getId(), lab);
        }
        
        // Auditorios (1 por edificio)
        for (int i = 1; i <= 3; i++) {
            Room auditorio = Room.crearAuditorio(i, 200);
            rooms.put(auditorio.getId(), auditorio);
        }
        }
    
}