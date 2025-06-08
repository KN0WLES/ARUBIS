package unicorn.controller;

import unicorn.model.Substitute;
import unicorn.exceptions.SubstituteException;
import unicorn.interfaces.IFile;
import java.util.List;
import java.util.stream.Collectors;

public class SubstituteController {
    private final IFile<Substitute> fileHandler;
    private static final String SUBSTITUTES_FILE = "src/main/java/unicorn/dto/substitutes.txt";

    public SubstituteController(IFile<Substitute> fileHandler) throws SubstituteException {
        this.fileHandler = fileHandler;
        try {
            fileHandler.createFileIfNotExists(SUBSTITUTES_FILE);
        } catch (Exception e) {
            throw SubstituteException.initializationError();
        }
    }

    public void createSubstitute(Substitute substitute) throws SubstituteException {
        try {
            List<Substitute> substitutes = fileHandler.loadData(SUBSTITUTES_FILE);

            // Verificar que no exista sustitución activa para este docente
            boolean exists = substitutes.stream()
                    .anyMatch(s -> s.getOriginalTeacherId().equals(substitute.getOriginalTeacherId())
                            && s.isCurrentlyActive());

            if (exists) {
                throw SubstituteException.activeSubstitutionExists();
            }

            substitutes.add(substitute);
            fileHandler.saveData(substitutes, SUBSTITUTES_FILE);
        } catch (Exception e) {
            throw new SubstituteException("Error al crear suplente: " + e.getMessage());
        }
    }

    /**
     * Obtiene todas las sustituciones activas en el sistema
     * @return Lista de sustituciones activas
     * @throws SubstituteException Si ocurre un error al leer los datos
     */
    public List<Substitute> getActiveSubstitutes() throws SubstituteException {
        try {
            return fileHandler.loadData(SUBSTITUTES_FILE)
                    .stream()
                    .filter(Substitute::isCurrentlyActive)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw SubstituteException.readError();
        }
    }

    /**
     * Obtiene las sustituciones para un profesor específico
     * @param teacherId ID del profesor
     * @return Lista de sustituciones asociadas al profesor
     * @throws SubstituteException Si ocurre un error al leer los datos
     */
    public List<Substitute> getSubstitutesForTeacher(String teacherId) throws SubstituteException {
        try {
            return fileHandler.loadData(SUBSTITUTES_FILE)
                    .stream()
                    .filter(s -> s.getOriginalTeacherId().equals(teacherId))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw SubstituteException.readError();
        }
    }

    /**
     * Finaliza una sustitución marcándola como completada
     * @param substituteId ID de la sustitución a finalizar
     * @throws SubstituteException Si ocurre un error al actualizar los datos
     */
    public void endSubstitution(String substituteId) throws SubstituteException {
        try {
            List<Substitute> substitutes = fileHandler.loadData(SUBSTITUTES_FILE);
            substitutes.stream()
                    .filter(s -> s.getId().equals(substituteId))
                    .findFirst()
                    .ifPresent(Substitute::markAsCompleted);

            fileHandler.saveData(substitutes, SUBSTITUTES_FILE);
        } catch (Exception e) {
            throw SubstituteException.updateError();
        }
    }
}