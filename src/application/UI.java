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
        public HBox titleBox;
        public double width;
        public double rowHeight;
        public int rowCount;
        public int columnCount;
        public UpdateButtonGroup ubg;
        public ArrayList<Value> values;

        public View() {
            super();

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

        public void buildTitle() {
            if (titleString != null) {
                titleBox = new HBox();
                titleBox.setPrefHeight(rowHeight);
                titleBox.setPrefWidth(width);
                
                Label titleLabel = new Label(titleString);
                titleLabel.setMinHeight(rowHeight);
                
                titleBox.getChildren().add(titleLabel);
                add(titleBox, 0, rowCount++);
                
                if (this instanceof SelectableTable || this instanceof EditableTable) {
                    titleBox.getStyleClass().add("table-title");
                }
                
                if (this instanceof InformationForm) {
                    titleBox.getStyleClass().add("form-title");
                }
            }
        }

        public abstract View build();
        public abstract void buildRows();
    }

    public static class InformationForm extends View {

        public InformationForm() {
            super();
        }

        public InformationForm withColumnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public void buildRows() {
            HBox row = new HBox();
            row.setPadding(new Insets(0, width / (columnCount * 4), 0, width / (columnCount * 16)));
            row.getStyleClass().add("form-row");

            for (int i = 0; i < values.size(); i++) {
                Value value = values.get(i);

                if (ubg != null) {
                    value.connectedTo(ubg);
                }

                Label valueLabel = new Label();
                valueLabel.setPrefHeight(rowHeight);
                valueLabel.setPrefWidth(width/ (2 * columnCount));
                valueLabel.getStyleClass().add("form-label");
                row.getChildren().add(valueLabel);

                if (value instanceof ValueField) {
                    valueLabel.setText(((ValueField) value).label);
                    ((ValueField) value).setPrefHeight(rowHeight);
                    ((ValueField) value).setPrefWidth(width / (columnCount * 2));
                    ((ValueField) value).getStyleClass().add("form-field");
                    row.getChildren().add(((ValueField) value));
                    
                } else if (value instanceof ValueOption) {
                    valueLabel.setText(((ValueOption) value).label);
                    ((ValueOption) value).setPrefHeight(rowHeight);
                    ((ValueOption) value).setPrefWidth(width / (columnCount * 2));
                    ((ValueOption) value).getStyleClass().add("form-option");

                    if (((ValueOption) value).converter == null) {
                        ((ValueOption) value).setConverter(new StringConverter<Datum>() {
                            public String toString(Datum datum) {
                                return datum.originalValue;
                            }
                            
                            public Datum fromString(String string) {
                                return null;
                            }
                            
                        });
                    }
                    
                    row.getChildren().add(((ValueOption) value));
                }

                if (i % columnCount == columnCount - 1) {
                    add(row, 0, rowCount++);
                    row = new HBox();
                    row.setPadding(new Insets(0, width / (columnCount * 4), 0, width / (columnCount * 16)));
                    row.getStyleClass().add("form-row");
                }
            }

            if (row.getChildren().size() > 0) {
                while (row.getChildren().size() != columnCount * 2) {
                    Label label = new Label("");
                    label.setPrefHeight(rowHeight);
                    label.setPrefWidth(width / (2 * columnCount));
                    row.getChildren().add(label);

                    Label label2 = new Label("");
                    label2.setPrefHeight(rowHeight);
                    label2.setPrefWidth(width / (2 * columnCount));
                    row.getChildren().add(label2);
                }
                add(row, 0, rowCount++);
            }
        }

        public InformationForm build() {
            buildTitle();
            buildRows();
            getStyleClass().add("form");
            return this;
        }
    }

    public static class SelectableTable extends View {
        public String[] headerStrings;
        public Consumer<HBox> rowAction;
        public GridPane header;

        public SelectableTable() {
            super();
        }

        public SelectableTable withRowAction(Consumer<HBox> rowAction) {
            this.rowAction = rowAction;
            return this;
        }

        public SelectableTable withCustomHeader(String... headerStrings) {
            this.headerStrings = headerStrings;
            this.columnCount = headerStrings.length;
            return this;
        }

        public void buildHeader() {
            if (headerStrings.length > 0) {
                columnCount = headerStrings.length;
                
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

                ((ValueLabel) value).setMinHeight(rowHeight);
                ((ValueLabel) value).setPrefWidth(width / columnCount);
                ((ValueLabel) value).getStyleClass().add("table-value");
                
                row.getChildren().add(((ValueLabel) value));

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

        public SelectableTable build() {
            buildTitle();
            buildHeader();
            buildRows();
            getStyleClass().add("table");
            return this;
        }
    }

    public static class EditableTable extends View {
        public String[] headerStrings;
        public HBox header;
        public boolean deletable;

        public EditableTable() {
            super();
        }

        public EditableTable isDeletable(boolean deletable) {
            this.deletable = deletable;
            return this;
        }

        public EditableTable withCustomHeader(String... headerStrings) {
            this.headerStrings = headerStrings;
            this.columnCount = headerStrings.length;
            return this;
        }

        public void buildHeader() {
            if (headerStrings.length > 0) {
                columnCount = headerStrings.length;

                header = new HBox();
                header.setMinHeight(rowHeight);
                header.setPrefWidth(width);
    
                for (int i = 0; i < headerStrings.length; i++) {
                    Label label = new Label(headerStrings[i]);
                    label.setPrefWidth(width / headerStrings.length);
                    header.getChildren().add(label);
                }

                header.getStyleClass().add("table-header");
                add(header, 0, rowCount++);

            }
        }

        public void buildRows() {
            ValueRow row = new ValueRow(this);
            row.setMinHeight(rowHeight);
            row.setPrefWidth(width);
            row.getStyleClass().add("table-row");

            for (int i = 0; i < values.size(); i++) {
                Value value = values.get(i);

                ((ValueField) value).setMinHeight(rowHeight);
                ((ValueField) value).setPrefWidth(width / columnCount);
                ((ValueField) value).getStyleClass().add("table-field");

                row.getChildren().add(((ValueField) value));

                ((ValueField) value).connectedTo(ubg);

                if (i % columnCount == columnCount - 1) {

                    if (deletable) {
                        row.makeDeletable(ubg);
                    }

                    add(row, 0, rowCount++);
                    row = new ValueRow(this);
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
                
                if (deletable) {
                    row.makeDeletable(ubg);
                }

                add(row, 0, rowCount++);
            }

        }

        public EditableTable build() {
            buildTitle();
            buildHeader();
            buildRows();
            getStyleClass().add("table");
            return this;
        }
    }

    public static SelectableTable allergiesTableBaseFor(int userID) throws Exception {
        ArrayList<Allergy> allergies = Database.Row.Allergy.getAllFor(userID);
        SelectableTable allergiesTable = new SelectableTable();

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

    public static InformationForm contactInformationFormBaseFor(int userID) throws Exception {
        Patient patient = Database.Row.Patient.getFor(userID);
        InformationForm contactInformationForm = new InformationForm();

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
                                return doctor.firstName.originalValue + " " + doctor.lastName.originalValue;
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
                    .withData(Database.Row.Employee.getAllDoctorNames()),
                patient.bloodType.createValueOption().withLabel("Blood Type: ")
                    .withData(Database.getOptionsFor(patient.bloodType)),
                patient.height.createValueField().withLabel("Height: "),
                patient.weight.createValueField().withLabel("Weight: "),
                patient.race.createValueOption().withLabel("Race: ")
                    .withData(Database.getOptionsFor(patient.race)),
                patient.ethnicity.createValueOption().withLabel("Ethnicity: ")
                    .withData(Database.getOptionsFor(patient.ethnicity)),
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

    public static EditableTable surgeriesTableBaseFor(int userID) throws Exception {
        ArrayList<Surgery> surgeries = Database.Row.Surgery.getAllFor(userID);
        EditableTable surgeriesTable = new EditableTable();

        surgeriesTable.isDeletable(true);

        for (Surgery surgery : surgeries) {
            Employee doctor = Database.Row.Employee.getFor(Integer.parseInt(surgery.doctorID.originalValue));
            doctor.userID.newValue = doctor.firstName.originalValue + " " + doctor.lastName.originalValue;

            surgeriesTable.withCustomHeader("Doctor", "Date", "Procedure", "Location", "Notes");

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
