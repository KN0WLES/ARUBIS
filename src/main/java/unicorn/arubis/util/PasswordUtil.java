package unicorn.arubis.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase que proporciona utilidades para el manejo seguro de contraseñas.
 * Incluye métodos para hashear contraseñas y verificar su validez utilizando SHA-256.
 * 
 * @description Funcionalidades principales:
 *                   - Hashear contraseñas de forma segura utilizando el algoritmo SHA-256.
 *                   - Verificar contraseñas ingresadas comparándolas con el hash almacenado.
 *                   - Garantizar la seguridad de las contraseñas en el sistema.
 * 
 * @note El algoritmo SHA-256 es seguro, pero no incluye salting. Para mayor seguridad, se recomienda agregar salting.
 * 
 * @throws RuntimeException Si ocurre un error al hashear la contraseña.
 * 
 * @see MessageDigest
 * @see AccountValidation
 * @author KNOWLES
 * @version 1.0
 * @since 2025-04-29
 */
public final class PasswordUtil {

    private PasswordUtil() {}

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al hashear la contraseña", e);
        }
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) {
        return hashPassword(inputPassword).equals(storedHash);
    }
}