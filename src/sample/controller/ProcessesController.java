package sample.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sample.model.Process;
import sample.util.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controller.MemoryController.FIRST_FIT;
import static sample.controller.MemoryController.allocationType;
import static sample.controller.MemoryController.memoryList;


public class ProcessesController implements Initializable {

    public TextField process_name;
    public TextField process_size;
    public Button process_add;
    public VBox memory_vBox;
    public TableView<Process> processes_table;
    public Button btn_allocate;
    public Button btn_deAllocate;

    private ObservableList<Process> processList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ViewUtil.drawMemory(memoryList, 0, memory_vBox);

        TableColumn<Process, String> col1 = new TableColumn<>("Process");
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col1.setSortable(false);

        TableColumn<Process, Integer> col2 = new TableColumn<>("Size");
        col2.setCellValueFactory(new PropertyValueFactory<>("size"));
        col2.setSortable(false);


        processes_table.getColumns().setAll(col1, col2);
        processes_table.setItems(processList);


        process_add.setOnMouseClicked(mouseEvent -> {
            if (ViewUtil.isNumeric(process_size.getText(), 1)) {
                Process process = new Process(process_name.getText(), Integer.valueOf(process_size.getText()), false);
                processList.add(process);
                process_name.setText("P" + (processList.size() + 1));
            } else
                ViewUtil.validate(process_size, 1);
        });

        //Validate the process size text field on the input
        process_size.textProperty().addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(process_size, 1));


        //To put a color of the row weather the process is allocated or not
//        processes_table.setRowFactory((column) -> new TableRow<Process>() {
//            @Override
//            protected void updateItem(Process process, boolean empty) {
//                super.updateItem(process, empty);
//                if (process == null || empty) { //If the cell is empty
//                    setStyle("");
//                } else { //If the cell is not empty
//
//                    if (process.isAllocated())
//                        setStyle("-fx-background-color: rgba(0,255,37,0.49)"); //The background of the cell in yellow
//                    else
//                        setStyle("-fx-background-color: rgba(255,15,0,0.51)"); //The background of the cell in yellow
//                }
//            }
//        });

        //To turn off cells Re-Ordering
        processes_table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) processes_table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });

        //To check if a process can be allocated or de-allocated
        processes_table.getSelectionModel().selectedItemProperty().addListener((observableValue, process, t1) -> {
            if (process != null) {
                if (process.isAllocated()) {

                    btn_allocate.setDisable(true);
                    btn_deAllocate.setDisable(false);
                } else {
                    btn_allocate.setDisable(false);
                    btn_deAllocate.setDisable(true);
                }
            }
        });

        btn_allocate.setOnMouseClicked(mouseEvent -> {
            Process process = processes_table.getSelectionModel().getSelectedItem();
            if (process != null) {
                if (allocationType.equals(FIRST_FIT))
                    firstFitAllocator(process);
                else
                    bestFitAllocator(process);
            }
        });

        btn_deAllocate.setOnMouseClicked(mouseEvent -> {
            Process process = processes_table.getSelectionModel().getSelectedItem();
            if (process != null) {
                processDeAllocator(process);
            }
        });
    }

    private void bestFitAllocator(Process process) {

    }

    private void firstFitAllocator(Process process) {

    }

    private void processDeAllocator(Process process) {

    }
}
