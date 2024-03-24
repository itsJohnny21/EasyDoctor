package application;

import java.util.ArrayList;
import java.util.function.Consumer;

import application.Database.Row.Allergy;
import application.Database.Row.Employee;
import application.Database.Row.Patient;
import application.Database.Row.Surgery;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.StringConverter;

public class UI {

    public static abstract class View extends GridPane {
        public String titleString;
        public GridPane title;
        public double width;
        public double rowHeight;
        public int rowCount;
        public int columnCount;
        public UpdateButtonGroup ubg;
        public ArrayList<Value> values;

        public View() {
            super();
            this.setAlignment(Pos.TOP_CENTER);

            this.rowCount = 0;
            this.columnCount = 1;
            this.values = new ArrayList<Value>();
            this.width = Screen.getPrimary().getVisualBounds().getWidth();
        }

        public View connectedTo(UpdateButtonGroup ubg) {
            this.ubg = ubg;
            return this;
        }

        public View withWidth(double width) {
            this.width = width;
            return this;
        }

        public View withRowHeight(double rowHeight) {
            this.rowHeight = rowHeight;
            return this;
        }

        public View withTitle(String titleString) {
            this.titleString = titleString;
            return this;
        }

        public View withValue(Value value) {
            values.add(value);
            return this;
        }

        public View withValues(Value... values) {
            for (Value value : values) {
                this.values.add(value);
            }
            return this;
        }

        public void buildTitle(String classCSS) {
            if (titleString != null) {
                title = new GridPane();
                title.setPrefHeight(rowHeight);
                title.setPrefWidth(width);
                title.getStyleClass().add(classCSS);
                
                Label titleLabel = new Label(titleString);
                titleLabel.setMinHeight(rowHeight);

                title.add(titleLabel, 0, 0);
                add(title, 0, rowCount++);
            }
        }

        public abstract View build();
        public abstract void buildRows();
    }

    public static class Form extends View {
        public String titleString;

        public Form() {
            super();
        }

        public Form withColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public void buildRows() {
            HBox row = new HBox();
            row.setMinHeight(rowHeight);
            row.setPrefWidth(width);
            row.getStyleClass().add("form-row");

            for (int i = 0; i < values.size(); i++) {
                row.setPrefWidth(width);

                Value field1 = values.get(i);

                Label label1 = new Label();
                label1.setPrefHeight(rowHeight);
                label1.setPrefWidth(width/ (3 * columnCount));
                label1.getStyleClass().add("form-label");

                row.getChildren().add(label1);

                if (field1 instanceof ValueField) {
                    label1.setText(((ValueField) field1).label);
                    ((ValueField) field1).setPrefHeight(rowHeight);
                    ((ValueField) field1).setPrefWidth(width / (columnCount * 2));
                    ((ValueField) field1).getStyleClass().add("form-field");

                    row.getChildren().add(((ValueField) field1));
                    
                } else if (field1 instanceof ValueOption) {
                    label1.setText(((ValueOption) field1).label);
                    ((ValueOption) field1).setPrefHeight(rowHeight);
                    ((ValueOption) field1).setPrefWidth(width / (columnCount * 2));
                    ((ValueOption) field1).getStyleClass().add("form-option");

                    row.getChildren().add(((ValueOption) field1));
                }

                if (i % columnCount == columnCount - 1) {
                    GridPane.setMargin(row, new Insets(0, 600 / (columnCount * 4), 0, 600 / (columnCount * 4)));
                    add(row, 0, rowCount++);
                    row = new HBox();
                    row.setMinHeight(rowHeight);
                    row.setPrefWidth(width);
                    row.getStyleClass().add("form-row");
                }

                if (ubg != null) {
                    field1.connectedTo(ubg);
                }
            }

            if (row.getChildren().size() > 0) {
                while (row.getChildren().size() != columnCount * 2) {
                    Label label = new Label("");
                    label.setPrefHeight(rowHeight);
                    label.setPrefWidth(width / (3 * columnCount));
                    row.getChildren().add(label);

                    Label label2 = new Label("");
                    label2.setPrefHeight(rowHeight);
                    label2.setPrefWidth(width / (2 * columnCount));
                    row.getChildren().add(label2);

                }
                GridPane.setMargin(row, new Insets(0, 600 / (columnCount * 4), 0, 600 / (columnCount * 4)));
                add(row, 0, rowCount++);
            }
        }

        public Form build() {
            buildTitle("form-title");
            buildRows();
            getStyleClass().add("form");
            return this;
        }
    }

    public static class Table extends View {
        public String[] headerStrings;
        public Consumer<HBox> rowAction;
        public GridPane header;

        public Table() {
            super();
        }

        public Table withRowAction(Consumer<HBox> rowAction) {
            this.rowAction = rowAction;
            return this;
        }

        public Table withHeader(String... headerStrings) {
            this.headerStrings = headerStrings;
            this.columnCount = headerStrings.length;
            return this;
        }

        public void buildHeader() {
            if (headerStrings.length > 0) {
                header = new GridPane();
                header.setMinHeight(rowHeight);
                header.setPrefWidth(width);
                header.setAlignment(Pos.CENTER);
    
                for (int i = 0; i < headerStrings.length; i++) {
                    Label label = new Label(headerStrings[i]);
                    label.setPrefWidth(width / headerStrings.length);
                    label.setAlignment(Pos.CENTER);
                    this.header.add(label, i, 0);
                }

                header.getStyleClass().add("table-header");
    
                add(header, 0, rowCount++);
            }
        }

        public void buildRows() {
            HBox row = new HBox();
            row.setMinHeight(rowHeight);
            row.setPrefWidth(width);
            row.getStyleClass().add("table-row");

            for (int i = 0; i < values.size(); i++) {
                Value value = values.get(i);

                if (value instanceof ValueField) {
                    ((ValueField) value).setMinHeight(rowHeight);
                    ((ValueField) value).setPrefWidth(width / columnCount);
                    ((ValueField) value).getStyleClass().add("table-field");

                    row.getChildren().add(((ValueField) value));

                    if (ubg != null) {
                        ((ValueField) value).connectedTo(ubg);
                    }

                } else if (value instanceof ValueLabel) {
                    ((ValueLabel) value).setMinHeight(rowHeight);
                    ((ValueLabel) value).setPrefWidth(width / columnCount);
                    ((ValueLabel) value).getStyleClass().add("table-value");
                    
                    row.getChildren().add(((ValueLabel) value));
                }


                if (i % columnCount == columnCount - 1) {

                    if (rowAction != null) {
                        rowAction.accept(row);
                    }

                    add(row, 0, rowCount++);
                    row = new HBox();
                    row.setMinHeight(rowHeight);
                    row.setPrefWidth(width);
                    row.getStyleClass().add("table-row");
                }
            }

            if (row.getChildren().size() > 0) {
                while (row.getChildren().size() != columnCount) {
                    Label label = new Label("");
                    label.setPrefWidth(width / columnCount);
                    row.getChildren().add(label);
                    row.getStyleClass().add("table-row");
                }

                if (rowAction != null) {
                    rowAction.accept(row);
                }

                add(row, 0, rowCount++);
            }

        }

        public Table build() {
            buildTitle("table-title");
            buildHeader();
            buildRows();
            return this;
        }
    }

    public static Table allergiesTableBaseFor(int userID) throws Exception {
        ArrayList<Allergy> allergies = Database.Row.Allergy.getAllFor(userID);
        Table allergiesTable = new Table();

        for (Allergy allergy : allergies) {
            allergiesTable.withValues(
                allergy.allergen.createValueLabel(),
                allergy.severity.createValueLabel(),
                allergy.commonSource.createValueLabel(),
                allergy.notes.createValueLabel()
            );
        }
        return allergiesTable;
    }

    public static Form contactInformationFormBaseFor(int userID) throws Exception {
        Patient patient = Database.Row.Patient.getFor(userID);
        Form contactInformationForm = new Form();

        contactInformationForm
            .withTitle("Contact Information")
            .withValues(
                patient.firstName.createValueField().withLabel("First Name: "),
                patient.lastName.createValueField().withLabel("Last Name: "),
                patient.phone.createValueField().withLabel("Phone Number: "),
                patient.email.createValueField().withLabel("Email: "),
                patient.phone.createValueField().withLabel("Phone: "),
                patient.address.createValueField().withLabel("Address: "),
                patient.preferredDoctorID.createValueOption().withLabel("Preferred Doctor: ")
                    .withConverter(new StringConverter<Datum>() {
                        @Override
                        public String toString(Datum datum) {
                            try {
                                Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(datum.originalValue));
                                datum.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
                                return datum.newValue;
                            } catch (Exception e) {
                                e.printStackTrace();
                                return "";
                            }
                        }
                        
                        @Override
                        public Datum fromString(String string) {
                            return null;
                        }
                    })
                    .withData(Database.Row.Employee.getAllDoctors()),
                patient.bloodType.createValueField().withLabel("Blood Type: "),
                patient.height.createValueField().withLabel("Height: "),
                patient.weight.createValueField().withLabel("Weight: "),
                patient.race.createValueField().withLabel("Race: "),
                patient.ethnicity.createValueField().withLabel("Ethnicity: "),
                patient.insuranceProvider.createValueField().withLabel("Insurance Provider: "),
                patient.insuranceID.createValueField().withLabel("Insurance ID: "),
                patient.emergencyContactName.createValueField().withLabel("Emergency Contact Name: "),
                patient.emergencyContactPhone.createValueField().withLabel("Emergency Phone: "),
                patient.motherFirstName.createValueField().withLabel("Mother Fist Name: "),
                patient.motherLastName.createValueField().withLabel("Mother Last Name: "),
                patient.fatherFirstName.createValueField().withLabel("Father First Name: "),
                patient.fatherLastName.createValueField().withLabel("Father Last Name: ")
            );

        return contactInformationForm;
    }

    public static Table surgeriesTableBaseFor(int userID) throws Exception {
        ArrayList<Surgery> surgeries = Database.Row.Surgery.getAllFor(userID);
        Table surgeriesTable = new Table();

        for (Surgery surgery : surgeries) {
            Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;

            surgeriesTable.withValues(
                doctor.userID.createValueField(),
                surgery.date.createValueField(),
                surgery.type.createValueField(),
                surgery.location.createValueField(),
                surgery.notes.createValueField()
            );
        }

        return surgeriesTable;
    }
}
