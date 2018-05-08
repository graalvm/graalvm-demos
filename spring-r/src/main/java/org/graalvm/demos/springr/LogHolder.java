package org.graalvm.demos.springr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;

public class LogHolder {

  private static final Log LOG = LogFactory.getLog(SpringRApplication.class);

  public static void log(double value, Object... args) {
    LOG.info(String.format("Logging (value = %s): %s", value, Arrays.toString(args)));
  }
}
