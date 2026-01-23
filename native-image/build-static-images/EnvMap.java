/*
 * Copyright (c) 2023, 2026, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

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
