interface OrderHandlingFunctionality {
  public void createOrder(Order order, Integer orderNum, String goodsType,
                          String orderData, String category);
  public void transmitOrder(Integer orderNum);
}
