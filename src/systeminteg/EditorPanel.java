package systeminteg;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class EditorPanel extends VBox
{
    private final DataModel model;
    private final TableView<ObservableList<String>> table = new TableView<>();

    public EditorPanel(DataModel model)
    {
        super(10);
        this.model = model;

        table.setEditable(true);
        table.setItems(model.getRows());
        table.setPlaceholder(new javafx.scene.control.Label("No rows yet. Click Add Row."));

        buildColumns();
        model.addStructureListener(this::buildColumns);

        getChildren().addAll(buildToolbar(), table);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private HBox buildToolbar()
    {
        Button addRow = new Button("Add Row");
        addRow.setOnAction(e -> model.addRow());

        Button deleteRow = new Button("Delete Row");
        deleteRow.setOnAction(e -> {
            int selected = table.getSelectionModel().getSelectedIndex();

            if (selected >= 0)
            {
                model.removeRow(selected);
            }
        });

        Button addColumn = new Button("Add Column");
        addColumn.setOnAction(e -> {
            String name = askName("Add Column", "");

            if (!name.isEmpty())
            {
                model.addColumn(name);
            }
        });

        return new HBox(10, addRow, deleteRow, addColumn);
    }

    private void buildColumns()
    {
        table.getColumns().clear();

        for (int i = 0; i < model.getColumns().size(); i++)
        {
            table.getColumns().add(createColumn(i));
        }
    }

    private TableColumn<ObservableList<String>, String> createColumn(int index)
    {
        TableColumn<ObservableList<String>, String> column = new TableColumn<>(model.getColumns().get(index));

        column.setCellValueFactory(data -> {
            ObservableList<String> row = data.getValue();
            String value = index < row.size() ? row.get(index) : "";
            return new SimpleStringProperty(value);
        });

        column.setCellFactory(TextFieldTableCell.forTableColumn());

        column.setOnEditCommit(event -> {
            int row = event.getTablePosition().getRow();
            model.setCell(row, index, event.getNewValue());
        });

        column.setSortable(false);
        column.setPrefWidth(140);
        column.setContextMenu(createHeaderMenu(index));

        return column;
    }

    private ContextMenu createHeaderMenu(int index)
    {
        MenuItem rename = new MenuItem("Rename Column");
        rename.setOnAction(e -> {
            String name = askName("Rename Column", model.getColumns().get(index));

            if (!name.isEmpty())
            {
                model.renameColumn(index, name);
            }
        });

        MenuItem delete = new MenuItem("Delete Column");
        delete.setOnAction(e -> model.removeColumn(index));

        return new ContextMenu(rename, delete);
    }

    private String askName(String title, String initial)
    {
        TextInputDialog dialog = new TextInputDialog(initial);

        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Name:");

        return dialog.showAndWait().map(String::trim).orElse("");
    }
}