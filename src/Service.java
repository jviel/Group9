class Service {
	String name;
	int code;
	Double fee;
	boolean status;
	
	// add Validated-constructor for objects from DB Wrapper

	public Service(String name, int code, double fee) {
		setName(name);
		setCode(code);
		setFee(fee);
		setStatus(true);
		return;
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
	
	private void setStatus(boolean status) {
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
	
	public boolean getStatus() {
		return status;
	}
	
	@Override
	public String toString() {
		return code + "\t" + name + "\t$" + fee;
	}
	
}