package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import sample.model.Memory;
import sample.util.ViewUtil;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MemoryController implements Initializable {


    public Button memory_next;
    public TextField memory_name;
    public TextField memory_size;
    public Button memory_add;
    public TextField memory_start;

    public VBox memory_vBox;

    static List<Memory> memoryList = new ArrayList<>();

    private int currentAddress;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        //Listening for adding a new hole
        memory_add.setOnMouseClicked(mouseEvent -> {
            //Checking if the text fields contain numbers only or not
            if (ViewUtil.isNumeric(memory_size.getText(), 1) && ViewUtil.isNumeric(memory_start.getText(), 0)) {
                //Must check if the starting address isn't lower than the last one in the list (No conflicts)
                if (Integer.valueOf(memory_start.getText()) >= currentAddress) {

                    //Creating the hole to add
                    Memory memory = new Memory(memory_name.getText()
                            , Integer.parseInt(memory_start.getText())
                            , Integer.parseInt(memory_size.getText())
                            , true);

                    //In case that there is a gap between the previous hole and next one
                    if (Integer.valueOf(memory_start.getText()) > currentAddress)
                        addGap(currentAddress, Integer.valueOf(memory_start.getText()) - currentAddress);

                    //Finally adding the memory and drawing the memory
                    memoryList.add(memory);
                    ViewUtil.drawMemory(memoryList, memory_vBox);
                } else {
                    //Give a little warning about the invalid field
                    ViewUtil.validate(memory_start, (memoryList.get(memoryList.size() - 1).getStartingAddress()
                            + memoryList.get(memoryList.size() - 1).getSize()));
                }

                //Getting last memory item in the list
                Memory lastMemory = memoryList.get(memoryList.size() - 1);

                //Calculating the next valid starting address
                currentAddress = lastMemory.getStartingAddress() + lastMemory.getSize();

                //Setting the name of the hole
                memory_name.setText("H" + (Integer.valueOf(memoryList.get(memoryList.size() - 1).getName().substring(1)) + 1));
                memory_start.setText(String.valueOf(currentAddress));
            }
            //Then there must be a wrong field Give a little warning about the invalid field
            else {
                ViewUtil.validate(memory_start, 0);
                ViewUtil.validate(memory_size, 1);
            }
        });

        //Listening for the next button clicks to proceed with the next scene
        memory_next.setOnMouseClicked(mouseEvent -> {
            //Making sure the memoryList isn't empty
            if (memoryList.size() >= 1) {
                Parent data = null;
                try {
                    data = FXMLLoader.load(getClass().getResource("/sample/view/Processes.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) memory_next.getScene().getWindow();
                assert data != null;
                Scene scene = new Scene(data);
                stage.setScene(scene);
            }
        });

        //Listening to text changes and styling the text field accordingly
        memory_size.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(memory_size, 1));
        memory_start.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(memory_start, currentAddress));
    }

    /**
     * It adds an OS gap between the holes that aren't successive
     * @param startingAdd:the starting address that the gap should be starting at
     * @param gapSize:the size of the gap
     */
    private void addGap(int startingAdd, int gapSize) {
        Memory memory = new Memory("OS"
                , startingAdd
                , gapSize
                , false);
        memoryList.add(memory);
    }
}
