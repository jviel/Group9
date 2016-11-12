class Service {
	String name;
	int code;
	double fee;
	boolean status;
	
	// add Validated-constructor for objects from DB Wrapper

	public Service(String name, int code, double fee) {
		this.name = name;
		this.code = code;
		this.fee = fee;
		status = false;
		return;
	}

	public void setName(String name) {
		return;
	}
	

	public String getName() {
		return "";
	}
}