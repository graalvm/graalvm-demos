package com.example.wasm_spring_shell.web;

import org.jline.utils.AttributedCharSequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.shell.ResultHandler;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.io.StringWriter;

@Component
public class CustomResultHandler implements ResultHandler<Object> {
    @Override
    public void handleResult(Object result) {
        switch (result) {
            case null -> Worker.sendErrorMessage("null result");
            case AttributedCharSequence attributed -> {
                System.out.println(attributed);
                Worker.sendOutputMessage(attributed.toAnsi());
            }
            case Throwable throwable -> {
                StringWriter stringWriter = new StringWriter();
                throwable.printStackTrace(new PrintWriter(stringWriter));
                Worker.sendErrorMessage(stringWriter.toString());
            }
            default -> Worker.sendOutputMessage(result.toString());
        }
    }
}
