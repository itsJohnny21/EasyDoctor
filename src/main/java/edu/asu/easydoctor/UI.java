package edu.asu.easydoctor;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public abstract class UI {
    public abstract static class Table extends GridPane {
        public String title;
        public String[] headers;
        public ArrayList<Row> rows;
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
    
        public Table withRows(Row... rows) {
            for (Row row : rows) {
                this.rows.add(row);
            }
    
            return this;
        }

        public Table withRows(ArrayList<Row> rows) {
            for (Row row : rows) {
                this.rows.add(row);
            }
    
            return this;
        }
    
        public void buildTitle() {
            if (this.title == null) {
                return;
            }
            HBox titleBox = new HBox();
            Label titleLabel = new Label(this.title);
            titleBox.getChildren().add(titleLabel);
            titleBox.getStyleClass().add("table-title");
    
            this.add(titleBox, 0, this.rowCounter++);
        }
    
        public abstract void buildHeader();
        public abstract void buildRows();
        public abstract Table build();
    }
}

