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
import sample.model.Process;
import sample.util.ViewUtil;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProcessesController implements Initializable {

    public Button process_next;
    public TextField process_name;
    public TextField process_size;
    public Button process_add;
    public ListView<Process> process_list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Process> processesList = FXCollections.observableArrayList();
        process_list.setItems(processesList);

        process_add.setOnMouseClicked(mouseEvent -> {
            if (process_size.getText() != null) {
                if (ViewUtil.isNumeric(process_size.getText(), 1)) {
                    Process process = new Process();
                    process.setName(process_name.getText());
                    process.setSize(Integer.parseInt(process_size.getText()));
                    processesList.add(process);
                    ViewUtil.setNextName(process_name, "P", processesList.size() + 1);
                } else
                    ViewUtil.validate(process_size, 1);
            }
        });

        process_next.setOnMouseClicked(mouseEvent -> {
            if (processesList.size() >= 1) {
                Parent data = null;
                try {
                    data = FXMLLoader.load(getClass().getResource("/sample/view/Allocator.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) process_next.getScene().getWindow();
                assert data != null;
                Scene scene = new Scene(data);
                stage.setScene(scene);
            }
        });

        process_size.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(process_size, 1));
    }
}
