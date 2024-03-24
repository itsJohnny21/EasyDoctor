package application;

import application.Database.Row;

public class Datum {
    public String originalValue;
    public String newValue;
    public String columnName;
    public Row parent;

    public Datum(Row parent, String originalValue, String columnName) {
        this.parent = parent;
        this.originalValue = originalValue;
        this.columnName = columnName;
        this.newValue = originalValue;
    }

    public Datum() {}

    public ValueField createValueField() {
        return new ValueField(this);
    }

    public ValueLabel createValueLabel() {
        return new ValueLabel(this);
    }

    public ValueOption createValueOption() {
        return new ValueOption(this);
    }
}
