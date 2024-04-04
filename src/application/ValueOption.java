package application;

import java.sql.SQLException;
import java.util.ArrayList;

import javafx.css.PseudoClass;
import javafx.scene.control.ChoiceBox;
import javafx.util.StringConverter;

public class ValueOption extends ChoiceBox<Datum> implements Connectable {
    public Datum datum;
    public boolean updatable;
    public String label;

    public ValueOption(ArrayList<Datum> data, String label) {
        super();
        this.label = label;
        getItems().addAll(data);
        datum = data.get(0);
        setValue(datum);

        updatable = datum.parent == null ? false : Database.canUpdate(datum.parent.tableName, datum.columnName);
        
        setConverter(new StringConverter<Datum>() {
            @Override
            public String toString(Datum datum) {
                return datum.originalValue;
            }

            @Override
            public Datum fromString(String string) {
                return null;
            }
        });
    }

    
    public ValueOption(Datum option, String label) {
        super();
        this.label = label;
        datum = option;
        setValue(datum);
        
        updatable = datum.parent == null ? false : Database.canUpdate(datum.parent.tableName, datum.columnName);

        setConverter(new StringConverter<Datum>() {
            @Override
            public String toString(Datum datum) {
                return datum.originalValue;
            }

            @Override
            public Datum fromString(String string) {
                return null;
            }
        });
    }

    public void setOption(Datum option) {
        datum = option;
        setValue(option);
    }

    public ValueOption withData(ArrayList<Datum> data) {
        getItems().addAll(data);
        return this;
    }

    public void onEdit() {
        setDisable(!updatable);
    }

    public void onSave() throws SQLException {
        if (!updatable) return;

        if (getValue() != null && getValue().originalValue != datum.originalValue) {
            System.out.printf("new value! %s -> %s\n", datum.originalValue, this);
            Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, getValue().originalValue);
            datum = getValue();
        }
        setDisable(true);
    }

    public void onCancel() {
        setValue(datum);
        setDisable(true);
    }

    public void onError() {
        setValue(datum);
        PseudoClass errorClass = PseudoClass.getPseudoClass("error");
        pseudoClassStateChanged(errorClass, true);
    }

    public void initialize() {
        setDisable(true);
    }

    public String toString() {
        return getValue().originalValue;
    }
}
