package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.*;
import unicorn.arubis.model.*;
import unicorn.arubis.util.*;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SubjectMenuController {
    private SubjectController Subjectcontroller;
    private ScheduleController scheduleController;
    private AccountController accountController;
    private final Account account;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SubjectMenuController(Account account) throws SubjectException, ScheduleException, AccountException{
        Subject prototype = new Subject();
        IFile<Subject> fileHandler = new FileHandler<>(prototype);

        this.account = account;
        Account accountPrototype = new Account();
        IFile<Account> accountFileHandler = new FileHandler<>(accountPrototype);
        this.accountController = new AccountController(accountFileHandler);
        try {
            this.Subjectcontroller = new SubjectController(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al inicializar SubjectController: " + e.getMessage());
            this.Subjectcontroller = null;
        }
    }
}

