package definitions.support;

import io.cucumber.spring.ScenarioScope;

public class TestState {

    public String email;
    public String password;
    public String token;
    public int status;
    public String responseBody;
    public String catalogBookId;
    public String catalogBookTitle;
    public String reservationId;
    public String librarianToken;
    public String patronId;
    public String librarianId;

    public void clear() {
        email = null;
        password = null;
        token = null;
        status = 0;
        responseBody = null;
        catalogBookId = null;
        catalogBookTitle = null;
        reservationId = null;
        librarianToken = null;
        patronId = null;
        librarianId = null;
    }
}