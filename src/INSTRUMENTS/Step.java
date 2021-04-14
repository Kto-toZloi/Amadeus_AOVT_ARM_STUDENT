package INSTRUMENTS;/*
 * Copyright (c) amadeus-aco.ru
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Collections;

public class Step {


    public ArrayList<Instrument> instruments = new ArrayList<>();
    private final VBox vBox_list;//Список инструментов
    public VBox step_vbox = new VBox();//Шаг
    public int index;
    public ChoiceBox<String> choiceBox;
    public Button button = new Button("✖");
    public TextField stepResultField = new TextField();
    public Label label = new Label();
    public GridPane insHeader = new GridPane();//Заголовок для добавления инструмента
    public ChoiceBox<String> options_instrument;
    public TextField indexField = new TextField();
    public static ArrayList<Step> steps = new ArrayList<>();
    public TitledPane titledPane;//панель шага
    public Button addInstrument;
    public Button addInstrumentIndex;
    public TextField instIndexField = new TextField();
    public TextField firstParamField = new TextField("4");
    public TextField secondParamField = new TextField("4");

    private static final String STEP_DRAG_KEY = "INSTRUMENTS.Step";
    private static final ObjectProperty<Step> draggingStep = new SimpleObjectProperty<>();

    private final Background focusBackground = new Background( new BackgroundFill( Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY ) );
    private final Background unfocusBackground = new Background( new BackgroundFill( Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY ) );
    private final Background Green = new Background( new BackgroundFill( Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY ) );

    public String getInform(){
        return null;
    }

    public Step(int index, boolean flag){
        if (flag){
            for (Step step: steps) {
                step.change_index_add(index);
            }
        }
        //Установка стиля кнопки с крестом
        this.button.setStyle(Const.button_style);
        //Определение индекса шага
        this.index = index;
        steps.add(this);
        this.label.setText(index + ".");
        //Выпадающее меню с вариантами шагов из констант
        this.choiceBox = new ChoiceBox<>(Const.options_step);
        this.choiceBox.setValue(Const.options_step.get(0));

        this.step_vbox.setPadding(new Insets(10));
        VBox.setMargin(this.step_vbox, new Insets(3));
        this.step_vbox.setBorder(new Border(new BorderStroke(Color.DEEPPINK, null,null, null,
                BorderStrokeStyle.SOLID, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE, BorderStrokeStyle.NONE,
                CornerRadii.EMPTY, new BorderWidths(2), Insets.EMPTY)));

        FlowPane flowPane = new FlowPane();
        flowPane.getChildren().addAll(this.label ,this.choiceBox ,this.button ,new Label(Const.step_result), this.stepResultField);

        this.titledPane = new TitledPane();
        titledPane.setText("Панель инструментов");
        //titledPane.setExpanded(false);

        this.vBox_list = new VBox();


        //строка с выбором инструмента
        this.options_instrument = new ChoiceBox<>(Const.options_instrument);
        this.options_instrument.setValue(Const.options_instrument.get(0));
        this.options_instrument.setOnAction(event -> this.change_optional_fields());
        this.button.setOnAction(actionEvent -> this.delete_step());

        this.insHeader.add(new Label("Выберите инструмент из списка:"), 0, 0);
        this.insHeader.add(this.options_instrument,1,0,2,1);

        this.step_vbox.setOnDragDetected(this::onDragDetected);
        this.step_vbox.setOnDragOver(this::onDragOver);
        this.step_vbox.setOnDragDropped(this::onDragDropped);
        this.step_vbox.setOnDragExited(this::onDragDone);

        //Финальная строка генерации

        this.addInstrument = new Button("Добавить инструмент в конец");
        this.addInstrument.setOnAction(actionEvent -> this.createInstrument());

        this.addInstrumentIndex = new Button("Вставить инструмент по индексу");
        this.addInstrumentIndex.setOnAction(actionEvent -> this.createInstrumentIndex());

        this.insHeader.add(this.addInstrument, 0, 2);
        this.insHeader.add(this.addInstrumentIndex,1,2);
        this.insHeader.add(this.instIndexField, 2, 2);

        //Отцентровка элементов таблицы
        for (int i = 0; i < this.insHeader.getChildren().size(); i++) {
            GridPane.setHalignment(this.insHeader.getChildren().get(i), HPos.CENTER);
        }

        vBox_list.getChildren().addAll(this.insHeader);
        vBox_list.setFillWidth(true);
        titledPane.setContent(vBox_list);
        step_vbox.setFillWidth(true);
        step_vbox.getChildren().addAll(flowPane,titledPane);

    }

    private void createInstrumentIndex() {

    }//TODO: Сделать обязательно

    private void createInstrument() {
        switch (this.options_instrument.getValue()){
            case "Сложение в столбик":
                AdditionInstrument additionInstrument = new AdditionInstrument(vBox_list.getChildren().size(),this, Integer.parseInt(this.firstParamField.getText()));
                this.vBox_list.getChildren().addAll(additionInstrument.vBox);
                this.instruments.add(additionInstrument);
                break;
            case "Вычитание в столбик":
                SubtractionInstrument subtractionInstrument = new SubtractionInstrument(vBox_list.getChildren().size(),this, Integer.parseInt(this.firstParamField.getText()));
                this.vBox_list.getChildren().addAll(subtractionInstrument.vBox);
                this.instruments.add(subtractionInstrument);
                break;
            case "Умножение в столбик":
                MultiplicationInstrument multiplicationInstrument = new MultiplicationInstrument(vBox_list.getChildren().size(),this, Integer.parseInt(this.firstParamField.getText()));
                this.vBox_list.getChildren().addAll(multiplicationInstrument.vBox);
                this.instruments.add(multiplicationInstrument);
                break;
            case "Стандарт IEEE 754":
                IEEEStandart ieeeStandart = new IEEEStandart(vBox_list.getChildren().size(),this);
                this.vBox_list.getChildren().addAll(ieeeStandart.vBox);
                this.instruments.add(ieeeStandart);
                break;
            case "Перевод дробной части в другую СС умножением":
                TranslateFractionalPart translateFractionalPart = new TranslateFractionalPart(vBox_list.getChildren().size(),this);
                this.vBox_list.getChildren().addAll(translateFractionalPart.vBox);
                this.instruments.add(translateFractionalPart);
                break;
            case "Сложение в IEEE 754":
                ieeeAdd ieeeAdd = new ieeeAdd(vBox_list.getChildren().size(),this);
                this.vBox_list.getChildren().addAll(ieeeAdd.vBox);
                this.instruments.add(ieeeAdd);
                break;
            case "Математическое выражение":
                Expression expression = new Expression(vBox_list.getChildren().size(),this);
                this.vBox_list.getChildren().addAll(expression.vBox);
                this.instruments.add(expression);
                break;

            default:
                return;
        }
    }


    private void onDragDropped(DragEvent dragEvent) {

        if (dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="INSTRUMENTS.Step") {
            dragEvent.acceptTransferModes(TransferMode.ANY);
            Step source = draggingStep.get();

            source.step_vbox.setBackground(unfocusBackground);

            ObservableList<Node> list = ((VBox)this.step_vbox.getParent()).getChildren();

            if(source.index < this.index){

                int startIndex = source.index - 1;
                int endIndex = this.index - 1;
                int tempIndex = source.index;
                Step tmp;

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
                Step tmp;

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

            for (Step step: steps) {
                step.label.setText(step.index + ".");
            }
        }

    }

    private void onDragOver(DragEvent dragEvent) {
        if(dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="INSTRUMENTS.Step") {
            if(!draggingStep.get().equals(this)){
                dragEvent.acceptTransferModes(TransferMode.ANY);
                this.step_vbox.setBackground(Green);
            }
        }
    }

    private void onDragDone(DragEvent dragEvent) {
        //System.out.println(dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT));
        if(dragEvent.getDragboard().getContent(DataFormat.PLAIN_TEXT)=="INSTRUMENTS.Step") {
            if (!draggingStep.get().equals(this)) {
                System.out.println(dragEvent.getDragboard().getContentTypes());
                dragEvent.acceptTransferModes(TransferMode.ANY);
                this.step_vbox.setBackground(unfocusBackground);
            }
        }
    }

    public void onDragDetected(MouseEvent mouseEvent){

        for (Step step: steps ) {
            step.step_vbox.setBackground(unfocusBackground);
        }

        Dragboard dragboard = this.step_vbox.startDragAndDrop(TransferMode.ANY);
        ClipboardContent content = new ClipboardContent();
        content.putString(STEP_DRAG_KEY);
        draggingStep.setValue(this);
        this.step_vbox.setBackground(focusBackground);
        dragboard.setContent(content);
        mouseEvent.consume();

    }

    public void change_optional_fields() {
        // columnIndex, rowIndex
        switch (this.options_instrument.getValue()) {
            case "Сложение в столбик", "Вычитание в столбик", "Умножение в столбик" -> {
                this.insHeader.getChildren().clear();
                this.insHeader.add(new Label("Выберите инструмент из списка:"), 0, 0);
                this.insHeader.add(this.options_instrument, 1, 0);
                this.insHeader.add(new Label("Количество разрядов:"), 0, 1);
                this.insHeader.add(this.firstParamField, 1, 1);
                this.insHeader.add(this.addInstrument, 0, 2);
                this.insHeader.add(this.addInstrumentIndex, 1, 2);
                this.insHeader.add(this.instIndexField, 2, 2);
            }
            case "Деление в столбик" -> {
                this.insHeader.getChildren().clear();
                this.insHeader.add(new Label("Выберите инструмент из списка:"), 0, 0);
                this.insHeader.add(this.options_instrument, 1, 0);
                this.insHeader.add(new Label("Количество разрядов делимого:"), 0, 1);
                this.insHeader.add(this.firstParamField, 1, 1);
                this.insHeader.add(new Label("Количество разрядов делителя:"), 0, 2);
                this.insHeader.add(this.secondParamField, 1, 2);
                this.insHeader.add(this.addInstrument, 0, 3);
                this.insHeader.add(this.addInstrumentIndex, 1, 3);
                this.insHeader.add(this.indexField, 2, 3);
            }
            case "Перевод целой части числа в другую СС делением" -> {
                this.insHeader.getChildren().clear();
                this.insHeader.add(new Label("Выберите инструмент из списка:"), 0, 0);
                this.insHeader.add(this.options_instrument, 1, 0);
                this.insHeader.add(new Label("Количество разрядов :"), 0, 1);
                this.insHeader.add(this.firstParamField, 1, 1);
                this.insHeader.add(new Label("Система счисления:"), 0, 2);
                this.insHeader.add(this.secondParamField, 1, 2);
                this.insHeader.add(this.addInstrument, 0, 3);
                this.insHeader.add(this.addInstrumentIndex, 1, 3);
                this.insHeader.add(this.indexField, 2, 3);
            }
            case "Стандарт IEEE 754", "Сложение в IEEE 754", "Математическое выражение", "Перевод дробной части в другую СС умножением" -> {
                this.insHeader.getChildren().clear();
                this.insHeader.add(new Label("Выберите инструмент из списка:"), 0, 0);
                this.insHeader.add(this.options_instrument, 1, 0);
                this.insHeader.add(this.addInstrument, 0, 2);
                this.insHeader.add(this.addInstrumentIndex, 1, 2);
                this.insHeader.add(this.indexField, 2, 2);
            }
        }
    }

    public void delete_step() {
        ((VBox)this.step_vbox.getParent()).getChildren().remove(this.step_vbox);
        for (Step step: steps) {
            step.change_index(this.index);
        }
    }

    public void change_index(int index){
        if (this.index > index){
            this.index--;
            this.label.setText(this.index + ".");
        }
    }

    public void change_index_add(int index){
        if (this.index >= index){
            this.index++;
            this.label.setText(this.index + ".");
        }
    }
}