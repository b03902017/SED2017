public interface FormatWriter {
  public void addHeader(Order order);
  public void addData(Order order, String orderData);
  public void addFooter(Order order);
}
