package controller;

import javafx.animation.Animation;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;

public class Column {
    static void setCellWrappable(TableColumn tableColumn){
        tableColumn.setCellFactory(tc -> {
            TableCell<Animation.Status, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(tableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
    }
}
