package example.buildtime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

public class Configuration {

    Handler handler;

    public Configuration(@JsonProperty("handlerClassName") String handlerClassName) throws Throwable {
        handler = (Handler) Class.forName(handlerClassName).getConstructor().newInstance();
    }

    public static Configuration loadFromFile() throws IOException {
        URL resourceURL = Configuration.class.getResource("configuration.json");
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resourceURL, Configuration.class);
    }
}
