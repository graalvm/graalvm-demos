package org.graalvm.demos.micronaut.service.todo;


import java.util.Collection;

public interface TodoService {
    Todo create(Todo todo) throws ServiceException;
    Todo update(String todoId, Todo newTodo) throws ServiceException;
    void remove(String todoId) throws ServiceException;
    Collection<Todo> listTodos(String userId) throws ServiceException;
    Todo get(String id) throws ServiceException;
}
