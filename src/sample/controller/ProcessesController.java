package sample.controller;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import sample.model.Memory;
import sample.model.Process;
import sample.util.ViewUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static sample.controller.MemoryController.memoryList;


public class ProcessesController implements Initializable {

    private String FIRST_FIT = "First Fit";

    public ChoiceBox allocator_type;
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

        //Setting allocator_type selection box items
        allocator_type.setItems(FXCollections.observableArrayList(FIRST_FIT, "Best Fit"));
        allocator_type.setValue(FIRST_FIT);

        //Drawing the initial memory to be played with
        ViewUtil.drawMemory(memoryList, memory_vBox);

        //Setting up the Table columns
        TableColumn<Process, String> col1 = new TableColumn<>("Process");
        col1.setCellValueFactory(new PropertyValueFactory<>("name"));
        col1.setSortable(false);

        TableColumn<Process, Integer> col2 = new TableColumn<>("Size");
        col2.setCellValueFactory(new PropertyValueFactory<>("size"));
        col2.setSortable(false);


        processes_table.getColumns().setAll(col1, col2);
        processes_table.setItems(processList);


        //Adding new processes
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


        //To turn off cells Re-Ordering
        processes_table.skinProperty().addListener((obs, oldSkin, newSkin) -> {
            final TableHeaderRow header = (TableHeaderRow) processes_table.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((o, oldVal, newVal) -> header.setReordering(false));
        });

        //To check if a process can be allocated or de-allocated
        processes_table.getSelectionModel().selectedItemProperty().addListener((observableValue, t1, process) -> {
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


        //Listen for allocating btn clicks
        btn_allocate.setOnMouseClicked(mouseEvent -> {
            Process process = processes_table.getSelectionModel().getSelectedItem();
            if (process != null) {
                if (allocator_type.getValue().equals(FIRST_FIT))
                    firstFitAllocator(process);
                else
                    bestFitAllocator(process);
                processes_table.getSelectionModel().clearSelection();
            }
        });

        //Listen for de-allocating btn clicks
        btn_deAllocate.setOnMouseClicked(mouseEvent -> {
            Process process = processes_table.getSelectionModel().getSelectedItem();
            if (process != null) {
                processDeAllocator(process);
                processes_table.getSelectionModel().clearSelection();
            }
        });
    }

    /**
     * Allocates the given process according to the smallest hole that can hold the process
     */
    private void bestFitAllocator(Process process) {
        int bestIndex = 0;
        int bestSize = 0;
        for (int i = 0; i < memoryList.size(); i++) {
            Memory memory = memoryList.get(i);
            if (memory.getSize() >= process.getSize() && memory.isHole()) {
                if (memory.getSize() < bestSize || bestSize == 0) {
                    bestSize = memory.getSize();
                    bestIndex = i;
                }
            }
        }
        if (bestSize != 0) {
            Memory memory = memoryList.get(bestIndex);

            memoryList.add(bestIndex, new Memory(process.getName(), memory.getStartingAddress(), process.getSize(), false));

            memory.setSize(memory.getSize() - process.getSize());
            memory.setStartingAddress(memory.getStartingAddress() + process.getSize());
            process.setAllocated(true);

        }
        ViewUtil.drawMemory(memoryList, memory_vBox);
    }

    /**
     * Allocates the given process according to the first hole that fits it
     */
    private void firstFitAllocator(Process process) {
        for (int i = 0; i < memoryList.size(); i++) {
            Memory memory = memoryList.get(i);
            if (memory.getSize() >= process.getSize() && memory.isHole()) {
                memoryList.add(i, new Memory(process.getName(), memory.getStartingAddress(), process.getSize(), false));
                memory.setSize(memory.getSize() - process.getSize());
                memory.setStartingAddress(memory.getStartingAddress() + process.getSize());
                process.setAllocated(true);
                break;
            }
        }
        ViewUtil.drawMemory(memoryList, memory_vBox);
    }


    /**
     * Method to de-allocate processes
     *
     * @param process:The process to be de-allocated
     */
    private void processDeAllocator(Process process) {
        for (int i = 0; i < memoryList.size(); i++) {

            //Checking if the current memoryList item is the process to be de-allocated
            if (memoryList.get(i).getName().equals(process.getName())) {
                //Removing the process from the memory list
                memoryList.remove(i);

                //Now we are checking on the item just right after the process in the memory list
                if (memoryList.get(i).isHole()) {
                    Memory memory = memoryList.get(i);
                    memory.setSize(memory.getSize() + process.getSize());
                    memory.setStartingAddress(memory.getStartingAddress() - process.getSize());
                } else {
                    //We should get the next hole that's after that process
                    for (int j = i; j < memoryList.size(); j++) {
                        if (memoryList.get(j).isHole()) {
                            memoryList.add(i, new Memory(memoryList.get(j).getName(), memoryList.get(i).getStartingAddress() - process.getSize(), process.getSize(), true));
                            break;
                        }
                    }
                }
                //Setting that the process is now free for allocation
                process.setAllocated(false);
                break;
            }
        }
        //Finally drawing the memory again
        ViewUtil.drawMemory(memoryList, memory_vBox);
    }
}
