/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.LimitedTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ieeeAdd extends IEEE {

    public Canvas sep1;
    public Canvas sep2;
    public Canvas sep3;
    public Canvas sep4;
    public Canvas sep5;
    public Canvas sep6;

    public ieeeAdd(int index, Step step) {
        super("Сложение в IEEE 754", index, step);
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
        create.setOnAction(this::create_digit_net);
        this.digitNet.add(create,6,0);
    }

    private void create_digit_net(ActionEvent actionEvent) {
        try {
            this.checkValue();
        }
        catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return;
        }

        for (FlowPane n: numbers) {
            this.ieee_grid.getChildren().remove(n);
        }
        numbers.clear();
        ieee_grid.getChildren().remove(t);
        ieee_grid.getChildren().remove(t1);
        ieee_grid.getChildren().remove(t2);
        ieee_grid.getChildren().remove(t21);
        ieee_grid.getChildren().remove(t22);
        ieee_grid.getChildren().remove(t23);
        ieee_grid.getChildren().remove(t31);
        ieee_grid.getChildren().remove(t32);
        ieee_grid.getChildren().remove(t33);
        ieee_grid.getChildren().remove(sep1);
        ieee_grid.getChildren().remove(sep2);
        ieee_grid.getChildren().remove(sep3);
        ieee_grid.getChildren().remove(sep4);
        ieee_grid.getChildren().remove(sep5);
        ieee_grid.getChildren().remove(sep6);

        int digit_sign = Integer.parseInt(sign.getText());
        int digit_exponent = Integer.parseInt(expAmount.getText());
        int digit_mant = Integer.parseInt(mantAmount.getText());

        Text comma = new Text(",");
        comma.setFont(new Font(20));

        FlowPane imag_one = createFlow();
        imag_one.getChildren().remove(0);
        imag_one.getChildren().add(comma);

        FlowPane imag_two = createFlow();
        imag_two.getChildren().remove(0);
        imag_two.getChildren().add(comma);

        FlowPane imag_three = createFlow();
        imag_three.getChildren().remove(0);
        imag_three.getChildren().add(comma);

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

        sep3 = new Canvas(3,30);
        gc = sep3.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());

        sep4 = new Canvas(3,30);
        gc = sep4.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());

        sep5 = new Canvas(3,30);
        gc = sep5.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());

        sep6 = new Canvas(3,30);
        gc = sep6.getGraphicsContext2D();
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
        gc.strokeLine(0, 2, (int) sep1.getWidth(), (int) sep1.getHeight());


        //Сепаратор между знаком и порядком
        this.ieee_grid.add(sep1, digit_sign, 1);
        this.ieee_grid.add(sep3, digit_sign, 3);
        this.ieee_grid.add(sep5, digit_sign, 5);

        //Сепаратор между мантиссой и порядком
        this.ieee_grid.add(sep2, digit_sign + digit_exponent + 1, 1);
        this.ieee_grid.add(sep4, digit_sign + digit_exponent + 1, 3);
        this.ieee_grid.add(sep6, digit_sign + digit_exponent + 1, 5);

        for(int k=1;k<6;k=k+2) {
            FlowPane pane;

            //блок новой генерации
            //генерация полей для знака
            for (int i = 0; i < digit_sign; i++) {
                pane = this.createFlow();
                numbers.add(pane);
                this.ieee_grid.add(pane, i, k);
            }

            //генерация полей для порядка
            for (int i = 0; i < digit_exponent; i++) {
                pane = this.createFlow();
                numbers.add(pane);
                this.ieee_grid.add(pane, digit_sign + 1 + i, k);
            }

            //генерация полей для мантиссы
            for (int i = 0; i < digit_mant; i++) {
                pane = this.createFlow();
                numbers.add(pane);
                this.ieee_grid.add(pane, digit_sign + digit_exponent + 2 + i, k);
            }


        }
        this.ieee_grid.add(imag_one,digit_sign + digit_exponent, 2,3,1);
        this.ieee_grid.add(imag_two,digit_sign + digit_exponent, 4, 3, 1);
        this.ieee_grid.add(imag_three,digit_sign + digit_exponent, 6, 3, 1);
        numbers.add(imag_one);
        numbers.add(imag_two);
        numbers.add(imag_three);

        this.ieee_grid.add(t,0,2,digit_sign ,1);
        this.ieee_grid.add(t1,digit_sign + 1,2,digit_exponent ,1);
        this.ieee_grid.add(t2,digit_sign + digit_exponent + 2,2,digit_mant ,1);

        this.ieee_grid.add(t21,0,4,digit_sign ,1);
        this.ieee_grid.add(t22,digit_sign + 1,4,digit_exponent ,1);
        this.ieee_grid.add(t23,digit_sign + digit_exponent + 2,4,digit_mant ,1);

        this.ieee_grid.add(t31,0,6,digit_sign ,1);
        this.ieee_grid.add(t32,digit_sign + 1,6,digit_exponent ,1);
        this.ieee_grid.add(t33,digit_sign + digit_exponent + 2,6,digit_mant ,1);
        
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
