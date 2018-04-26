package sample.util;

import javafx.scene.control.TextField;

public class ViewUtil {
    /**
     * checking if the given String is a number or not
     * also checking if that number is bigger than the given int @Param :compare
     */
    static boolean isNumeric(String time, int compare) {
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
    static void validate(TextField textField, int compare) {
        if (textField.getText().isEmpty() || !isNumeric(textField.getText(), compare)) {
            {
                textField.setStyle("-fx-focus-color:RED");
                textField.requestFocus();
            }
        } else {
            textField.setStyle("");
        }
    }

}
