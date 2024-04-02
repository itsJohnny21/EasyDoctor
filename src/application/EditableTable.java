package application;

import java.util.function.Consumer;

import application.UI.Table;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public  class EditableTable extends UI.Table {
    public UpdateButtonGroup ubg;
    public Consumer<Row> deleteAction;

    public EditableTable() {
        super();
    }

    public EditableTable withDeleteAction(Consumer<Row> deleteAction) {
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
        for (Row row : rows) {
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