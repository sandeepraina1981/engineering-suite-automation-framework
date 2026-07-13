package demo.api;
import com.microsoft.playwright.APIResponse;

public class APIResponseWrapper {

    private final APIResponse response;


    public APIResponseWrapper(APIResponse response) {
        this.response = response;
    }


    public int status() {
        return response.status();
    }

    public String statusText() {
        return response.statusText();
    }

    public String bodyAsString() {
        return response.text();
    }

    public String header(String name) {
        return response.headers().get(name);
    }

    public APIResponse raw() {
        return response;
    }


}
