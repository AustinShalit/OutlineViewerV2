package edu.wpi.first.outlineviewer;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.networktables.NetworkTablesJNI;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeSortMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.text.Text;

public class TableViewerController {

  @FXML
  private TreeTableView<TableEntryData> table;

  @FXML
  private Text connectionIndicator;

  @FXML
  void initialize() {
    TreeTableColumn<TableEntryData, String> keyCol = new TreeTableColumn("Key");
    TreeTableColumn<TableEntryData, String> valueCol = new TreeTableColumn("Value");
    TreeTableColumn<TableEntryData, String> typeCol = new TreeTableColumn("Type");
    table.getColumns().setAll(keyCol, valueCol, typeCol);

    keyCol.setCellValueFactory(p -> p.getValue().getValue().getKey());
    valueCol.setCellValueFactory(p -> p.getValue().getValue().getValue());
    typeCol.setCellValueFactory(p -> p.getValue().getValue().getType());

    keyCol.setSortType(TreeTableColumn.SortType.DESCENDING);
    table.setSortMode(TreeSortMode.ALL_DESCENDANTS);

    TreeItem<TableEntryData> root = new TreeItem<>(new TableEntryData("Root", null));
    root.setExpanded(true);
    table.setRoot(root);

    setupTableListener(root, "");
    setupSubTableListeners("");

    NetworkTablesJNI.addConnectionListener((uid, connected, conn) -> {
      if (NetworkTable.getTable("").isServer()) {
        connectionIndicator.setText("Number of Clients:\t"
            + NetworkTablesJNI.getConnections().length);
      } else {
        connectionIndicator.setText("Connection Status: " + connected);
      }
    }, true);

  }

  private void setupSubTableListeners(String networktable) {
    NetworkTable.getTable(networktable).addSubTableListener((source, key, value, isNew) -> {
      TreeItem<TableEntryData> subtable = new TreeItem<>(new TableEntryData(key, null));
      table.getRoot().getChildren().add(subtable);
      setupTableListener(subtable);
    });
  }

  private void setupTableListener(TreeItem<TableEntryData> table) {
    setupTableListener(table, table.getValue().getKey().getValue());
  }

  private void setupTableListener(TreeItem<TableEntryData> table, String networkTable) {
    NetworkTable.getTable(networkTable).addTableListener((source, key, value, isNew) -> {
      table.getChildren().removeIf(t -> t.getValue().getKey().getValue().equals(key));
      TreeItem item = new TreeItem<>(new TableEntryData(key, value));
      table.getChildren().add(item);
    });
  }
}
