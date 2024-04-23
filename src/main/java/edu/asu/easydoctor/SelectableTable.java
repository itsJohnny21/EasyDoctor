package edu.asu.easydoctor;

import java.util.function.Consumer;

import edu.asu.easydoctor.UI.Table;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;

public class SelectableTable extends UI.Table {
    public Consumer<Row> rowAction;
    public ToggleGroup toggleGroup;
    public Row selectedRow;

    public SelectableTable() {
        super();
    }

    public SelectableTable withRowAction(Consumer<Row> rowAction) {
        this.rowAction = rowAction;
        return this;
    }

    public SelectableTable isToggable(boolean isToggable) {
        if (isToggable) {
            this.toggleGroup = new ToggleGroup();
        }

        return this;
    }

    public Row getSelectedRow() {
        return selectedRow;
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

            if (toggleGroup != null) {
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);
                radioButton.getStyleClass().add("table-radio-button");

                radioButton.setOnAction(event -> {
                    selectedRow = row;
                });

                row.getChildren().add(radioButton);
            }
            
            if (rowAction != null) {
                rowAction.accept(row);
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
