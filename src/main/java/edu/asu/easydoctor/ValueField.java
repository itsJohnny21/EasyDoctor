package edu.asu.easydoctor;

import java.sql.SQLException;

import javafx.css.PseudoClass;
import javafx.scene.control.TextField;

public class ValueField extends TextField implements Connectable {
    public Datum datum;
    public boolean updatable;
    public String label;
    
    public ValueField(Datum datum, String label) {
        super(datum.displayValue);
        this.datum = datum;
        this.label = label;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
    }

    public ValueField(Datum datum) {
        super(datum.displayValue);
        this.datum = datum;
        this.label = "";
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
    }

    public ValueField(String label) {
        super(label);
        this.label = label;
    }

    public void onEdit() {
        setEditable(updatable);
    }
    
    public void onSave() throws SQLException {
        if (!updatable) return;

        if (!getText().equals(datum.displayValue)) {
            System.out.printf("new value! %s -> %s\n", datum.displayValue, this);
            datum.displayValue = getText();
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.convertValue());
            datum.originalValue = datum.displayValue;
            setText(datum.displayValue);
        }
        setEditable(false);
        setStyle("-fx-border-color: transparent;");
    }
    
    public void onCancel() {
        setEditable(false);
        setText(datum.originalValue);
        setStyle("-fx-border-color: transparent;");
    }

    public void onError() {
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        setText(datum.originalValue);
        datum.displayValue = datum.originalValue;
        pseudoClassStateChanged(errorClass, true);
        requestFocus();
    }

    public void initialize() {
        setEditable(false);
    }

    public String toString() {
        return getText();
    }
}