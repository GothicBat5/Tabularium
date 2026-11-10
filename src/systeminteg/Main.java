package systeminteg;

import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class Main extends Application
{
    private final DataModel model = new DataModel();

    private final ChartPanel chartPanel = new ChartPanel();
    private final PieChart pieChart = new PieChart();
    private final Label totalLabel = new Label();

    private final ComboBox<ChartPanel.Type> typeBox = new ComboBox<>();
    private final ComboBox<String> labelBox = new ComboBox<>();
    private final ComboBox<String> valueBox = new ComboBox<>();
    private final ComboBox<String> groupBox = new ComboBox<>();

    private boolean updatingBoxes = false;

    @Override
    public void start(Stage stage)
    {
        loadSampleData();

        pieChart.setTitle("Distribution");
        pieChart.setAnimated(false);
        pieChart.setPrefHeight(300);

        totalLabel.setStyle("-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" + "-fx-text-fill: #2563eb;");

        typeBox.getItems().setAll(ChartPanel.Type.values());
        typeBox.getSelectionModel().select(ChartPanel.Type.BAR);
        typeBox.setOnAction(e -> changeChartType());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, totalLabel, spacer, new Label("Chart:"), typeBox);
        header.setAlignment(Pos.CENTER_LEFT);

        Button newButton = new Button("New");
        newButton.setOnAction(e -> createNewSheet());

        HBox controls = new HBox(10,
            newButton,
            new Label("Label:"), labelBox,
            new Label("Value:"), valueBox,
            new Label("Group:"), groupBox);

        controls.setAlignment(Pos.CENTER_LEFT);

        labelBox.setOnAction(e -> refreshCharts());
        valueBox.setOnAction(e -> refreshCharts());
        groupBox.setOnAction(e -> refreshCharts());

        EditorPanel editor = new EditorPanel(model);
        VBox.setVgrow(editor, Priority.ALWAYS);

        VBox leftPanel = new VBox(15, header, controls, chartPanel, editor);
        VBox rightPanel = new VBox(15, pieChart);

        leftPanel.setPrefWidth(650);
        rightPanel.setPrefWidth(350);

        HBox dashboard = new HBox(20, leftPanel, rightPanel);

        dashboard.setPadding(new Insets(20));
        dashboard.setAlignment(Pos.TOP_CENTER);
        dashboard.setStyle("-fx-background-color: #f4f6f9;");

        refreshBoxes();
        model.addStructureListener(this::refreshBoxes);
        model.addDataListener(this::refreshCharts);

        Scene scene = new Scene(dashboard, 1050, 780);

        stage.setTitle("VisualData");
        stage.setScene(scene);
        stage.show();
    }

    private void loadSampleData()
    {
        String[] products = {
            "Laptop", "Mouse", "Keyboard",
            "Monitor", "Headset", "Webcam"
        };

        String[] categories = {
            "Electronics", "Accessories", "Accessories",
            "Electronics", "Accessories", "Electronics"
        };

        int[] quantities = {15, 40, 25, 12, 30, 18};

        model.addColumn("Product");
        model.addColumn("Category");
        model.addColumn("Quantity");

        for (int i = 0; i < products.length; i++)
        {
            model.addRow(products[i], categories[i], String.valueOf(quantities[i]));
        }
    }

    private void changeChartType()
    {
        ChartPanel.Type type = typeBox.getValue();

        if (type != null)
        {
            chartPanel.setType(type);
        }
    }

    private void createNewSheet()
    {
        if (!model.getRows().isEmpty())
        {
            Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Discard the current data and start a blank sheet?",
                ButtonType.OK,
                ButtonType.CANCEL);

            alert.setTitle("New Sheet");
            alert.setHeaderText(null);

            boolean confirmed = alert.showAndWait()
                .filter(button -> button == ButtonType.OK)
                .isPresent();

            if (!confirmed)
            {
                return;
            }
        }

        clearBoxSelections();
        model.newSheet("Name", "Category", "Quantity");
        model.addRow();
    }

    private void clearBoxSelections()
    {
        updatingBoxes = true;

        labelBox.getSelectionModel().clearSelection();
        valueBox.getSelectionModel().clearSelection();
        groupBox.getSelectionModel().clearSelection();

        updatingBoxes = false;
    }

    private void refreshBoxes()
    {
        updatingBoxes = true;

        fillBox(labelBox, 0);
        fillBox(valueBox, 2);
        fillBox(groupBox, 1);

        updatingBoxes = false;
    }

    private void fillBox(ComboBox<String> box, int defaultIndex)
    {
        int previous = box.getSelectionModel().getSelectedIndex();
        int size = model.getColumns().size();

        box.getItems().setAll(model.getColumns());

        if (previous >= 0 && previous < size)
        {
            box.getSelectionModel().select(previous);
        }
        else
        {
            box.getSelectionModel().select(Math.min(defaultIndex, size - 1));
        }
    }

    private void refreshCharts()
    {
        if (updatingBoxes)
        {
            return;
        }

        int labelCol = labelBox.getSelectionModel().getSelectedIndex();
        int valueCol = valueBox.getSelectionModel().getSelectedIndex();
        int groupCol = groupBox.getSelectionModel().getSelectedIndex();

        if (labelCol < 0 || valueCol < 0 || groupCol < 0)
        {
            return;
        }

        Map<String, Double> barTotals = model.getGroupTotals(labelCol, valueCol);
        Map<String, Double> pieTotals = model.getGroupTotals(groupCol, valueCol);
        double total = model.getTotal(valueCol);

        chartPanel.update(barTotals, labelBox.getValue(), valueBox.getValue());
        updatePieChart(pieTotals);

        totalLabel.setText("Total " + valueBox.getValue() + ": " + format(total));
    }

    private void updatePieChart(Map<String, Double> totals)
    {
        pieChart.getData().clear();

        for (Map.Entry<String, Double> entry : totals.entrySet())
        {
            if (entry.getValue() > 0)
            {
                pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
            }
        }
    }

    private String format(double value)
    {
        if (value == Math.floor(value))
        {
            return String.valueOf((long) value);
        }

        return String.format("%.2f", value);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}