public class Order {
  private String _header;
  private String _data;
  private String _footer;
  public Order() {}
  public void setHeader(String header) { this._header = header; }
  public void setData(String data) { this._data = data; }
  public void setFooter(String footer) { this._footer = footer; }
  public String toString() {
    return (this._header + this._data + this._footer);
  }
}
