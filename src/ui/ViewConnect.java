/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Model;

/**
 *
 * @author Nicolas
 */
public class ViewConnect extends View {
    protected Model model;
    
    protected JPanel panelConnect;
    protected JLabel labelIp, labelName;
    protected JButton buttonLogin, buttonCancel, buttonQuit;
    protected JTextField textFieldIp, textFieldName;
    
    public ViewConnect(Model model){
        this.model = model;
        
        initAttributes();
        createView();
    }
    
    public void initAttributes(){
        textFieldIp = new JTextField(16);
        textFieldName = new JTextField(16);
        
        labelIp = new JLabel("Adresse ip : ");
        labelName = new JLabel("Nom d'utilisateur : ");
        
        buttonLogin = new JButton("Connexion");
        buttonCancel = new JButton("Annuler");
        buttonCancel.setVisible(false);
        buttonQuit = new JButton("Quitter");
        
        panelConnect = new JPanel();
        panelConnect.setLayout(new BoxLayout(panelConnect, BoxLayout.Y_AXIS));
        panelConnect.add(labelIp);
        panelConnect.add(textFieldIp);
        panelConnect.add(labelName);
        panelConnect.add(textFieldName);
        panelConnect.add(buttonLogin);
        panelConnect.add(buttonCancel);
        panelConnect.add(buttonQuit);
    }
    
    public void createView(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panelConnect);
        setContentPane(panel);
        setTitle("Connecting to chat");
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void display(){
        setVisible(true);
    }
}
