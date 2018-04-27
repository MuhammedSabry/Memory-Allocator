package sample.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import sample.model.Memory;

import java.util.List;

public class ViewUtil {

    /**
     * checking if the given String is a number or not
     * also checking if that number is bigger than the given int @Param :compare
     */
    public static boolean isNumeric(String time, int compare) {
        int inti;
        try {
            inti = Integer.parseInt(time);
        } catch (Exception e) {
            return false;
        }
        return inti >= compare;
    }

    /**
     * Styling method to check if a given field matches the properties
     * and style it accordingly
     */
    public static void validate(TextField textField, int compare) {
        if (textField.getText().isEmpty() || !isNumeric(textField.getText(), compare)) {
            {
                textField.setStyle("-fx-focus-color:RED");
                textField.requestFocus();
            }
        } else {
            textField.setStyle("");
        }
    }

    /**
     * Give it a List of memory and a VBox and it will draw it for you
     * Even organizing the memory before doing and drawing
     * Making sure that any adjacent same memory fragments are added together
     */
    public static void drawMemory(List<Memory> memoryList, VBox parentVBox) {
        //Making sure that the memory list is well organized before any drawing
        organizeMemory(memoryList);

        //Clearing all the previous drawings
        parentVBox.getChildren().clear();

        for (int i = 0; i < memoryList.size(); i++) {
            Memory memory = memoryList.get(i);

            //We just won't draw the zero sized holes (But they still exist !!!)
            if (memory.getSize() != 0) {
                //Rectangle representing the fragment
                Rectangle rectangle = new Rectangle(180, 45);

                //Customizing the background color of the box
                if (memory.isHole())
                    rectangle.setFill(Color.CADETBLUE);
                else if (memory.getName().substring(0, 1).equals("P"))
                    rectangle.setFill(Color.CORAL);
                else
                    rectangle.setFill(Color.DARKGREY);

                //Ending address label
                Label endingAddressLabel = new Label("0x" + (memory.getStartingAddress() + memory.getSize()));
                endingAddressLabel.setFont(Font.font("Courier", FontWeight.BOLD, 13));

                //Label inside the box
                StackPane stackPane = new StackPane(rectangle, new Label("Frag: " + memory.getName() + "\nSize: " + memory.getSize()));
                BorderPane bane;

                //In case it's the first box we add a 0x0 address
                if (i == 0) {
                    Label startingLabel = new Label("0x0");
                    startingLabel.setFont(Font.font("Courier", FontWeight.BOLD, 13));
                    bane = new BorderPane(stackPane, startingLabel, null, endingAddressLabel, null);
                    BorderPane.setAlignment(startingLabel, Pos.CENTER);
                } else
                    bane = new BorderPane(stackPane, null, null, endingAddressLabel, null);

                //Setting the right alignment on labels
                BorderPane.setAlignment(endingAddressLabel, Pos.CENTER);

                //Finally adding all that to the Parent VBox
                parentVBox.getChildren().add(bane);
            }
        }
    }

    /**
     * Give it a list of memory and it will add adjacent memory fragments together
     */
    private static void organizeMemory(List<Memory> memoryList) {
        for (int i = 0; i < memoryList.size(); i++) {
            //Making sure it's a hole also it's not the last one !
            if (memoryList.get(i).isHole() && i < (memoryList.size() - 1)) {
                //Making sure that the one after it is also a hole!
                if (memoryList.get(i + 1).isHole()) {
                    //Now adding their sizes together and removing the one after it from the list
                    memoryList.get(i).setSize(memoryList.get(i).getSize() + memoryList.get(i + 1).getSize());
                    memoryList.remove(i + 1);
                }
            }
        }
    }
}
