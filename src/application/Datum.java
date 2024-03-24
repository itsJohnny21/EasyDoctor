package application;

import application.Database.Table;

public class Datum {
    public String originalValue;
    public String newValue;
    public String columnName;
    public Table parent;

    public Datum(Table parent, String originalValue, String columnName) {
        this.parent = parent;
        this.originalValue = originalValue;
        this.columnName = columnName;
        this.newValue = originalValue;
    }

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
