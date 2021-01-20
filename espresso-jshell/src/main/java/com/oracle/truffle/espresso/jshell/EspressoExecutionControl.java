/*
 * Copyright (c) 2021, 2021, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package com.oracle.truffle.espresso.jshell;

import java.util.Objects;

import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;

import jdk.jshell.spi.ExecutionControl;

/**
 * Wrapper for a guest ExecutionControl instance.
 */
public abstract class EspressoExecutionControl implements ExecutionControl {

    private final Lazy<Value> delegate;

    protected EspressoExecutionControl(Lazy<Value> delegate) {
        this.delegate = Objects.requireNonNull(delegate);
    }

    protected final Lazy<Value> ClassBytecodes = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$ClassBytecodes"));
    protected final Lazy<Value> byte_array = Lazy.of(() -> loadClass("[B"));
    protected final Lazy<Value> ExecutionControlException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$ExecutionControlException"));
    protected final Lazy<Value> RunException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$RunException"));
    protected final Lazy<Value> ClassInstallException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$ClassInstallException"));
    protected final Lazy<Value> NotImplementedException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$NotImplementedException"));
    protected final Lazy<Value> EngineTerminationException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$EngineTerminationException"));
    protected final Lazy<Value> InternalException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$InternalException"));
    protected final Lazy<Value> ResolutionException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$ResolutionException"));
    protected final Lazy<Value> StoppedException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$StoppedException"));
    protected final Lazy<Value> UserException = Lazy.of(() -> loadClass("jdk.jshell.spi.ExecutionControl$UserException"));

    public abstract Value loadClass(String className);

    @Override
    public void load(ClassBytecodes[] classBytecodes) throws ClassInstallException, NotImplementedException, EngineTerminationException {
        try {
            delegate.get().invokeMember("load", toGuest(classBytecodes));
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public void redefine(ClassBytecodes[] classBytecodes) throws ClassInstallException, NotImplementedException, EngineTerminationException {
        try {
            delegate.get().invokeMember("redefine", toGuest(classBytecodes));
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public String invoke(String className, String methodName) throws RunException, EngineTerminationException, InternalException {

        try {
            return asString(delegate.get().invokeMember("invoke", className, methodName));
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public String varValue(String className, String varName) throws RunException, EngineTerminationException, InternalException {

        try {
            return asString(delegate.get().invokeMember("varValue", className, varName));
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public void addToClasspath(String s) throws EngineTerminationException, InternalException {
        try {
            delegate.get().invokeMember("addToClasspath", s);
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public void stop() throws EngineTerminationException, InternalException {
        try {
            delegate.get().invokeMember("stop");
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    @Override
    public Object extensionCommand(String command, Object arg) throws RunException, EngineTerminationException, InternalException {
        throw new NotImplementedException("extensionCommand: " + command);
    }

    @Override
    public void close() {
        try {
            delegate.get().invokeMember("close");
        } catch (PolyglotException e) {
            throw throwAsHost(e);
        }
    }

    protected static String asString(Value stringOrNull) {
        return stringOrNull.isNull() ? null : stringOrNull.asString();
    }

    protected static String getMessage(Value guestException) {
        assert guestException.isException();
        return asString(guestException.invokeMember("getMessage"));
    }

    protected static String getCauseExceptionClass(Value guestException) {
        assert guestException.isException();
        return asString(guestException.invokeMember("causeExceptionClass"));
    }

    protected static StackTraceElement[] getStackTrace(@SuppressWarnings("unused") Value guestException) {
        // TODO(peterssen): Create host stack trace.
        return new StackTraceElement[0];
    }

    protected RuntimeException throwAsHost(PolyglotException polyglotException) {
        throw throwCheckedException(toHost(polyglotException));
    }

    protected Exception toHost(PolyglotException polyglotException) {
        Value e = polyglotException.getGuestObject();
        if (e == null || !ExecutionControlException.get().isMetaInstance(e)) {
            // Not a jshell exception, propagate.
            return polyglotException;
        }

        if (UserException.get().isMetaInstance(e)) {
            return new UserException(getMessage(e), getCauseExceptionClass(e), getStackTrace(e));
        }
        if (ClassInstallException.get().isMetaInstance(e)) {
            Value guestInstalled = e.invokeMember("installed");
            boolean[] installed = null;
            if (!guestInstalled.isNull()) {
                assert guestInstalled.hasArrayElements();
                int installedLength = Math.toIntExact(guestInstalled.getArraySize());
                installed = new boolean[installedLength];
                for (int i = 0; i < installedLength; i++) {
                    installed[i] = guestInstalled.getArrayElement(i).asBoolean();
                }
            }
            return new ClassInstallException(getMessage(e), installed);
        }
        if (NotImplementedException.get().isMetaInstance(e)) {
            return new NotImplementedException(getMessage(e));
        }
        if (EngineTerminationException.get().isMetaInstance(e)) {
            return new EngineTerminationException(getMessage(e));
        }
        if (InternalException.get().isMetaInstance(e)) {
            return new InternalException(getMessage(e));
        }
        if (ResolutionException.get().isMetaInstance(e)) {
            int id = e.invokeMember("id").asInt();
            return new ResolutionException(id, getStackTrace(e));
        }
        if (StoppedException.get().isMetaInstance(e)) {
            return new StoppedException();
        }

        // Unknown ExecutionControlException exception, propagate.
        return polyglotException;
    }

    protected Value toGuest(ClassBytecodes[] classBytecodes) {
        Value values = ClassBytecodes.get().getMember("array").newInstance(classBytecodes.length);
        for (int i = 0; i < classBytecodes.length; i++) {
            byte[] bytecodes = classBytecodes[i].bytecodes();
            Value bytes = byte_array.get().newInstance(bytecodes.length);
            for (int j = 0; j < bytecodes.length; j++) {
                bytes.setArrayElement(j, bytecodes[j]);
            }
            Value elem = ClassBytecodes.get().newInstance(classBytecodes[i].name(), bytes);
            values.setArrayElement(i, elem);
        }
        return values;
    }

    @SuppressWarnings({"unchecked"})
    protected static <E extends Throwable> RuntimeException throwCheckedException(Throwable ex) throws E {
        throw (E) ex;
    }
}
