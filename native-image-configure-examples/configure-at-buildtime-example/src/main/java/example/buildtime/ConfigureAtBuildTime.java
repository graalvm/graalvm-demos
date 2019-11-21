package example.buildtime;

import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

class ConfigureAtBuildTimeFeature implements Feature {

    public void beforeAnalysis(BeforeAnalysisAccess access) {
		RuntimeClassInitialization.initializeAtBuildTime(Configuration.class.getPackage().getName());
        try {
            ImageSingletons.add(Configuration.class, Configuration.loadFromFile());
        } catch (Throwable ex) {
            throw new RuntimeException("native-image build-time configuration failed", ex);
        }
    }
}

public class ConfigureAtBuildTime {
    public static void main(String[] args) {
        Configuration configuration = ImageSingletons.lookup(Configuration.class);
        System.out.println(configuration.handler.handle());
    }
}
