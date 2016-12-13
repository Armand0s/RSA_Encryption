/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Client.Client;
import model.Model;
import model.Server.Server;
import ui.View;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Nicolas
 */
public class ControlGroup {
    private Model model;
    private View view;
    
    public ControlGroup(Model model)
    {
        this.model = model;
        view = new View(model);
        Control control = new ControlConnect(model, view);
        view.display();
    }


    private boolean initLogger() {
        try {
            main.main.logger = Logger.getLogger("MyLog");
            FileHandler fileHandler;

            // get date for logfile name
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH'h'mm'm'ss's'");
            //get current date time with Date()
            Date date = new Date();

            fileHandler = new FileHandler("log_"+dateFormat.format(date)+".log");
            main.main.logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void askClientOrServer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("client or server ?\n");
        String answer = scan.nextLine();
        switch (answer) {
            case "c":
            case "client":
                createClient();
                break;
            case "s":
            case "server":
                createServer();
                break;
            default:
                main.main.logger.severe("Usage : \"client\" or \"server\"");
        }
    }

    private void createClient() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Server IP ?\n");
        //String serverIP = scan.nextLine();
        String serverIP = "0.0.0.0";
        System.out.println("Server port ?\n");
        //int serverPort = scan.nextInt();
        int serverPort = 4444;
        //scan.nextLine();
        System.out.println("Pseudo ?\n");
        //String pseudo = scan.nextLine();
        String pseudo = "ss";

        Client client = new Client(serverIP,serverPort,pseudo, view);

    }

    private void createServer() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Server port ?\n");
        //int port = scan.nextInt();
        int port = 4444;

        Server server = new Server(port);
    }

    public boolean init() {

        if(initLogger()) {
            askClientOrServer();
        } else {
            return false;
        }
        return true;
    }
}
