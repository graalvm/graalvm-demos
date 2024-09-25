/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at http://oss.oracle.com/licenses/upl.
 */

package com.example;

import org.graalvm.polyglot.*;
import org.graalvm.polyglot.proxy.*;

public class App {
    public static void main(String[] args) throws Exception {
        try (Context context = Context.newBuilder("js")
                    .allowHostAccess(HostAccess.ALL)
                    .option("engine.WarnInterpreterOnly", "false")
                    .option("js.esm-eval-returns-exports", "true")
                    .option("js.unhandled-rejections", "throw")
                    .build()) {
            Source bundleSrc = Source.newBuilder("js", App.class.getResource("/bundle/bundle.mjs")).build();
            Value exports = context.eval(bundleSrc);
            String input = args.length > 0 ? args[0] : "https://www.graalvm.org/javascript/";

            // Using Value.as(Class)
            QRCode qrCode = exports.getMember("QRCode").as(QRCode.class);
            Promise resultPromise = qrCode.toString(input);
            resultPromise.then(
                (Value result) -> {
                    System.out.println("Successfully generated QR code for \"" + input + "\".");
                    System.out.println(result.asString());
                }
            );

            // Value API version
            Value qrCodeValue = exports.getMember("QRCode");
            Value resultValue = qrCodeValue.invokeMember("toString", input);
            resultValue.invokeMember("then",
                (ProxyExecutable) (arguments) -> {
                    Value result = arguments[0];
                    System.out.println("Successfully generated QR code for \"" + input + "\".");
                    System.out.println(result.asString());
                    return result;
                }
            );
        }
    }
}
