package unicorn.arubis.util;

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

    public static boolean validateUsername(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{4,}$");
    }

    public static boolean validateEmail(String email) {
        return email != null && Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean validatePhone(String phone) {
        return phone != null && phone.matches("^\\d{7,15}$");
    }

    public static boolean validatePasswordStrength(String password) {
        return password != null && Pattern.matches(PASSWORD_REGEX, password);
    }
}
