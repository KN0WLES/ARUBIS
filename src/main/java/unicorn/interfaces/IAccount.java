package unicorn.interfaces;

import unicorn.model.Account;
import unicorn.util.TipoCuenta;
import unicorn.exceptions.AccountException;
import java.util.List;

/**
 * Interfaz para la gestión de cuentas de usuario.
 * Define los métodos necesarios para registrar, autenticar, actualizar y eliminar cuentas,
 * así como para gestionar privilegios de administrador.
 * 
 * @description Funcionalidades principales:
 *                   - Registro de nuevas cuentas.
 *                   - Validación de credenciales de usuario.
 *                   - Actualización de información de cuentas (nombre, apellido, teléfono, correo, contraseña).
 *                   - Eliminación de cuentas.
 *                   - Gestión de privilegios de administrador.
 *                   - Búsqueda de cuentas por nombre de usuario, correo electrónico o ID.
 *                   - Listado de todas las cuentas registradas.
 * 
 * Ejemplo de uso:
 * <pre>
 *     IAccount accountManager = new AccountController(fileHandler);
 *     accountManager.registerAccount(new Account("user1", "password123", ...));
 * </pre>
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-05-28
 */
public interface IAccount {
    
    /**
     * Registra una nueva cuenta.
     *                   Los datos de la cuenta deben cumplir con las validaciones de AccountValidation:
     *                          - Nombre de usuario: alfanumérico con al menos 4 caracteres
     *                          - Email: formato válido según patrón de correo electrónico
     *                          - Teléfono: entre 7 y 15 dígitos
     *                          - Contraseña: al menos 8 caracteres con al menos una mayúscula y un número
     * 
     * @param account Cuenta a registrar.
     * @throws AccountException Si la cuenta ya existe o los datos son inválidos.
     */
    void registerAccount(Account account) throws AccountException;

    /**
     * Valida las credenciales de una cuenta.
     *                  La validación de la contraseña se realiza mediante PasswordUtil.verifyPassword
     *                  comparando el hash de la contraseña ingresada con el hash almacenado.
     * 
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @return Cuenta si las credenciales son válidas, o {@code null} si no lo son.
     * @throws AccountException Si ocurre un error en la validación.
     */
    Account login(String username, String password) throws AccountException;

    /**
     * Actualiza el nombre de una cuenta.
     * 
     * @param username Nombre de usuario de la cuenta a modificar.
     * @param newName Nuevo nombre.
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void updateName(String username, String newName) throws AccountException;

    /**
     * Actualiza el apellido de una cuenta.
     * 
     * @param username Nombre de usuario de la cuenta a modificar.
     * @param newlast Nuevo apellido.
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void updateLast(String username, String newLast) throws AccountException;

    /**
     * Actualiza el teléfono de una cuenta.
     *                          El número debe cumplir con la validación especificada en AccountValidation
     *                          (entre 7 y 15 dígitos numéricos).
     * 
     * @param username Nombre de usuario de la cuenta a modificar.
     * @param newPhone Nuevo número de teléfono (solo dígitos, entre 7-15 caracteres).
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void updatePhone(String username, String newPhone) throws AccountException;

    /**
     * Actualiza el correo electrónico de una cuenta.
     *                          El email debe cumplir con la validación especificada en AccountValidation
     *                          (formato válido según patrón de email: usuario@dominio).
     * 
     * @param username Nombre de usuario de la cuenta a modificar.
     * @param newEmail Nuevo correo electrónico (debe seguir el patrón definido en EMAIL_REGEX).
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void updateEmail(String username, String newEmail) throws AccountException;

    /**
     * Cambia la contraseña de una cuenta.
     *                          La nueva contraseña debe cumplir con la validación de AccountValidation:
     *                              - Al menos 8 caracteres
     *                              - Al menos una letra mayúscula
     *                              - Al menos un dígito numérico
     *                          La verificación de la contraseña actual se realiza mediante PasswordUtil.
     * 
     * @param username Nombre de usuario de la cuenta.
     * @param currentPassword Contraseña actual.
     * @param newPassword Nueva contraseña (debe cumplir con los requisitos de seguridad definidos en PASSWORD_REGEX).
     * @throws AccountException Si la cuenta no existe, la contraseña actual es incorrecta o la nueva contraseña no cumple los requisitos.
     */
    void updatePassword(String username, String currentPassword, String newPassword) throws AccountException;

    /**
     * Resetea la contraseña de una cuenta.
     *                          La nueva contraseña debe cumplir con la validación de AccountValidation:
     *                              - Al menos 8 caracteres
     *                              - Al menos una letra mayúscula
     *                              - Al menos un dígito numérico
     *                         La verificación de la contraseña actual se realiza mediante PasswordUtil.
     * 
     * @param username Nombre de usuario.
     * @param newPassword Nueva contraseña (debe cumplir con los requisitos de seguridad definidos en PASSWORD_REGEX).
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void resetUserPassword(String username, String newPassword) throws AccountException;
    
    /**
     * Establece el estado de administrador de una cuenta.
     * 
     * @param username Nombre de usuario de la cuenta.
     * @param isAdmin Estado de administrador (true para activar, false para desactivar).
     * @throws AccountException Si la cuenta no existe o los datos son inválidos.
     */
    void promoteToAccount(String username, TipoCuenta newType) throws AccountException;
    
    /**
     * Busca una cuenta por nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar.
     * @return Cuenta si existe, null en caso contrario.
     * @throws AccountException Si ocurre un error durante la búsqueda.
     */
    Account getByUsername(String username) throws AccountException;

    /**
     * Busca una cuenta por correo electrónico.
     * 
     * @param email Correo electrónico a buscar.
     * @return Cuenta si existe, null en caso contrario.
     * @throws AccountException Si ocurre un error durante la búsqueda.
     */
    Account getByEmail(String email) throws AccountException;

    /**
     * Busca una cuenta por id.
     * 
     * @param id ID único de la cuenta a buscar.
     * @return Cuenta si existe, null en caso contrario.
     * @throws AccountException Si ocurre un error durante la búsqueda.
     */
    Account getById(String id) throws AccountException;

    /**
     * Elimina una cuenta.
     * 
     * @param username Nombre de usuario de la cuenta a eliminar.
     * @throws AccountException Si la cuenta no existe o no se puede eliminar.
     */
    void deleteAccount(String username) throws AccountException;

    /**
     * Obtiene todas las cuentas registradas.
     * 
     * @return Lista de cuentas registradas.
     * @throws AccountException Si ocurre un error al obtener las cuentas.
     */
    List<Account> getAllAccounts() throws AccountException;
}
