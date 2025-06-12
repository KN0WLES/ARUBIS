package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.*;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class SubjectMenuController {
    private SubjectController Subjectcontroller;
    private ScheduleController scheduleController;
    private AccountController accountController;
    private Account account;
    private Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public SubjectMenuController(Account account) throws SubjectException, ScheduleException, AccountException, NewsException {
        Subject prototype = new Subject();
        IFile<Subject> fileHandler = new FileHandler<>(prototype);

        this.account = account;
        Account accountPrototype = new Account();
        Substitute substitutePrototype = new Substitute();
        IFile<Account> accountFileHandler = new FileHandler<>(new Account());
        IFile<Substitute> substituteFileHandler = new FileHandler<>(new Substitute());
        IFile<FaQ> faqFileHandler = new FileHandler<>(new FaQ());
        INews newsController = new NewsController(new FileHandler<>(new News()), account != null ? account.getId() : null);

        this.accountController = new AccountController(accountFileHandler, substituteFileHandler, newsController, faqFileHandler);
        try {
            this.Subjectcontroller = new SubjectController(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al inicializar SubjectController: " + e.getMessage());
            this.Subjectcontroller = null;
        }
    }

    public void showAdmMenu(){
        System.out.println("\nEn desarrollo");
    }
    public void showEstMenu(){
        System.out.println("\nEn desarrollo");
    }
    public void showPrfMenu(){
        System.out.println("\nEn desarrollo");
    }
}

