import java.io.*;
import java.util.*;
import java.lang.*;

class YTDSalesChart {
  private class Sale implements Comparable<Sale> {
    private Integer _monthNum;
    private Integer _totalPrice;
    Sale(Integer monthNum, Integer totalPrice) {
      this._monthNum = monthNum;
      this._totalPrice = totalPrice;
    }
    public String toString() {
      return String.format("month %d price %d", this._monthNum,
                           this._totalPrice);
    }
    public int compareTo(Sale sale) {
      return (this._monthNum - sale._monthNum);
    }
  }

  private List<Sale> _sales;
  YTDSalesChart() { this._sales = new ArrayList<Sale>(); }
  public void addSale(Integer monthNum, Integer totalPrice) {
    Sale sale = new Sale(monthNum, totalPrice);
    this._sales.add(sale);
  }
  public void display() {
    Collections.sort(_sales);
    for (Sale s : _sales) {
      System.out.println(s);
    }
  }
}

class MonthlyReport {
  private class Transaction {
    private String _product;
    private Integer _price;
    Transaction(String product, Integer price) {
      this._product = product;
      this._price = price;
    }
    public String product() { return this._product; }
    public Integer price() { return this._price; }
  }
  private List<Transaction> _transactions;
  MonthlyReport() {
    this._transactions = new ArrayList<Transaction>();
  }
  public void addTransaction(String product, Integer price) {
    Transaction transaction = new Transaction(product, price);
    this._transactions.add(transaction);
  }
  public void display() {
    for (int i = 0; i < this._transactions.size(); i++) {
      Transaction tran = this._transactions.get(i);
      String transactionInfo =
          String.format("%s %d", tran.product(), tran.price());
      System.out.println(transactionInfo);
    }
  }
}

class Store {
  private class Department {
    private YTDSalesChart _chart;
    private Map<Integer, MonthlyReport> _reports;
    Department() {
      _chart = new YTDSalesChart();
      _reports = new HashMap<Integer, MonthlyReport>();
    }
    public void addRepot(Integer monthNum, List<String> products,
                         List<Integer> prices) {
      if (this._reports.get(monthNum) == null) {
        MonthlyReport report = new MonthlyReport();
        this._reports.put(monthNum, report);
      }
      MonthlyReport report = this._reports.get(monthNum);
      Integer totalPrice = 0;
      for (int i = 0; i < products.size(); i++) {
        report.addTransaction(products.get(i), prices.get(i));
        totalPrice += prices.get(i);
      }
      this._chart.addSale(monthNum, totalPrice);
    }
    public void display(String departmentName, Integer monthNum) {
      if (this._reports.get(monthNum) == null) {
        System.out.println("no data in selected month!");
      } else {
        MonthlyReport report = this._reports.get(monthNum);
        System.out.printf("display MonthlyReport for %s month %d\n",
                          departmentName, monthNum);
        report.display();
        System.out.printf("display YTDSalesChart for %s\n", departmentName);
        this._chart.display();
      }
    }
  }

  private Map<String, Department> _deparments;
  public void refresh() {}
  Store() { this._deparments = new HashMap<String, Department>(); }
  public Department getDepartment(String departmentName) {
    return this._deparments.get(departmentName);
  }
  public void addDepartment(String departmentName) {
    Department department = new Department();
    this._deparments.put(departmentName, department);
  }
  public void selectDepartment(String departmentName, Integer monthNum) {
    Department department = this._deparments.get(departmentName);
    department.display(departmentName, monthNum);
  }
  public boolean existDepartment(String departmentName) {
    return (this._deparments.get(departmentName) != null);
  }
  public void addRepot(String departmentName, Integer monthNum,
                       List<String> products, List<Integer> prices) {
    Department department = this._deparments.get(departmentName);
    department.addRepot(monthNum, products, prices);
  }
  public String toString() {
    return String.format("department nums :%d", this._deparments.size());
  }
}

public class Main {
  static Store store = new Store();
  public static void main(String[] argv) {
    try {
      File inputFile = new File(argv[0]);
      Scanner scanner = new Scanner(inputFile);
      readInput(scanner);
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static void readInput(Scanner scanner) {
    while (scanner.hasNextLine()) {
      String[] line = scanner.nextLine().split(" ");
      if (line[0].equals("department")) {
        String departmentName = line[1];
        if (!store.existDepartment(departmentName)) {
          store.addDepartment(departmentName);
        }
        Integer monthNum = Integer.parseInt(line[2]);
        List<String> products = new ArrayList<String>();
        List<Integer> prices = new ArrayList<Integer>();
        for (int i = 3; i < line.length; i += 1) {
          String[] transactionInfo = line[i].split(",");
          String product = transactionInfo[0];
          Integer price = Integer.parseInt(transactionInfo[1]);
          products.add(product);
          prices.add(price);
        }
        store.addRepot(departmentName, monthNum, products, prices);
      } else if (line[0].equals("select")) {
        String departmentName = line[1];
        Integer monthNum = Integer.parseInt(line[2]);
        store.selectDepartment(departmentName, monthNum);
      }
    }
  }
}
