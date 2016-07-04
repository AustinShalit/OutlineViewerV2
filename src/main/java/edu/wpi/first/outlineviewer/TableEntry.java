package edu.wpi.first.outlineviewer;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TreeItem;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class TableEntry {

  protected final ReadOnlyStringWrapper key;
  protected final SimpleObjectProperty value;
  protected final SimpleStringProperty type;

  private final TreeItem<TableEntry> treeItem;

  @SuppressWarnings("JavadocMethod")
  public TableEntry(final String key,
                    final Object value,
                    final String type) {
    checkNotNull(key, "A key must be provided to create a TableEntry");
    checkNotNull(value, "An value must be provided to create a TableEntry");
    checkNotNull(type, "A type must be provided to create a TableEntry");

    this.key = new ReadOnlyStringWrapper(key);
    this.value = new SimpleObjectProperty(value);
    this.type = new SimpleStringProperty(type);

    treeItem = new TreeItem<>(this);
  }

  public SimpleStringProperty getKey() {
    return key;
  }

  public SimpleObjectProperty getValue() {
    return value;
  }

  public SimpleStringProperty getType() {
    return type;
  }

  public TreeItem<TableEntry> getTreeItem() {
    return treeItem;
  }

  public String getNetworkTablePath() {
    if (isRoot()) {
      return "";
    }
    return getTreeItem().getParent().getValue().getNetworkTablePath() + "/" + getKey().getValue();
  }

  public boolean isRoot() {
    return getTreeItem().getParent() == null;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "\t" + getKey().getValue();
  }

  /**
   * Sees if the data within this structure is metadata (i.e. has a key
   * bookended by tildes ("~") and is in all caps). Used to show/hide metadata
   * leaves in branches.
   */
  public static boolean isMetadata(final String key) {
    return key.startsWith("~")
        && key.endsWith("~")
        && key.toUpperCase().equals(key);
  }
}
