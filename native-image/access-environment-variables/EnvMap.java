import java.util.Map;

public class EnvMap {
    public static void main (String[] args) {
        var filter = args.length > 0 ? args[0] : "";
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            if(envName.contains(filter)) {
                System.out.format("%s=%s%n",
                                envName,
                                env.get(envName));
            }
        }
    }
}