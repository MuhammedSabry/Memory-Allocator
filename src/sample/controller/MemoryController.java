package sample.controller;


import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
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


    public StackedBarChart memory_chart;
    public NumberAxis address_axis;

    static List<Memory> memoryList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        memory_chart.setLegendVisible(false);

        memoryList = new ArrayList<>();

        memory_add.setOnMouseClicked(mouseEvent -> {
            //Checking if the text fields contain numbers only or not
            if (ViewUtil.isNumeric(memory_size.getText(), 1) && ViewUtil.isNumeric(memory_start.getText(), 0)) {
                //If there are already holes in the list
                if (!memoryList.isEmpty()) {
                    //Must check if the starting address isn't lower than the last one in the list (No conflicts)
                    if (Integer.valueOf(memory_start.getText()) >= (memoryList.get(memoryList.size() - 1).getSize() + memoryList.get(memoryList.size() - 1).getStartingAddress())) {

                        //Creating the hole to add
                        Memory memory = new Memory();
                        memory.setName(memory_name.getText());
                        memory.setSize(Integer.parseInt(memory_size.getText()));
                        memory.setStartingAddress(Integer.parseInt(memory_start.getText()));
                        memory.setHole(true);


                        Memory last = memoryList.get(memoryList.size() - 1);

                        //Checking if there
                        int startingAdd = last.getStartingAddress() + last.getSize();
                        int index;
                        if (startingAdd < Integer.valueOf(memory_start.getText())) {
                            addGap(startingAdd, Integer.valueOf(memory_start.getText()) - startingAdd);
                            index = memoryList.size() - 1;
                        } else
                            index = memoryList.size();
                        memoryList.add(memory);
                        prepareChart(index);
                    } else {
                        //Give a little warning about the invalid field
                        ViewUtil.validate(memory_start, (memoryList.get(memoryList.size() - 1).getStartingAddress()
                                + memoryList.get(memoryList.size() - 1).getSize()));
                    }
                }
                //If it's the first time to add a hole
                else {

                    Memory memory = new Memory();
                    memory.setName(memory_name.getText());
                    memory.setSize(Integer.parseInt(memory_size.getText()));
                    memory.setStartingAddress(Integer.parseInt(memory_start.getText()));
                    memory.setHole(true);

                    memoryList.add(memory);
                    prepareChart(0);
                }
                //Setting the name of the hole
                ViewUtil.setNextName(memory_name, "H", memoryList.size());
            }
            //Then there must be a wrong field Give a little warning about the invalid field
            else {
                ViewUtil.validate(memory_start, 0);
                ViewUtil.validate(memory_size, 1);
            }
        });

        memory_next.setOnMouseClicked(mouseEvent ->
        {
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

        memory_size.textProperty().
                addListener((observableValue, oldValue, newValue) -> ViewUtil.validate(memory_size, 1));
        memory_start.textProperty().
                addListener((observableValue, oldValue, newValue) -> {
                    if (memoryList.isEmpty())
                        ViewUtil.validate(memory_start, 0);
                    else
                        ViewUtil.validate(memory_start, (memoryList.get(memoryList.size() - 1).getStartingAddress()
                                + memoryList.get(memoryList.size() - 1).getSize()));
                });
    }

    private void addGap(int startingAdd, int i) {
        Memory memory = new Memory();
        memory.setHole(false);
        memory.setName("OS");
        memory.setStartingAddress(startingAdd);
        memory.setSize(i);
        memoryList.add(memory);
    }

    private void prepareChart(int index) {
        address_axis.setLowerBound(0);
        address_axis.setUpperBound(memoryList.get(memoryList.size() - 1).getStartingAddress() + memoryList.get(memoryList.size() - 1).getSize());
        address_axis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                for (Memory memory : memoryList)
                    if (number.intValue() == memory.getStartingAddress() + memory.getSize())
                        return String.valueOf(number.intValue());
                return null;
            }

            @Override
            public Number fromString(String s) {
                return null;
            }
        });

        for (int i = index; i < memoryList.size(); i++) {
            XYChart.Series series = new XYChart.Series();
            series.getData().add(new XYChart.Data<>("", memoryList.get(i).getSize()));
            series.setName("Label");
            memory_chart.getData().add(series);
            if (memoryList.get(i).isHole())
                memory_chart.lookup(".default-color" + i + ".chart-bar").setStyle("-fx-bar-fill: GREY;");
            else
                memory_chart.lookup(".default-color" + i + ".chart-bar").setStyle("-fx-bar-fill: rgba(96,96,96,0.34);");
        }
    }
}
