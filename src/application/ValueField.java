package application;

import java.sql.SQLException;

import javafx.css.PseudoClass;
import javafx.scene.control.TextField;

public class ValueField extends TextField implements Value {
    public Datum datum;
    public boolean updatable;
    public String label;
    
    public ValueField(Datum datum) {
        super(datum.newValue);
        this.datum = datum;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
        setEditable(false);
    }

    public ValueField() {
        super();
    }

    public void onEdit() {
        setEditable(updatable);
    }
    
    public void onSave() throws SQLException {
        if (!updatable) return;

        if (!getText().equals(datum.originalValue)) {
            System.out.println("new value!: " + getText());
            datum.newValue = getText();
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.newValue);
            datum.originalValue = datum.newValue;
            setText(datum.newValue);
        }
        setEditable(false);
        setStyle("-fx-border-color: transparent;");
    }
    
    public void onCancel() {
        setEditable(false);
        undo();
        setStyle("-fx-border-color: transparent;");
    }

    public void onError() {
        undo();
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        pseudoClassStateChanged(errorClass, true); //! not working
        setStyle("-fx-border-color: red;");
        requestFocus();
    }

    public ValueField connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.values.add(this);
        return this;
    }

    public ValueField withLabel(String label) {
        this.label = label;
        return this;
    }
}