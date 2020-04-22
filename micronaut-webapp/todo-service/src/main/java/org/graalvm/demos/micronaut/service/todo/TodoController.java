package org.graalvm.demos.micronaut.service.todo;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import io.micronaut.validation.Validated;
import org.graalvm.demos.micronaut.service.api.v1.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;

@Controller("/api/todos")
@Validated
public class TodoController {

    @Inject TodoService todoService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    private HttpResponse<?> guardedResponse(ServiceException.ServiceExceptingSupplier<?> operation){
        try {
            return HttpResponse.ok(operation.get());
        } catch (ServiceException ex) {
            return HttpResponse.badRequest(ex);
        }
    }

    private HttpResponse<?> guardedResponse(ServiceException.ServiceExceptingAction operation){
        try {
            operation.get();
            return HttpResponse.ok();
        } catch (ServiceException ex) {
            return HttpResponse.badRequest(ex);
        }
    }


    @Get("/{?userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> list(Optional<String> userId) {
        LOGGER.debug("Listing all todos");
        if(userId.isPresent()) {
            return HttpResponse.badRequest("querying todos by user id is not supported");
        }
        return guardedResponse(() -> todoService.listTodos(null));
    }

    @Post
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> create(@Body @Valid Todo todo){
        LOGGER.debug("Saving " + todo);
        return guardedResponse(() -> todoService.create(todo));
    }

    @Get("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> get(String id) {
        return guardedResponse(() -> todoService.get(id));
    }

    @Put("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> update(String id, @Body @Valid Todo todo){
        return guardedResponse(() -> todoService.update(id, todo));
    }

    @Delete("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<?> delete(String id){
        return guardedResponse(() -> todoService.remove(id));
    }

}
