import java.io.*;
import java.util.*;
import java.lang.*;

public class Main {
  static OnlineShoppingSite site = new OnlineShoppingSite();
  public static void main(String[] args) {
    try {
      File inputFile = new File(args[0]);
      Scanner scanner = new Scanner(inputFile);
      readInput(scanner);
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void readInput(Scanner scanner) {
    String companyType, category, goodsType, orderData;
    Integer orderNum;
    scannerLoop:
    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");
      switch (line[0]) {
      case "company":
        companyType = line[1];
        category = line[2];
        Company company = new Company(companyType, category);
        site.addCompany(company);
        break;
      case "order":
        orderNum = Integer.parseInt(line[1]);
        goodsType = line[2];
        orderData = line[3];
        category = site.getOrderCategory(goodsType);
        if (category != null) {
          Order order = new Order();
          site.createOrder(order, orderNum, goodsType, orderData, category);
          System.out.printf("order %d: %s order created in %s format\n",
                            orderNum, goodsType, category);
        } else {
          System.out.println("no company can fulfill such order!");
        }
        break;
      case "transmit":
        orderNum = Integer.parseInt(line[1]);
        if (site.orderNumExists(orderNum)) {
          site.transmitOrder(orderNum);
        } else {
          System.out.println("no such order!");
        }
        break;
      default:
        System.out.printf("Error: illegal Input \"%s\"\n", line[0]);
        break scannerLoop;
      }
    }
  }
}
