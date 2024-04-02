package application;

import java.util.function.Consumer;

import application.UI.Table;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class SelectableTable extends UI.Table {
    public Consumer<Row> rowAction;

    public SelectableTable() {
        super();
    }

    public SelectableTable withRowAction(Consumer<Row> rowAction) {
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
        for (Row row : this.rows) {
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
