package sample.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.model.Memory;
import sample.util.ViewUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MemoryController implements Initializable {


    public Button memory_next;
    public TextField memory_name;
    public TextField memory_size;
    public Button memory_add;
    public ListView<Memory> memory_list;
    public TextField memory_start;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Memory> memoryList = FXCollections.observableArrayList();
        memory_list.setItems(memoryList);

        memory_add.setOnMouseClicked(mouseEvent -> {
            if (memory_size.getText() != null && memory_start.getText() != null) {
                if (ViewUtil.isNumeric(memory_size.getText(), 1) && ViewUtil.isNumeric(memory_start.getText(), 0)) {
                    Memory memory = new Memory();
                    memory.setName(memory_name.getText());
                    memory.setSize(Integer.parseInt(memory_size.getText()));
                    memory.setStartingAddress(Integer.parseInt(memory_start.getText()));
                    memory.setHole(true);
                    memoryList.add(memory);
                    ViewUtil.setNextName(memory_name, "H", memoryList.size());
                } else {
                    ViewUtil.validate(memory_start, 1);
                    ViewUtil.validate(memory_size, 0);
                }
            }
        });

        memory_next.setOnMouseClicked(mouseEvent -> {
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

        memory_size.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(memory_size, 1));
        memory_start.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(memory_start, 0));
    }
}
