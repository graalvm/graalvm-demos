import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class BuildTimeLoggerInit {
    private static final Logger LOGGER;
    static {
        try {
            LogManager.getLogManager().readConfiguration(BuildTimeLoggerInit.class.getResourceAsStream("/logging.properties"));
        } catch (IOException | SecurityException | ExceptionInInitializerError ex) {
            Logger.getLogger(BuildTimeLoggerInit.class.getName()).log(Level.SEVERE, "Failed to read logging.properties file", ex);
        }
        LOGGER = Logger.getLogger(BuildTimeLoggerInit.class.getName());
    }

    public static void main(String[] args) throws IOException {
        LOGGER.log(Level.WARNING, "Danger, Will Robinson!");
    }
} 
