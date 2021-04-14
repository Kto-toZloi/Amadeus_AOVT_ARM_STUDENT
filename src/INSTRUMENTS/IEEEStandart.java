/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.LimitedTextField;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO вынести конструкцию числа в класс-предок?
public class IEEEStandart extends IEEE {

    public Canvas sep1;
    public Canvas sep2;

    public IEEEStandart(int index, Step step) {
        super("Стандарт IEEE 754", index, step);
        sign = new TextField();
        expAmount = new TextField();
        mantAmount = new TextField();
        this.digitNet.add(new Label("Знак:"),0,0);
        this.digitNet.add(sign, 1, 0);
        this.digitNet.add(new Label("Порядок:"),2,0);
        this.digitNet.add(expAmount, 3, 0);
        this.digitNet.add(new Label("Мантисса:"),4,0);
        this.digitNet.add(mantAmount, 5, 0);

        Button create = new Button("Сгенерировать разрядную сетку");
        create.setOnAction(actionEvent -> this.create_digit_net());
        this.digitNet.add(create,6,0);
    }

    public void create_digit_net(){

        try {
            this.checkValue();
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }

        //Блок очистки--------------------------------------
        for (FlowPane n: numbers) {
            this.ieee_grid.getChildren().remove(n);
        }
        numbers.clear();
        ieee_grid.getChildren().remove(t);
        ieee_grid.getChildren().remove(t1);
        ieee_grid.getChildren().remove(t2);
        ieee_grid.getChildren().remove(sep1);
        ieee_grid.getChildren().remove(sep2);
        //--------------------------------------------------


        int digit_sign = Integer.parseInt(sign.getText());
        int digit_exponent = Integer.parseInt(expAmount.getText());
        int digit_mant = Integer.parseInt(mantAmount.getText());

        Text comma = new Text(",");
        comma.setFont(new Font(20));

        FlowPane imag_one = createFlow();
        imag_one.getChildren().remove(0);
        imag_one.getChildren().add(comma);


        sep1 = new Canvas(3,30);
        GraphicsContext gc;
        gc = sep1.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());

        sep2 = new Canvas(3,30);
        gc = sep2.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());


        FlowPane pane;

        //блок новой генерации

        //генерация полей для знака
        for (int i = 0; i < digit_sign; i++) {
            pane = this.createFlow();
            numbers.add(pane);
            this.ieee_grid.add(pane,i,1);
        }
        //Сепаратор между знаком и порядком
        this.ieee_grid.add(sep1,digit_sign,1);
        //генерация полей для порядка
        for (int i = 0; i < digit_exponent; i++) {
            pane = this.createFlow();
            numbers.add(pane);
            this.ieee_grid.add(pane,digit_sign + 1 +  i,1);
        }
        //Сепаратор между мантиссой и порядком
        this.ieee_grid.add(sep2,digit_sign + digit_exponent + 1,1);
        //генерация полей для мантиссы
        for (int i = 0; i < digit_mant; i++) {
            pane = this.createFlow();
            numbers.add(pane);
            this.ieee_grid.add(pane,digit_sign + digit_exponent+ 2 + i,1);
        }

        this.ieee_grid.add(imag_one,digit_sign + digit_exponent, 2, 3, 1);
        numbers.add(imag_one);

        this.ieee_grid.add(t,0,3,digit_sign ,1);
        this.ieee_grid.add(t1,digit_sign + 1,3,digit_exponent ,1);
        this.ieee_grid.add(t2,digit_sign + digit_exponent + 2,3,digit_mant ,1);

        GridPane.setHalignment(t, HPos.CENTER);
        GridPane.setHalignment(t1, HPos.CENTER);
        GridPane.setHalignment(t2, HPos.CENTER);
        GridPane.setHalignment(imag_one, HPos.CENTER);


    }
//TODO собрать инфу
    @Override
    public String getInform() {
        return null;
    }

    @Override
    public void separate_digits(ActionEvent actionEvent) {

    }

    private FlowPane createFlow(){
        FlowPane pane = new FlowPane();
        pane.setOrientation(Orientation.HORIZONTAL);
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

    @Override
    public void checkValue() throws IllegalArgumentException {
        int digit_sign, digit_exponent, digit_mant;
        try {
            digit_sign = Integer.parseInt(sign.getText());
            digit_exponent = Integer.parseInt(expAmount.getText());
            digit_mant = Integer.parseInt(mantAmount.getText());
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Проверьте входные данные");
        }
        if(     digit_exponent < 0  || digit_sign < 0 || digit_mant < 0 ||
                digit_exponent > 11 || digit_sign > 5 || digit_mant > 52){
            throw new IllegalArgumentException("Проверьте входные данные");
        }

    }


    private void behaviour(KeyEvent keyEvent) {

        FlowPane flowPane = (FlowPane) ((TextField)keyEvent.getSource()).getParent();

        Pattern pattern = Pattern.compile("^[01]$");
        Matcher matcher = pattern.matcher(keyEvent.getText());

        if (matcher.matches()) {
            try {
                ListIterator<FlowPane> iterator = numbers.listIterator(numbers.indexOf(flowPane) + 1);
                if (iterator.hasNext()) {
                    iterator.next().getChildren().get(1).requestFocus();
                }
            } catch (Exception ignored) {}
        }
        else {
            ((TextField)flowPane.getChildren().get(1)).setText(((TextField)flowPane.getChildren().get(1)).getText());
        }
    }
}
