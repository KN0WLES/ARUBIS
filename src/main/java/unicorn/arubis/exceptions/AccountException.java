package unicorn.arubis.exceptions;

import unicorn.arubis.model.Account;

/**
 * Clase que representa excepciones específicas relacionadas con la gestión de cuentas de usuario.
 * Proporciona mensajes de error predefinidos para situaciones comunes, como cuentas no encontradas,
 * usuarios duplicados o credenciales inválidas.
 * 
 * @description Funcionalidades principales:
 *                   - Lanzar una excepción cuando una cuenta no es encontrada.
 *                   - Lanzar una excepción cuando un usuario ya existe.
 *                   - Lanzar una excepción cuando las credenciales son inválidas.
 *                   - Lanzar una excepción cuando se intenta modificar/eliminar una cuenta de administrador.
 * 
 * @autor KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public class AccountException extends Exception {
     /**
     * Excepción personalizada para errores de gestión de cuentas.
     * Proporciona constructores estáticos para errores comunes.
     */
    public AccountException(String message) {
        super(message);
    }
    /**
     * Factory method para crear una excepción estándar de "cuenta no encontrada".
     * @return AccountException configurada con mensaje estándar
     * @see AccountService#getByUsername(String)
     * @see AccountService#getById(String)
     */
    public static AccountException userNotFound() {
        return new AccountException("Cuenta no encontrada");
    }
     /**
     * Crea una excepción estándar para casos de username duplicado.
     * @return AccountException preconfigurada con mensaje estándar
     * @see AccountService#registerAccount(Account) Ejemplo de uso principal
     */
    public static AccountException duplicateUser() {
        return new AccountException("El usuario ya existe");
    }
     /**
     * Crea una excepción estándar para credenciales de autenticación inválidas.
     * @return AccountException preconfigurada con mensaje de seguridad estándar 
     * @see AccountService#login(String, String) Caso de uso principal
     */
    public static AccountException invalidCredentials() {
        return new AccountException("Usuario o contraseña incorrectos");
    }
     /**
     * Excepción para operaciones no permitidas en cuentas de administrador.
     * 
     * @return Excepción con mensaje estándar para restricciones de admin
     * @see AccountService#deleteAccount() Caso de uso típico
     */
    public static AccountException adminRestriction() {
        return new AccountException("No se puede modificar/eliminar una cuenta admin");
    }
     /**
     * Excepción para direcciones de email con formato incorrecto.
     * 
     * @return Excepción con mensaje estándar para emails inválidos
     * @see AccountValidator#validateEmail() Validación asociada
     */
    public static AccountException emailInvalido() {
        return new AccountException("Email inválido");
    }
     /**
     * Excepción para números de teléfono mal formateados.
     * 
     * @return Excepción con mensaje estándar para teléfonos inválidos
     * @see AccountValidator#validatePhone() Validación asociada
     */
    public static AccountException telefonoInvalido() {
        return new AccountException("Teléfono inválido");
    }
     /**
     * Excepción para nombres de usuario que no cumplen requisitos.
     * 
     * @return Excepción con mensaje que especifica los requisitos
     * @see AccountValidator#validateUsername() Validación asociada
     */
    public static AccountException usuarioInvalido() {
        return new AccountException("Usuario inválido (mínimo 4 caracteres alfanuméricos)");
    }
     /**
     * Excepción para contraseñas que no cumplen políticas de seguridad.
     * 
     * @return Excepción con mensaje que detalla los requisitos
     * @see AccountValidator#validatePassword() Validación asociada
     */
    public static AccountException contrasenaInvalida() {
        return new AccountException("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
    }
     /**
     * Excepción estándar para cuentas no existentes.
     * 
     * @return Excepción con mensaje genérico
     * @see AccountService#getByUsername() Caso de uso típico
     */
    public static AccountException cuentaNoEncontrada() {
        return new AccountException("Cuenta no encontrada");
    }
     /**
     * Excepción para intentos de registrar usernames existentes.
     * 
     * @return Excepción con mensaje estándar
     * @see AccountService#registerAccount() Caso de uso principal
     */
    public static AccountException usuarioDuplicado() {
        return new AccountException("El usuario ya existe");
    }
     /**
     * Excepción para operaciones prohibidas en cuentas administrador.
     * 
     * @return Excepción con mensaje descriptivo
     * @see AccountService#promoteToAccount() Contexto relacionado
     */
    public static AccountException restriccionAdmin() {
        return new AccountException("No se puede modificar/eliminar una cuenta admin");
    }
}