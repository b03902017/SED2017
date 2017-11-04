import java.io.*;
import java.util.*;
import java.lang.*;

public class OnlineShoppingSite implements OrderHandlingFunctionality {
  // Key of _companies is <companyType,category>
  private Map<Pair<String, String>, Company> _companies;
  private Map<Integer, Order> _orders;
  private Map<Integer, String> _orderNumToCompanyType;
  private static final String[] _categories =
      new String[] {"CSV", "XML", "Object"};
  private static final CSV CSVWriter = new CSV();
  private static final XML XMLWriter = new XML();
  private static final CustomObject CustomObjectWriter = new CustomObject();
  public OnlineShoppingSite() {
    this._companies = new HashMap<Pair<String, String>, Company>();
    this._orders = new HashMap<Integer, Order>();
    this._orderNumToCompanyType = new HashMap<Integer, String>();
  }
  public void createOrder(Order order, Integer orderNum, String goodsType,
                          String orderData, String category) {
    switch (category) {
    case "CSV":
      fitFormat(CSVWriter, order, orderData);
      break;
    case "XML":
      fitFormat(XMLWriter, order, orderData);
      break;
    case "Object":
      fitFormat(CustomObjectWriter, order, orderData);
      break;
    }
    this._orders.put(orderNum, order);
    this._orderNumToCompanyType.put(orderNum, goodsType);
  }
  public void transmitOrder(Integer orderNum) {
    Order order = this._orders.get(orderNum);
    String companyType = this._orderNumToCompanyType.get(orderNum);
    System.out.printf("%s company receive order %d:\n", companyType, orderNum);
    System.out.println(order);
  }
  private void fitFormat(FormatWriter formatwriter, Order order,
                         String orderData) {
    formatwriter.addHeader(order);
    formatwriter.addData(order, orderData);
    formatwriter.addFooter(order);
  }
  public void addCompany(Company company) {
    String companyType = company.companyType();
    String category = company.category();
    Pair<String, String> key = new Pair(companyType, category);
    this._companies.put(key, company);
  }
  public String getOrderCategory(String goodsType) {
    for (String category : this._categories) {
      if (this._companies.get(new Pair(goodsType, category)) != null) {
        return category;
      }
    }
    return null;
  }
  public boolean orderNumExists(Integer orderNum) {
    if (this._orders.get(orderNum) != null) {
      return true;
    } else {
      return false;
    }
  }
}
