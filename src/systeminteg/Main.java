package systeminteg;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        String[] products = {
            "Laptop", "Mouse", "Keyboard",
            "Monitor", "Headset", "Webcam"
        };

        String[] categories = {
            "Electronics", "Accessories", "Accessories",
            "Electronics", "Accessories", "Electronics"
        };

        int[] quantities = {15, 40, 25, 12, 30, 18};
//bar chart

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Products");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Quantity");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        barChart.setTitle("Product Inventory");
        barChart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int total = 0;

        for (int i = 0; i < products.length; i++) 
        {
            series.getData().add(new XYChart.Data<>(products[i], quantities[i]));
            total += quantities[i];
        }

        barChart.getData().add(series);
        barChart.setPrefHeight(350);

        int electronics = 0;
        int accessories = 0;

        for (int i = 0; i < products.length; i++) 
        {
            if (categories[i].equals("Electronics")) 
            {
                electronics += quantities[i];
            } 
            else {
                accessories += quantities[i];
            }
        }

        PieChart pieChart = new PieChart(); 

        pieChart.setTitle("Inventory Distribution");
        pieChart.getData().addAll(new PieChart.Data("Electronics", electronics), new PieChart.Data("Accessories", accessories));
        pieChart.setPrefHeight(300);


        TableView<Product> table = new TableView<>();
        TableColumn<Product, String> nameCol = new TableColumn<>("Product");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());
        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());
        TableColumn<Product, Number> quantityCol = new TableColumn<>("Quantity");

        quantityCol.setCellValueFactory(data -> data.getValue().quantityProperty());

        table.getColumns().addAll(nameCol, categoryCol, quantityCol);

        for (int i = 0; i < products.length; i++) 
        {
            table.getItems().add(new Product(products[i], categories[i], quantities[i]));
        }

        table.setPrefHeight(250);


        Label totalLabel = new Label("Total Inventory: " + total + " units");

        totalLabel.setStyle("-fx-font-size: 22px;" +
            "-fx-font-weight: bold;" + "-fx-text-fill: #2563eb;");


        VBox leftPanel = new VBox(15, totalLabel, barChart, table);

        VBox rightPanel = new VBox(15, pieChart);

        leftPanel.setPrefWidth(650);
        rightPanel.setPrefWidth(350);

        HBox dashboard = new HBox(20, leftPanel, rightPanel);

        dashboard.setPadding(new Insets(20));
        dashboard.setAlignment(Pos.TOP_CENTER);

        dashboard.setStyle("-fx-background-color: #f4f6f9;");

        Scene scene = new Scene(dashboard, 850, 650);

        stage.setTitle("VisualData");
        stage.setScene(scene);
        stage.show();
    }


    public static class Product {

        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty category;
        private final javafx.beans.property.SimpleIntegerProperty quantity;

        public Product(String name, String category, int quantity) {

            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.category = new javafx.beans.property.SimpleStringProperty(category);
            this.quantity = new javafx.beans.property.SimpleIntegerProperty(quantity);
        }

        public javafx.beans.property.StringProperty nameProperty() 
        {
            return name;
        }

        public javafx.beans.property.StringProperty categoryProperty() 
        {
            return category;
        }

        public javafx.beans.property.IntegerProperty quantityProperty() 
        {
            return quantity;
        }
    }

    public static void main(String[] args) 
    {
        launch(args); //let's see
    }
}

