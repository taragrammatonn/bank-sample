package md.maib.infra.authentication.dto;

public class AuthenticationRequest {

    private String pan;
    private String cvv;

    public AuthenticationRequest(String pan, String cvv) {
        this.pan = pan;
        this.cvv = cvv;
    }

    public String getPan() {
        return pan;
    }

    public String getCvv() {
        return cvv;
    }


}
