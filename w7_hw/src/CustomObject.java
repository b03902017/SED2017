public class CustomObject implements FormatWriter {
  private static final String header = "<Object header>";
  private static final String footer = "<Object footer>";
  public void addHeader(Order order) { order.setHeader(header); }
  public void addData(Order order, String orderData) {
    order.setData(orderData);
  }
  public void addFooter(Order order) { order.setFooter(footer); }
}
