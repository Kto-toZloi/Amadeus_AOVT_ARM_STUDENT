/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import INSTRUMENTS.SUBCLASS.SwitchCarry;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class SubtractionInstrument extends ADD_SUB_INSTRUMENT {

    public SubtractionInstrument(int index, Step step, int digit) {
        super("Вычитание в столбик", index, step, digit);
        signLabel.setText("-");

        SwitchCarry switchCarry = new SwitchCarry();
        switchCarry.column = this.digit;
        switchCarry.row = 0;
        this.carryList.add(switchCarry);
        this.digitNet.add(switchCarry.get(),this.digit,0);

        GridPane.setHalignment(this.carryList.get(this.carryList.size()-1).button, HPos.CENTER);

    }

    @Override
    public void removeDigit(){

        if (this.digit == 1) return;

        SwitchCarry s = this.carryList.get(0);

        this.digitNet.getChildren().remove(s.get());
        this.carryList.remove(s);

        for (int i = 0; i < this.digit-1; i++) {
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
