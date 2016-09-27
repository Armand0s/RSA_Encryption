/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import model.Model;

/**
 *
 * @author Nicolas
 */
public class ControlButtonConnect extends ControlButton{
    private Model model;
    private ViewConnect viewConnect;
    
    public ControlButtonConnect(Model model, ViewConnect viewConnect)
    {
        this.model = model;
        this.viewConnect = viewConnect;
        viewConnect.setControlButton(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == viewConnect.buttonLogin){
            if(!model.IsConnecting()){
                model.connect(viewConnect.textFieldIp.getText());
                viewConnect.buttonLogin.setVisible(false);
                viewConnect.buttonCancel.setVisible(true);
            }
        }
    }
    
}
