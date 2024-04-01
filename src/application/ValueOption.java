package application;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ValueOption extends ChoiceBox<Datum> implements Connectable {
    public Datum datum;
    public boolean updatable;
    public String label;
    public StringConverter<Datum> converter;

    public ValueOption(ArrayList<Datum> data, String label) {
        super();
        this.label = label;
        getItems().addAll(data);
        datum = data.get(0);
        setValue(datum);

        updatable = datum.parent == null ? false : Database.canUpdate(datum.parent.tableName, datum.columnName);
    }
    
    public ValueOption(Datum option, String label) {
        super();
        this.label = label;
        datum = option;
        setValue(datum);

        updatable = datum.parent == null ? false : Database.canUpdate(datum.parent.tableName, datum.columnName);
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
        if (!updatable) return;

        if (getValue() != null && getValue().originalValue != datum.originalValue) {
            datum.originalValue = getValue().originalValue;
            System.out.println("new value!");
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.originalValue);
        }
        setDisable(true);
    }

    public void onCancel() {
        setValue(datum);
        setDisable(true);
    }

    public void onError() {
        setValue(datum);
    }

    public void initialize() {
        setDisable(true);
    }

    public String toString() {
        return label + " " + getValue().originalValue;
    }
}
