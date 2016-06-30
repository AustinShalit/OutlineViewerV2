package edu.wpi.first.outlineviewer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableEntryData {

  private SimpleStringProperty key;
  private SimpleObjectProperty value;
  private SimpleStringProperty type;

  private static final String[] typeNames
      = new String[]{"Boolean", "Number", "String", "Raw", "Boolean[]", "Number[]", "String[]"};

  @SuppressWarnings("JavadocMethod")
  public TableEntryData(String key, Object value) {
    this.key = new SimpleStringProperty(key);
    if (value != null) {
      this.value = new SimpleObjectProperty(value);
      this.type = new SimpleStringProperty(typeFromValue(value));
    }
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

  /**
   * Explicitly changes the type of this data. Used on BranchNodes when a
   * metadata entry comes in and to show what kind of system it shows data
   * from (such as "Speed controller", "Subsystem", etc.).
   */
  public void setType(String type) {
    this.type.set(type);
  }

  /**
   * Generates a type string based on the value of the table entry.
   */
  private String typeFromValue(Object value) {
    if (isMetadata()) {
      return "Metadata";
    }
    if (value instanceof Boolean) {
      return typeNames[0];
    }
    if (value instanceof Double) {
      return typeNames[1];
    }
    if (value instanceof String) {
      return typeNames[2];
    }
    if (value instanceof byte[]) {
      return typeNames[3];
    }
    if (value instanceof boolean[]) {
      return typeNames[4].substring(0, 8) + ((boolean[]) value).length + "]";
    }
    if (value instanceof double[]) {
      return typeNames[5].substring(0, 7) + ((double[]) value).length + "]";
    }
    if (value instanceof String[]) {
      return typeNames[6].substring(0, 7) + ((String[]) value).length + "]";
    }
    return "ERROR";
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
