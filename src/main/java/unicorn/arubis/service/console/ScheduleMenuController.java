package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.*;
import unicorn.arubis.model.*;
import unicorn.arubis.util.*;

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
}

