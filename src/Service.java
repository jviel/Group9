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
    public Service(int code, String name, Double fee, int status)
        throws InputException {
        String exceptionString = "";

        if (code < 0 || code > 999999) {
            exceptionString += "Code must be a six-digit integer.\n";
        }

        if (name.length() > 20) {
            exceptionString += "Name must have 20 characters or fewer.\n";
        }

        if (fee < 0 || fee > 9999.99) {
            exceptionString += "Fee must be between 0 and 9999.99 dollars";
        }

        if (!(exceptionString.isEmpty())) {
            exceptionString = exceptionString.substring(0,
                exceptionString.length() -1);
            throw new InputException(exceptionString);
        }

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
