package systeminteg;

import java.util.LinkedHashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;

public class ChartPanel extends StackPane
{
    public enum Type
    {
        BAR("Bar"),
        LINE("Line"),
        AREA("Area");

        private final String label;

        Type(String label)
        {
            this.label = label;
        }

        @Override
        public String toString()
        {
            return label;
        }
    }

    private Type type = Type.BAR;
    private Map<String, Double> totals = new LinkedHashMap<>();
    private String xLabel = "";
    private String yLabel = "";

    public ChartPanel()
    {
        setPrefHeight(320);
        render();
    }

    public void setType(Type type)
    {
        this.type = type;
        render();
    }

    public void update(Map<String, Double> totals, String xLabel, String yLabel)
    {
        this.totals = new LinkedHashMap<>(totals);
        this.xLabel = xLabel == null ? "" : xLabel;
        this.yLabel = yLabel == null ? "" : yLabel;
        render();
    }

    private void render()
    {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setAutoRanging(false);
        xAxis.setCategories(FXCollections.observableArrayList(totals.keySet()));
        xAxis.setLabel(xLabel);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(yLabel);

        XYChart<String, Number> chart = createChart(xAxis, yAxis);

        chart.setTitle("Data Overview");
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.getData().add(buildSeries());

        getChildren().setAll(chart);
    }

    private XYChart<String, Number> createChart(CategoryAxis xAxis, NumberAxis yAxis)
    {
        switch (type)
        {
            case LINE: return new LineChart<>(xAxis, yAxis);

            case AREA: return new AreaChart<>(xAxis, yAxis);

            default: return new BarChart<>(xAxis, yAxis);
        }
    }

    private XYChart.Series<String, Number> buildSeries()
    {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Double> entry : totals.entrySet())
        {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        return series;
    }
}