package org.graalvm.demos.micronaut.service.frontend;

import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanIntrospector;
import io.micronaut.core.beans.BeanMap;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import io.micronaut.views.ModelAndView;
import io.micronaut.views.View;
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

    @Get("/{?format}")
    public ModelAndView<?> list(Optional<String> format) {
        Collection<Todo> allTodos = todoServiceClient.listTodos();
        Map data = CollectionUtils.mapOf("allTodos", allTodos);

        return new ModelAndView<>("base", data);
    }

    @Post
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public HttpResponse<?> add(@Body Map<String, String> formData) throws Exception {
        Todo newTodo = new Todo();
        newTodo.setContent(formData.get("content"));
        LOGGER.info("Sending to backend service " + formData);
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
