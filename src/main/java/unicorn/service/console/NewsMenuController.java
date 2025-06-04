package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class NewsMenuController {
    private final AccountController accountController;
    private final NewsController newsController;
    private final Account account;
    private final Scanner scanner;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public NewsMenuController(Account account) throws NewsException, AccountException{
        this.account = account;
        Account accountPrototype = new Account();
        IFile<Account> accountFileHandler = new FileHandler<>(accountPrototype);
        this.accountController = new AccountController(accountFileHandler);

        News newsPrototype = new News();
        IFile<News> newsFileHandler = new FileHandler<>(newsPrototype);
        this.newsController = new NewsController(newsFileHandler, account.getId());

        this.scanner = new Scanner(System.in);
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