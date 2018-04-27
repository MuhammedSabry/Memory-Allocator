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

    public static void drawMemory(List<Memory> memoryList, int startingIndex, VBox parentVBox) {
        parentVBox.getChildren().clear();
        for (int i = startingIndex; i < memoryList.size(); i++) {
            Memory memory = memoryList.get(i);

            //Rectangle representing the fragment
            Rectangle rectangle = new Rectangle(180, 45);

            //Customizing the background color of the box
            if (memory.isHole())
                rectangle.setFill(Color.GREY);
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
                bane.setAlignment(startingLabel, Pos.CENTER);
            } else
                bane = new BorderPane(stackPane, null, null, endingAddressLabel, null);

            bane.setAlignment(endingAddressLabel, Pos.CENTER);
            parentVBox.getChildren().add(bane);
        }
    }
}
