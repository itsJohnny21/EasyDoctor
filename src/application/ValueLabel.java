package application;

import javafx.scene.control.Label;

public class ValueLabel extends Label {
    public Datum datum;
    
    public ValueLabel(Datum datum) {
        super(datum.newValue);
        this.datum = datum;
    }
}