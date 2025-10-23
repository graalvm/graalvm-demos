package com.example.wasm_spring_shell.web;

import org.graalvm.webimage.api.JS;
import org.jline.reader.History;
import org.jline.reader.ParsedLine;
import org.jline.reader.impl.DefaultParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.shell.Input;
import org.springframework.shell.InputProvider;
import org.springframework.shell.Utils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoInputProvider implements InputProvider {
    private static final Logger LOG = LoggerFactory.getLogger(DemoInputProvider.class);

    private final History history;

    public DemoInputProvider(History history) {
        this.history = history;
    }

    @Override
    public Input readInput() {
        var lineParser = new DefaultParser();
        Worker.sendReadyMessage();
        LOG.info("Waiting for message...");
        waitForMessage();
        String line = getMessage();
        LOG.info("Got message {}", line);
        history.add(line);
        ParsedLine parsedLine = lineParser.parse(line, line.length() + 1);
        return new SimpleInput(parsedLine);
    }

    @JS.Coerce
    @JS("""
            const length = globalThis.sharedI32[1];
            const byteArray = new Uint8Array(length);
            byteArray.set(new Uint8Array(globalThis.sharedBuffer, 8, length))
            return new TextDecoder().decode(byteArray);
            """)
    public static native String getMessage();

    @JS("""
            Atomics.wait(globalThis.sharedI32, 0, 0);
            Atomics.store(globalThis.sharedI32, 0, 0);
            """)
    public static native void waitForMessage();

    public static class SimpleInput implements Input {
        private final ParsedLine parsedLine;

        public SimpleInput(ParsedLine parsedLine) {
            this.parsedLine = parsedLine;
        }

        @Override
        public String rawText() {
            return parsedLine.line();
        }

        @Override
        public List<String> words() {
            return Utils.sanitizeInput(parsedLine.words());
        }
    }
}
