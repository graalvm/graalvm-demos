package example.runtime;

public class ConfigureAtRunTime {
    public static void main(String[] args) throws Throwable {
        Configuration config = Configuration.loadFromFile();
        System.out.println(config.handler.handle());
    }
}
