package systeminteg;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataModel
{
    private final ObservableList<String> columns = FXCollections.observableArrayList();
    private final ObservableList<ObservableList<String>> rows = FXCollections.observableArrayList();

    private final List<Runnable> structureListeners = new ArrayList<>();
    private final List<Runnable> dataListeners = new ArrayList<>();

    public ObservableList<String> getColumns()
    {
        return columns;
    }

    public ObservableList<ObservableList<String>> getRows()
    {
        return rows;
    }

    public void addStructureListener(Runnable listener)
    {
        structureListeners.add(listener);
    }

    public void addDataListener(Runnable listener)
    {
        dataListeners.add(listener);
    }

    public void addColumn(String name)
    {
        columns.add(name);

        for (ObservableList<String> row : rows)
        {
            row.add("");
        }

        fireStructure();
    }

    public void removeColumn(int index)
    {
        if (columns.size() <= 1)
        {
            return;
        }

        columns.remove(index);

        for (ObservableList<String> row : rows)
        {
            row.remove(index);
        }

        fireStructure();
    }

    public void renameColumn(int index, String name)
    {
        columns.set(index, name);
        fireStructure();
    }

    public void addRow(String... values)
    {
        ObservableList<String> row = FXCollections.observableArrayList();

        for (int i = 0; i < columns.size(); i++)
        {
            row.add(i < values.length ? values[i] : "");
        }

        rows.add(row);
        fireData();
    }

    public void removeRow(int index)
    {
        rows.remove(index);
        fireData();
    }

    public void setCell(int row, int column, String value)
    {
        rows.get(row).set(column, value);
        fireData();
    }

    public double getTotal(int valueColumn)
    {
        double total = 0;

        for (ObservableList<String> row : rows)
        {
            total += parseNumber(row.get(valueColumn));
        }

        return total;
    }

    public Map<String, Double> getGroupTotals(int groupColumn, int valueColumn)
    {
        Map<String, Double> totals = new LinkedHashMap<>();

        for (ObservableList<String> row : rows)
        {
            String key = row.get(groupColumn).trim();

            if (key.isEmpty())
            {
                key = "(empty)";
            }

            double value = parseNumber(row.get(valueColumn));
            totals.merge(key, value, Double::sum);
        }

        return totals;
    }

    private double parseNumber(String text)
    {
        try
        {
            return Double.parseDouble(text.trim());
        }
        catch (NumberFormatException e)
        {
            return 0;
        }
    }

    private void fireStructure()
    {
        for (Runnable listener : structureListeners)
        {
            listener.run();
        }

        fireData();
    }

    private void fireData()
    {
        for (Runnable listener : dataListeners)
        {
            listener.run();
        }
    }
}