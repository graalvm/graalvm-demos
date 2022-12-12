import java.util.Map;
import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public class LibEnvMap {
    //NOTE: this class has no main() method

    @CEntryPoint(name = "filter_env")
    private static int filterEnv(IsolateThread thread, CCharPointer cFilter) {
        String filter = CTypeConversion.toJavaString(cFilter);
        Map<String, String> env = System.getenv();
        int count = 0;
        for (String envName : env.keySet()) {
            if(!envName.contains(filter)) continue;
            System.out.format("%s=%s%n",
                            envName,
                            env.get(envName));
            count++;
        }
        return count;
    }
}
