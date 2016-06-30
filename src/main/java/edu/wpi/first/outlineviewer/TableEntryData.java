package edu.wpi.first.outlineviewer;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import static com.google.common.base.Preconditions.checkNotNull;

public class TableEntryData implements TableEntry {

  private SimpleStringProperty key;
  private SimpleObjectProperty value;
  private SimpleStringProperty type;

  @SuppressWarnings("JavadocMethod")
  public TableEntryData(String key, Object value) {
    checkNotNull(key, "A key must be provided to create a piece of table data");
    checkNotNull(value, "Use the other constructor if you want to create a folder");

    this.key = new SimpleStringProperty(key);
    this.value = new SimpleObjectProperty<>(value);
    this.type = new SimpleStringProperty();
    if (!typeFromValue(value)) {
      throw new IllegalArgumentException("The provided object is not a valid type");
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
    checkNotNull(type, "Type cannot be null");

    this.type.setValue(type);
  }

  /**
   * Generates a type string based on the value of the table entry.
   */
  private boolean typeFromValue(Object value) {
    if (isMetadata()) {
      type.setValue("Metadata");
    } else if (value instanceof Boolean) {
      type.setValue("Boolean");
    } else if (value instanceof Double) {
      type.setValue("Number");
    } else if (value instanceof String) {
      type.setValue("String");
    } else if (value instanceof byte[]) {
      type.setValue("Raw");
    } else if (value instanceof boolean[]) {
      type.setValue("Boolean[" + ((boolean[]) value).length + "]");
    } else if (value instanceof double[]) {
      type.setValue("Number[" + ((double[]) value).length + "]");
    } else if (value instanceof String[]) {
      type.setValue("String[" + ((String[]) value).length + "]");
    } else {
      return false; // The method did not change the value of type
    }
    return true;
  }

  /**
   * Sees if the data within this structure is metadata (i.e. has a key
   * bookended by tildes ("~") and is in all caps). Used to show/hide metadata
   * leaves in branches.
   */
  public boolean isMetadata() {
    return key.getValue().startsWith("~")
        && key.getValue().endsWith("~")
        && key.getValue().toUpperCase().equals(key.getValue());
  }

}
