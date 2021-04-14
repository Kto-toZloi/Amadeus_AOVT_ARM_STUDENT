/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.SwitchCarry;
import javafx.geometry.HPos;
import javafx.scene.layout.GridPane;


public class AdditionInstrument extends ADD_SUB_INSTRUMENT {

    public SwitchCarry result_carry = new SwitchCarry();

    public AdditionInstrument(int index, Step step, int digit) {
        super("Сложение в столбик", index, step, digit);

        signLabel.setText("+");

        this.result_carry.row = 4;
        this.result_carry.column = 0;
        this.result_carry.isOverFlowRes = true;
        this.digitNet.add(this.result_carry.get(),0,4);
        GridPane.setHalignment(this.result_carry.get(),HPos.RIGHT);
    }

    @Override
    public void removeDigit(){
        SwitchCarry s = this.carryList.get(0);

        this.digitNet.getChildren().remove(s.get());
        this.carryList.remove(s);

        for (int i = 0; i < this.digit-2; i++) {
            s = this.carryList.get(i);
            this.digitNet.getChildren().remove(s.get());
            this.digitNet.add(s.get(), s.column-1, s.row);
            s.column--;
        }
        super.removeDigit();

    }

    @Override
    public void checkValue() throws IllegalArgumentException {

    }
}