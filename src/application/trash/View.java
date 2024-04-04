// package application.trash;

// import java.util.ArrayList;

// import application.Database.Row;
// import application.UpdateButtonGroup;
// import javafx.scene.control.Label;
// import javafx.scene.layout.GridPane;
// import javafx.scene.layout.HBox;

// public abstract class View extends GridPane {
//     public double width;
//     public double rowHeight;
//     public int rowCounter;
//     public UpdateButtonGroup updateButtonGroup;
//     public HBox titleBox;
//     public String title;
//     public ArrayList<Row> rows;

//     public View() {
//         super();
//         width = 0;
//         rowHeight = 0;
//         rowCounter = 0;
//         updateButtonGroup = null;
//         titleBox = null;
//         rows = new ArrayList<Row>();
//     }

//     public void buildTitle() {
//         titleBox = new HBox();
//         Label titleLabel = new Label(title);
//         titleBox.getChildren().add(titleLabel);
//         add(titleBox, 0, rowCounter++);
//         rowCounter++;
//     }

//     public void addRow(Row row) {
//         rows.add(row);
//     }

//     public abstract void buildRows();
//     public abstract View build();
// }
