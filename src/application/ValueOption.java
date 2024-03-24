package application;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ValueOption extends ChoiceBox<Datum> implements Value {
    public Datum datum;
    public boolean updatable;
    public String label;
    public StringConverter<Datum> converter;
    
    public ValueOption(Datum datum) {
        super();
        this.datum = datum;
        updatable = Database.canUpdate(datum.parent.tableName, datum.columnName);
        setDisable(true);
        setValue(datum);
    }

    public ValueOption withData(ArrayList<Datum> data) {
        getItems().addAll(data);
        return this;
    }

    public ValueOption withConverter(StringConverter<Datum> converter) {
        this.converter = converter;
        setConverter(converter);
        return this;
    }

    public void onEdit() {
        setDisable(!updatable);
    }

    public void onSave() throws SQLException {
        if (getValue() != null && getValue().originalValue != datum.originalValue) {
            datum.originalValue = getValue().originalValue;
            System.out.println("new value!");
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.originalValue);
        }
        setDisable(true);
    }

    public ValueOption connectedTo(UpdateButtonGroup updateButtonGroup) {
        updateButtonGroup.values.add(this);
        return this;
    }

    public void onCancel() {
        setValue(datum);
        setDisable(true);
    }

    public void onError() {
        setValue(datum);
    }

    public String toString() {
        return datum.newValue;
    }

    public ValueOption withLabel(String label) {
        this.label = label;
        return this;
    }
}
