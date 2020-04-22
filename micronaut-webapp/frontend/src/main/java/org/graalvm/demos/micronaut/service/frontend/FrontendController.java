package org.graalvm.demos.micronaut.service.frontend;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.views.ModelAndView;
import org.graalvm.demos.micronaut.service.api.v1.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Controller("/todos")
public class FrontendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrontendController.class);

    @Inject TodoServiceClient todoServiceClient;
    @Inject ObjectMapper om;

    @Get("/{?format}")
    public HttpResponse<?> list(Optional<String> format) {
        Collection<Todo> allTodos = todoServiceClient.listTodos();
        Map data = CollectionUtils.mapOf("allTodos", allTodos);
        if (format.isPresent() && format.get().equalsIgnoreCase("json")) {
            return HttpResponse.ok(data);
        }
        return HttpResponse.ok(new ModelAndView<Map>("base", data));
    }

    @Post
    @Consumes({MediaType.APPLICATION_FORM_URLENCODED, MediaType.APPLICATION_JSON})
    public HttpResponse<?> add(HttpRequest<Object> request, @Body Object body) throws Exception {
        Todo newTodo;
        if (request.getContentType().get().getName().equalsIgnoreCase(MediaType.APPLICATION_JSON)) {
            newTodo = request.getBody(Todo.class).get();
        } else {
            Map map = (Map) body;
            newTodo = new Todo();
            newTodo.setContent((String)map.get("content"));
        }
        todoServiceClient.addTodo(newTodo);
        return HttpResponse.redirect(new URI("/todos"));
    }

    @Delete("/{todoId}")
    public HttpResponse<?> remove(String todoId) throws Exception {
        LOGGER.info("removing todo with id {}", todoId);
        todoServiceClient.removeTodo(todoId);
        return HttpResponse.ok();
    }

}
