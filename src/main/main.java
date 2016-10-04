/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.Client.Client;
import model.Common.RSA;
import model.Server.Server;
import model.Model;
import model.Common.*;
import ui.ControlGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author p1408610
 */
public class main {

    public static Logger logger;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RSA rsa = new RSA(64);

        javax.swing.SwingUtilities.invokeLater( new Runnable() {
            public void run() {
                Model model = new Model();
                ControlGroup controler = new ControlGroup(model);
            }
        });

        try {
            logger = Logger.getLogger("MyLog");
            FileHandler fileHandler;

            // get date for logfile name
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH'h'mm'm'ss's'");
            //get current date time with Date()
            Date date = new Date();

            fileHandler = new FileHandler("log_"+dateFormat.format(date)+".log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scanner scan = new Scanner(System.in);
        System.out.println("client or server ?\n");
        String answer = scan.nextLine();
        switch (answer) {
            case "client":
                break;
            case "server":
                System.out.println("Server port ?\n");
                int port = scan.nextInt();
                Server server = new Server(port);
                break;
            default:
                logger.severe("Usage : \"client\" or \"server\"");
        }

        }

    }

