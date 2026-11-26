package systeminteg;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
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
        table.setPlaceholder(new Label("No rows yet. Click Add Row."));
        table.setStyle("-fx-control-inner-background-alt: -fx-control-inner-background;");

        buildColumns();
        model.addStructureListener(this::buildColumns);

        getChildren().addAll(buildToolbar(), table);
        VBox.setVgrow(table, Priority.ALWAYS);
    }

    private HBox buildToolbar()
    {
        Button addRow = new Button("Add Row");
        addRow.setOnAction(e -> addRow());

        Button deleteRow = new Button("Delete Row");
        deleteRow.setOnAction(e -> deleteSelectedRow());

        Button addColumn = new Button("Add Column");
        addColumn.setOnAction(e -> addColumn());

        return new HBox(10, addRow, deleteRow, addColumn);
    }

    private void addRow()
    {
        model.addRow();

        int last = model.getRows().size() - 1;
        table.getSelectionModel().select(last);
        table.scrollTo(last);
    }

    private void deleteSelectedRow()
    {
        int selected = table.getSelectionModel().getSelectedIndex();

        if (selected < 0)
        {
            return;
        }

        model.removeRow(selected);

        int size = model.getRows().size();

        if (size > 0)
        {
            table.getSelectionModel().select(Math.min(selected, size - 1));
        }
    }

    private void addColumn()
    {
        String name = askName("Add Column", "");

        if (!name.isEmpty())
        {
            model.addColumn(name);
        }
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
        TableColumn<ObservableList<String>, String> column = new TableColumn<>();

        column.setGraphic(new HeaderEditor(index, column));

        column.setCellValueFactory(data -> {
            ObservableList<String> row = data.getValue();
            String value = index < row.size() ? row.get(index) : "";
            return new SimpleStringProperty(value);
        });

        column.setCellFactory(c -> new EditCell());

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

    private class HeaderEditor extends StackPane
    {
        private final int index;
        private final Label label;
        private final TextField field = new TextField();
        private boolean editing = false;

        HeaderEditor(int index, TableColumn<ObservableList<String>, String> column)
        {
            this.index = index;
            this.label = new Label(model.getColumns().get(index));

            getChildren().add(label);
            prefWidthProperty().bind(column.widthProperty().subtract(24));
            Tooltip.install(this, new Tooltip("Double-click to rename"));

            setOnMouseClicked(e -> {
                if (e.getClickCount() == 2)
                {
                    startEdit();
                }
            });

            field.setOnAction(e -> commit());

            field.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ESCAPE)
                {
                    cancel();
                }
            });

            field.focusedProperty().addListener((obs, wasFocused, focused) -> {
                if (!focused)
                {
                    commit();
                }
            });
        }

        private void startEdit()
        {
            editing = true;
            field.setText(label.getText());
            getChildren().setAll(field);

            Platform.runLater(() ->
            {
                field.requestFocus();
                field.selectAll();
            });
        }

        private void commit()
        {
            if (!editing)
            {
                return;
            }

            editing = false;
            getChildren().setAll(label);

            String name = field.getText().trim();

            if (!name.isEmpty() && !name.equals(label.getText()))
            {
                model.renameColumn(index, name);
            }
        }

        private void cancel()
        {
            editing = false;
            getChildren().setAll(label);
        }
    }

    private static class EditCell extends TableCell<ObservableList<String>, String>
    {
        private static final String FILLED_STYLE = "-fx-background-color: rgba(0, 0, 0, 0.08);";

        private TextField field;

        @Override
        public void startEdit()
        {
            super.startEdit();

            if (field == null)
            {
                createField();
            }

            field.setText(getItem() == null ? "" : getItem());
            setText(null);
            setGraphic(field);
            field.requestFocus();
            field.selectAll();
        }

        @Override
        public void cancelEdit()
        {
            super.cancelEdit();
            setText(getItem());
            setGraphic(null);
        }

        @Override
        protected void updateItem(String item, boolean empty)
        {
            super.updateItem(item, empty);

            if (empty)
            {
                setText(null);
                setGraphic(null);
                setStyle("");
                return;
            }

            boolean filled = item != null && !item.trim().isEmpty();
            setStyle(filled ? FILLED_STYLE : "");

            if (isEditing())
            {
                setText(null);
                setGraphic(field);
            }
            else {
                setText(item);
                setGraphic(null);
            }
        }

        private void createField()
        {
            field = new TextField();
            field.setOnAction(e -> commitEdit(field.getText()));

            field.setOnKeyPressed(e ->
            {
                if (e.getCode() == KeyCode.ESCAPE)
                {
                    cancelEdit();
                }
            });

            field.focusedProperty().addListener((obs, wasFocused, focused) ->
            {
                if (!focused && isEditing())
                {
                    commitEdit(field.getText());
                }
            });
        }
    }
}