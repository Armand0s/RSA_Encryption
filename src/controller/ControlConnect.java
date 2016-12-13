/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;

import model.Model;
import ui.View;

/**
 *
 * @author Nicolas
 */
public class ControlConnect extends Control {
    private Model model;
    private View view;
    
    public ControlConnect(Model model, View view)
    {
        this.model = model;
        this.view = view;
        view.setControl(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == view.buttonLogin){
            if(!model.IsConnecting()){
                //Connect to server
                model.connect(view.textFieldIp.getText());
                view.buttonLogin.setText("Annuler");
                view.showChat();
            } else {
                //Cancel connection
                view.buttonLogin.setText("Connexion");
            }
        }

        if(e.getSource() == view.textFieldMessageZone){
            //Send message to server and clear text field

            view.clearTextBox();
        }
    }
    
}
