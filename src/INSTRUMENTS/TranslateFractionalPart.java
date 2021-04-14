/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.LinkedList;

public class TranslateFractionalPart extends Instrument {

    public LinkedList<MultiplicationInstrument> list = new LinkedList<>();
    public Button button_plus;
    public VBox TranslateVBox;
    public FlowPane pane;
    public TextField dig;


    public TranslateFractionalPart(int index, Step step) {
        super("Перевод дробной части в другую СС умножением", index, step);

        button_plus = new Button("+");
        button_plus.setOnAction(this::add_multi);

        dig = new TextField();

        pane = new FlowPane();

        this.TranslateVBox = new VBox();
        this.titledPane.setContent(TranslateVBox);

        pane.getChildren().addAll(new Label("Количество разрядов"), dig, button_plus);

        this.TranslateVBox.getChildren().addAll(pane);


    }

    private void add_multi(ActionEvent actionEvent) {
        try {
            checkValue();
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        MultiplicationInstrument multiplicationInstrument = new MultiplicationInstrument(this.list.size()+1, step, Integer.parseInt(dig.getText()));
        multiplicationInstrument.deleteButton.setOnAction(actionEvent1 ->  multiplicationInstrument.delete_1(this));
        this.list.add(multiplicationInstrument);
        this.TranslateVBox.getChildren().add(multiplicationInstrument.vBox);
        //multiplicationInstrument.vBox.getAlignment().getVpos()
        multiplicationInstrument.vBox.setOnDragDetected(multiplicationInstrument::onDragDetected);
        multiplicationInstrument.vBox.setOnDragOver(multiplicationInstrument::onDragOver);
        multiplicationInstrument.vBox.setOnDragDropped(multiplicationInstrument::onDragDropped);
        multiplicationInstrument.vBox.setOnDragExited(multiplicationInstrument::onDragDone);
    }




    @Override
    public String getInform() {
        return null;
    }

    @Override
    public void separate_digits(ActionEvent actionEvent) {

    }

    @Override
    public void checkValue() throws IllegalArgumentException {

    }

    //drag'n'drop
    /*private void onDragDropped(DragEvent dragEvent) {

        dragEvent.acceptTransferModes(TransferMode.ANY);
        INSTRUMENTS.Step source = draggingStep.get();

        source.step_vbox.setBackground(unfocusBackground);

        ObservableList<Node> list = ((VBox)this.step_vbox.getParent()).getChildren();

        if(source.index < this.index){

            int startIndex = source.index - 1;
            int endIndex = this.index - 1;
            int tempIndex = source.index;
            INSTRUMENTS.Step tmp;

            while (startIndex != endIndex){

                tmp = steps.get(tempIndex);
                Node tempNode = list.get(tempIndex);
                Collections.swap(steps, tempIndex, startIndex);

                list.set(startIndex, new VBox());
                list.set(tempIndex,  source.step_vbox);
                list.set(startIndex, tempNode);

                source.index = source.index+1;
                tmp.index   -= 1;

                startIndex  += 1;
                tempIndex   += 1;
            }


        }
        else{
            int startIndex = source.index - 1;
            int endIndex = this.index - 1;
            int tempIndex = source.index - 2;
            INSTRUMENTS.Step tmp;

            while (startIndex != endIndex){

                tmp = steps.get(tempIndex);

                Node tempNode = list.get(tempIndex);
                Collections.swap(steps, tempIndex, startIndex);

                list.set(startIndex, new VBox());
                list.set(tempIndex,  source.step_vbox);
                list.set(startIndex, tempNode);

                source.index = source.index-1;
                tmp.index   += 1;

                startIndex  -= 1;
                tempIndex   -= 1;
            }
        }

        for (INSTRUMENTS.Step step: steps) {
            step.label.setText(step.index + ".");
        }

    }*/

    /*private void onDragOver(DragEvent dragEvent) {
        VBox vBox = (VBox) dragEvent.getSource();
        //System.out.println(vBox);
        if(!draggingvBox.get().equals(this)){
            dragEvent.acceptTransferModes(TransferMode.ANY);
            this.step_vbox.setBackground(Green);
        }
    }*/

    /*private void onDragDone(DragEvent dragEvent) {
        if(!draggingStep.get().equals(this)) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
            this.step_vbox.setBackground(unfocusBackground);
        }
    }*/

        //TODO как получить объект MultiplicationInstrument отсюда? Его же нельзя передать вроде бы
    /*public void onDragDetected(MouseEvent mouseEvent){

        for (MultiplicationInstrument m: list ) {
            m.vBox.setBackground(unfocusBackground);
        }
        System.out.println(mouseEvent.getSource());
        VBox vBox = (VBox) mouseEvent.getSource();
        Dragboard dragboard = vBox.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString("vBox");
        draggingvBox.setValue(vBox);
        vBox.setBackground(focusBackground);
        dragboard.setContent(content);
        mouseEvent.consume();

    }*/

//TODO добавить drag'n'drop, убрать очкодавню с количеством разрядов
    //TODO ошибка с драгндропом шагов не пофиксилась, и надо обработать выход за пределы окна

}
