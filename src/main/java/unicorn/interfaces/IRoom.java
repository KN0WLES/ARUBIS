package unicorn.interfaces;

import unicorn.model.Room;
import unicorn.exceptions.RoomException;
import java.util.List;

/**
 * Interfaz para la gestión de habitaciones.
 * Define los métodos necesarios para agregar, consultar, actualizar y eliminar habitaciones,
 * así como para gestionar su estado (disponibilidad, mantenimiento) y realizar búsquedas
 * por diferentes criterios.
 *
 * @description Funcionalidades principales:
 *                   - Agregar nuevas habitaciones al sistema.
 *                   - Obtener habitaciones por identificador o tipo.
 *                   - Actualizar información de habitaciones existentes.
 *                   - Eliminar habitaciones del sistema.
 *                   - Listar habitaciones disponibles.
 *                   - Gestionar estados de ocupación y mantenimiento.
 *
 * Ejemplo de uso:
 * <pre>
 *     IRoom roomManager = new RoomController(fileHandler);
 *     Classroom nuevaAula = new Classroom("E1-B2-P3-AulaA", true, 30, true);
 *     roomManager.addRoom(nuevaAula);
 * </pre>
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public interface IRoom {
    /**
     * Agrega una nueva aula.
     *                      El aula debe contener:
     *                          -Nombre único
     *                          -Tipo (física/virtual)
     *                          -Capacidad
     *                          -Estado inicial.
     *                       El sistema validará que no exista otra aula con el mismo nombre.
     *
     * @param room Aula a agregar.
     * @throws RoomException Si ocurre un error al agregar el aula.
     */
    void addRoom(Room room) throws RoomException;

    /**
     * Obtiene un aula por su ID.
     *
     * @param id Identificador del aula.
     * @return El aula correspondiente.
     * @throws RoomException Si el aula no existe.
     */
    Room getRoomById(String id) throws RoomException;

    /**
     * Actualiza un aula existente.
     *
     * @param room Aula con los datos actualizados.
     * @throws RoomException Si el aula no existe o los datos son inválidos.
     */
    void updateRoom(Room room) throws RoomException;

    /**
     * Elimina un aula.
     *                      Remueve completamente el aula del sistema. Esta operación solo debe realizarse
     *                      cuando el aula ya no está en uso.
     * 
     * @param id Identificador del aula a eliminar.
     * @throws RoomException Si el aula no existe o está ocupada.
     */
    void deleteRoom(String id) throws RoomException;

    /**
     * Obtiene aulas filtrando por tipo físico/virtual.
     * 
     * @param type Tipo de aula: "FISICA" para aulas físicas, "VIRTUAL" para virtuales
     * @return Lista de aulas del tipo especificado
     * @throws RoomException Si el tipo especificado no es válido
     */
    List<Room> getRoomsByType(String type) throws RoomException;
    
    /**
     * Obtiene aulas filtrando por tipo físico y sub-tipo.
     *                      El sub-tipo puede ser "LABORATORIO", "AUDITORIO", etc.
     * 
     * @param physicalType Tipo físico: "FISICA" o "VIRTUAL"
     * @param subType Sub-tipo de aula (ej. "LABORATORIO", "AUDITORIO")
     * @return Lista de aulas que coinciden con el tipo y sub-tipo especificados
     * @throws RoomException Si el tipo o sub-tipo especificado no es válido
     */
    List<Room> getRoomsByType(String physicalType, String subType) throws RoomException;

    /**
     * Obtiene aulas disponibles.
     *                      Un aula disponible es aquella que no está ocupada ni en mantenimiento.
     * 
     * @return Lista de aulas disponibles.
     * @throws RoomException Si ocurre un error al obtener las aulas.
     */
    List<Room> getAvailableRooms() throws RoomException;

    /**
     * Establece el estado de mantenimiento de un aula.
     *                      Cuando un aula está en mantenimiento, no puede ser utilizada.
     * 
     * @param roomId Identificador del aula.
     * @param inMaintenance true si el aula está en mantenimiento, false en caso contrario.
     * @throws RoomException Si el aula no existe.
     */
    void setRoomMaintenance(String roomId, boolean inMaintenance) throws RoomException;

    /**
     * Establece el estado de ocupación de un aula.
     *                      Este método se utiliza cuando un aula es ocupada o desocupada.
     * 
     * @param roomId Identificador del aula.
     * @param occupied true si el aula está ocupada, false en caso contrario.
     * @throws RoomException Si el aula no existe.
     */
    void setRoomOccupied(String roomId, boolean occupied) throws RoomException;
}