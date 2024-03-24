package application;

import javafx.scene.control.TextField;

public class ValueField extends TextField {
    public Datum datum;
    public boolean updatable;
    
    public ValueField(Datum datum) {
        super(datum.newValue);
        this.datum = datum;
        updatable = Database.isUpdatable(datum.parent.tableName, datum.columnName);
        setEditable(false);
    }

    public void onEdit() {
        setEditable(updatable);
    }
    
    public void onSave() throws Exception {
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
        updateButtonGroup.fields.add(this);
        return this;
    }
}