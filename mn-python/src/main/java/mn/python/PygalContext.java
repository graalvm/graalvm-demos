package mn.python;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.env.Environment;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;

@Singleton
public class PygalContext {

  private static final String SCRIPT_NAME = "mypython.py";
  private final Engine engine;
  private final Environment env;

  public PygalContext (Environment env){
    this.engine = Engine.create();
    this.env = env;
  }

  @PostConstruct
  public void initialize() throws IOException {
    URL url = env.getResource(SCRIPT_NAME).get();
    Source pythonScript = Source.newBuilder("python", url).build();
    Context context = getContext();
    context.eval(pythonScript);

  }

  // thread local because python is not multithreaded
  private ThreadLocal<Context> contexts = ThreadLocal.withInitial(() -> {
    // we assume the `env` will be near the jar too
    Context ctx = Context.newBuilder().option("python.Executable", "env/bin/graalpython")
      .allowAllAccess(true)
      .build();

    ctx.eval("python",
      "import site\n"+
             "import pygal\n");
    return ctx;
  });

  public Context getContext() {
    return contexts.get();
  }

}
