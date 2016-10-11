/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import javax.swing.*;

import controller.Control;
import model.Model;

import java.awt.*;

/**
 *
 * @author Nicolas
 */
public class View extends ViewAbstract {
    protected Model model;
    
    public JPanel panelConnect;
    public JLabel labelIp, labelName;
    public JButton buttonLogin, buttonQuit;
    public JTextField textFieldIp, textFieldName;

    public JTextArea textAreaChatLog;
    public JTextField textFieldMessageZone;
    public JPanel panelChat;
    
    public View(Model model){
        this.model = model;
        
        initAttributes();
        createView();
    }

    @Override
    public void initAttributes(){
        textFieldIp = new JTextField(16);
        textFieldName = new JTextField(16);
        
        labelIp = new JLabel("Adresse ip : ");
        labelName = new JLabel("Nom d'utilisateur : ");
        
        buttonLogin = new JButton("Connexion");
        buttonQuit = new JButton("Quitter");
        
        panelConnect = new JPanel();
        panelConnect.setLayout(new GridLayout(3, 2));
        panelConnect.add(labelIp);
        panelConnect.add(textFieldIp);
        panelConnect.add(labelName);
        panelConnect.add(textFieldName);
        panelConnect.add(buttonLogin);
        panelConnect.add(buttonQuit);

        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));
        textAreaChatLog = new JTextArea(40, 100);
        textAreaChatLog.setEnabled(false);
        textFieldMessageZone = new JTextField(100);

        panelChat.add(textAreaChatLog);
        panelChat.add(new JSeparator());
        panelChat.add(textFieldMessageZone);
    }

    @Override
    public void createView(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(panelConnect);
        panel.add(panelChat);
        showConnect();
        setContentPane(panel);
        setTitle("RSA Chat");
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void display(){
        setVisible(true);
    }

    @Override
    public void setControl(Control control){
        buttonLogin.addActionListener(control);
        buttonQuit.addActionListener(control);
        textFieldMessageZone.addActionListener(control);
    }

    public void resetChat(){

    }

    public void showConnect(){
        panelChat.setVisible(false);
        panelConnect.setVisible(true);
        pack();
    }

    public void showChat(){
        panelConnect.setVisible(false);
        panelChat.setVisible(true);
        pack();
    }

    public void clearTextBox(){
        addTextToLog(textFieldMessageZone.getText());
        textFieldMessageZone.setText("");
    }

    public void addTextToLog(String text){
        textAreaChatLog.setText(textAreaChatLog.getText() + "\n" + text);
    }
}
