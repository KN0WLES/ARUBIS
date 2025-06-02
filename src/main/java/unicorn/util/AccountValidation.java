package unicorn.util;

import java.util.regex.Pattern;

/**
 * Clase que proporciona métodos para validar datos relacionados con cuentas de usuario.
 * Incluye validaciones para email, teléfono, nombre de usuario y fortaleza de contraseñas.
 * 
 * @description Funcionalidades principales:
 *                   - Validar el formato de correos electrónicos.
 *                   - Validar números de teléfono con longitud y formato correctos.
 *                   - Validar nombres de usuario con restricciones de longitud y caracteres permitidos.
 *                   - Validar la fortaleza de contraseñas (mínimo 8 caracteres, al menos una mayúscula y un número).
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public class AccountValidation {

    private AccountValidation(){}

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*\\d).{8,}$";

    /**
     * Valida que el nombre de usuario cumpla con los requisitos establecidos.
     * El nombre debe contener al menos 4 caracteres alfanuméricos.
     * 
     * @param username El nombre de usuario a validar
     * @return boolean true si el nombre de usuario es válido, false en caso contrario
     */
    public static boolean validateUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{4,}$");
    }

    /**
     * Valida que el correo electrónico tenga un formato válido.
     * Verifica la estructura básica de un email: usuario@dominio.
     * 
     * @param email El correo electrónico a validar
     * @return boolean true si el email es válido, false en caso contrario
     */
    public static boolean validateEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    /**
     * Valida que el número de teléfono tenga un formato válido.
     * Acepta números entre 7 y 15 dígitos de longitud.
     * 
     * @param phone El número de teléfono a validar
     * @return boolean true si el teléfono es válido, false en caso contrario
     */
    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("^\\d{7,15}$");
    }

    /**
     * Valida que la contraseña cumpla con los requisitos mínimos de seguridad.
     * Requiere al menos 8 caracteres, una mayúscula y un número.
     * 
     * @param password La contraseña a validar
     * @return boolean true si la contraseña cumple los requisitos, false en caso contrario
     */
    public static boolean validatePasswordStrength(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}
