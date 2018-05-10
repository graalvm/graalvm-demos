package org.graalvm.demos.springr;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.function.Function;

@Controller
@SpringBootApplication
public class SpringRApplication {

  @Value(value = "classpath:plot.R")
  private Resource rSource;

  @Autowired
  private Function<DataHolder, String> plotFunction;

  @Bean
  Function<DataHolder, String> getPlotFunction(@Autowired Context ctx)
    throws IOException {
    Source source =
      Source.newBuilder("R", rSource.getURL()).build();
    return ctx.eval(source).as(Function.class);
  }

  @RequestMapping(value = "/load", produces = "image/svg+xml")
  public ResponseEntity<String> load() {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Refresh", "1");
    String svg = "";
    synchronized(plotFunction){
      svg = plotFunction.apply(new DataHolder(ManagementFactory.getOperatingSystemMXBean()
        .getSystemLoadAverage())
      );
    }

    return new ResponseEntity<String>(
      svg,
      responseHeaders,
      HttpStatus.OK);
  }

  @Bean
  public Context getGraalVMContext() {
    return Context.newBuilder().allowAllAccess(true).build();
  }

  public static void main(String[] args) {
    SpringApplication.run(SpringRApplication.class, args);
  }

}
