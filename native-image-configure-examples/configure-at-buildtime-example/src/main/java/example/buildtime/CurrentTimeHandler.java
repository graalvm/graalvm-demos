package example.buildtime;

import java.util.Date;

public class CurrentTimeHandler implements Handler {
    public String handle() {
        return new Date().toString();
    }
}
