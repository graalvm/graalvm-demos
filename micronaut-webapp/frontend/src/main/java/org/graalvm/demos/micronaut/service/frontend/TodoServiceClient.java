package org.graalvm.demos.micronaut.service.frontend;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.validation.Validated;
import org.graalvm.demos.micronaut.service.api.v1.Todo;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Collection;

//todo make it a configuration
@Client("${todoservice.url:`https://localhost:8443`}")
@Validated
public interface TodoServiceClient {

    @Get("/api/todos")
    Collection<Todo> listTodos();

    @Post("/api/todos")
    Todo addTodo(@Body @Valid Todo todo);

    @Delete("/api/todos/{id}")
    void removeTodo(@Body @Valid String id);
}
