package org.graalvm.demos.micronaut.service.todo;

import org.graalvm.demos.micronaut.service.api.v1.Todo;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public class InMemoryTodoService implements TodoService {
    private Map<String, Todo> allTodos;
    private AtomicInteger idCounter;

    public InMemoryTodoService() throws ServiceException {
        allTodos = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger();
    }

    private String nextId() {
        int id = idCounter.incrementAndGet();
        return String.format("%d", id);
    }

    @Override
    public Todo create(Todo todo) throws ServiceException {
        todo.setId(nextId());
        todo.setTimeCreated(new Date());
        todo.setLasTimeUpdated(new Date());
        allTodos.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public void remove(String todoId) throws ServiceException{
        if (!allTodos.containsKey(todoId)) {
            throw new ServiceException("can not remove todo with id: " + todoId);
        }
        allTodos.remove(todoId);
    }

    @Override
    public Todo update(String todoId, Todo newTodoImpl) throws ServiceException{
        if (!allTodos.containsKey(todoId)) {
            throw new ServiceException("can not update todo with id: " + todoId);
        }

        Todo current = allTodos.get(todoId);
        current.setContent(newTodoImpl.getContent());
        current.setLasTimeUpdated(new Date());
        return current;
    }

    @Override
    public Collection<Todo> listTodos(String userId) throws ServiceException{
        if (userId != null && !userId.isEmpty()) {
            //find todos by user, for the moment return all
            throw new ServiceException("querying by user Id not supported");
        }

        return allTodos.values();
    }

    @Override
    public Todo get(String id) throws ServiceException{
        if (allTodos.containsKey(id)) {
            return allTodos.get(id);
        }
        throw new ServiceException("todo id: " + id + " not found");
    }
}
