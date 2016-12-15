/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.Common.RSA;
import model.Server.Server;
import model.Model;
import controller.ControlGroup;

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
        if(args.length > 0)
        {
            if (args[0].equals("client"))
            {
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        Model model = new Model();
                        ControlGroup controler = new ControlGroup(model);
                        if(!controler.init(args[0]))
                            return;
                    }
                });
            }
            else if (args[0].equals("server"))
            {
                javax.swing.SwingUtilities.invokeLater( new Runnable() {
                    public void run() {
                        Model model = new Model();
                        ControlGroup controler = new ControlGroup(model);
                        if(!controler.init(args[0]))
                            return;
                    }
                });
            } else {
                System.out.println("usage ./");
            }
        }
        }

    }

