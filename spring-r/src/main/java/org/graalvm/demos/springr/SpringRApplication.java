package org.graalvm.demos.springr;

import org.graalvm.polyglot.*;
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
  private Function<Double, String> plotFunction;

  public static void main(String[] args) {
    SpringApplication.run(SpringRApplication.class, args);
  }

  @Bean
  public Context getGraalVMContext() {
    return Context.newBuilder().allowAllAccess(true).build();
  }

  @Bean Function<Double, String> getPlot(@Autowired Context ctx) throws IOException {
    Source source = Source.newBuilder("R", rSource.getURL()).build();
    return ctx.eval(source).as(Function.class);
  }

  @RequestMapping(value = "/load", produces = "image/svg+xml")
  public ResponseEntity<String> load() {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Refresh", "1");
    return new ResponseEntity<String>(
      plotFunction.apply(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage()),
      responseHeaders,
      HttpStatus.OK);
  }
}
