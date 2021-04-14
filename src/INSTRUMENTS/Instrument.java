/*
 * Copyright (c) amadeus-aco.ru
 */
//TODO: сложение/вычитание в K-Q-ичной системе счисления
package INSTRUMENTS;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public abstract class Instrument {

    public VBox vBox = new VBox();
    public int index;
    public FlowPane flowPane;
    public TitledPane titledPane;
    public Label label_text;
    public Label indexLabel = new Label();
    public TextField resField;
    public Button deleteButton;
    public Label resStep = new Label(Const.step_result);
    public Step step;
    public Label signLabel = new Label();
    public int digit;
    public GridPane digitNet;

    public abstract String getInform();

    public Instrument(String label_text, int index, Step step) {
        this.step = step;
        this.index = index;
        this.indexLabel.setText(this.index + ".");
        flowPane = new FlowPane();
        titledPane = new TitledPane();
        resField = new TextField();
        deleteButton = new Button("✖");
        deleteButton.setOnAction(actionEvent -> this.delete());
        this.label_text = new Label(label_text);
        titledPane.setText("Панель инструмента");
        //titledPane.setExpanded(false);
        FlowPane.setMargin(this.deleteButton, new Insets(0,4,0,4));
        FlowPane.setMargin(this.resField,     new Insets(0,4,0,4));
        this.flowPane.setBorder(new Border(new BorderStroke(Color.BLACK,null,null,null,  BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY)));
        VBox.setMargin(this.flowPane,new Insets(4,0,4,0));
        this.flowPane.setPadding(new Insets(4,0,0,0));

        flowPane.getChildren().addAll( this.indexLabel, this.label_text,this.deleteButton,this.resStep, this.resField);
        vBox.getChildren().addAll(flowPane, this.titledPane);
        vBox.setFillWidth(true);
    }

    private void delete() {

        ((VBox)this.vBox.getParent()).getChildren().remove(this.vBox);
        for (Instrument instrument: step.instruments) {
            instrument.change_index(this.index);

        }
    }

    public void change_index(int index){
        if (this.index > index){
            this.index--;
            this.indexLabel.setText(this.index + ".");
        }
    }

    public abstract void separate_digits(ActionEvent actionEvent);

    public FlowPane createSeparatorPane(){
        Button separate = new Button("Отделить");
        TextField sepDigitAmount = new TextField();
        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(sepDigitAmount,separate);
        return flowPane;
    }
    public abstract void checkValue() throws IllegalArgumentException;

}
