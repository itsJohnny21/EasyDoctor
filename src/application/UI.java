package application;

import java.util.ArrayList;
import java.util.function.Consumer;

import application.Database.Table.Allergy;
import application.Database.Table.Employee;
import application.Database.Table.Patient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.util.StringConverter;

public class UI {

    public static class Form extends GridPane {
        public String titleString;
        public ArrayList<Value> fieldValues;
        public ArrayList<String> labelStrings;
        public UpdateButtonGroup ubg;
        public int columnCount;

        public GridPane title;

        public double width;
        public double rowHeight;
        public int rowCount;

        public Form() {
            super();
            this.setAlignment(Pos.TOP_CENTER);

            this.fieldValues = new ArrayList<Value>();
            this.labelStrings = new ArrayList<String>();
            this.width = Screen.getPrimary().getVisualBounds().getWidth();
            this.rowCount = 0;
            this.columnCount = 2;
        }

        public Form withColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public Form connectedTo(UpdateButtonGroup ubg) {
            this.ubg = ubg;
            return this;
        }

        public Form withWidth(double width) {
            this.width = width;
            return this;
        }

        public Form withRowHeight(double rowHeight) {
            this.rowHeight = rowHeight;
            return this;
        }

        public Form withTitle(String titleString) {
            this.titleString = titleString;
            return this;
        }

        public Form withField(String labelString, ValueField field) {
            fieldValues.add(field);
            labelStrings.add(labelString);
            return this;
        }

        public Form withOption(String labelString, ValueOption option) {
            fieldValues.add(option);
            labelStrings.add(labelString);
            return this;
        }

        public void buildTitle() {
            if (titleString != null) {
                title = new GridPane();
                title.setPrefHeight(rowHeight);
                title.setPrefWidth(width);
                title.setAlignment(Pos.CENTER);
                title.getStyleClass().add("form-title");
                
                Label titleLabel = new Label(titleString);

                title.add(titleLabel, 0, 0);
                add(title, 0, rowCount++);
            }
        }

        public void buildFields() {
            HBox row = new HBox();

            for (int i = 0; i < fieldValues.size(); i++) {
                row.setPrefWidth(width);
                row.setAlignment(Pos.CENTER);

                Value field1 = fieldValues.get(i);

                Label label1 = new Label(labelStrings.get(i));
                label1.setPrefHeight(rowHeight);
                label1.setPrefWidth(width/ (3 * columnCount));
                label1.setAlignment(Pos.CENTER_RIGHT);

                row.getChildren().add(label1);

                if (field1 instanceof ValueField) {
                    ((ValueField) field1).setPrefHeight(rowHeight);
                    ((ValueField) field1).setPrefWidth(width / (columnCount * 2));
                    ((ValueField) field1).setAlignment(Pos.CENTER_LEFT);
                    row.getChildren().add(((ValueField) field1));
                    
                } else if (field1 instanceof ValueOption) {
                    ((ValueOption) field1).setPrefHeight(rowHeight);
                    ((ValueOption) field1).setPrefWidth(width / (columnCount * 2));
                    row.getChildren().add(((ValueOption) field1));
                }

                if (i % columnCount == columnCount - 1) {
                    GridPane.setMargin(row, new Insets(0, 600 / (columnCount * 4), 0, 600 / (columnCount * 4)));
                    add(row, 0, rowCount++);
                    row = new HBox();
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
                    label.setAlignment(Pos.CENTER_RIGHT);
                    row.getChildren().add(label);

                    Label label2 = new Label("");
                    label2.setPrefHeight(rowHeight);
                    label2.setPrefWidth(width / (2 * columnCount));
                    label2.setAlignment(Pos.CENTER_RIGHT);
                    row.getChildren().add(label2);

                }
                GridPane.setMargin(row, new Insets(0, 600 / (columnCount * 4), 0, 600 / (columnCount * 4)));
                add(row, 0, rowCount++);
            }
        }

        public Form build() {
            buildTitle();
            buildFields();
            return this;
        }
    }

    public static class Table extends GridPane {
        public String titleString;
        public String[] headerStrings;
        public ArrayList<ValueLabel[]> rowLabels;
        public Consumer<GridPane> rowAction;

        public GridPane title;
        public GridPane header;
        
        public double width;
        public double rowHeight;
        public int rowCount;

        public Table() {
            super();
            this.setAlignment(Pos.TOP_CENTER);

            this.rowLabels = new ArrayList<ValueLabel[]>();
            this.width = Screen.getPrimary().getVisualBounds().getWidth();
            this.rowCount = 0;
        }

        public Table withRowAction(Consumer<GridPane> rowAction) {
            this.rowAction = rowAction;
            return this;
        }

        public Table withWidth(double width) {
            this.width = width;
            return this;
        }

        public Table withRowHeight(double rowHeight) {
            this.rowHeight = rowHeight;
            return this;
        }

        public Table withTitle(String titleString) {
            this.titleString = titleString;
            return this;
        }

        public Table withHeader(String... headerStrings) {
            this.headerStrings = headerStrings;
            return this;
        }

        public Table withRows(ValueLabel... row) {
            rowLabels.add(row);
            return this;
        }

        public void buildTitle() {
            if (titleString != null) {
                title = new GridPane();
                title.setPrefHeight(rowHeight);
                title.setPrefWidth(width);
                title.setAlignment(Pos.CENTER);
                title.getStyleClass().add("table-title");
                
                Label titleLabel = new Label(titleString);

                title.add(titleLabel, 0, 0);
                add(title, 0, rowCount++);
            }
        }

        public void buildHeader() {
            if (headerStrings.length > 0) {
                header = new GridPane();
                header.setPrefHeight(rowHeight);
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
            for (int i = 0; i < rowLabels.size(); i++) {
                GridPane row = new GridPane();
                row.setPrefHeight(rowHeight);
                row.setPrefWidth(width);
                row.setAlignment(Pos.CENTER);

                ValueLabel[] dataRow = rowLabels.get(i);

                for (int j = 0; j < dataRow.length; j++) {
                    ValueLabel value = dataRow[j];

                    value.setPrefHeight(rowHeight);
                    value.setPrefWidth(width / dataRow.length);
                    value.setAlignment(Pos.CENTER);

                    row.add(value, j, 0);
                }

                if (rowAction != null) {
                    rowAction.accept(row);
                }

                row.getStyleClass().add("table-row");

                add(row, 0, rowCount++);
            }
        }

        public Table build() {
            buildTitle();
            buildHeader();
            buildRows();
            return this;
        }
    }

    public static Table allergiesTableFor(int userID) throws Exception {
        ArrayList<Allergy> allergies = Database.Table.Allergy.getAllFor(userID);

        Table allergiesTable = new Table();

        for (Allergy allergy : allergies) {
            
            allergiesTable.withRows(
                allergy.allergen.createValueLabel(),
                allergy.severity.createValueLabel(),
                allergy.commonSource.createValueLabel(),
                allergy.notes.createValueLabel()
            );
        }

        return allergiesTable;
    }

    public static Form contactInformationFormFor(int userID) throws Exception {
        Patient patient = Database.Table.Patient.getFor(userID);
        Employee doctor = Database.Table.Employee.getFor(Integer.parseInt(patient.preferredDoctorID.originalValue));
        patient.preferredDoctorID.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;

        Form contactInformationForm = new Form();

        contactInformationForm.withTitle("Contact Information")
            .withField("First Name: ", patient.firstName.createValueField())
            .withField("Last Name: ", patient.lastName.createValueField())
            .withField("Phone Number: ", patient.phone.createValueField())
            .withField("Email: ", patient.email.createValueField())
            .withField("Phone: ", patient.phone.createValueField())
            .withField("Address: ", patient.address.createValueField())
            .withOption("Preferred Doctor: ", patient.preferredDoctorID.createValueOption()
                .withConverter(new StringConverter<Datum>() {
                    @Override
                    public String toString(Datum datum) {
                        try {
                            Employee doctor = Database.Table.Employee.getFor(Integer.parseInt(datum.originalValue));
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
                .withData(Database.Table.Employee.getAllDoctors())
            )
            .withField("Blood Type: ", patient.bloodType.createValueField())
            .withField("Height: ", patient.height.createValueField())
            .withField("Weight: ", patient.weight.createValueField())
            .withField("Race: ", patient.race.createValueField())
            .withField("Ethnicity: ", patient.ethnicity.createValueField())
            .withField("Insurance Provider: ", patient.insuranceProvider.createValueField())
            .withField("Insurance ID: ", patient.insuranceID.createValueField())
            .withField("Emergency Contact Name: ", patient.emergencyContactName.createValueField())
            .withField("Emergency Phone: ", patient.emergencyContactPhone.createValueField())
            .withField("Mother Fist Name: ", patient.motherFirstName.createValueField())
            .withField("Mother Last Name: ", patient.motherLastName.createValueField())
            .withField("Father First Name: ", patient.fatherFirstName.createValueField())
            .withField("Father Last Name: ", patient.fatherLastName.createValueField());

        return contactInformationForm;
    }
}
