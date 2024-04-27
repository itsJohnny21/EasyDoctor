package edu.asu.easydoctor;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Form extends GridPane {

    public enum RowStyle {
        HORIZONTAL, VERTICAL
    }

    public String title;
    public ArrayList<Connectable> fields;
    public int rowCounter;
    public int columnCount;
    public Consumer<Form> submitAction;
    public UpdateButtonGroup ubg;
    public RowStyle rowStyle;
    
    public Form() {
        getStyleClass().add("form");
        fields = new ArrayList<>();
        columnCount = 2;
        rowStyle = RowStyle.HORIZONTAL;
    }

    public Form withRowStyle(RowStyle rowStyle) {
        this.rowStyle = rowStyle;
        return this;
    }

    public Form withSubmitAction(Consumer<Form> submitAction) {
        this.submitAction = submitAction;
        return this;
    }

    public Form withColumnCount(int columnCount) {
        this.columnCount = columnCount;
        return this;
    }

    public Form connectedTo(UpdateButtonGroup ubg) {
        this.ubg = ubg;
        return this;
    }

    public Form withTitle(String title) {
        this.title = title;
        return this;
    }

    public Form withFields(Connectable... fields) {
        for (Connectable field : fields) {
            this.fields.add(field);
        }

        return this;
    }

    public void buildTitle() {
        HBox titleBox = new HBox();
        Label titleLabel = new Label(this.title);
        titleBox.getStyleClass().add("form-title");
        titleBox.getChildren().add(titleLabel);

        this.add(titleBox, 0, rowCounter++);
    }

    public void buildRowsHorizontal() {
        HBox row = new HBox();
        row.getStyleClass().add("form-row");

        for (int i = 0; i < fields.size(); i++) {
            HBox fieldBox = new HBox();
            fieldBox.getStyleClass().add("form-field-box");

            Connectable value = fields.get(i);

            Label valueLabel = new Label();
            valueLabel.getStyleClass().add("form-label");
            fieldBox.getChildren().add(valueLabel);

            if (value instanceof ValueField) {
                ValueField valueField = (ValueField) value;
                valueLabel.setText(valueField.label);
                valueField.getStyleClass().add("form-field");
                
                if (ubg != null) {
                    valueField.setEditable(false);
                    ubg.addConnection(value);
                }

                fieldBox.getChildren().add(valueField);
                
            } else if (value instanceof ValueOption) {
                ValueOption valueOption = (ValueOption) value;
                valueLabel.setText(valueOption.label);
                valueOption.getStyleClass().add("form-option");
                
                if (ubg != null) {
                    valueOption.setDisable(true);
                    ubg.addConnection(value);
                }

                fieldBox.getChildren().add(valueOption);
            }

            row.getChildren().add(fieldBox);

            if (i % columnCount == columnCount - 1) {
                HBox.setMargin(fieldBox, new Insets(0, 150, 0, 0));
                add(row, 0, rowCounter++);
                row = new HBox();
                row.getStyleClass().add("form-row");
            }
        }

        if (row.getChildren().size() > 0) {
            while (row.getChildren().size() != columnCount) {
                HBox fieldBox = new HBox();
                fieldBox.getStyleClass().add("form-field");

                Label label = new Label("");
                label.setDisable(true);
                label.setVisible(false);
                label.getStyleClass().add("form-label");
                fieldBox.getChildren().add(label);
                
                ValueField valueField = new ValueField("");
                valueField.setDisable(true);
                valueField.setVisible(false);
                valueField.getStyleClass().add("form-field");
                fieldBox.getChildren().add(valueField);

                if (row.getChildren().size() + 1 == columnCount) {
                    HBox.setMargin(fieldBox, new Insets(0, 150, 0, 0));
                }
                
                row.getChildren().add(fieldBox);
            }
            add(row, 0, rowCounter++);
        }
    }

    public void buildRowsVertical() {
        HBox row = new HBox();
        row.getStyleClass().add("form-row");

        for (int i = 0; i < fields.size(); i++) {
            VBox fieldBox = new VBox();
            fieldBox.getStyleClass().add("form-field-box");
            HBox.setMargin(fieldBox, new Insets(0, 200 / columnCount, 0, 200 / columnCount));

            Connectable value = fields.get(i);

            Label valueLabel = new Label();
            valueLabel.getStyleClass().add("form-label");
            fieldBox.getChildren().add(valueLabel);

            if (value instanceof ValueField) {
                ValueField valueField = (ValueField) value;
                valueLabel.setText(valueField.label);
                valueField.getStyleClass().add("form-field");
                
                if (ubg != null) {
                    valueField.setEditable(false);
                    ubg.addConnection(value);
                }

                fieldBox.getChildren().add(valueField);
                
            } else if (value instanceof ValueOption) {
                ValueOption valueOption = (ValueOption) value;
                valueLabel.setText(valueOption.label);
                valueOption.getStyleClass().add("form-option");
                
                if (ubg != null) {
                    valueOption.setDisable(true);
                    ubg.addConnection(value);
                }

                fieldBox.getChildren().add(valueOption);
            }

            row.getChildren().add(fieldBox);

            if (i % columnCount == columnCount - 1) {
                add(row, 0, rowCounter++);
                row = new HBox();
                row.getStyleClass().add("form-row");
            }
        }
    }

    public void buildSubmitButton() {
        if (submitAction != null) {
            HBox row = new HBox();
            row.getStyleClass().add("form-button-row");

            Button submitButton = new Button("Submit");
            submitButton.getStyleClass().add("form-submit-button");

            submitButton.setOnAction(e -> {
                submitAction.accept(this);
            });

            row.getChildren().add(submitButton);

            add(row, 0, rowCounter++);
        }
    }

    public Form build() {
        buildTitle();

        if (rowStyle == RowStyle.HORIZONTAL) {
            buildRowsHorizontal();
        } else {
            buildRowsVertical();
        }
        
        buildSubmitButton();
        return this;
    }
}