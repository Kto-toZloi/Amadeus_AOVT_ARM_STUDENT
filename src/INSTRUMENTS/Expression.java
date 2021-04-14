/*
 * Copyright (c) amadeus-aco.ru
 */

package INSTRUMENTS;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.HTMLEditor;

public class Expression extends Instrument {
    public TextField textField;
    public TextFlow textFlow;

    String sup = " ⁺⁻⁼⁽⁾⁰¹²³⁴⁵⁶⁷⁸⁹ᴬᵃᴭᵆᵄᵅᶛᴮᵇᶜᶝᴰᵈᶞᴱᵉᴲᵊᵋᶟᵌᶠᴳᵍᶢˠʰᴴʱᴵⁱᶦᶤᶧᶥʲᴶᶨᶡᴷᵏˡᴸᶫᶪᶩᴹᵐᶬᴺⁿᶰᶮᶯᵑᴼᵒᵓᵔᵕᶱᴽᴾᵖᶲʳᴿʴʵʶˢᶳᶴᵀᵗᶵᵁᵘᶸᵙᶶᶣᵚᶭᶷᵛⱽᶹᶺʷᵂˣʸᶻᶼᶽᶾꝰᵜᵝᵞᵟᶿᵠᵡᵸჼˤⵯ";
    String supchars = " +−=()0123456789AaÆᴂɐɑɒBbcɕDdðEeƎəɛɜɜfGgɡɣhHɦIiɪɨᵻɩjJʝɟKklLʟᶅɭMmɱNnɴɲɳŋOoɔᴖᴗɵȢPpɸrRɹɻʁsʂʃTtƫUuᴜᴝʉɥɯɰʊvVʋʌwWxyzʐʑʒꝯᴥβγδθφχнნʕⵡ";

    String subchars=" +−=()0123456789aeəhijklmnoprstuvxβγρφχ";
    String sub=" ₊₋₌₍₎₀₁₂₃₄₅₆₇₈₉ₐₑₔₕᵢⱼₖₗₘₙₒₚᵣₛₜᵤᵥₓᵦᵧᵨᵩᵪ";

    char[] csup = sup.toCharArray();
    char[] characters = supchars.toCharArray();
    char[] csub = sub.toCharArray();
    char[] character = subchars.toCharArray();

    public Expression(int index, Step step) {
        super("Математическое выражение", index, step);
        /*this.digitNet = new GridPane();
        textField = new TextField("QQ");
        textFlow = new TextFlow();
        Button upper = new Button("^");
        Button lower = new Button("v");
        upper.setOnAction(actionEvent -> this.superscript());
        lower.setOnAction(actionEvent -> this.subscript());
        digitNet.add(textFlow,0,0);
        digitNet.add(upper,1,0);
        digitNet.add(lower,2,0);
        this.titledPane.setContent(this.digitNet);
        //textField.setFont(Font.font("Verdana",1));
        */

        final HTMLEditor htmlEditor = new HTMLEditor();
        titledPane.setContent(htmlEditor);

        // add a custom button to the top toolbar.
        Node node = htmlEditor.lookup(".top-toolbar");
        if (node instanceof ToolBar) {
            ToolBar bar = (ToolBar) node;
            ToggleButton supButton = new ToggleButton("x²");
            ToggleButton subButton = new ToggleButton("x₂");
            TextField txt = new TextField();
            Button okBtn = new Button("OK");
            Button clrBtn = new Button("CLEAR");
            ToggleGroup group = new ToggleGroup();
            supButton.setToggleGroup(group);
            subButton.setToggleGroup(group);
            Separator v1=new Separator();
            v1.setOrientation(Orientation.VERTICAL);
            Separator v2=new Separator();
            v2.setOrientation(Orientation.VERTICAL);

            txt.setDisable(true);
            okBtn.setDisable(true);;
            clrBtn.setDisable(true);


            bar.getItems().add(v1);
            bar.getItems().add(supButton);
            bar.getItems().add(subButton);
            bar.getItems().add(v2);
            bar.getItems().add(txt);
            bar.getItems().add(okBtn);
            bar.getItems().add(clrBtn);

            okBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    System.out.println(htmlEditor.getHtmlText());
                    if (supButton.isSelected()) {
                        txt.setPromptText(" Enter the superscript text ");
                        String text = htmlEditor.getHtmlText().replaceAll("</p></body></html>", "");
                        text = text.replaceAll("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>", "");
                        System.out.println(text);
                        text="<p>"+text + "<sup>"+ txt.getText()+"</sup></p>";
                        System.out.println(text);
                        htmlEditor.setHtmlText(text);
                    }
                    else if (subButton.isSelected()) {
                        txt.setPromptText(" Enter the superscript text ");
                        String text = htmlEditor.getHtmlText().replaceAll("</p></body></html>", "");
                        text = text.replaceAll("<html dir=\"ltr\"><head></head><body contenteditable=\"true\"><p>", "");
                        System.out.println(text);
                        text=text + "<sub>"+ txt.getText()+"</sup></p>";

                        System.out.println(text);
                        htmlEditor.setHtmlText(text);
                    }
                }
            });
            clrBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    txt.clear();
                }
            });
            supButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (supButton.isSelected()) {
                        txt.setPromptText(" Enter the superscript text ");
                        txt.setDisable(false);
                        okBtn.setDisable(false);;
                        clrBtn.setDisable(false);
                    }
                }
            });
            subButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent arg0) {
                    if (subButton.isSelected()) {
                        txt.setPromptText(" Enter the subscript text ");
                        txt.setDisable(false);
                        okBtn.setDisable(false);;
                        clrBtn.setDisable(false);
                    }
                }
            });
        }
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

    public void superscript(){
        textFlow.getChildren().addAll(new Text("QQ"));
    }
    public void subscript(){

    }

    private String convertSupText(String dsup) {
        char[] cdsup = dsup.toCharArray();
        String data="";
        for (int i = 0; i < cdsup.length; i++) {
            for (int j = 0; j < characters.length; j++) {
                if (cdsup[i] == characters[j]) {
                    data = data + csup[j];
                }
            }
        }
        return data;
    }

    private String convertSubText(String dsup) {
        char[] cdsup = dsup.toCharArray();
        String data="";
        for (int i = 0; i < cdsup.length; i++) {
            for (int j = 0; j < character.length; j++) {
                if (cdsup[i] == character[j]) {
                    data = data + csub[j];
                }
            }
        }
        return data;
    }
}
