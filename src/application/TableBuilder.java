package application;

import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import application.FormBuilder.Form;
import application.FormBuilder.FormData;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class TableBuilder {

    public static class TableData {
        public String originalName;
        public String name;
        public String[] originalHeader;
        public String[] header;
        public ArrayList<String[]> originalRows;
        public ArrayList<String[]> rows;

        public TableData(String originalName, String name, String[] originalHeader, String[] header, ArrayList<String[]> originalRows, ArrayList<String[]> rows) throws Exception {
            this.originalName = originalName;
            this.name = name;
            this.originalHeader = originalHeader;
            this.header = header;
            this.originalRows = originalRows;
            this.rows = rows;
        }
    }

    public static class Table extends GridPane {
        public TableData data;
        public Label name;
        public GridPane header;
        public String style;
        public int rowCount;
        public String rowStyle;
        public double rowWidth;
        public double rowHeight;
        public boolean displayTableNameOption;
        public boolean displayHeaderOption;
        public boolean scrollableOption;
        public Consumer<Row> selectAction;
        public Consumer<Row> onMouseEntered;
        public Consumer<Row> onMouseExited;

        //! Use a builder pattern
        public Table(TableData data, double rowWidth, double rowHeight, String style, String rowStyle, boolean displayTableNameOption, boolean displayHeaderOption, boolean scrollableOption, Consumer<Row> selectAction) {
            super();

            this.data = data;
            this.rowCount = 0;
            this.style = style;
            this.rowWidth = rowWidth;
            this.rowHeight = rowHeight;
            this.displayTableNameOption = displayTableNameOption;
            this.displayHeaderOption = displayHeaderOption;
            this.scrollableOption = scrollableOption;
            this.selectAction = selectAction;
            this.rowStyle = rowStyle;
        }

        public void generateTableName() {
            name = new Label(data.name);
            name.setFont(Font.font("Arial", 25));
            name.setAlignment(Pos.CENTER);
            name.setPrefWidth(rowWidth);
            name.setPrefHeight(rowHeight);
            name.setStyle(style);
            
            add(name, 0, rowCount++);
        }
        
        public void generateHeader() {
            header = new GridPane();
            header.setPrefWidth(rowWidth);
            header.setPrefHeight(rowHeight);
            header.setAlignment(Pos.CENTER);

            for (int i = 0; i < data.header.length; i++) {
                String columnName = data.header[i];
                Label columnLabel = new Label(columnName);
                columnLabel.setFont(Font.font("Arial", 21));
                columnLabel.setAlignment(Pos.CENTER);
                columnLabel.setPrefWidth(rowWidth / data.header.length);
                columnLabel.setPrefHeight(rowHeight);

                header.add(columnLabel, i, 0);
            }

            header.setStyle(style);
            add(header, 0, rowCount++);
        }
        
        public void generateRows() {
            for (int i = 0; i < data.rows.size(); i++) {
                String[] originalValues = data.originalRows.get(i);
                Row row = new Row(originalValues);
                row.setPrefWidth(rowWidth);
                row.setPrefHeight(rowHeight);
                row.setAlignment(Pos.CENTER);
                row.setStyle(rowStyle);

                String[] displayValues = data.rows.get(i);
                for (int j = 0; j < displayValues.length; j++) {
                    String value = displayValues[j];
                    Label valueLabel = new Label(value);
                    valueLabel.setFont(Font.font("Arial", 21));
                    valueLabel.setAlignment(Pos.CENTER);
                    valueLabel.setPrefWidth(rowWidth / displayValues.length);

                    row.add(valueLabel, j, 0);
                }

                if (selectAction != null) {
                    row.setOnMouseClicked(event -> selectAction.accept(row));
                }

                if (onMouseEntered != null) {
                    row.setOnMouseEntered(event -> onMouseEntered.accept(row));
                }

                if (onMouseExited != null) {
                    row.setOnMouseExited(event -> onMouseExited.accept(row));
                }
                
                add(row, 0, rowCount++);
            }
        }

        public Table generate() {
            setPrefWidth(rowWidth);
            setAlignment(Pos.CENTER);

            if (displayTableNameOption) {
                generateTableName();
            }

            if (displayHeaderOption) {
                generateHeader();
            }

            generateRows();

            return this;
        }

    }
    
    public static class Row extends GridPane {
        public String[] originalValues;

        public Row(String[] originalValues) {
            super();
            this.originalValues = originalValues;
        }
    }

	public static Label generateEmptyLabel(double width, double height) {
		Label emptyLabel = new Label();
		emptyLabel.setPrefHeight(height);
		emptyLabel.setPrefWidth(width);
		emptyLabel.setVisible(false);

		return emptyLabel;
	}

	public static Form generateContactInformationFormFor(int userID, String tableName, double rowWidth, double rowHeight, String style, String fieldStyle) throws Exception {
		ResultSet resultSet = Database.getMyContactInformation();
        resultSet.next();
        ArrayList<String[]> fields = new ArrayList<String[]>();

        for (int i =1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            String originalColumnName = resultSet.getMetaData().getColumnName(i);
            String value = resultSet.getString(i) + "modified";
            String columnName = resultSet.getMetaData().getColumnLabel(i);
            String originalValue = resultSet.getString(i);
            String[] field = {columnName, value, originalColumnName, originalValue};
            fields.add(field);
        }

        FormData formData = new FormData(tableName, fields);
		Form form = new Form(formData, rowWidth, rowHeight, style, fieldStyle, null);
		return form;
	}

	public static Form generateMedicalInformationFormFor(int userID, String tableName, double rowWidth, double rowHeight, String style, String fieldStyle) throws Exception {
		ResultSet resultSet = Database.getMedicalInformationFor(userID);
        resultSet.next();
        ArrayList<String[]> fields = new ArrayList<String[]>();

        for (int i =1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            String originalColumnName = resultSet.getMetaData().getColumnName(i);
            String value = resultSet.getString(i);
            String columnName = resultSet.getMetaData().getColumnLabel(i);
            String originalValue = resultSet.getString(i);
            String[] field = {columnName, value, originalColumnName, originalValue};

            fields.add(field);
        }

        FormData formData = new FormData(tableName, fields);
		Form form = new Form(formData, rowWidth, rowHeight, style, fieldStyle, null);
		return form;
	}

	public static Table generateHealthConditionsTableFor(int userID, String tableAlias, double rowWidth, double rowHeight, String style, String rowStyle) throws Exception {
		ResultSet resultSet = Database.getHealthConditionsFor(userID);

        String tableName = resultSet.getMetaData().getTableName(2);

        String[] originalHeader = new String[resultSet.getMetaData().getColumnCount()];
        for (int i = 0; i < originalHeader.length; i++) {
            String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
            originalHeader[i] = columnName;
        }

        String[] header = {"Condition", "Severity", "Type", "Notes"};

        ArrayList<String[]> rows = new ArrayList<String[]>();
        while (resultSet.next()) {
            String[] row = new String[header.length];
            for (int i = 0; i < row.length; i++) {
                row[i] = resultSet.getString(i + 1);
            }
            rows.add(row);
        }

        TableData tableData = new TableData(tableName, tableAlias, originalHeader, header, rows, rows);
        Table table = new Table(tableData, rowWidth, rowHeight, style, rowStyle, true, true, false, null);
        return table;
	}
    
	public static Table generateVaccineRecordFor(int userID, String tableAlias, double rowWidth, double rowHeight, String style, String rowStyle) throws Exception {
		ResultSet resultSet = Database.getVaccineRecordFor(userID);
        String tableName = resultSet.getMetaData().getTableName(2);

        String[] originalHeader = new String[resultSet.getMetaData().getColumnCount()];
        for (int i = 0; i < originalHeader.length; i++) {
            String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
            originalHeader[i] = columnName;
        }

        String [] header = {"Vaccine Group", "Dose 1", "Dose 2", "Dose 3", "Dose 4", "Dose 5", "Dose 6"};

        ArrayList<String[]> originalRows = new ArrayList<String[]>();
        ArrayList<String[]> rows = new ArrayList<String[]>();
        LinkedHashMap<String, ArrayList<String>> vaccineGroups = new LinkedHashMap<String, ArrayList<String>>();
        vaccineGroups.put("COVID-19", new ArrayList<String>());
        vaccineGroups.put("Influenza", new ArrayList<String>());
        vaccineGroups.put("Hepatitis B", new ArrayList<String>());
        vaccineGroups.put("Hepatitis A", new ArrayList<String>());
        vaccineGroups.put("Measles, Mumps, Rubella", new ArrayList<String>());
        vaccineGroups.put("Varicella", new ArrayList<String>());
        vaccineGroups.put("Pneumococcal", new ArrayList<String>());
        vaccineGroups.put("Polio", new ArrayList<String>());

        while (resultSet.next()) {
            String vaccineGroup = resultSet.getString(1);
            String date = resultSet.getString(2);

            vaccineGroups.get(vaccineGroup).add(date);
        }

        for (String vaccineGroup : vaccineGroups.keySet()) {
            ArrayList<String> dates = vaccineGroups.get(vaccineGroup);
            String[] row = new String[header.length];
            row[0] = vaccineGroup;

            for (int i = 0; i < header.length-1; i++) {
                if (i < dates.size()) {
                    row[i + 1] = dates.get(i);
                } else {
                    row[i + 1] = "N/A";
                }

            }
            originalRows.add(row);
            rows.add(row);
        }

        TableData tableData = new TableData(tableName, tableAlias, originalHeader, header, originalRows, rows);
        Table table = new Table(tableData, rowWidth, rowHeight, style, rowStyle, true, true, true, null);
        return table;
	}

	public static Table generateAllergiesTableFor(int userID, String tableAlias, double rowWidth, double rowHeight, String style, String rowStyle) throws Exception {
		ResultSet resultSet = Database.getAllergiesFor(userID);

        String tableName = resultSet.getMetaData().getTableName(2);

        String[] originalHeader = new String[resultSet.getMetaData().getColumnCount()];
        for (int i = 0; i < originalHeader.length; i++) {
            String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
            originalHeader[i] = columnName;
        }

        String[] header = {"Allergen", "Common Source", "Severity", "Type", "Notes"};

        ArrayList<String[]> rows = new ArrayList<String[]>();
        while (resultSet.next()) {
            String[] row = new String[originalHeader.length];
            for (int i = 0; i < row.length; i++) {
                row[i] = resultSet.getString(i + 1);
            }
            rows.add(row);
        }

        TableData tableData = new TableData(tableName, tableAlias, originalHeader, header, rows, rows);
        Table table = new Table(tableData, rowWidth, rowHeight, style, rowStyle, true, true, true, null);
        return table;
	}

	public static Table generateSurgeriesTableFor(int userID, String tableAlias, double rowWidth, double rowHeight, String style, String rowStyle) throws Exception {
		ResultSet resultSet = Database.getSurgeriesFor(userID);
        
        String tableName = resultSet.getMetaData().getTableName(2);
        
        String[] originalHeader = new String[resultSet.getMetaData().getColumnCount()];
        for (int i = 0; i < originalHeader.length; i++) {
            String columnName = resultSet.getMetaData().getColumnLabel(i + 1);
            originalHeader[i] = columnName;
        }

        String[] header = {"Surgeon", "Date", "Location", "Notes"};
        
        ArrayList<String[]> rows = new ArrayList<String[]>();
        while (resultSet.next()) {
            String surgeon = resultSet.getString(1) + " " + resultSet.getString(2);
            String date = resultSet.getString(3);
            String location = resultSet.getString(4);
            String notes = resultSet.getString(5);
            String[] row = {surgeon, date, location, notes};
            rows.add(row);
        }

        TableData tableData = new TableData(tableName, tableAlias, header, header, rows, rows);
        Table table = new Table(tableData, rowWidth, rowHeight, style, rowStyle, true, true, true, null);
        return table;
	}

	public static Table generateVisitsTableFor(int userID, String tableAlias, double rowWidth, double rowHeight, String style, String rowStyle) throws Exception {
		ResultSet resultSet = Database.getVisitsFor(userID);
        resultSet.next();

        String[] originalHeader = new String[resultSet.getMetaData().getColumnCount()];

        for (int i = 0; i < originalHeader.length; i++) {
            originalHeader[i] = resultSet.getMetaData().getColumnLabel(i + 1);
        }

        
        String name = resultSet.getMetaData().getTableName(2);
        String[] header = {"Doctor", "Date", "Day", "Time", "Reason", "Status"};
        
        ArrayList<String[]> originalRows = new ArrayList<String[]>();
        ArrayList<String[]> rows = new ArrayList<String[]>();

        while (resultSet.next()) {
            String[] originalRow = {resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6)};
            originalRows.add(originalRow);

            String doctor = resultSet.getString(1) + " " + resultSet.getString(2);
            LocalDateTime dateTime = resultSet.getTimestamp(3).toLocalDateTime();
            String date = dateTime.format(DateTimeFormatter.ofPattern("M/d/yyyy"));
            String day = dateTime.format(DateTimeFormatter.ofPattern("EEEE"));
            String time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            String reason = resultSet.getString(4);
            Boolean completed = resultSet.getBoolean(5);
            String status;

            if (!completed) {
                if (LocalDateTime.now().isAfter(dateTime)) {
                    status = "Missed";
                } else {
                    status = "Upcoming";
                }
            } else {
                status = "Completed";
            }

            String[] row = {doctor, date, day, time, reason, status};
            rows.add(row);
        }

        TableData tableData = new TableData(name, tableAlias, originalHeader, header, originalRows, rows);
        Table table = new Table(tableData, rowWidth, rowHeight, style, rowStyle, true, true, true, null);
        return table;
	}
}
