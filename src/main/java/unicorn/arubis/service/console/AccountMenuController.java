package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.*;
import unicorn.arubis.model.*;
import unicorn.arubis.util.*;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que representa el controlador de menú para gestión de cuentas.
 * Gestiona la interacción del usuario con el sistema de cuentas, permitiendo
 * registrar, actualizar, eliminar y consultar cuentas de usuario.
 * 
 * @description Funcionalidades principales:
 *                   - Registrar nuevas cuentas de usuario.
 *                   - Iniciar sesión con credenciales.
 *                   - Actualizar información de cuentas existentes.
 *                   - Eliminar cuentas de usuario.
 *                   - Gestionar privilegios de administrador.
 *                   - Consultar información de cuentas.
 *                   - Validar entradas y manejar excepciones.
 * 
 * @author KNOWLES
 * @version 1.0
 * @since 2025-05-28
 * @see AccountController
 * @see Account
 * @see IAccount
 */
public class AccountMenuController {
    private AccountController accountController;
    private final Account account;
    private final Scanner scanner;

    public AccountMenuController(Account account) throws AccountException {
        Account prototype = new Account();
        IFile<Account> fileHandler = new FileHandler<>(prototype);
        this.accountController = new AccountController(fileHandler);
        this.scanner = new Scanner(System.in);
        this.account = account;
    }



}