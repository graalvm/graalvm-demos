/*
 * Copyright (c) 2023, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * The Universal Permissive License (UPL), Version 1.0
 *
 * Subject to the condition set forth below, permission is hereby granted to any
 * person obtaining a copy of this software, associated documentation and/or
 * data (collectively the "Software"), free of charge and under any and all
 * copyright rights in the Software, and any and all patent rights owned or
 * freely licensable by each licensor hereunder covering either (i) the
 * unmodified Software as contributed to or provided by such licensor, or (ii)
 * the Larger Works (as defined below), to deal in both
 *
 * (a) the Software, and
 *
 * (b) any piece of software and/or hardware listed in the lrgrwrks.txt file if
 * one is included with the Software each a "Larger Work" to which the Software
 * is contributed by such licensors),
 *
 * without restriction, including without limitation the rights to copy, create
 * derivative works of, display, perform, and distribute the Software and make,
 * use, sell, offer for sale, import, export, have made, and have sold the
 * Software and the Larger Work(s), and to sublicense the foregoing rights on
 * either these or other terms.
 *
 * This license is subject to the following condition:
 *
 * The above copyright notice and either this complete permission notice or at a
 * minimum a reference to the UPL must be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
