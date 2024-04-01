package application;

import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

public class UI2 {

    public abstract static class Table extends GridPane {
        public String title;
        public String[] headers;
        public ArrayList<Row2> rows;
        public int rowCounter;

        public Table() {
            getStyleClass().add("table");
            rows = new ArrayList<>();
            rowCounter = 0;
        }

        public Table withTitle(String title) {
            this.title = title;
            return this;
        }

        public Table withHeader(String... headers) {
            this.headers = headers;
            return this;
        }

        public Table withRows(Row2... rows) {
            for (Row2 row : rows) {
                this.rows.add(row);
            }

            return this;
        }

        public void buildTitle() {
            HBox titleBox = new HBox();
            Label titleLabel = new Label(this.title);
            titleBox.getChildren().add(titleLabel);
            titleBox.getStyleClass().add("table-title");

            this.add(titleBox, 0, this.rowCounter++);
        }

        public abstract void buildHeader();
        public abstract void buildRows();
        public abstract Table build();

        public static class SelectableTable extends Table {
            public Consumer<Row2> rowAction;

            public SelectableTable() {
                super();
            }

            public SelectableTable withRowAction(Consumer<Row2> rowAction) {
                this.rowAction = rowAction;
                return this;
            }

            public void buildHeader() {
                HBox headerBox = new HBox();
                headerBox.getStyleClass().add("table-header");
                
                for (String header : this.headers) {
                    Label headerLabel = new Label(header);
                    headerLabel.getStyleClass().add("table-label");
                    headerBox.getChildren().add(headerLabel);
                }

                this.add(headerBox, 0, this.rowCounter++);
            }

            public void buildRows() {
                for (Row2 row : this.rows) {
                    this.add(row, 0, this.rowCounter++);
                    
                    if (rowAction != null) {
                        rowAction.accept(row);
                        row.getStyleClass().add("table-selectable-row");
                    }
                }
            }

            public Table build() {
                buildTitle();
                buildHeader();
                buildRows();
                return this;
            }
        }

        public static class EditableTable extends Table {
            public UpdateButtonGroup ubg;
            public Consumer<Row2> deleteAction;

            public EditableTable() {
                super();
            }

            public EditableTable withDeleteAction(Consumer<Row2> deleteAction) {
                this.deleteAction = deleteAction;
                return this;
            }

            public EditableTable connectedTo(UpdateButtonGroup ubg) {
                this.ubg = ubg;
                return this;
            }

            public void buildHeader() {
                HBox headerBox = new HBox();
                headerBox.getStyleClass().add("table-header");
                
                for (String header : this.headers) {
                    Label headerLabel = new Label(header);
                    headerLabel.getStyleClass().add("table-label");
                    headerBox.getChildren().add(headerLabel);
                }
                
                if (deleteAction != null) {
                    Label deleteLabel = new Label("Delete");
                    deleteLabel.getStyleClass().add("table-label");
                    headerBox.getChildren().add(deleteLabel);
                }

                this.add(headerBox, 0, this.rowCounter++);
            }

            public void buildRows() {
                for (Row2 row : rows) {
                    row.getStyleClass().add("table-row");

                    if (ubg != null) {
                        for (Node node : row.getChildren()) {
                            Connectable value = ((Connectable) node);
                            value.initialize();
                            ubg.addConnection(value);
                        }
                    }

                    if (deleteAction != null) {
                        ubg.addConnection(row);
                        row.makeDeletable(deleteAction);
                        row.deleteButton.getStyleClass().add("table-delete-button");
                    }

                    this.add(row, 0, this.rowCounter++);
                }
            }

            public Table build() {
                buildTitle();
                buildHeader();
                buildRows();
                return this;
            }
        }
    }

    public static class Form extends GridPane {
        public String title;
        public ArrayList<Connectable> fields;
        public int rowCounter;
        public int columnCount;
        public boolean submittable;
        public UpdateButtonGroup ubg;
        
        public Form() {
            getStyleClass().add("form");
            fields = new ArrayList<>();
            columnCount = 2;
        }

        public Form isSubmittable(boolean submittable) {
            this.submittable = submittable;
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

        public void buildRows() {
            HBox row = new HBox();
            row.getStyleClass().add("form-row");

            for (int i = 0; i < fields.size(); i++) {
                HBox fieldBox = new HBox();
                fieldBox.getStyleClass().add("form-field");

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

                    if (valueOption.converter == null) {
                        valueOption.setConverter(new StringConverter<Datum>() {
                            public String toString(Datum datum) {
                                return datum.originalValue;
                            }
                            
                            public Datum fromString(String string) {
                                return null;
                            }
                        });
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

        public void buildSubmitButton() {
            if (submittable) {
                HBox row = new HBox();
                row.getStyleClass().add("form-button-row");

                Button submitButton = new Button("Submit");
                submitButton.getStyleClass().add("form-submit-button");

                submitButton.setOnAction(e -> {
                    for (Connectable field : fields) {
                        System.out.println(field.toString());
                    }
                });

                row.getChildren().add(submitButton);

                add(row, 0, rowCounter++);
            }
        }

        public Form build() {
            buildTitle();
            buildRows();
            buildSubmitButton();
            return this;
        }
    }
}
