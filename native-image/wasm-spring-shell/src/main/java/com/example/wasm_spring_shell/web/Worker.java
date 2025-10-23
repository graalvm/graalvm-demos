package com.example.wasm_spring_shell.web;

import com.example.wasm_spring_shell.common.MyCommands;
import org.graalvm.webimage.api.JS;
import org.graalvm.webimage.api.JSObject;
import org.graalvm.webimage.api.JSString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiOutput.Enabled;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackageClasses = {Worker.class, MyCommands.class})
public class Worker {
    public static void main(String[] args) {
        AnsiOutput.setEnabled(Enabled.NEVER);
        SpringApplication.run(Worker.class, args);
    }

    @JS("""
            postMessage(message);
            """)
    public static native void postMessage(JSObject message);

    public static JSObject message(String type) {
        JSObject obj = JSObject.create();
        obj.set("type", JSString.of(type));
        return obj;
    }

    public static void sendReadyMessage() {
        postMessage(message("ready"));
    }

    public static void sendErrorMessage(String message) {
        JSObject error = message("error");
        error.set("message", JSString.of(message));
        postMessage(error);
    }

    public static void sendOutputMessage(String output) {
        JSObject obj = message("output");
        obj.set("message", JSString.of(output));
        postMessage(obj);
    }

}
