package com.os.sudoku.controller;

import com.os.sudoku.model.Grid;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.Optional;

@FxmlView("main.fxml")
@Component
public class ControllerMain {

    @FXML
    Pane mainPane;

    private GridPane gridPane;
    private Grid grid;

    public void newGrid() {
        final int size = showNewBoardDialog();
        buildGrid(size);
    }

    private Integer showNewBoardDialog() {
        TextInputDialog dialog = new TextInputDialog("9");
        dialog.setTitle("Nowa Plansza");
        dialog.setHeaderText("Wypełnij Formularz");
        dialog.setContentText("Rozmiar Planszy:");

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();

        return Integer.parseInt(result.orElse("0"));
    }

    private void buildGrid(final int size) {
        gridPane = new GridPane();
        grid = new Grid(size);
        mainPane.getChildren().clear();
        mainPane.getChildren().addAll(gridPane);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                gridPane.add(createVBoxPane(grid.getEntryAt(i,j).getNumbers(),i, j), j, i);
            }
        }

        for (int i = 0; i < size; i++) {
            RowConstraints row = new RowConstraints(50);
            gridPane.getRowConstraints().add(row);
            ColumnConstraints col = new ColumnConstraints(80);
            gridPane.getColumnConstraints().add(col);
        }
    }

    private VBox createVBoxPane(final String lblText, final int x, final int y) {

        // label
        final Label label = new Label(lblText);
        label.setFont(new Font("Times", 9));
        label.setId("availLbl");

        // text field
        final TextField value = new TextField();
        value.setId("val");
        value.textProperty().addListener((o, oldV, newV) -> {
            if (newV != null && !newV.isEmpty()) {
                grid.setValue(Integer.parseInt(newV), x, y);
            } else {
                grid.setValue(0, x, y);
            }
            if (!grid.validate()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Błąd na planszy!!!");
                alert.showAndWait();
            }
            accept();
            //label.setText(grid.getEntryAt(x, y).getNumbers());
        });
        value.setMaxHeight(30);
        value.setMaxWidth(30);

        // pane
        VBox pane = new VBox();
        pane.getChildren().addAll(value, label);
        pane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        pane.setAlignment(Pos.CENTER);

        return pane;
    }

    public void solve() {
        if (grid != null) {
            grid.solve(0, 0);
            refreshGrid();
        }
    }

    private void accept() {
        if (grid != null) {
            grid.computeHints();

            for (int i = 0; i < grid.getX(); i++) {
                for (int j = 0; j < grid.getY(); j++) {
                    updateLabel(grid.getEntryAt(i, j).getNumbers(), i, j);
                }
            }
        }
    }

    private void refreshGrid() {
        if (grid != null) {
            for (int i = 0; i < grid.getX(); i++) {
                for (int j = 0; j < grid.getY(); j++) {
                    updateValue(grid.getEntryAt(i, j).getVal() + "", i, j);
                }
            }
        }
    }

    private void updateLabel(String value, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                VBox vbox = (VBox) node;
                for (Node sNode : vbox.getChildren()) {
                    if (sNode.getId() != null && sNode.getId().equals("availLbl")) {
                        Label lbl = (Label) sNode;
                        lbl.setText(value);
                    }
                }
            }
        }
    }

    private void updateValue(String value, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                VBox vbox = (VBox) node;
                for (Node sNode : vbox.getChildren()) {
                    if (sNode.getId() != null && sNode.getId().equals("val")) {
                        TextField txtFld = (TextField) sNode;
                        txtFld.setText(value);
                    }
                }
            }
        }
    }
}
