package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.*;

import java.util.Scanner;

public class ScheduleMenuController {
    private ScheduleController controller;
    private final Account account;
    private final Scanner scanner;

    public ScheduleMenuController(Account account) {
        Schedule prototype = new Schedule();
        this.account = account;
        IFile<Schedule> fileHandler = new FileHandler<>(prototype);

        try {
            this.controller = new ScheduleController(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al inicializar ScheduleController: " + e.getMessage());
            this.controller = null;
        }

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

