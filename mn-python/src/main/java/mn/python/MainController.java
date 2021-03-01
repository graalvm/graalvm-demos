package mn.python;

import io.micronaut.context.env.Environment;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Controller("/")
public class MainController {

  private PygalContext ctx;

  public MainController(PygalContext ctx) {
    this.ctx = ctx;
  }

  @Get("/")
  @Produces("text/html")
  public String index() {
    return "<html><h1>Hello World</h1>" +
      "<img src='/chart' />" +
      "<div>"  + System.currentTimeMillis() + "<div>" +
      "</html>";
  }

  @Get("/chart")
  @Produces("image/svg+xml")
  public String chart() {
    Value barChart = ctx.getContext().getBindings("python").getMember("bar_chart");
    return barChart.execute(List.of(0, 1, 1, 2, 3, 5, 8, 13, 21, 7, 55), "Fibonacci-ish").asString();
  }

}