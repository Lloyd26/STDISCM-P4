package stdiscm.p4.view.model;

public class LoginRequest {
    private String idNumber;
    private String password;

    public LoginRequest(String idNumber, String password) {
        this.idNumber = idNumber;
        this.password = password;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getPassword() {
        return password;
    }
}