package edu.wpi.first.outlineviewer;

import javafx.beans.property.SimpleStringProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class TableEntryParent implements TableEntry {

  private SimpleStringProperty key;

  @SuppressWarnings("JavadocMethod")
  public TableEntryParent(String key) {
    checkNotNull(key, "A key must be provided to create a piece of table data");

    this.key = new SimpleStringProperty(key);
  }

  public SimpleStringProperty getKey() {
    return key;
  }

  /**
   * Sees if the data within this structure is metadata (i.e. has a key
   * bookended by tildes ("~") and is in all caps). Used to show/hide metadata
   * leaves in branches.
   */
  public boolean isMetadata() {
    return key.getValue().startsWith("~")
        && key.getValue().endsWith("~")
        && key.getValue().toUpperCase().equals(key);
  }

}
