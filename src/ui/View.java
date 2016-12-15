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
import java.awt.event.KeyEvent;

/**
 *
 * @author Nicolas
 */
public class View extends ViewAbstract {
    public Model model;
    
    public JPanel panelConnect;
    public JLabel labelIp, labelName, labelPort;
    public JButton buttonLogin, buttonQuit;
    public JTextField textFieldIp, textFieldName, textFieldPort;

    public JTextArea textAreaChatLog;
    public JTextField textFieldMessageZone;
    public JPanel panelChat;
    public JScrollPane jScrollPane;
    
    public View(Model model){
        this.model = model;
        
        initAttributes();
        createView();
    }

    @Override
    public void initAttributes(){
        textFieldIp = new JTextField(16);
        textFieldPort = new JTextField(16);
        textFieldName = new JTextField(16);
        textFieldName.setTransferHandler(null);
        textFieldName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                if(textFieldName.getText().length()>=16&&!(evt.getKeyChar()== KeyEvent.VK_DELETE||evt.getKeyChar()==KeyEvent.VK_BACK_SPACE)) {
                    getToolkit().beep();
                    evt.consume();
                }
            }
        });
        
        labelIp = new JLabel("Adresse ip : ");
        labelPort = new JLabel("Port : ");
        labelName = new JLabel("Nom d'utilisateur : ");
        
        buttonLogin = new JButton("Connexion");
        buttonQuit = new JButton("Quitter");
        
        panelConnect = new JPanel();
        panelConnect.setLayout(new GridLayout(4, 2));
        panelConnect.add(labelIp);
        panelConnect.add(textFieldIp);
        panelConnect.add(labelPort);
        panelConnect.add(textFieldPort);
        panelConnect.add(labelName);
        panelConnect.add(textFieldName);
        panelConnect.add(buttonLogin);
        panelConnect.add(buttonQuit);

        panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.Y_AXIS));

        textAreaChatLog = new JTextArea(40, 100);
        textAreaChatLog.setEditable(false);
        textAreaChatLog.setLineWrap(true);
        jScrollPane = new JScrollPane(textAreaChatLog);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.setBounds(0, 0, 300, 50);
        textFieldMessageZone = new JTextField(100);

        panelChat.add(jScrollPane);
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
        setLocationRelativeTo(null);
    }

    public void showChat(){
        panelConnect.setVisible(false);
        panelChat.setVisible(true);
        pack();
        setLocationRelativeTo(null);
    }

    public void clearTextBox(){
        textFieldMessageZone.setText("");
    }

    public void addTextToLog(String text){
        textAreaChatLog.setText(textAreaChatLog.getText() + "\n" + text);
        JScrollBar vertical = jScrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
        textAreaChatLog.setCaretPosition(textAreaChatLog.getDocument().getLength());
    }
}
