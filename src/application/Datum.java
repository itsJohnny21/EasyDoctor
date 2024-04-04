package application;

import java.util.ArrayList;

import application.DataRow.Employee;
import javafx.util.StringConverter;

public class Datum {
    public String originalValue;
    public String displayValue;
    public String columnName;
    public DataRow parent;

    public Datum(DataRow parent, String originalValue, String columnName) {
        this.parent = parent;
        this.originalValue = originalValue;
        this.columnName = columnName;
        this.displayValue = originalValue;
    }

    public String toPerttyColumnName() {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1 $2";
        String result = columnName.replaceAll(regex, replacement);
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    public Datum() {}

    public static ValueOption createValueOptionForDoctors(Datum doctorID, String label) throws Exception {
        ArrayList<Datum> options = new ArrayList<>();
        ArrayList<Employee> doctors = DataRow.Employee.getAllDoctors();

        String columnName = null;
        DataRow parent = null;

        if (doctorID == null) {
            columnName = "preferredDoctorID";
            parent = null;
        } else {
            columnName = doctorID.columnName;
            parent = doctorID.parent;
        }

        for (Employee doctor : doctors) {
            options.add(new Datum(parent, doctor.userID.originalValue, columnName));
        }

        ValueOption option = new ValueOption(options, label);

        option.setConverter(new StringConverter<Datum>() {
            @Override
            public String toString(Datum datum) {
                try {
                    Employee doctor = DataRow.Employee.getFor(Integer.parseInt(datum.originalValue));
                    return doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
                } catch (Exception e) {
                    return "";
                }
            }

            @Override
            public Datum fromString(String string) {
                return null;
            }
        });

        if (doctorID != null) {
            option.setOption(doctorID);
        }

        return option;
    }

    public static <E extends Enum<E>> ValueOption createValueOptionFromEnum(Class<E> enumClass, Datum datum, String label) {
        ArrayList<Datum> options = new ArrayList<Datum>();

        String columnName = null;
        DataRow parent = null;

        if (datum == null) {
            columnName = enumClass.getSimpleName();
            columnName = Character.toLowerCase(columnName.charAt(0)) + columnName.substring(1);
            parent = null;
        } else {
            columnName = datum.columnName;
            parent = datum.parent;
        }

        for (E enumConstant : enumClass.getEnumConstants()) {
            Datum option = new Datum(parent, enumConstant.toString(), columnName);
            options.add(option);
        }

        ValueOption option = new ValueOption(options, label);

        if (datum == null) {
            option.setOption(options.get(0));
        } else {
            option.setOption(datum);
        }

        return option;
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

    public String convertValue() {
        return displayValue;
    }
}
