/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import model.Model;
import ui.View;

/**
 *
 * @author Nicolas
 */
public class ControlGroup {
    private Model model;
    private View view;
    
    public ControlGroup(Model model)
    {
        this.model = model;
        view = new View(model);
        Control control = new ControlConnect(model, view);
        view.display();
    }
}
