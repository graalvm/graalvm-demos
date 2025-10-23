package com.example.wasm_spring_shell.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBooleanProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.Shell;
import org.springframework.shell.context.InteractionMode;
import org.springframework.shell.context.ShellContext;
import org.springframework.stereotype.Component;

@Component
public class ShellRunner implements CommandLineRunner {
    private final ApplicationContext context;

    public ShellRunner(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(String... args) throws Exception {
        context.getBean(ShellContext.class).setInteractionMode(InteractionMode.INTERACTIVE);
        context.getBean(Shell.class).run(context.getBean(DemoInputProvider.class));
    }
}
