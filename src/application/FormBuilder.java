package application;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class FormBuilder {
    
    public static class FormData {
        public String name;
        public ArrayList<String[]> fields;

        public FormData(String name, ArrayList<String[]> fields) throws Exception {
            this.name = name;
            this.fields = fields;
        }
    }
    
	public static class Form extends GridPane {
		public FormData data;
        public int rowCount;
        public Label name;
		public String style;
		public String fieldStyle;
		public double rowWidth;
		public double rowHeight;
        public Consumer<ValueField> selectAction;

		public Form(FormData data, double rowWidth, double rowHeight, String style, String fieldStyle, Consumer<ValueField> selectAction) {
            super();

			this.data = data;
            this.rowCount = 0;
			this.rowWidth = rowWidth;
			this.rowHeight = rowHeight;
			this.style = style;
            this.fieldStyle = fieldStyle;
            this.selectAction = selectAction;
		}

		public void generateFormName() {
			name = new Label(data.name);
			name.setFont(Font.font("Arial", 25));
			name.setAlignment(Pos.CENTER);
			name.setPrefWidth(rowWidth);
			name.setPrefHeight(rowHeight);
			name.setStyle(style);

			add(name, 0, rowCount++);
		}

		public void generateRows() {
			for (int i = 0; i < data.fields.size(); i+=2) {
				String[] field1 = data.fields.get(i);

				String[] field2;

				if (i + 1 < data.fields.size()) {
					field2 = data.fields.get(i + 1);
				} else {
					field2 = new String[] {"", "", ""};
				}
				GridPane rowPane = generateRow(field1, field2);

				add(rowPane, 0, rowCount++);
			}
		}

		public GridPane generateRow(String[] field1, String[] field2) {
			GridPane row = new GridPane();
			row.setPrefWidth(rowWidth);
			row.setPrefHeight(rowHeight);
			row.setAlignment(Pos.CENTER);

			String columnName1 = String.format("%s: ", field1[0]);
			String value1 = field1[1];
            String originalColumnName1 = field1[3];
            String originalValue1 = field1[2];
			
            Label label1 = new Label(columnName1);
			ValueField valueField1 = new ValueField(value1, originalColumnName1, originalValue1);
            
            valueField1.setEditable(false); //! Fix me
            valueField1.setFont(Font.font("Arial", 21));
            valueField1.setAlignment(Pos.CENTER_LEFT);
            valueField1.setPrefWidth(rowWidth / 4);
            valueField1.setPrefHeight(rowHeight);

            label1.setFont(Font.font("Arial", 21));
            label1.setAlignment(Pos.CENTER_RIGHT);
            label1.setPrefWidth(rowWidth / 4);
            label1.setPrefHeight(rowHeight);

            String columnName2 = String.format("%s: ", field2[0]);
            String value2 = field2[1];
            String originalColumnName2 = field2[3];
            String originalValue2 = field2[2];
			
            Label label2 = new Label(columnName2);
			ValueField valueField2 = new ValueField(value2, originalColumnName2, originalValue2);
			GridPane.setMargin(valueField2, new Insets(0, rowWidth / 8, 0, 0));

            valueField2.setEditable(false); //! Fix me
            valueField2.setFont(Font.font("Arial", 21));
            valueField2.setAlignment(Pos.CENTER_LEFT);
            valueField2.setPrefWidth(rowWidth / 4);
            valueField2.setPrefHeight(rowHeight);

            label2.setFont(Font.font("Arial", 21));
            label2.setAlignment(Pos.CENTER_RIGHT);
            label2.setPrefWidth(rowWidth / 4);
            label2.setPrefHeight(rowHeight);

            if (field2[3] == "") {
                valueField2.setVisible(false);
                label2.setVisible(false);
                valueField2.setEditable(false);
            }
			
			row.add(label1, 0, 0);
			row.add(valueField1, 1, 0);
			row.add(label2, 2, 0);
			row.add(valueField2, 3, 0);

			return row;
		}

        public Form generate() {
            setPrefWidth(rowWidth);
            setAlignment(Pos.CENTER);
            generateFormName();
            generateRows();

            return this;
        }
	}

	public static class ValueField extends TextField {
		public String originalColumnName;
		public String originalValue;

		public ValueField(String value, String originalColumnName, String originalValue) {
            super(value);
            this.originalColumnName = originalColumnName;
            this.originalValue = originalValue;
		}

		public boolean wasChanged() {
			return !getText().equals(this.originalValue);
		}
	}

    public FormData getMyContactInformationData() throws Exception {
        ResultSet resultSet = Database.getMyContactInformation();
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

        FormData formData = new FormData("My Medical Information", fields);
        return formData;
    }
}
