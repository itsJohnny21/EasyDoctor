package application;

import java.sql.ResultSet;
import java.util.ArrayList;

import application.controllers.TestController.UpdateButtonGroup;
import javafx.scene.control.TextField;

public abstract class Row {
    public String tableName;
    public int rowID;

    public Row(ResultSet resultSet) throws Exception {
        tableName = resultSet.getMetaData().getTableName(2);
    }

    // public ResultSet getForPatient(int userID) throws Exception {
    //     return Database.getFromTableForPatient(userID, tableName);
    // }

    // public ResultSet getRow(int rowID) {
    //     // Database.getFromTableRow(rowID, tableName);
    //     return null;

    // }
    
    public static class ValueField extends TextField { //! Move to FormBuilder
        public Datum datum;
        public boolean updatable;
        
        public ValueField(Datum datum, boolean updatable) {
            super(datum.originalValue);
            this.datum = datum;
            this.updatable = updatable;
            setEditable(updatable);
        }
        
        public void onEdit() {
            setEditable(updatable);
        }
        
        public void onSave() throws Exception {
            if (!getText().equals(datum.originalValue))  {
                datum.newValue = getText();
                Database.updateRow(datum.parent.rowID, datum.parent.tableName, datum.columnName, datum.newValue);
                datum.originalValue = datum.newValue;
                setText(datum.newValue);
            }
            setEditable(false);
        }
        
        public void onCancel() {
            setEditable(false);
            undo();
        }

        public ValueField connectedTo(UpdateButtonGroup updateButtonGroup) {
            updateButtonGroup.fields.add(this);
            return this;
        }
    }
    
    public static class Datum {
        public String originalValue;
        public String columnName;
        public Row parent;
        public String newValue;
        
        public Datum(Row parent, String originalValue, String columnName, String tableName) {
            this.parent = parent;
            this.originalValue = originalValue;
            this.columnName = columnName;
            this.newValue = originalValue;
        }
        
        public Datum() {}

        public ValueField createField(boolean updatable) {
            return new ValueField(this, updatable);
        }

        public ValueField connectedTo(UpdateButtonGroup updateButtonGroup) {
            ValueField field = createField(true);
            updateButtonGroup.fields.add(field);
            updateButtonGroup.fields.add(field);
            updateButtonGroup.fields.add(field);
            return field;
        }
    }

    public static class Surgery extends Row {
        public Datum creationTime;
        public Datum date;
        public Datum patientID;
        public Datum doctorID;
        public Datum type;
        public Datum location;
        public Datum notes;
    
        public Surgery(ResultSet resultSet) throws Exception {
            super(resultSet);
        
            int rowID = resultSet.getInt("ID");
            String creationTime = resultSet.getString("creationTime");
            String date = resultSet.getString("date");
            String patientID = resultSet.getString("patientID");
            String doctorID = resultSet.getString("doctorID");
            String type = resultSet.getString("type");
            String location = resultSet.getString("location");
            String notes = resultSet.getString("notes");
    
            this.rowID = rowID;
            this.creationTime = new Datum(this, creationTime, "creationTime", tableName);
            this.date = new Datum(this, date, "date", tableName);
            this.patientID = new Datum(this, String.valueOf(patientID), "patientID", tableName);
            this.doctorID = new Datum(this, doctorID, "doctorID", tableName);
            this.type = new Datum(this, type, "type", tableName);
            this.location = new Datum(this, location, "location", tableName);
            this.notes = new Datum(this, notes, "notes", tableName);
        }
    }

    public static class HealthCondition extends Row {
        public Datum creationTime;
        public Datum patientID;
        public Datum healthCondition;
        public Datum severity;
        public Datum type;
        public Datum notes;

        public HealthCondition(ResultSet resultSet) throws Exception {
            super(resultSet);

            int rowID = resultSet.getInt("ID");
            String creationTime = resultSet.getString("creationTime");
            String patientID = resultSet.getString("patientID");
            String healthCondition = resultSet.getString("healthCondition");
            String severity = resultSet.getString("severity");
            String type = resultSet.getString("type");
            String notes = resultSet.getString("notes");

            this.rowID = rowID;
            this.creationTime = new Datum(this, creationTime, "creationTime", tableName);
            this.patientID = new Datum(this, patientID, "patientID", tableName);
            this.healthCondition = new Datum(this, healthCondition, "healthCondition", tableName);
            this.severity = new Datum(this, severity, "severity", tableName);
            this.type = new Datum(this, type, "type", tableName);
            this.notes = new Datum(this, notes, "notes", tableName);
        }

        public static HealthCondition getForPatient(int patientID) throws Exception {
            ResultSet resultSet = Database.getHealthConditionsFor2(patientID);
            resultSet.next();
            return new HealthCondition(resultSet);
        }

        public static ArrayList<HealthCondition> getAllForPatient(int patientID) throws Exception {
            ResultSet resultSet = Database.getHealthConditionsFor2(patientID);

            ArrayList<HealthCondition> healthConditions = new ArrayList<HealthCondition>();
            
            while (resultSet.next()) {
                healthConditions.add(new HealthCondition(resultSet));
            }

            return healthConditions;
        }
    }
}