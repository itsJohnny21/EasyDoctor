package application;

import java.util.ArrayList;

import application.Database.Row;
import application.Database.Row.Employee;

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

    public static ArrayList<Datum> getOptionsForDoctors(Datum doctorID) throws Exception {
        ArrayList<Datum> options = new ArrayList<>();
        ArrayList<Employee> doctors = Database.Row.Employee.getAllDoctors();

        for (Employee doctor : doctors) {
            options.add(new Datum(doctorID.parent, doctor.userID.originalValue, doctor.userID.columnName));
        }

        return options;
    }

    public ValueField createValueField(String labelString) {
        return new ValueField(this, labelString);
    }

    public ValueLabel createValueLabel() {
        return new ValueLabel(this);
    }

    public ValueOption createValueOption(String labelString) {
        return new ValueOption(this, labelString);
    }

    public static Datum createParentless(String originalValue, String columnName) {
        return new Datum(null, originalValue, columnName);
    }
}
