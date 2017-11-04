public class XML implements FormatWriter {
  private static final String header = "<XML header>";
  private static final String footer = "<XML footer>";
  public void addHeader(Order order) { order.setHeader(header); }
  public void addData(Order order, String orderData) {
    order.setData(orderData);
  }
  public void addFooter(Order order) { order.setFooter(footer); }
}
