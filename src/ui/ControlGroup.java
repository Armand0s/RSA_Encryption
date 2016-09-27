/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import model.Model;

/**
 *
 * @author Nicolas
 */
public class ControlGroup {
    private Model model;
    private ViewConnect view;
    
    public ControlGroup(Model model)
    {
        this.model = model;
        view = new ViewConnect(model);
        ControlButtonConnect controlButtonConnect = new ControlButtonConnect(model, view);
        view.display();
    }
}
