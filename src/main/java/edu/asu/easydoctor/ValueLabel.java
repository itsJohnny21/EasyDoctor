package edu.asu.easydoctor;

import java.sql.SQLException;

import javafx.scene.control.Label;

public class ValueLabel extends Label implements Connectable {
    public Datum datum;
    
    public ValueLabel(Datum datum) {
        super(datum.displayValue);
        this.datum = datum;
    }

    public void onEdit() {}

    public void onSave() throws SQLException {}

    public void onCancel() {}

    public Connectable connectTo(UpdateButtonGroup updateButtonGroup) {
        return this;
    }

    public void onError() {}

    public void initialize() {}
}