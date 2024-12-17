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

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

class ConcurrentJsExecutor {

    private static final String JS = "js";
    private static final String THEN = "then";
    private static final String CATCH = "catch";

    private final String jsCode;

    /**
     * Utility class to associate locks with polyglot contexts.
     */
    private static class ContextProvider {

        private final Context context;
        private final ReentrantLock lock;

        ContextProvider(Context cx) {
            this.context = cx;
            this.lock = new ReentrantLock();
        }

        Context getContext() {
            return context;
        }

        Lock getLock() {
            return lock;
        }
    }

    /**
     * A GraalVM engine shared between multiple JavaScript contexts.
     */
    private final Engine sharedEngine = Engine.newBuilder().build();

    /**
     * A Thread-local used to ensure that we have one JavaScript context per
     * Helidon thread.
     */
    private final ThreadLocal<ContextProvider> jsContext = ThreadLocal.withInitial(() -> {
        /*
         * For simplicity, allow ALL accesses. In a real application, access to resources should be restricted.
         */
        Context cx = Context.newBuilder(JS).allowHostAccess(HostAccess.ALL).allowPolyglotAccess(PolyglotAccess.ALL)
                .engine(sharedEngine).build();
        /*
         * Register a Java method in the Context global scope as a JavaScript function.
         */
        ContextProvider provider = new ContextProvider(cx);
        cx.getBindings(JS).putMember("computeFromJava", createJavaInteropComputeFunction(provider));
        System.out.println("Created new JS context for thread " + Thread.currentThread());
        return provider;
    });

    ConcurrentJsExecutor(String jsCode) {
        this.jsCode = jsCode;
    }

    /**
     * Returns the Java implementation of <code>computeFromJava</code> exposed
     * to JavaScript.
     */
    private Function<?, ?> createJavaInteropComputeFunction(ContextProvider cx) {
        /*
         * The Java implementation of the `computeFromJava` function takes one argument
         * as input and returns a JavaScript Promise.
         *
         * In Java terms, this is equivalent to returning an instance of
         * `ComputeFromJavaFunction`.
         */
        return (requestId) -> (ComputeFromJavaFunction) (onResolve, onReject) -> {
            /*
             * Simulate async execution by submitting the actual task to a thread pool.
             */
            CompletableFuture.supplyAsync(() -> {
                /*
                 * This code will resolve or reject a JavaScript Promise that was created in another thread.
                 * Synchronization can be used to ensure that no concurrent access can happen on JavaScript objects
                 * that belong to the same context.
                 */
                cx.getLock().lock();
                try {
                    /*
                     * Reject the Promise if requestId is smaller than 42.
                     */
                    if ((int) requestId < 42) {
                        return onReject.execute(requestId + " is not a valid request id!");
                    }
                    /*
                     * Do some random calculation using `requestId`.
                     */
                    double v = (int) requestId + Math.random();
                    /*
                     * Resolve the JavaScript Promise with the computed value. This will resume the
                     * JavaScript `async` function execution.
                     */
                    return onResolve.execute(v);
                } catch (PolyglotException e) {
                    /*
                     * Something went wrong. Reject the JavaScript Promise.
                     */
                    return onReject.execute(e.getGuestObject() == null ? e.getGuestObject() : e.getMessage());
                } finally {
                    cx.getLock().unlock();
                }
            });
        };
    }

    /**
     * Submit a new request to the JavaScript engine. Returns a
     * `CompletionStage` instance that will complete when GraalVM JavaScript
     * produces a value.
     */
    public CompletionStage<Object> callJavaScriptAsyncFunction(int requestId) {
        /*
         * Create a new future. It will be completed by the JavaScript engine as a
         * consequence of a JavaScript Promise resolution.
         */
        CompletableFuture<Object> jsExecution = new CompletableFuture<>();
        /*
         * Helidon might use multiple threads to handle concurrent requests. To minimize the number of contexts
         * in the application, we can use one polyglot context per thread. To this end, we use thread-locals.
         */
        ContextProvider cx = jsContext.get();
        /*
         * A concurrent thread might resolve or reject a JavaScript Promise that was created in this context.
         * Synchronization can be used to ensure that no concurrent access can happen on objects that belong
         * to the same polyglot context.
         */
        cx.getLock().lock();
        try {
            /*
             * Execute the JavaScript async function. Will return a JavaScript Promise.
             */
            Value jsAsyncFunctionPromise = cx.getContext().eval(JS, jsCode).execute(requestId);
            /*
             * Register event reactions for the given Promise. The corresponding Java
             * methods will be executed when the Promise completes.
             */
            jsAsyncFunctionPromise.invokeMember(THEN, (Consumer<?>) jsExecution::complete)
                    .invokeMember(CATCH, (Consumer<Throwable>) jsExecution::completeExceptionally);
        } catch (Throwable t) {
            /*
             * Something went wrong while running JavaScript.
             */
            jsExecution.completeExceptionally(t);
        } finally {
            cx.getLock().unlock();
        }
        return jsExecution;
    }

    /**
     * An arbitrary "thenable" interface. Used to expose Java methods to
     * JavaScript Promise objects.
     */
    @FunctionalInterface
    public interface ComputeFromJavaFunction {

        void then(Value onResolve, Value onReject);
    }

}
