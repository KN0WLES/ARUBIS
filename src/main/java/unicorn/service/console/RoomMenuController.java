package unicorn.service.console;

import unicorn.controller.*;
import unicorn.exceptions.*;
import unicorn.interfaces.*;
import unicorn.model.*;
import unicorn.util.*;

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

