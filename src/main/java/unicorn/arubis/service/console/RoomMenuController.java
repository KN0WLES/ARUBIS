package unicorn.arubis.service.console;

import unicorn.arubis.controller.*;
import unicorn.arubis.exceptions.*;
import unicorn.arubis.interfaces.*;
import unicorn.arubis.model.*;
import unicorn.arubis.util.*;

import java.util.List;
import java.util.Scanner;

public class RoomMenuController {
    private RoomController controller;
    private final Account account;
    private final Scanner scanner;

    public RoomMenuController(Account account) throws RoomException{
        Room prototype = new Room();
        this.account = account;
        IFile<Room> fileHandler = new FileHandler<>(prototype);

        try {
            this.controller = new RoomController(fileHandler);
        } catch (Exception e) {
            System.err.println("Error al inicializar RoomController: " + e.getMessage());
            this.controller = null;
        }

        this.scanner = new Scanner(System.in);
    }
}

