package sample.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ProcessesController implements Initializable {

    public Button process_next;
    public TextField process_name;
    public TextField process_size;
    public Button process_add;
    public ListView process_list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
