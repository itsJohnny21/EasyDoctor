package application;

import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ValueOption extends ChoiceBox<Datum> implements Value {
    public Datum datum;
    public boolean updatable;
    public String label;
    
    public ValueOption(Datum datum) {
        super();
        this.datum = datum;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
        updatable = true;
        setDisable(true);
        setValue(datum);
    }

    public ValueOption withData(ArrayList<Datum> data) {
        for (Datum datum : data) {
            if (!datum.originalValue.equals(this.datum.originalValue)) {
                getItems().add(datum);
            }
        }
        return this;
    }

    public ValueOption withConverter(StringConverter<Datum> converter) {
        setConverter(converter);
        return this;
    }

    public void onEdit() {
        setDisable(!updatable);
    }

    public void onSave() throws Exception {
        if (getValue() != null && getValue().originalValue != datum.originalValue) {
            datum.originalValue = getValue().originalValue;
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.originalValue);
        }
        setDisable(true);
    }

    public ValueOption connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.values.add(this);
        return this;
    }

    public void onCancel() {
        setDisable(true);
    }

    public String toString() {
        return datum.newValue;
    }

    public ValueOption withLabel(String label) {
        this.label = label;
        return this;
    }
}
