/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import model.RSA;

/**
 *
 * @author p1408610
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        RSA rsa = new RSA(1024);
        System.out.println(rsa.toString());
        
    }
    
}
