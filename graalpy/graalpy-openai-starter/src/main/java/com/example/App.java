/*
 * Copyright (c) 2024, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at https://opensource.org/license/UPL.
 */

package com.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.graalvm.polyglot.Value;
import org.graalvm.python.embedding.utils.GraalPyResources;

import java.util.List;

public class App {

    public static void main(String[] args)  {
        String userInput = args.length > 0 ? args[0] : "Say this is a test";
        try (Context context = GraalPyResources.createContext()) {
            Value createChatCompletion = context.eval("python",
                    // language=python
                    """
                            import os
                            from openai import OpenAI
                            
                            client = OpenAI(
                                # This is the default and can be omitted
                                api_key=os.environ.get("OPENAI_API_KEY"),
                            )
                            
                            def create_chat_completion(user_input):
                                return client.chat.completions.create(
                                    messages=[
                                        {
                                            "role": "user",
                                            "content": user_input,
                                        }
                                    ],
                                    model="gpt-3.5-turbo",
                                )

                            create_chat_completion
                            """);
            ChatCompletion chatCompletion = createChatCompletion.execute(userInput).as(ChatCompletion.class);
            for (Choice choice : chatCompletion.choices()) {
                System.out.println(choice.message().content());
            }
        } catch (PolyglotException e) {
            throw new RuntimeException("Failed to run Python code. Did you set the OPENAI_API_KEY environment variable?", e);
        }
    }

    public interface ChatCompletion {
         List<Choice> choices();
    }

    public interface Choice {
        ChatCompletionMessage message();
    }

    public interface  ChatCompletionMessage {
        String content();
    }
}
