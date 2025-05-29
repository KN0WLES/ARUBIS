package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.*;
import unicorn.arubis.model.*;
import unicorn.arubis.util.*;

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


}