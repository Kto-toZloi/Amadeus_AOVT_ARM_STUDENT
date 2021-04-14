/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.LinkedList;

public abstract class IEEE extends Instrument  {
    public TextField sign;
    public TextField expAmount;
    public TextField mantAmount;
    public VBox vBox_IEEE = new VBox();
    public GridPane ieee_grid = new GridPane();
    public boolean notNew;

    public Text t  = new Text("Знак");
    public Text t1 = new Text("Порядок");
    public Text t2 = new Text("Мантисса");
    public Text t21 = new Text("Знак");
    public Text t22 = new Text("Порядок");
    public Text t23 = new Text("Мантисса");
    public Text t31  = new Text("Знак");
    public Text t32 = new Text("Порядок");
    public Text t33 = new Text("Мантисса");
    public LinkedList<FlowPane> numbers = new LinkedList<>();

    public IEEE(String label_text, int index, Step step){
        super(label_text,index,step);
        this.digitNet = new GridPane();
        this.digitNet.setGridLinesVisible(true);
        this.titledPane.setContent(vBox_IEEE);
        vBox_IEEE.getChildren().addAll(this.digitNet,this.ieee_grid);

        VBox.setMargin(this.ieee_grid,new Insets(10,0,10,0));

        //this.ieee_grid.setGridLinesVisible(true);

        //this.ieee_grid.add(new Button(),5,5);
    }


}
