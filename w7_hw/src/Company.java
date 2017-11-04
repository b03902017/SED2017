public class Company {
  private String _companyType;
  private String _category;
  public Company(String companyType, String category) {
    this._companyType = companyType;
    this._category = category;
  }
  public String companyType() { return this._companyType; }
  public String category() { return this._category; }
}
