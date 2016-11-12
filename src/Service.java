class Service {
  String name;
  int code;
  Double fee;
  Boolean status;

  // makes new objects
  public Service(String name, Double fee) {
    setName(name);
    setCode(0);
    setFee(fee);
    setStatus(true);
  }

  // makes objects for DB Wrapper
  public Service(int code, String name, Double fee, int status) {
    setName(name);
    setCode(code);
    setFee(fee);
    if (status == 1) {
      setStatus(true);
    } else {
      setStatus(false);
    }
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setCode(int code) {
    this.code = code;
  }

  private void setFee(Double fee) {
    this.fee = fee;
  }

  private void setStatus(Boolean status) {
    this.status = status;
  }

  public String getName() {
    return name;
  }

  public int getCode() {
    return code;
  }

  public double getFee() {
    return fee;
  }

  public Boolean getStatus() {
    return status;
  }

  @Override
  public String toString() {
    return code + "\t" + name + "\t$" + fee;
  }

}
