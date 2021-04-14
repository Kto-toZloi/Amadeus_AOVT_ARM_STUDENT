/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.LimitedTextField;
import INSTRUMENTS.SUBCLASS.SwitchCarry;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ADD_SUB_INSTRUMENT extends Instrument{
    //public Button separate = new Button("Отделить");
    //public TextField sepDigitAmount = new TextField();
    public Button plusButton = new Button("+");
    public Button minusButton = new Button("-");
    public FlowPane separatorPane = createSeparatorPane();
    public LinkedList<SwitchCarry> carryList = new LinkedList<>();
    public LinkedList<FlowPane> fields = new LinkedList<>();
    public Canvas canvas;

    public static class add_sub_json{
        public String overflow_list;
        public String first_term;
        public String second_term;
        public String result;
        public String index;


        public add_sub_json(String overflow_list, String first_term, String second_term, String result, String index) {
            this.overflow_list = overflow_list;
            this.first_term = first_term;
            this.second_term = second_term;
            this.result = result;
            this.index = index;
        }
    }

    public FlowPane createFlow(){
        FlowPane pane = new FlowPane();

        pane.setMaxWidth(35);
        pane.setPrefWidth(pane.getMaxWidth());

        LimitedTextField textField = new LimitedTextField();
        textField.setMaxLength(1);
        textField.setPrefWidth(30);
        textField.setPrefWidth(textField.getPrefWidth());

        textField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches(Const.regex_bin)) {
                textField.setText("");
            }
        });

        textField.setOnKeyReleased(this::behaviour);
        pane.getChildren().addAll(new Text(" "), textField);
        return pane;
    }

    public void separate_digits(ActionEvent actionEvent) {
        String s = ((TextField)separatorPane.getChildren().get(0)).getText();
        int position;

        try {
            position = Integer.parseInt(s);
        }
        catch (Exception e){
            return;
        }

        for (FlowPane field : this.fields) {
            if (((Text) field.getChildren().get(0)).getText().equals(".")) {
                ((Text) field.getChildren().get(0)).setText(" ");
            }
        }

        if(position >= 0 && position < this.digit){

            if(position != 0) {
                ((Text) this.fields.get(this.digit - position).getChildren().get(0)).setText(".");
                ((Text) this.fields.get(2 * this.digit - position).getChildren().get(0)).setText(".");
                ((Text) this.fields.get(2 * this.digit - 1 + position).getChildren().get(0)).setText(".");
            }
        }

    }

    public void addDigit(){
        FlowPane pane;

        FlowPane t  = this.createFlow();
        FlowPane t1 = this.createFlow();
        FlowPane t2 = this.createFlow();

        ArrayList<FlowPane> arrayList = new ArrayList<>();
        arrayList.add(t);
        arrayList.add(t1);
        arrayList.add(t2);

        SwitchCarry switchCarry = new SwitchCarry();
        switchCarry.column = 1;
        switchCarry.row    = 0;

        for (int j = 1; j < 4; j++) {
            for (int i = 0; i < this.digit; i++) {
                if (j==3){
                    pane = this.fields.get(this.digit * j -  this.digit + i );
                    this.digitNet.getChildren().remove(pane);
                    this.digitNet.add(pane,  this.digit-i+1,j+1);
                }
                else {
                    pane = this.fields.get( this.digit * j -1 - i);
                    this.digitNet.getChildren().remove(pane);
                    this.digitNet.add(pane,  this.digit-i+1,j);
                }
            }
            if (j==3){
                this.digitNet.add(arrayList.get(j-1),1,j+1);
            }
            else {
                this.digitNet.add(arrayList.get(j-1),1,j);
            }

        }

        for (int i = this.carryList.size() - 1; i >= 0 ; i--) {
            this.digitNet.getChildren().remove(this.carryList.get(i).get());
            this.digitNet.add(this.carryList.get(i).get(), i + 2, 0);
            this.carryList.get(i).column++;
        }
        this.digitNet.add(switchCarry.button,1,0);
        GridPane.setHalignment(switchCarry.button, HPos.CENTER);

        this.carryList.add(0,switchCarry);

        this.fields.add(0,t);
        this.fields.add(this.digit+1,t1);
        this.fields.add(t2);

        this.digit++;

        this.change_line();
    }

    public void removeDigit(){


        if (this.digit == 1) return;

        FlowPane f = this.fields.get(0);
        FlowPane f1 = this.fields.get(this.fields.size()-1);
        FlowPane f2 = this.fields.get(this.digit);

        this.digitNet.getChildren().remove(f);
        this.digitNet.getChildren().remove(f1);
        this.digitNet.getChildren().remove(f2);

        this.fields.remove(f);
        this.fields.remove(f1);
        this.fields.remove(f2);

        //i - номер строки, j - столбца
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < this.digit-1; j++) {
                if(i != 2) {
                    f = this.fields.get(j + (this.digit - 1) * i);
                    if ( j == 0) ((Text)f.getChildren().get(0)).setText(" ");
                    this.digitNet.getChildren().remove(f);
                    this.digitNet.add(f, j+1, i+1);
                }
                else{
                    f = this.fields.get(this.fields.size()-1-j);
                    if ( j == 0) ((Text)f.getChildren().get(0)).setText(" ");
                    this.digitNet.getChildren().remove(f);
                    this.digitNet.add(f, j+1, i+2);
                }
            }
        }



        this.digit--;
        this.change_line();

    }

    private void change_line() {
        this.canvas.setWidth(this.digit*(this.fields.get(0).getPrefWidth()));
        this.canvas.getGraphicsContext2D().strokeLine(0, 2, this.canvas.getWidth(), 2);
        this.digitNet.getChildren().remove(this.canvas);
        this.digitNet.add(this.canvas,1,3, this.digit,1);
    }

    private void behaviour(KeyEvent keyEvent) {

        FlowPane flowPane = (FlowPane) ((TextField)keyEvent.getSource()).getParent();

        Pattern pattern = Pattern.compile("^[01]$");
        Matcher matcher = pattern.matcher(keyEvent.getText());

        if (matcher.matches()) {
            try {
                ListIterator<FlowPane> iterator = fields.listIterator(fields.indexOf(flowPane) + 1);
                if (iterator.hasNext()) {
                    iterator.next().getChildren().get(1).requestFocus();
                }
            } catch (Exception ignored) {}
        }
        else {
            ((TextField)flowPane.getChildren().get(1)).setText(((TextField)flowPane.getChildren().get(1)).getText());
        }
    }

    public void create_overflows(int count){
        for (int i = 0; i < count; i++) {
            SwitchCarry carry = new SwitchCarry();
            carryList.add(carry);
        }
    }

    public ADD_SUB_INSTRUMENT(String label_text, int index, Step step, int digit) {
        super(label_text, index, step);
        ((Button)separatorPane.getChildren().get(1)).setOnAction(this::separate_digits);

        Button b = new Button("print json");
        b.setOnAction(actionEvent -> this.getInform());

        this.separatorPane.getChildren().addAll(this.plusButton, this.minusButton, b);
        this.digitNet = new GridPane();
        this.digitNet.setGridLinesVisible(true);

        this.digit = digit;
        this.create_overflows(digit-1);

        for (int j = 1; j < 4; j++) {
            for (int i = 1; i < digit+1; i++) {
                if(i<digit && j == 1) {
                    this.carryList.get(i-1).row = 0;
                    this.carryList.get(i-1).column = i;
                    this.digitNet.add(this.carryList.get(i-1).button, i, 0);
                    GridPane.setHalignment(this.carryList.get(i-1).button, HPos.CENTER);
                }

                FlowPane pane = this.createFlow();
                if(j != 3){
                    fields.add(pane);
                    this.digitNet.add(pane, i, j);
                }
                else {
                    fields.add(2*this.digit, pane);
                    this.digitNet.add(pane, i, j+1);
                }
            }
        }

        canvas = new Canvas(this.digit*(this.fields.get(0).getPrefWidth()),3);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.FUCHSIA);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) canvas.getWidth(), 2);
        this.digitNet.add(canvas,1,3, this.digit,1);

        this.plusButton.setOnAction(actionEvent -> this.addDigit());
        this.minusButton.setOnAction(actionEvent -> this.removeDigit());

        this.digitNet.add(this.signLabel, 0, 2);
        GridPane.setHalignment(this.signLabel, HPos.RIGHT);
        this.digitNet.add(this.separatorPane, 0, 0);
        this.titledPane.setContent(this.digitNet);

    }

    public String getInform() {
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();
        StringBuilder res = new StringBuilder();
        StringBuilder overflows = new StringBuilder();

        for (int i = 0; i < this.digit; i++) {

            if(((Text) this.fields.get(i).getChildren().get(0)).getText().equals(".")){
                first.append(((Text) this.fields.get(i).getChildren().get(0)).getText());
            }
            first.append(((LimitedTextField) this.fields.get(i).getChildren().get(1)).getText());
        }

        for (int i = this.digit; i < this.digit*2; i++) {

            if(((Text) this.fields.get(i).getChildren().get(0)).getText().equals(".")){
                second.append(((Text) this.fields.get(i).getChildren().get(0)).getText());
            }
            second.append(((LimitedTextField) this.fields.get(i).getChildren().get(1)).getText());
        }

        for (int i = this.digit * 2; i < this.fields.size(); i++) {

            res.insert(0, ((LimitedTextField) this.fields.get(i).getChildren().get(1)).getText());
            if(((Text) this.fields.get(i).getChildren().get(0)).getText().equals(".")){
                res.insert(0, ((Text) this.fields.get(i).getChildren().get(0)).getText());
            }
        }

        for (SwitchCarry switchCarry : this.carryList) {
            overflows.append(switchCarry.getValue());
        }

        //Gson g = new Gson();
        //System.out.println(g.toJson(new add_sub_json(overflows.toString(), first.toString(), second.toString(), res.toString(), String.valueOf(this.index))));

        return new Gson().toJson(new add_sub_json(overflows.toString(), first.toString(), second.toString(), res.toString(), String.valueOf(this.index)));
    }

}
