package edu.asu.easydoctor.controllers;

import java.util.HashMap;

public abstract class DialogController extends Controller {

    public Controller parentController;
    public HashMap<String, String> result;

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }

    public abstract String getTitle();
    public abstract HashMap<String, String> getResult();
}
