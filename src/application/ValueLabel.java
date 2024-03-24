package application;

import java.sql.SQLException;

import javafx.scene.control.Label;

public class ValueLabel extends Label implements Value {
    public Datum datum;
    
    public ValueLabel(Datum datum) {
        super(datum.newValue);
        this.datum = datum;
    }

    public void onEdit() {
    }

    public void onSave() throws SQLException {
    }

    public void onCancel() {
    }

    public Value connectedTo(UpdateButtonGroup updateButtonGroup) {
        return this;
    }

    public Value withConversion() {
        return this;
    }

    public void onError() {
    }
}