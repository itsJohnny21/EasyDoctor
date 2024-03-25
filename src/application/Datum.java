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

    public String toPerttyColumnName() {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1 $2";
        String result = columnName.replaceAll(regex, replacement);
        return result.substring(0, 1).toUpperCase() + result.substring(1);
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

    public static Datum createParentless(String originalValue, String columnName) {
        return new Datum(null, originalValue, columnName);
    }
}
