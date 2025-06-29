package unicorn.exceptions;

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
    public AccountException(String message) {
        super(message);
    }
    
    public static AccountException userNotFound() {
        return new AccountException("Cuenta no encontrada");
    }
    public static AccountException duplicateUser() {
        return new AccountException("El usuario ya existe");
    }
    public static AccountException invalidCredentials() {
        return new AccountException("Usuario o contraseña incorrectos");
    }
    public static AccountException adminRestriction() {
        return new AccountException("No se puede modificar/eliminar una cuenta admin");
    }
    public static AccountException emailInvalido() {
        return new AccountException("Email inválido");
    }

    public static AccountException telefonoInvalido() {
        return new AccountException("Teléfono inválido");
    }

    public static AccountException usuarioInvalido() {
        return new AccountException("Usuario inválido (mínimo 4 caracteres alfanuméricos)");
    }

    public static AccountException contrasenaInvalida() {
        return new AccountException("La contraseña debe tener 8+ caracteres, 1 mayúscula y 1 número");
    }

    public static AccountException cuentaNoEncontrada() {
        return new AccountException("Cuenta no encontrada");
    }

    public static AccountException usuarioDuplicado() {
        return new AccountException("El usuario ya existe");
    }

    public static AccountException restriccionAdmin() {
        return new AccountException("No se puede modificar/eliminar una cuenta admin");
    }

    public static AccountException accountPending() {
        return new AccountException("La cuenta está pendiente de aprobación");
    }
}