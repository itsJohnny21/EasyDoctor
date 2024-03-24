package application;

import javafx.scene.control.TextField;

public class ValueField extends TextField implements Value {
    public Datum datum;
    public boolean updatable;
    
    public ValueField(Datum datum) {
        super(datum.newValue);
        this.datum = datum;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
        setEditable(false);
    }

    public void onEdit() {
        setEditable(updatable);
    }
    
    public void onSave() throws Exception {
        if (!getText().equals(datum.originalValue))  {
            datum.newValue = getText();
            System.out.println("new value: " + datum.newValue + " original value: " + datum.originalValue + " column name: " + datum.columnName + " row id: " + datum.parent.rowID + " table name: " + datum.parent.tableName);
            // Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.newValue);
            datum.originalValue = datum.newValue;
            setText(datum.newValue);
        }
        setEditable(false);
    }
    
    public void onCancel() {
        setEditable(false);
        undo();
    }

    public ValueField connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.fields.add(this);
        return this;
    }

    public ValueField withConversion() {
        return this;
    }
}