package src;

public class Service {
    String name;
    int code;
    float fee;
    Boolean status;

    // ---- Constructors ----
    // makes service for DB Wrapper
    public Service(int code, String name, float fee, int status)
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

    // makes new service -- uses first constructor
    public Service (String name, float fee)
        throws InputException {
        this(0, name, fee, 1);
        return;
    }

    // ---- Setters and getters ---
    private void setName(String name) {
        this.name = name;
    }

    private void setCode(int code) {
        this.code = code;
    }

    private void setFee(float fee) {
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

    public float getFee() {
        return fee;
    }

    public Boolean getStatus() {
        return status;
    }

    // ---- Overrides ----
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(code).append("\t")
                 .append(name).append("\t$")
                 .append(fee).toString();
    }

    // Overrides to help Database Wrapper ----
    @Override
    public boolean equals (Object obj) {
        boolean ret;
        if (obj == null) {
            // no unit test
            ret = false;
        } else if (!Service.class.isAssignableFrom(obj.getClass())) {
            // no unit test
            ret = false;
        } else {
            final Service other = (Service) obj;
            if (!(compareStrings(name, other.getName())) ||
                (Math.abs(fee - other.getFee()) > 0.000000001)) {     // ugliness to compare floats
                // mismatch or service not in db
                ret = false;
            } else {
                // match
                ret = true;
            }
        }
        return ret;
    }

    private boolean compareStrings(String s1, String s2) {
        boolean ret;
        if (s1 == null && s2 != null) {
            // service not in db -- no unit test
            ret = false;
        } else if (!s1.equals(s2)) {
            // services do not match
            ret = false;
        } else {
            // match found in db
            ret = true;
        }
        return ret;
    }
}
