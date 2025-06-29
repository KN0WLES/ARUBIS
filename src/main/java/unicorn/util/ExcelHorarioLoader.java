package unicorn.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import unicorn.controller.*;
import unicorn.model.*;
import unicorn.exceptions.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExcelHorarioLoader {
    private static final Logger logger = Logger.getLogger(ExcelHorarioLoader.class.getName());

    private final AccountController accountController;
    private final SubjectController subjectController;
    private final RoomController roomController;
    private final ScheduleController scheduleController;
    private final String estadoPath = "src/main/java/unicorn/dto/schedules/estado_carga.txt";

    public ExcelHorarioLoader(AccountController accountController,
                            SubjectController subjectController,
                            RoomController roomController,
                            ScheduleController scheduleController) {
        this.accountController = accountController;
        this.subjectController = subjectController;
        this.roomController = roomController;
        this.scheduleController = scheduleController;
    }
    
    public void cargarExcelSiCorresponde(String excelPath) throws HorarioLoaderException {
        if (archivoCargadoPreviamente(excelPath)) {
            logger.info(() -> "El archivo " + excelPath + " ya fue cargado previamente. No se realizará la carga.");
            return;
        }

        boolean datosCargados = false;

        try (FileInputStream fis = new FileInputStream(new File(excelPath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                throw new HorarioLoaderException("El archivo Excel no contiene hojas");
            }

            Map<String, Room> aulasExistentes = new HashMap<>();
            roomController.getAllRooms().forEach(room ->
                    aulasExistentes.put(room.getNombre().toLowerCase(), room));

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    procesarFila(row, aulasExistentes);
                    datosCargados = true;
                } catch (AccountException | RoomException | ScheduleException | SubjectException e) {
                    logger.log(Level.WARNING, "Error procesando fila " + i + ": " + e.getMessage(), e);
                }
            }

            if (datosCargados) {
                registrarCargaExitosa(excelPath);
                logger.info("Carga de horarios desde Excel completada y registrada exitosamente.");
            } else {
                logger.warning("No se cargaron datos desde el archivo Excel.");
            }

        } catch (Exception e) {
            throw new HorarioLoaderException("Error al procesar el archivo Excel: " + e.getMessage(), e);
        }
    }

    private boolean archivoCargadoPreviamente(String excelPath) {
        File estado = new File(estadoPath);
        if (!estado.exists()) {
            return false;
        }

        try (Scanner scanner = new Scanner(estado)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine().trim();
                if (linea.isEmpty()) continue;
                String[] partes = linea.split("\\|");
                if (partes.length < 2) continue;
                String archivo = partes[0].trim();
                String estadoCarga = partes[1].trim();

                if (archivo.equalsIgnoreCase(excelPath) && estadoCarga.equalsIgnoreCase("true")) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al leer estado de carga", e);
        }
        return false;
    }

    private void registrarCargaExitosa(String excelPath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(estadoPath, true))) {
            writer.println(excelPath + " | true");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al registrar estado de carga", e);
        }
    }

    private void procesarFila(Row row, Map<String, Room> aulasExistentes) 
            throws AccountException, SubjectException, RoomException, ScheduleException {
        String materia = getCellStringValue(row, 1); // Columna B
        String grupo = getCellStringValue(row, 2);   // Columna C
        String tipo = getCellStringValue(row, 3);    // Columna D
        String nombreProfesor = getCellStringValue(row, 4); // Columna E

        if (materia.isEmpty() || grupo.isEmpty() || tipo.isEmpty() || nombreProfesor.isEmpty()) {
            logger.warning("Fila incompleta, se omite.");
            return;
        }

        // 1. Procesar materia
        Subject subject = procesarMateria(materia, tipo);
        
        // 2. Procesar profesor
        Account profesor;
        try {
            profesor = procesarProfesor(nombreProfesor);
            if (profesor == null) {
                logger.warning(() -> "No se pudo procesar profesor: " + nombreProfesor);
                return;
            }
        } catch (AccountException e) {
            logger.log(Level.WARNING, "Error al procesar profesor: " + nombreProfesor, e);
            return;
        }
        
        // 3. Procesar periodos
        List<ScheduleController.PeriodInfo> periodos = procesarPeriodos(row, aulasExistentes);

        if (periodos.isEmpty()) {
            logger.warning(() -> "No hay periodos válidos para " + materia + " - " + nombreProfesor);
            return;
        }

        // 4. Crear horario completo
        Schedule schedule = scheduleController.createCompleteSchedule(
                subject.getId(), 
                grupo, 
                profesor.getId(), 
                tipo, 
                periodos
        );
        
        // 5. Guardar en archivo TXT
        guardarHorariosEnTxt(materia, grupo, nombreProfesor, periodos, profesor.getUser());
        
        logger.info(() -> "Horario creado para: " + materia + " - Grupo " + grupo + 
                " - Profesor " + nombreProfesor + " con " + periodos.size() + " periodos");
    }

    private Subject procesarMateria(String nombreMateria, String tipo) throws SubjectException {
        try {
            // Si la materia existe, la retornamos
            return subjectController.getSubjectByName(nombreMateria);
        } catch (SubjectException e) {
            // Si no existe, la creamos
            Subject subject = new Subject(
                nombreMateria, 
                "Materia generada automáticamente", 
                3, 
                tipo
            );
            subjectController.addSubject(subject);
            logger.info(() -> "Materia creada: " + nombreMateria);
            return subject;
        }
    }

    private Account procesarProfesor(String nombreCompleto) throws AccountException {
        // Validación inicial más estricta
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new AccountException("Nombre de profesor no puede estar vacío");
        }
        
        nombreCompleto = nombreCompleto.trim().replaceAll("\\s+", " ");
        
        // Validar formato básico del nombre (al menos dos partes)
        if (nombreCompleto.split(" ").length < 2) {
            throw new AccountException("Nombre de profesor debe contener al menos un nombre y un apellido: " + nombreCompleto);
        }

        try {
            String username = generarUsernameProfesor(nombreCompleto);
            String email = username + "@prf.umss.edu";
            
            // 1. Intentar obtener profesor existente
            try {
                Account profesorExistente = accountController.getByUsername(username);
                if (profesorExistente != null) {
                    return profesorExistente;
                }
            } catch (AccountException e) {
                // Continuar para crear nuevo profesor
            }
            
            // 2. Crear nuevo profesor
            String[] partes = nombreCompleto.split(" ");
            String nombre = partes[0];
            String apellido = partes[partes.length - 1];

            Account nuevoProfesor = new Account(
                nombreCompleto,
                apellido,
                "00000000",
                email,
                username,
                "Hola1234",
                TipoCuenta.PROFESOR
            );
            
            accountController.registerAccount(nuevoProfesor);
            logger.info(() -> "Nuevo profesor creado: " + username);
            return nuevoProfesor;
            
        } catch (AccountException e) {
            logger.log(Level.SEVERE, "Error crítico al procesar profesor: " + nombreCompleto, e);
            throw new AccountException("Error crítico al procesar profesor: " + nombreCompleto + ". Causa: " + e.getMessage());
        }
    }


    private String generarUsernameProfesor(String nombreCompleto) {
    if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
        throw new IllegalArgumentException("Nombre de profesor no puede estar vacío");
    }

    // Normalizar el nombre (mayúsculas, eliminar espacios extras)
    nombreCompleto = nombreCompleto.trim().toUpperCase().replaceAll("\\s+", " ");
    String[] partes = nombreCompleto.split(" ");
    
    if (partes.length < 2) {
        throw new IllegalArgumentException("Nombre debe contener al menos un apellido y un nombre: " + nombreCompleto);
    }

    // Primera letra del primer nombre en minúscula
    char primeraLetraNombre = Character.toLowerCase(partes[2].charAt(0));
    
    // Primer apellido en minúscula (normalizado)
    String apellido = normalizarApellido(partes[0]);
    
    return primeraLetraNombre + "." + apellido;
}


    private String normalizarApellido(String apellido) {
        // Eliminar acentos y caracteres especiales
        return apellido.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replaceAll("[^a-z]", "");
    }

    private List<ScheduleController.PeriodInfo> procesarPeriodos(Row row, Map<String, Room> aulasExistentes) {
        List<ScheduleController.PeriodInfo> periodos = new ArrayList<>();
        
        // Definición de los bloques de columnas (dia, inicio, fin, aula)
        int[][] bloques = {
            {5, 6, 7, 8},   // Bloque 1: columnas F-I
            {9, 10, 11, 12}, // Bloque 2: columnas J-M
            {13, 14, 15, 16}, // Bloque 3: columnas N-Q
            {17, 18, 19, 20}  // Bloque 4: columnas R-U
        };

        for (int[] bloque : bloques) {
            try {
                String diaStr = getCellStringValue(row, bloque[0]);
                String inicioStr = getCellStringValue(row, bloque[1]);
                String finStr = getCellStringValue(row, bloque[2]);
                String aulaNombre = getCellStringValue(row, bloque[3]);

                // Si el bloque está vacío, pasamos al siguiente
                if (diaStr.isEmpty() && inicioStr.isEmpty() && finStr.isEmpty() && aulaNombre.isEmpty()) {
                    continue;
                }

                // Validar que el bloque esté completo
                if (diaStr.isEmpty() || inicioStr.isEmpty() || finStr.isEmpty() || aulaNombre.isEmpty()) {
                    logger.warning(() -> "Bloque incompleto en fila " + (row.getRowNum() + 1));
                    continue;
                }

                // 1. Validar día (Lunes-Viernes)
                DayOfWeek dia = parseDia(diaStr);
                if (dia == DayOfWeek.SUNDAY) {
                    logger.warning(() -> "Domingo no permitido en fila " + (row.getRowNum() + 1));
                    continue;
                }

                // 2. Procesar aula
                Room aula = obtenerOCrearAula(aulaNombre, aulasExistentes);
                if (aula == null) {
                    continue;
                }

                // 3. Validar horas
                LocalTime inicio = parseHora(inicioStr);
                LocalTime fin = parseHora(finStr);
                
                if (inicio.isAfter(fin)) {
                    logger.warning(() -> "Hora de inicio posterior a fin en fila " + (row.getRowNum() + 1));
                    continue;
                }

                // 4. Crear periodo
                periodos.add(new ScheduleController.PeriodInfo(dia, inicio, fin, aula.getId()));

            } catch (Exception e) {
                logger.warning(() -> "Error procesando bloque en fila " + (row.getRowNum() + 1) + ": " + e.getMessage());
            }
        }

        return periodos;
    }

    private Room obtenerOCrearAula(String aulaNombre, Map<String, Room> aulasExistentes) {
        // Buscar aula existente (case insensitive)
        Room aula = aulasExistentes.get(aulaNombre.toLowerCase());
        
        if (aula == null) {
            try {
                // Crear nueva aula si no existe
                aula = new Room(aulaNombre, TipoRoom.AULA, 40, false);
                roomController.addRoom(aula);
                aulasExistentes.put(aulaNombre.toLowerCase(), aula);
                logger.info(() -> "Aula creada: " + aulaNombre);
            } catch (RoomException e) {
                logger.log(Level.WARNING, "Error al crear aula: " + aulaNombre, e);
                return null;
            }
        }
        
        return aula;
    }

    private String getCellStringValue(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case STRING -> {
                return cell.getStringCellValue().trim();
            }
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getLocalDateTimeCellValue().toLocalTime().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    // Para horas como 8.25 (8:15)
                    if (numValue % 1 != 0) {
                        int horas = (int) numValue;
                        int minutos = (int) ((numValue - horas) * 60);
                        return String.format("%02d:%02d", horas, minutos);
                    }
                    return String.valueOf((int) numValue);
                }
            }
            default -> {
                return "";
            }
        }
    }

    private DayOfWeek parseDia(String abreviado) {
        if (abreviado == null || abreviado.isEmpty()) {
            throw new IllegalArgumentException("Día no puede ser nulo o vacío");
        }

        return switch (abreviado.toUpperCase()) {
            case "LU" -> DayOfWeek.MONDAY;
            case "MA" -> DayOfWeek.TUESDAY;
            case "MI" -> DayOfWeek.WEDNESDAY;
            case "JU" -> DayOfWeek.THURSDAY;
            case "VI" -> DayOfWeek.FRIDAY;
            case "SA" -> DayOfWeek.SATURDAY;
            default -> throw new IllegalArgumentException("Día inválido: " + abreviado);
        };
    }

    private LocalTime parseHora(String horaStr) {
        if (horaStr == null || horaStr.isEmpty()) {
            throw new IllegalArgumentException("Hora no puede estar vacía");
        }

        // Manejar formatos como "8.25" (8:15)
        if (horaStr.contains(".")) {
            String[] partes = horaStr.split("\\.");
            int horas = Integer.parseInt(partes[0]);
            int minutos = (int) (Double.parseDouble("0." + partes[1]) * 60);
            return LocalTime.of(horas, minutos);
        }

        // Manejar formatos como "8:15" o "815"
        horaStr = horaStr.replace(":", "");
        if (horaStr.length() == 3) {
            horaStr = "0" + horaStr; // Para "815" -> "0815"
        }

        try {
            int horas = Integer.parseInt(horaStr.substring(0, 2));
            int minutos = Integer.parseInt(horaStr.substring(2));
            return LocalTime.of(horas, minutos);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato de hora inválido: " + horaStr);
        }
    }

    private void guardarHorariosEnTxt(String materia, String grupo, String profesorNombre, 
                                List<ScheduleController.PeriodInfo> periodos,
                                String profesorUsername) {
        // 1. Guardar en archivo individual de materia/grupo (opcional, puedes eliminar si no lo necesitas)
        guardarHorarioIndividual(materia, grupo, profesorNombre, periodos);
        
        // 2. Guardar en archivo del profesor
        guardarHorarioProfesor(materia, grupo, profesorNombre, periodos, profesorUsername);
    }

    private void guardarHorarioIndividual(String materia, String grupo, String profesor, 
                                List<ScheduleController.PeriodInfo> periodos) {
        // Crear nombre de archivo seguro (reemplazar caracteres inválidos)
        String nombreArchivoSeguro = materia.replaceAll("[^a-zA-Z0-9]", "_") + "_" + 
                                    grupo.replaceAll("[^a-zA-Z0-9]", "_") + ".txt";
        
        // Ruta completa del archivo
        String rutaCompleta = "src/main/java/unicorn/dto/schedules/materia/" + nombreArchivoSeguro;
        
        // Crear directorio si no existe
        new File("src/main/java/unicorn/dto/schedules/materia").mkdirs();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaCompleta, true))) {
            writer.println("=== Horario ===");
            writer.println("Materia: " + materia);
            writer.println("Grupo: " + grupo);
            writer.println("Profesor: " + profesor);
            writer.println("Periodos:");
            
            for (ScheduleController.PeriodInfo periodo : periodos) {
                writer.printf("- %s: %s a %s en aula %s%n",
                    periodo.day(), 
                    periodo.startTime(), 
                    periodo.endTime(),
                    periodo.roomId());
            }
            writer.println("===============");
            logger.info(() -> "Horario guardado en: " + rutaCompleta);
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo guardar el horario en " + rutaCompleta, e);
        }
    }

    private void guardarHorarioProfesor(String materia, String grupo, String profesorNombre,
                                  List<ScheduleController.PeriodInfo> periodos,
                                  String profesorUsername) {
        String nombreArchivoProfesor = profesorUsername + "_schedule.txt";
        String rutaProfesor = "src/main/java/unicorn/dto/schedules/profesor/" + nombreArchivoProfesor;
        
        new File("src/main/java/unicorn/dto/schedules/profesor/").mkdirs();
        
        // Verificar si el horario ya existe
        if (horarioYaExiste(rutaProfesor, materia, grupo)) {
            logger.info(() -> "Horario ya existe para " + profesorUsername + " - " + materia + " " + grupo);
            return;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaProfesor, true))) {
            escribirHorario(writer, materia, grupo, profesorNombre, periodos);
            logger.info(() -> "Horario agregado al archivo del profesor: " + rutaProfesor);
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo guardar horario del profesor", e);
        }
    }

    private boolean horarioYaExiste(String rutaArchivo, String materia, String grupo) {
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return false;
        }
        
        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                if (linea.contains("Materia: " + materia) && 
                    scanner.nextLine().contains("Grupo: " + grupo)) {
                    return true;
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al verificar horario existente", e);
        }
        return false;
    }

    private void escribirHorario(PrintWriter writer, String materia, String grupo, 
                           String profesor, List<ScheduleController.PeriodInfo> periodos) {
        writer.println("=== Horario ===");
        writer.println("Materia: " + materia);
        writer.println("Grupo: " + grupo);
        writer.println("Profesor: " + profesor);
        writer.println("Periodos:");
        
        for (ScheduleController.PeriodInfo periodo : periodos) {
            writer.printf("- %s: %s a %s en aula %s%n",
                periodo.day(), 
                periodo.startTime(), 
                periodo.endTime(),
                periodo.roomId());
        }
        writer.println("===============");
    }
}



class HorarioLoaderException extends Exception {
    public HorarioLoaderException(String message) {
        super(message);
    }

    public HorarioLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}