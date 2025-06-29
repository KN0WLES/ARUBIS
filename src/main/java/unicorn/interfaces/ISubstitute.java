package unicorn.interfaces;

import unicorn.model.Substitute;
import unicorn.exceptions.SubstituteException;
import java.util.List;
//import java.time.LocalDate;

/**
 * Interfaz para la gestión de sustituciones temporales de docentes.
 * Define las operaciones básicas para crear, consultar y gestionar suplentes.
 *
 * @description Funcionalidades principales:
 *                   - Registrar nuevas sustituciones
 *                   - Consultar sustituciones activas
 *                   - Finalizar sustituciones anticipadamente
 *                   - Verificar disponibilidad de docentes
 *                   - Gestionar el historial de suplencias
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-06-15
 */
public interface ISubstitute {

    /**
     * Crea una nueva sustitución temporal.
     *
     * @param substitute El objeto Substitute con los datos de la sustitución
     * @throws SubstituteException Si ya existe una sustitución activa para el docente
     *                            o si ocurre un error al guardar
     */
    void createSubstitute(Substitute substitute) throws SubstituteException;

    /**
     * Obtiene todas las sustituciones activas actualmente.
     *
     * @return Lista de sustituciones activas
     * @throws SubstituteException Si ocurre un error al leer los datos
     */
    List<Substitute> getActiveSubstitutes() throws SubstituteException;

    /**
     * Finaliza una sustitución antes de la fecha programada.
     *
     * @param substituteId ID de la sustitución a finalizar
     * @throws SubstituteException Si la sustitución no existe o no está activa
     */
    void endSubstitution(String substituteId) throws SubstituteException;

    /**
     * Verifica si un docente tiene una sustitución activa.
     *
     * @param teacherId ID del docente a verificar
     * @return true si tiene una sustitución activa, false en caso contrario
     * @throws SubstituteException Si ocurre un error en la consulta
     */
    boolean hasActiveSubstitute(String teacherId) throws SubstituteException;

    /**
     * Obtiene la sustitución activa para un docente específico.
     *
     * @param teacherId ID del docente
     * @return El objeto Substitute activo o null si no existe
     * @throws SubstituteException Si ocurre un error en la consulta
     */
    Substitute getActiveSubstituteForTeacher(String teacherId) throws SubstituteException;

    /**
     * Procesa sustituciones expiradas y las marca como completadas.
     *
     * @throws SubstituteException Si ocurre un error al actualizar los registros
     */
    void processExpiredSubstitutes() throws SubstituteException;

    /**
     * Obtiene todas las sustituciones registradas en el sistema.
     *
     * @return Lista completa de sustituciones
     * @throws SubstituteException Si ocurre un error al leer los datos
     */
    List<Substitute> getAllSubstitutes() throws SubstituteException;

    /**
     * Transfiere el horario de un docente a su suplente.
     *
     * @param originalTeacherId ID del docente original
     * @param substituteTeacherId ID del docente suplente
     * @throws SubstituteException Si ocurre un error en la transferencia
     */
    void transferSchedule(String originalTeacherId, String substituteTeacherId) throws SubstituteException;

    /**
     * Restaura el horario original después de una sustitución.
     *
     * @param substituteId ID de la sustitución completada
     * @throws SubstituteException Si ocurre un error en la restauración
     */
    void restoreSchedule(String substituteId) throws SubstituteException;
}