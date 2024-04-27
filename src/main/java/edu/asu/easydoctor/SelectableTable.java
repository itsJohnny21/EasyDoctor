package edu.asu.easydoctor;

import java.util.HashSet;
import java.util.function.Consumer;

import edu.asu.easydoctor.UI.Table;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;

public class SelectableTable extends UI.Table {
    public Consumer<Row> rowAction;
    public boolean isToggable;
    public HashSet<Row> selectedRows = new HashSet<>();

    public SelectableTable() {
        super();
    }

    public SelectableTable withRowAction(Consumer<Row> rowAction) {
        this.rowAction = rowAction;
        return this;
    }

    public SelectableTable isToggable(boolean isToggable) {
        this.isToggable = isToggable;
        return this;
    }

    public HashSet<Row> getSelectedRows() {
        return selectedRows;
    }

    public void buildHeader() {
        if (this.headers == null) {
            return;
        }

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
        for (Row row : this.rows) {
            this.add(row, 0, this.rowCounter++);

            if (isToggable) {
                RadioButton radioButton = new RadioButton();
                radioButton.getStyleClass().add("table-radio-button");

                radioButton.setOnAction(event -> {
                    if (radioButton.isSelected()) {
                        selectedRows.add(row);
                    } else {
                        selectedRows.remove(row);
                    }
                });

                row.getChildren().add(radioButton);
            }
            
            if (rowAction != null) {
                row.setOnMouseClicked(event -> {
                    rowAction.accept(row);
                });
                
                row.getStyleClass().add("table-selectable-row");
            } else {
                row.getStyleClass().add("table-row");
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
