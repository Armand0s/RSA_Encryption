/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.RSA;
import model.Server;

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
        RSA rsa = new RSA(1024);
        System.out.println(rsa.toString());

        try {
            logger = Logger.getLogger("MyLog");
            FileHandler fileHandler;

            // get date for logfile name
            DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
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
        System.out.println("Client ou server ?\n");
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
                System.err.println("Usage : \"cleint\" or \"server\"");
        }


    }
    
}
