package com.example.wasm_spring_shell.web;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.shell.result.GenericResultHandlerService;
import org.springframework.stereotype.Component;

@Primary
@Component
public class MyResultHandlerService extends GenericResultHandlerService {
    public MyResultHandlerService(CustomResultHandler handler) {
        this.addResultHandler(handler);
    }
}
