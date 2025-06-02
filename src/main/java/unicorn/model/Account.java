package unicorn.model;

import unicorn.util.*;
import java.util.UUID;

/**
 * Clase que representa el modelo de una cuenta de usuario en el sistema.
 * Contiene todos los campos necesarios para gestionar información de usuarios, como nombre, email, teléfono,
 * nombre de usuario, contraseña y rol (estudiante, profesor y administrador).
 *
 * @description Funcionalidades principales:
 *                   - Validar datos al crear o actualizar una cuenta (email, teléfono, usuario, contraseña).
 *                   - Generar automáticamente un identificador único (UUID) para cada cuenta.
 *                   - Hashing seguro de contraseñas utilizando `PasswordUtil`.
 *                   - Verificar contraseñas ingresadas contra las almacenadas.
 *                   - Gestionar roles de administrador.
 *                   - Serializar y deserializar cuentas para almacenamiento en archivos.
 *
 * @note Las contraseñas nunca se almacenan en texto plano, solo sus hashes.
 *       La validación de datos se realiza mediante la clase AccountValidation.
 *       Los roles están limitados a los definidos en el enum TipoCuenta.
 * 
 * @throws IllegalArgumentException Si los datos proporcionados no cumplen con las validaciones.
 *
 * @see PasswordUtil Para el manejo seguro de contraseñas
 * @see AccountValidation Para la validación de datos
 * @see TipoCuenta Para los roles disponibles
 * @see Base Para la funcionalidad base de modelos
 *
 * @author KNOWLES
 * @version 1.0
 * @since 2025-05-22
 */
public class Account extends Base<Account> {
    private String id;
    private String nombre;
    private String apellido;
    private String phone;
    private String email;
    private String user;
    private String hashedPassword;
    private TipoCuenta tipoCuenta;

    /**
     * Constructor vacío necesario para la deserialización.
     */
    public Account(){ }

    /**
     * Constructor que crea una nueva cuenta con validación de datos.
     *
     * @param nombre Nombre del usuario
     * @param apellido Apellido del usuario
     * @param phone Número de teléfono (debe cumplir con el formato válido)
     * @param email Correo electrónico (debe cumplir con el formato válido)
     * @param user Nombre de usuario (debe tener mínimo 4 caracteres alfanuméricos)
     * @param plainPassword Contraseña en texto plano que será hasheada
     * @throws IllegalArgumentException Si alguno de los datos no cumple con las validaciones
     */
    public Account(String nombre, String apellido, String phone, String email, String user, String plainPassword) {
        if (!AccountValidation.validateEmail(email)) 
            throw new IllegalArgumentException("Email inválido");
        if (!AccountValidation.validatePhone(phone)) 
            throw new IllegalArgumentException("Teléfono inválido");
        if (!AccountValidation.validateUsername(user)) 
            throw new IllegalArgumentException("Usuario inválido (mínimo 4 caracteres alfanuméricos)");
        if (!AccountValidation.validatePasswordStrength(plainPassword))
            throw new IllegalArgumentException("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
        
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.apellido = apellido;
        this.phone = phone;
        this.email = email;
        this.user = user;
        this.hashedPassword = PasswordUtil.hashPassword(plainPassword);
        this.tipoCuenta = TipoCuenta.ESTUDIANTE;
    }

    /**
     * Obtiene el identificador único de la cuenta.
     * Este ID es generado automáticamente al crear la cuenta y es inmutable.
     *
     * @return El UUID de la cuenta como String
     * @see UUID Para el formato del identificador
     */
    @Override
    public String getId() { return this.id; }
    
    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario
     */
    public String getNombre() { return nombre; }
    
    /**
     * Obtiene el apellido del usuario.
     *
     * @return El apellido del usuario
     */
    public String getApellido() { return apellido; }
    
    /**
     * Obtiene el nombre completo del usuario.
     *
     * @return El nombre y apellido concatenados
     */
    public String getFullName() { return nombre + " " + apellido; }
    
    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono
     */
    public String getPhone() { return phone; }
    
    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico
     */
    public String getEmail() { return email; }
    
    /**
     * Obtiene el nombre de usuario.
     *
     * @return El nombre de usuario
     */
    public String getUser() { return user; }
    
    /**
     * Obtiene el tipo de cuenta del usuario.
     *
     * @return El tipo de cuenta (ADMIN, PROFESOR, ESTUDIANTE)
     */
    public TipoCuenta getTipoCuenta() { return tipoCuenta; }

    /**
     * Establece un nuevo número de teléfono con validación.
     *
     * @param phone El nuevo número de teléfono
     * @throws IllegalArgumentException Si el teléfono no cumple con el formato válido
     */
    public void setPhone(String phone) {
        if (!AccountValidation.validatePhone(phone))
            throw new IllegalArgumentException("Teléfono inválido");
        this.phone = phone;
    }

    // Método para establecer ID (necesario para fromFile)
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Establece un nuevo nombre de usuario con validación.
     *
     * @param user El nuevo nombre de usuario
     * @throws IllegalArgumentException Si el usuario no cumple con el formato válido
     */
    public void setUser(String user) {
        if (!AccountValidation.validateUsername(user))
            throw new IllegalArgumentException("Usuario inválido");
        this.user = user;
    }

    /**
     * Establece una nueva contraseña con validación y hashing.
     *
     * @param newPassword La nueva contraseña en texto plano
     * @throws IllegalArgumentException Si la contraseña no cumple con los requisitos de seguridad
     */
    public void setPassword(String newPassword) {
        if (!AccountValidation.validatePasswordStrength(newPassword))
            throw new IllegalArgumentException("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
        this.hashedPassword = PasswordUtil.hashPassword(newPassword);
    }

    // Método para establecer hash de contraseña (necesario para fromFile)
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Establece el tipo de cuenta para el usuario.
     *
     * @param tipoCuenta El nuevo tipo de cuenta
     */
    public void setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    /**
     * Verifica si la cuenta tiene permisos de administrador.
     *
     * @return true si la cuenta es de administrador, false en caso contrario
     */
    public boolean isAdmin() {
        return tipoCuenta == TipoCuenta.ADMIN;
    }

    /**
     * Verifica si la cuenta es de un profesor.
     *
     * @return true si la cuenta es de profesor, false en caso contrario
     */
    public boolean isProfesor() {
        return tipoCuenta == TipoCuenta.PROFESOR;
    }

    /**
     * Verifica si la cuenta es de un estudiante.
     *
     * @return true si la cuenta es de estudiante, false en caso contrario
     */
    public boolean isEstudiante() {
        return tipoCuenta == TipoCuenta.ESTUDIANTE;
    }

    /**
     * Establece un nuevo nombre para el usuario.
     *
     * @param nombre El nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    /**
     * Establece un nuevo apellido para el usuario.
     *
     * @param apellido El nuevo apellido
     */
    public void setApellido(String apellido) { this.apellido = apellido; }
    
    /**
     * Establece un nuevo correo electrónico para el usuario.
     *
     * @param email El nuevo correo electrónico
     */
    public void setEmail(String email) {
        if (!AccountValidation.validateEmail(email))
            throw new IllegalArgumentException("Email inválido");
        this.email = email;
    }
    
    /**
     * Verifica si la contraseña proporcionada coincide con la almacenada.
     *
     * @param inputPassword La contraseña en texto plano a verificar
     * @return true si la contraseña es correcta, false en caso contrario
     */
    public boolean verifyPassword(String inputPassword) {
        return PasswordUtil.verifyPassword(inputPassword, hashedPassword);
    }

    @Override
    public String toFile() {
        return String.join("|", 
            id, nombre, apellido, phone, email, user, 
            hashedPassword, tipoCuenta.name()
        );
    }

    @Override
    public Account fromFile(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 8) {
            throw new IllegalArgumentException("Formato de línea inválido");
        }
        
        Account account = new Account();
        account.setId(parts[0]);
        account.setNombre(parts[1]);
        account.setApellido(parts[2]);
        account.phone = parts[3]; // Asignación directa para evitar validación
        account.email = parts[4];  // Asignación directa para evitar validación
        account.user = parts[5];   // Asignación directa para evitar validación
        account.setHashedPassword(parts[6]);
        account.setTipoCuenta(TipoCuenta.valueOf(parts[7]));
        
        return account;
    }

    @Override
    public String getInfo() {
        return String.format(
            "Usuario: %s (%s)\nTeléfono: %s\nRol: %s", 
            getFullName(), user, phone, tipoCuenta.getDescripcion()
        );
    }
}