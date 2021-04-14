/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS.SUBCLASS;

import INSTRUMENTS.Const;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SwitchCarry {
    public Button button;
    public LimitedTextField textField;
    public boolean flag = true;
    public int row;
    public int column;
    public boolean isOverFlowRes = false;

    public SwitchCarry() {
        this.button = new Button();
        this.button.setOnAction(this::switch_type);
        this.button.setBackground(new Background( new BackgroundFill( Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY ) ));
        this.button.setMaxWidth(30);
        this.button.setPrefWidth(30);
        this.button.setText("+");
    }

    private void switch_type(ActionEvent actionEvent) {
        this.flag = !this.flag;
        this.textField = new LimitedTextField();
        this.textField.setMaxLength(1);
        this.textField.setMaxWidth(30);

        this.textField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches(Const.regex_bin)) {
                textField.setText("");
            }
        });

        GridPane gridPane = ((GridPane) this.button.getParent());

        gridPane.getChildren().remove(this.button);
        gridPane.add(this.textField, this.column, this.row);
        if(isOverFlowRes) {
            GridPane.setHalignment(this.textField, HPos.RIGHT);
        }else {
            GridPane.setHalignment(this.textField, HPos.CENTER);
        }

        this.button = null;
    }

    public Node get() {
        if(this.flag){
            return this.button;
        }
        return this.textField;
    }

    public String getValue(){
        if(flag) return "0";
        else{
            return this.textField.getText();
        }
    }
}
