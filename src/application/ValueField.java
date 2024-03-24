package application;

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

    public void onEdit() {
        setEditable(updatable);
    }
    
    public void onSave() throws Exception {
        if (!updatable) return;

        if (!getText().equals(datum.originalValue))  {
            datum.newValue = getText();
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.newValue);
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
        updateButtonGroup.values.add(this);
        return this;
    }

    public ValueField withLabel(String label) {
        this.label = label;
        return this;
    }
}