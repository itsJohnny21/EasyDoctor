package edu.asu.easydoctor.controllers;

public abstract class DialogController extends Controller {

    public Controller parentController;

    public void setParentController(Controller parentController) {
        this.parentController = parentController;
    }
}
