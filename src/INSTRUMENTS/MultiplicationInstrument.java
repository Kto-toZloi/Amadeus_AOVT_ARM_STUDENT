/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.SwitchCarry;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.canvas.Canvas;
import java.util.ArrayList;
import java.util.Collections;


public class MultiplicationInstrument extends ADD_SUB_INSTRUMENT{

    public FlowPane separatorPaneMultiplier_2 = createSeparatorPane();
    public FlowPane separatorPaneResult = createSeparatorPane();
    public FlowPane lineManipulator = new FlowPane();
    public Canvas canvas2 = new Canvas();
    private static final ObjectProperty<MultiplicationInstrument> draggingM = new SimpleObjectProperty<>();
    public static ArrayList<MultiplicationInstrument> mults = new ArrayList<>();
    public int line_amount = 3;
    private final Background focusBackground = new Background( new BackgroundFill( Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY ) );
    private final Background unfocusBackground = new Background( new BackgroundFill( Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY ) );
    private final Background Green = new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) );
    private static final String M_DRAG_KEY = "MultiplicationInstrument";

    public MultiplicationInstrument(int index, Step step, int digit) {
        super("Умножение в столбик", index, step, digit);
        this.signLabel.setText("X");

        Button button = new Button("Добавить строку");
        Button button1 = new Button("Удалить строку");
        mults.add(this);
        button.setOnAction(this::add_line);
        button1.setOnAction(this::remove_line);

        this.lineManipulator.getChildren().addAll(button, button1);

        this.digitNet.add(this.separatorPaneMultiplier_2, 0, 2);
        this.digitNet.add(this.separatorPaneResult, 0, 4);
        this.digitNet.add(this.lineManipulator, 0, 5);


        ((Button)this.separatorPane.getChildren().get(1)).setOnAction(this::separateMultiplayer_1);
        ((Button)this.separatorPaneMultiplier_2.getChildren().get(1)).setOnAction(this::separateMultiplayer_2);
        ((Button)this.separatorPaneResult.getChildren().get(1)).setOnAction(this::separateResult);
    }

    private void separateResult(ActionEvent actionEvent) {
        String s = ((TextField)separatorPaneResult.getChildren().get(0)).getText();
        int position;

        try {
            position = Integer.parseInt(s);
        }
        catch (Exception e){
            return;
        }
        for (int i = 0; i < this.digit + (line_amount-3); i++) {
            if (((Text) this.fields.get(this.fields.size() - 1 - i).getChildren().get(0)).getText().equals(".")) {
                ((Text) this.fields.get(this.fields.size() - 1 - i).getChildren().get(0)).setText(" ");
            }
        }

        if(position >= 0 && position < this.digit + (line_amount - 3)) {


            if(position != 0) {
                ((Text) this.fields.get(this.fields.size() - 1 - (this.digit) - (line_amount - 3) + position).getChildren().get(0)).setText(".");
            }
        }

    }

    private void separateMultiplayer_2(ActionEvent actionEvent) {

        String s = ((TextField)separatorPaneMultiplier_2.getChildren().get(0)).getText();
        int position;

        try {
            position = Integer.parseInt(s);
        }
        catch (Exception e){
            return;
        }

        if(position >= 0 && position < this.digit) {
            for (int i = 0; i < this.digit; i++) {
                if (((Text) this.fields.get(i + this.digit).getChildren().get(0)).getText().equals(".")) {
                    ((Text) this.fields.get(i + this.digit).getChildren().get(0)).setText(" ");
                }
            }

            if(position != 0) {
                ((Text) this.fields.get(2 * this.digit - position).getChildren().get(0)).setText(".");
            }


        }

    }

    private void separateMultiplayer_1(ActionEvent actionEvent) {
        String s = ((TextField)separatorPane.getChildren().get(0)).getText();
        int position;

        try {
            position = Integer.parseInt(s);
        }
        catch (Exception e){
            return;
        }

        if(position >= 0 && position < this.digit) {
            for (int i = 0; i < this.digit; i++) {
                if (((Text) this.fields.get(i).getChildren().get(0)).getText().equals(".")) {
                    ((Text) this.fields.get(i).getChildren().get(0)).setText(" ");
                }
            }

            if(position != 0) {
                ((Text) this.fields.get(this.digit - position).getChildren().get(0)).setText(".");
            }
        }
    }

    @Override
    public void removeDigit(){
        if (this.digit == 1){
            return;
        }

        FlowPane flowPane;
        GraphicsContext gc;

        ArrayList<FlowPane> deleted = new ArrayList<>();

        flowPane = this.fields.get(this.digit-1);
        this.digitNet.getChildren().remove(flowPane);
        deleted.add(flowPane);

        flowPane = this.fields.get(2*this.digit - 1);
        this.digitNet.getChildren().remove(flowPane);
        deleted.add(flowPane);

        this.digitNet.getChildren().remove(this.canvas);
        canvas.setWidth(this.fields.get(0).getPrefWidth() * (this.digit - 1));
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.FUCHSIA);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) canvas.getWidth(), 2);
        this.digitNet.add(canvas, this.line_amount-3+1, 3, this.digit  - 1, 1);

        flowPane = this.fields.get(this.fields.size() - this.digit - this.line_amount + 3);
        this.digitNet.getChildren().remove(flowPane);
        deleted.add(flowPane);

        if(line_amount != 3){
            for (int i = 0; i < this.line_amount-2; i++){
                flowPane = this.fields.get(this.digit*2  + this.digit * i);
                this.digitNet.getChildren().remove(flowPane);
                deleted.add(flowPane);
            }

            this.digitNet.getChildren().remove(this.canvas2);
            canvas2.setWidth(this.fields.get(0).getPrefWidth() * (this.digit - 1 + line_amount - 3));
            gc = canvas2.getGraphicsContext2D();
            gc.setStroke(Color.FUCHSIA);
            gc.setLineWidth(5);
            gc.strokeLine(0, 2, (int) canvas2.getWidth(), 2);
            this.digitNet.add(canvas2, 1, this.line_amount + 2, this.digit  - 1 + line_amount - 3, 1);

        }

        for (FlowPane f: deleted) {
            this.fields.remove(f);
        }

        SwitchCarry switchCarry = this.carryList.getLast();
        this.digitNet.getChildren().remove(switchCarry.get());
        this.carryList.remove(switchCarry);

        this.digit--;

    }

    @Override
    public void addDigit(){

        FlowPane flowPane;
        GraphicsContext gc;

        this.fields.add(this.digit,createFlow());
        this.digitNet.add(this.fields.get(this.digit),1 + this.digit + line_amount-3, 1);
        this.fields.add(2*this.digit + 1,createFlow());
        this.digitNet.add(this.fields.get(2*this.digit + 1),1 + this.digit + line_amount-3, 2);

        flowPane = createFlow();
        this.fields.add(this.fields.size() - this.digit - line_amount + 3, flowPane);

        if (line_amount == 3){
            this.digitNet.add(flowPane, this.digit + line_amount - 2, this.line_amount + 1);
        }
        else {
            this.digitNet.getChildren().remove(this.canvas2);
            canvas2.setWidth(this.fields.get(0).getPrefWidth() * (this.digit + 1 + line_amount - 3));
            gc = canvas2.getGraphicsContext2D();
            gc.setStroke(Color.FUCHSIA);
            gc.setLineWidth(5);
            gc.strokeLine(0, 2, (int) canvas2.getWidth(), 2);
            this.digitNet.add(canvas2, 1, this.line_amount + 2, this.digit  + 1 + line_amount - 3, 1);

            this.digitNet.add(flowPane, this.digit + line_amount - 2, this.line_amount + 3);

            for (int i = 0; i < this.line_amount-2; i++) {
                flowPane = createFlow();
                this.fields.add((this.digit+1) * (2+i), flowPane);
                this.digitNet.add(flowPane, 1 + this.digit + line_amount - 3 - i, 4+i);
            }
        }

        this.digitNet.getChildren().remove(this.canvas);
        canvas.setWidth(this.fields.get(0).getPrefWidth() * (this.digit + 1));
        gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.FUCHSIA);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) canvas.getWidth(), 2);
        this.digitNet.add(canvas, this.line_amount-3+1, 3, this.digit  + 1, 1);

        SwitchCarry switchCarry = new SwitchCarry();

        if(this.carryList.isEmpty()){
            switchCarry.column = line_amount - 2;
        }
        else{
            switchCarry.column = this.carryList.getLast().column+1;
        }
        switchCarry.row = 0;
        this.digitNet.add(switchCarry.get(), switchCarry.column,0);
        this.carryList.add(switchCarry);
        GridPane.setHalignment(switchCarry.get(), HPos.CENTER);

        this.digit++;

    }

    private void remove_line(ActionEvent actionEvent) {

        FlowPane buffer;
        SwitchCarry buff;

        if(this.line_amount == 3) return;

        if(this.line_amount == 4){
            for (int i = 0; i < 2 * this.digit; i++) {
                this.digitNet.getChildren().remove(this.fields.get(2*digit + i));
            }
            for (int i = 0; i < 2 * this.digit; i++) {
                this.fields.remove(this.fields.get(2*digit));
            }
            this.digitNet.getChildren().remove(this.canvas2);
            this.digitNet.getChildren().remove(this.fields.getLast());
            this.fields.remove(this.fields.getLast());

            for (int i = 0; i < this.digit; i++) {
                buffer = this.fields.get(this.fields.size() - 1 - i);
                this.digitNet.getChildren().remove(buffer);
                this.digitNet.add(buffer, 1+i,4);
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < this.digit; j++) {
                    buffer=  fields.get(j + this.digit * i);
                    this.digitNet.getChildren().remove(buffer);
                    this.digitNet.add(buffer,j+1,i+1);
                }
            }

            this.digitNet.getChildren().remove(this.canvas);
            this.digitNet.add(this.canvas, 1 + (line_amount-4),3, this.digit,1);

        }
        else {
            this.digitNet.getChildren().remove(this.fields.getLast());
            this.fields.removeLast();

            for (int i = 0; i < this.digit; i++) {
                buffer = this.fields.get(this.digit * (line_amount-1));
                this.digitNet.getChildren().remove(buffer);
                this.fields.remove(buffer);
            }
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < this.digit; j++) {
                    buffer = this.fields.get(i*this.digit + j);
                    this.digitNet.getChildren().remove(buffer);
                    this.digitNet.add(buffer, j + line_amount-3, i + 1);
                }
            }

            this.digitNet.getChildren().remove(this.canvas);
            this.digitNet.add(this.canvas, 1 + (line_amount-4),3, this.digit,1);

            for (int i = 2; i < line_amount-1; i++) {
                System.out.println(i);
                for (int j = 0; j < this.digit; j++) {
                    buffer = this.fields.get( this.digit * (i + 1) - 1 - j);
                    this.digitNet.getChildren().remove(buffer);
                    this.digitNet.add(buffer, j + line_amount-3 - (i - 2), i + 2);
                }
            }

            this.digitNet.getChildren().remove(this.canvas2);
            canvas2.setWidth(this.fields.get(0).getPrefWidth() * (this.digit + line_amount - 4));
            GraphicsContext gc = canvas2.getGraphicsContext2D();
            gc.setStroke(Color.FUCHSIA);
            gc.setLineWidth(5);
            gc.strokeLine(0, 2, (int) canvas2.getWidth(), 2);
            this.digitNet.add(canvas2, 1, this.line_amount + 1, this.digit + line_amount - 3, 1);

            for (int i = 0; i < this.digit + this.line_amount -3 - 1; i++) {
                buffer = this.fields.get(this.fields.size() - 1 - i);
                this.digitNet.getChildren().remove(buffer);
                this.digitNet.add(buffer, 1 + i , this.line_amount + 2);
            }
        }

        for (SwitchCarry switchCarry : this.carryList) {
            buff = switchCarry;
            this.digitNet.getChildren().remove(buff.get());
            this.digitNet.add(buff.get(), buff.column - 1, 0);
            buff.column--;
        }

        line_amount--;

    }

    private void add_line(ActionEvent actionEvent) {
        FlowPane buf;
        SwitchCarry buff;
        int index = 0;


        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < this.digit; j++) {
                buf = this.fields.get(this.digit - 1 - j + this.digit*i);
                this.digitNet.getChildren().remove(buf);
                this.digitNet.add(buf, this.digit + 1 - j + (line_amount - 3), i + 1);

                if (j == this.digit-1) index = this.digit + 1 - j + (line_amount - 3);

            }
        }

        this.digitNet.getChildren().remove(this.canvas);
        this.digitNet.add(this.canvas, this.line_amount - 2 + 1,3, this.digit,1);

        for (int i = this.carryList.size() - 1; i >=0 ; i--) {
            buff = this.carryList.get(i);
            this.digitNet.getChildren().remove(buff.get());
            this.digitNet.add(buff.get(), line_amount + i -1,0);
            buff.column++;
        }

        for (int i = 0; i < this.digit + this.line_amount - 3; i++) {
            this.digitNet.getChildren().remove(this.fields.get(this.fields.size() - 1 - i));
        }

        for (int i = 0; i < this.digit + this.line_amount - 3; i++) {
            this.digitNet.add(this.fields.get(this.fields.size() - (this.digit + this.line_amount - 3)  + i), this.digit + this.line_amount - 3 + 1 - i, this.line_amount + 4);
        }

        this.fields.add(this.createFlow());
        this.digitNet.add(this.fields.getLast(), 1, this.line_amount + 4);

        canvas2.setHeight(3);
        canvas2.setWidth(this.fields.get(0).getPrefWidth() * (this.digit + line_amount - 2));
        GraphicsContext gc = canvas2.getGraphicsContext2D();
        gc.setStroke(Color.FUCHSIA);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) canvas2.getWidth(), 2);
        this.digitNet.getChildren().remove(canvas2);
        this.digitNet.add(canvas2, 1, this.line_amount + 3, this.digit + line_amount - 2, 1);

        if(line_amount == 3) {
            for (int j = 4; j < 6; j++) {
                for (int i = 0; i < this.digit; i++) {
                    buf = this.createFlow();
                    this.fields.add(this.digit * 2, buf);
                    if (j == 4) {
                        this.digitNet.add(buf, index - 1 + i, j+1);
                    }
                    else {
                        this.digitNet.add(buf, index + i, j-1);
                    }
                }
            }
        }
        else{
            int k = this.digit*2;
            int ind;

            for (int j = 0; j < this.line_amount-2; j++) {
                for (int i = 0; i < this.digit; i++){
                    //i+1+j - столбец, this.line_amount + 2 - j - строка
                    buf = this.fields.get(k);
                    this.digitNet.getChildren().remove(buf);
                    this.digitNet.add(buf, 1 + this.digit + 1 - i - j + (this.line_amount-4), j+4);
                    k++;
                }
            }

            for (int i = 0; i < this.digit; i++) {
                buf = this.createFlow();
                ind = this.fields.size()-1-this.digit + 1 - (this.line_amount - 2);
                this.fields.add(ind, buf);
                this.digitNet.add(buf, this.digit - i, this.line_amount + 2);
            }
        }

        this.line_amount++;
    }

    @Override
    public void checkValue() throws IllegalArgumentException {

    }

    public void delete_1(TranslateFractionalPart translateFractionalPart){
        ((VBox)this.vBox.getParent()).getChildren().remove(this.vBox);
        translateFractionalPart.list.remove(this);

        for (MultiplicationInstrument m: translateFractionalPart.list) {
            m.change_index(this.index);
        }
    }

    public void onDragDropped(DragEvent dragEvent) {

        if (dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="MultiplicationInstrument") {
            dragEvent.acceptTransferModes(TransferMode.ANY);
            MultiplicationInstrument source = draggingM.get();

            source.vBox.setBackground(unfocusBackground);

            ObservableList<Node> list = ((VBox)this.vBox.getParent()).getChildren();
            list.remove(0);
            System.out.println(list);
            if(source.index < this.index){

                int startIndex = source.index - 1;
                int endIndex = this.index - 1;
                int tempIndex = source.index;
                MultiplicationInstrument tmp;

                while (startIndex != endIndex){

                    tmp = mults.get(tempIndex);
                    Node tempNode = list.get(tempIndex);
                    Collections.swap(mults, tempIndex, startIndex);

                    list.set(startIndex, new VBox());
                    list.set(tempIndex,  source.vBox);
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
                MultiplicationInstrument tmp;

                while (startIndex != endIndex){

                    tmp = mults.get(tempIndex);

                    Node tempNode = list.get(tempIndex);
                    Collections.swap(mults, tempIndex, startIndex);

                    list.set(startIndex, new VBox());
                    list.set(tempIndex,  source.vBox);
                    list.set(startIndex, tempNode);

                    source.index = source.index-1;
                    tmp.index   += 1;

                    startIndex  -= 1;
                    tempIndex   -= 1;
                }
            }

            for (MultiplicationInstrument m: mults) {
                m.indexLabel.setText(m.index + ".");
            }
        }

    }

    public void onDragOver(DragEvent dragEvent) {
        if (dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="MultiplicationInstrument") {
            if (!draggingM.get().equals(this)) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
                this.vBox.setBackground(Green);
            }
        }

    }

    public void onDragDone(DragEvent dragEvent) {
        //System.out.println("QQ");
        if(dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="MultiplicationInstrument") {
            if (!draggingM.get().equals(this)) {
                dragEvent.acceptTransferModes(TransferMode.ANY);
                this.vBox.setBackground(unfocusBackground);
            }
        }
    }

    public void onDragDetected(MouseEvent mouseEvent){

        for (MultiplicationInstrument m: mults ) {
            m.vBox.setBackground(unfocusBackground);
        }

        Dragboard dragboard = this.vBox.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(M_DRAG_KEY);
        draggingM.setValue(this);
        this.vBox.setBackground(focusBackground);
        dragboard.setContent(content);
        mouseEvent.consume();

    }
}
