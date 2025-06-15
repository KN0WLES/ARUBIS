package unicorn.arubis.controller;

import unicorn.arubis.model.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.IFile;
import unicorn.arubis.interfaces.IRoom;
import unicorn.arubis.util.*;

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
     /**
     * Constructor que inicializa el controlador cargando aulas desde persistencia.
     * Si no existen aulas, crea un conjunto predeterminado.
     * 
     * @param fileHandler Manejador de archivos para persistencia (debe implementar {@link IFile<Room>}).
     * @throws RoomException Si ocurre un error al cargar/crear las aulas iniciales.
     * 
     * @example 
     * {@code
     * IFile<Room> fileHandler = new JsonFileHandler<>();
     * RoomController controller = new RoomController(fileHandler);
     * }
     */
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
     /**
     * Persiste los cambios actuales en el archivo de almacenamiento.
     * 
     * @throws RoomException Si falla la escritura en el archivo.
     * @see #loadData() Método complementario para cargar datos.
     */
    private void saveChanges() throws RoomException {
        try {
            fileHandler.saveData(new ArrayList<>(rooms.values()), filePath);
        } catch (FileException e) {
            throw new RoomException("Error al guardar los cambios: " + e.getMessage());
        }
    }
     /**
     * Registra un nuevo aula en el sistema.
     * 
     * @param room Instancia de {@link Room} a registrar (no nula).
     * @throws RoomException Si:
     * - El ID del aula ya existe ({@code "Ya existe un aula con este ID"})
     * - El nombre del aula ya está en uso ({@code "Ya existe un aula con este nombre"})
     * 
     * @see #updateRoom(Room) Para modificar aulas existentes.
     */
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
     /**
     * Recupera un aula por su ID único.
     * 
     * @param id Identificador único del aula (formato UUID).
     * @return Instancia de {@link Room} correspondiente.
     * @throws RoomException Si no se encuentra el aula ({@code "Aula no encontrada"}).
     */
    @Override
    public Room getRoomById(String id) throws RoomException {
        Room room = rooms.get(id);
        if (room == null) {
            throw new RoomException("Aula no encontrada");
        }
        return room;
    }
     /**
     * Actualiza los datos de un aula existente.
     * 
     * @param room Instancia de {@link Room} con los nuevos datos (debe contener ID existente).
     * @throws RoomException Si:
     * - El aula no existe ({@code "Aula no encontrada"})
     * - El nuevo nombre ya está en uso por otra aula
     * 
     * @see #addRoom(Room) Para crear nuevas aulas.
     */
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
     /**
     * Elimina un aula del sistema (solo si está disponible).
     * 
     * @param id Identificador único del aula a eliminar.
     * @throws RoomException Si:
     * - El aula no existe
     * - El aula está ocupada ({@code 'O'}) o en mantenimiento ({@code 'M'})
     * 
     * @see #setRoomOccupied(String, boolean) Para cambiar estado de ocupación.
     * @see #setRoomMaintenance(String, boolean) Para poner aulas en mantenimiento.
     */
    @Override
    public void deleteRoom(String id) throws RoomException {
        Room room = getRoomById(id);
        
        if (room.getDisponible() != 'L') {
            throw new RoomException("El aula está ocupada o en mantenimiento");
        }
        
        rooms.remove(id);
        saveChanges();
    }
     /**
     * Filtra aulas por tipo principal (FÍSICA o VIRTUAL).
     * 
     * @param type Tipo de aula (case-insensitive): "FISICA" o "VIRTUAL".
     * @return Lista de aulas coincidentes (vacía si no hay resultados).
     * @throws RoomException Si el tipo no es válido.
     * 
     * @see #getRoomsByType(String, String) Para filtrar por subtipos de aulas físicas.
     */
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
     /**
     * Filtra aulas físicas por subtipo específico (AULA, LABORATORIO, AUDITORIO).
     * 
     * @param type Tipo principal (debe ser "FISICA").
     * @param subType Subtipo físico (case-insensitive): "AULA", "LABORATORIO" o "AUDITORIO".
     * @return Lista de aulas coincidentes.
     * @throws RoomException Si:
     * - El tipo principal no es "FISICA"
     * - El subtipo no es válido
     * 
     * @see TipoRoom Enumerado con los subtipos soportados.
     */
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
     /**
     * Obtiene todas las aulas disponibles (estado 'L' - Libre).
     * 
     * @return Lista no modificable de aulas disponibles.
     * @see Room#getDisponible() Para consultar estados posibles.
     */
    @Override
    public List<Room> getAvailableRooms() throws RoomException {
        return rooms.values().stream()
            .filter(r -> r.getDisponible() == 'L')
            .collect(Collectors.toList());
    }
     /**
     * Cambia el estado de mantenimiento de un aula.
     * 
     * @param roomId ID del aula a modificar.
     * @param inMaintenance true para poner en mantenimiento (estado 'M'), false para liberar (estado 'L').
     * @throws RoomException Si el aula no existe.
     */
    @Override
    public void setRoomMaintenance(String roomId, boolean inMaintenance) throws RoomException {
        Room room = getRoomById(roomId);
        room.setDisponible(inMaintenance ? 'M' : 'L');
        saveChanges();
    }
     /**
     * Cambia el estado de ocupación de un aula.
     * 
     * @param roomId ID del aula a modificar.
     * @param occupied true para marcar como ocupada (estado 'O'), false para liberar (estado 'L').
     * @throws RoomException Si el aula no existe.
     */
    @Override
    public void setRoomOccupied(String roomId, boolean occupied) throws RoomException {
        Room room = getRoomById(roomId);
        room.setDisponible(occupied ? 'O' : 'L');
        saveChanges();
    }
     /**
     * Crea un conjunto predeterminado de aulas (3 edificios, 20 virtuales, laboratorios y auditorios).
     * 
     * @throws RoomException Si falla la persistencia inicial.
     */
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