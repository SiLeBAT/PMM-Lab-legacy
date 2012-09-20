/**
 * 
 */
package org.hsh.bfr.db.gui.dbtable.editoren;

/**
 * @author Armin
 *
 */
public class KeyValue {

  private Object key;
  private Object value;

  KeyValue () {
  }

  KeyValue (Object key, Object value) {
      this.key=key;
      this.value=value;
  }

  public Object getKey() {
      return this.key;
  }
  public void setKey(Object key) {
      this.key=key;
  }

  public Object getValue() {
      return this.value;
  }
  public void setValue(String value) {
      this.value=value;
  }

  @Override
  public String toString() {
      return this.value == null ? "" : this.value.toString();
  }

}