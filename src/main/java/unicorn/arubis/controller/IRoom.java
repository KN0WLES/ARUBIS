package interfaces;

import model.Room;
import exceptions.RoomException;
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
 *     Room nuevaHabitacion = new Room("101", "Habitación Individual", 'I', 75.00, true);
 *     roomManager.addRoom(nuevaHabitacion);
 * </pre>
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public interface IRoom {
    /**
     * Agrega una nueva habitación.
     *                      La habitación debe contener:
     *                          -ID único
     *                          -Nombre
     *                          -Tipo
     *                          -Precio
     *                          -Estado inicial.
     *                       El sistema validará que no exista otra habitación con el mismo ID.
     *
     * @param room Habitación a agregar.
     * @throws RoomException Si ocurre un error al agregar la habitación.
     */
    void addRoom(Classroom room) throws RoomException;

    /**
     * Obtiene una habitación por su ID.
     *
     * @param id Identificador de la habitación.
     * @return La habitación correspondiente.
     * @throws RoomException Si la habitación no existe.
     */
    Classroom getRoomById(String id) throws RoomException;

    /**
     * Actualiza una habitación existente.
     *
     * @param room Habitación con los datos actualizados.
     * @throws RoomException Si la habitación no existe o los datos son inválidos.
     */
    void updateRoom(Classroom room) throws RoomException;

    /**
     * Elimina una habitación.
     *                      Remueve completamente la habitación del sistema. Esta operación solo debe realizarse
     *                      cuando la habitación ya no está en uso y no tiene reservas asociadas.
     * 
     * @param id Identificador de la habitación a eliminar.
     * @throws RoomException Si la habitación no existe.
     */
    void deleteRoom(String id) throws RoomException;

    /**
     * Obtiene habitaciones por tipo.
     *                      Los tipos pueden ser: 'I' (Individual), 'D' (Doble), 'S' (Suite), etc.,
     *                      según la clasificación establecida en el sistema.
     * 
     * @param type Tipo de habitación.
     * @return Lista de habitaciones del tipo especificado.
     * @throws RoomException Si ocurre un error al obtener las habitaciones.
     */
    List<Classroom> getRoomsByType(char type) throws RoomException;

    /**
     * Obtiene habitaciones disponibles.
     *                      Una habitación disponible es aquella que no está ocupada ni en mantenimiento.
     * 
     * @return Lista de habitaciones disponibles.
     * @throws RoomException Si ocurre un error al obtener las habitaciones.
     */
    List<Classroom> getAvailableRooms() throws RoomException;

    /**
     * Establece el estado de mantenimiento de una habitación.
     *                      Cuando una habitación está en mantenimiento, no puede ser reservada ni ocupada.
     * 
     * @param roomId Identificador de la habitación.
     * @param inMaintenance true si la habitación está en mantenimiento, false en caso contrario.
     * @throws RoomException Si la habitación no existe.
     */
    void setRoomMaintenance(String roomId, boolean inMaintenance) throws RoomException;

    /**
     * Establece el estado de ocupación de una habitación.
     *                      Este método se utiliza cuando un huésped ocupa o desocupa una habitación.
     * 
     * @param roomId Identificador de la habitación.
     * @param occupied true si la habitación está ocupada, false en caso contrario.
     * @throws RoomException Si la habitación no existe.
     */
    void setRoomOccupied(String roomId, boolean occupied) throws RoomException;
}