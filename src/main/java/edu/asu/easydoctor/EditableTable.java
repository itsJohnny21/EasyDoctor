package edu.asu.easydoctor;

import java.sql.SQLException;
import java.util.function.Consumer;

import edu.asu.easydoctor.UI.Table;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class EditableTable extends UI.Table {
    public UpdateButtonGroup ubg;
    public Consumer<Row> deleteAction;
    public static final Consumer<Row> DEFAULT_DELETE_ACTION = (row) -> {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(String.format("Delete from %s", row.tableName));
        alert.setHeaderText(String.format("Are you sure you want to delete this row from %s?", row.tableName));
        alert.setContentText("This action cannot be undone.");
        if (alert.showAndWait().get().getText().equals("OK")) {
            try {
                Database.deleteRow(row.tableName, row.rowID);
                EditableTable table = (EditableTable) row.getParent();
                table.rows.remove(row);
                table.getChildren().remove(row);

            } catch (SQLException e) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred while deleting the row.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    };

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

                if (deleteAction != null) {
                    row.makeDeletable(deleteAction);
                    ubg.addConnection(row);
                    row.deleteButton.getStyleClass().add("table-delete-button");
                }

                for (Node node : row.getChildren()) {
                    if (node instanceof Connectable) {
                        Connectable value = ((Connectable) node);
                        ubg.addConnection(value);
                    }
                }
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