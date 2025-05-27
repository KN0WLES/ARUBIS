package controller;

import interfaces.*;
import model.Room;
import exceptions.*;

import java.util.ArrayList;
import java.util.List;
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
 * @see Classroom
 * @see IFile
 * @see FileException
 * @see RoomException
 */
public class RoomController implements IRoom {
    
    private final IFile<Classroom> fileHandler;
    private final String filePath = "src/main/java/data/rooms.txt";
    private List<Classroom> rooms;

    public RoomController(IFile<Classroom> fileHandler)throws RoomException {
        this.fileHandler = fileHandler;
        try {
            this.fileHandler.createFileIfNotExists(filePath);
            this.rooms = this.fileHandler.loadData(filePath);

            if (this.rooms == null || this.rooms.isEmpty()) {
                this.rooms = new ArrayList<>();
                createDefaultRooms();
                saveChanges();
            }
        } catch (FileException e) {
            this.rooms = new ArrayList<>();
            System.err.println("Error al cargar habitaciones: " + e.getMessage());
        }
    }

    private void saveChanges() throws RoomException {
        try {
            fileHandler.saveData(rooms, filePath);
        } catch (FileException e) {
            throw new RoomException("Error al guardar los cambios: " + e.getMessage());
        }
    }

    @Override
    public void addRoom(Classroom room) throws RoomException {
        if (rooms.stream().anyMatch(r -> r.getNombre().equalsIgnoreCase(room.getNombre()))) {
            throw new RoomException("Ya existe una habitación con este nombre");
        }
        
        rooms.add(room);
        saveChanges();
    }

    @Override
    public Classroom getRoomById(String id) throws RoomException {
        return rooms.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst()
                .orElseThrow(RoomException::notFound);
    }

    @Override
    public void updateRoom(Classroom room) throws RoomException {
        Classroom existingRoom = getRoomById(room.getId());
        
        if (!existingRoom.getNombre().equals(room.getNombre()) &&
            rooms.stream()
                .filter(r -> !r.getId().equals(room.getId()))
                .anyMatch(r -> r.getNombre().equalsIgnoreCase(room.getNombre()))) {
            throw new RoomException("Ya existe una habitación con este nombre");
        }
        
        rooms.removeIf(r -> r.getId().equals(room.getId()));
        rooms.add(room);
        
        saveChanges();
    }

    @Override
    public void deleteRoom(String id) throws RoomException {
        Classroom room = getRoomById(id);
        
        if (!room.isDisponible()) throw RoomException.isOccupied();
        
        rooms.removeIf(r -> r.getId().equals(id));
        saveChanges();
    }

    @Override
    public List<Classroom> getRoomsByType(char type) throws RoomException {
        if (!"IPDM".contains(String.valueOf(type))) throw RoomException.invalidType();
        
        return rooms.stream()
                .filter(r -> r.getTipo() == type)
                .collect(Collectors.toList());
    }

    @Override
    public List<Classroom> getAvailableRooms() throws RoomException {
        return rooms.stream()
                .filter(Classroom::isDisponible)
                .collect(Collectors.toList());
    }

    @Override
    public void setRoomMaintenance(String roomId, boolean inMaintenance) throws RoomException {
        Classroom room = getRoomById(roomId);

        room.setDisponible(!inMaintenance);
        saveChanges();
    }

    @Override
    public void setRoomOccupied(String roomId, boolean occupied) throws RoomException {
        Classroom room = getRoomById(roomId);

        room.setDisponible(!occupied);
        saveChanges();
    }

    private void createDefaultRooms() throws RoomException {
        for (int piso = 1; piso <= 5; piso++) {
            for (int sala = 1; sala <= 10; sala++) {
                String id = String.format("I%d%02d", piso, sala);
                rooms.add(new Classroom(id, 'I', 20.0 + (piso * 2)));
            }
        }
        
        for (int piso = 1; piso <= 4; piso++) {
            for (int sala = 1; sala <= 5; sala++) {
                String id = String.format("P%d%02d", piso, sala);
                rooms.add(new Classroom(id, 'P', 35.0 + (piso * 5)));
            }
        }
        
        for (int piso = 1; piso <= 6; piso++) {
            for (int sala = 1; sala <= 5; sala++) {
                String id = String.format("D%d%02d", piso, sala);
                rooms.add(new Classroom(id, 'D', 45.0 + (piso * 3)));
            }
        }
        
        for (int piso = 1; piso <= 3; piso++) {
            for (int sala = 1; sala <= 5; sala++) {
                String id = String.format("M%d%02d", piso, sala);
                rooms.add(new Classroom(id, 'M', 55.0 + (piso * 4)));

            }
        }
    }
}