/*
 * Copyright (c) 2020, 2023, Oracle and/or its affiliates. All rights reserved.
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
package org.graalvm.demo;

import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;

/**
 * A basic Helidon web service. The service's GET request handler will accept
 * incoming requests and use GraalVM JavaScript polyglot capabilities to create
 * a JSON object response.
 */
class HelidonService {

    /**
     * The actual JavaScript code executed by this Web service. The
     * <code>requestId</code> variable is received from Java and corresponds to
     * the value parsed from the request's URL.
     * <p>
     * The code awaits for some asynchronous computation (performed in Java).
     * {
     *
     * @see createJavaInteropComputeFunction}.
     */
    private static final String jsCode = "(async function(requestId) {"
            + "  try {"
            + "    let data = await computeFromJava(requestId);"
            + "    return JSON.stringify({requestId: requestId, result: data});"
            + "  } catch (e) {"
            + "    return 'There was an error in JS-land! ' + e;"
            + "  }"
            + "})";
    private final String requestPath;
    private final ConcurrentJsExecutor jsExecutor;
    private final int port;

    HelidonService(String requestPath, int port) {
        this.requestPath = requestPath;
        this.jsExecutor = new ConcurrentJsExecutor(jsCode);
        this.port = port;
    }

    /**
     * Create and start a new Helidon HTTP server.
     */
    public void init() {
        /*
         * Register a request handler for the HTTP GET request.
         */
        Routing routing = Routing.builder().get(requestPath, (req, res) -> {
            /*
             * Parse `?request=xxx` from an incoming request's URL. If not found, use a
             * default value.
             */
            int requestId;
            try {
                requestId = req.queryParams().first("request").map(Integer::parseInt).orElse(42);
            } catch (NumberFormatException e) {
                res.send("Request id must be a number");
                return;
            }

            /*
             * Execute a JavaScript asynchronous function using GraalVM JavaScript.
             * The result of the invocation is a Java `CompletableFuture` which will execute
             * its handler once the JavaScript async function returns.
             */
            jsExecutor.callJavaScriptAsyncFunction(requestId).whenComplete((jsonResult, ex) -> {
                /*
                 * The JavaScript engine has completed this future.
                 */
                if (ex == null) {
                    /*
                     * No exceptions. Send JSON answer to client as produced by GraalVM JavaScript.
                     */
                    res.send(jsonResult);
                } else {
                    /*
                     * There was an error.
                     */
                    res.send("There was an error: " + ex.getMessage());
                }
            });
        }).build();
        WebServer.builder()
            .port(port)
            .addRouting(routing)
            .build()
            .start();
    }
}
