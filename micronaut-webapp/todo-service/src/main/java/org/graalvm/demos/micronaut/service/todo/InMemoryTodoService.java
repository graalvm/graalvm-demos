package org.graalvm.demos.micronaut.service.todo;

import org.graalvm.demos.micronaut.service.api.v1.Todo;

import javax.inject.Singleton;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class InMemoryTodoService implements TodoService {
    private Map<String, Todo> allTodos;
    private final Random random;
    private final MessageDigest hasher;
    private final Base64.Encoder encoder;

    private static final int PAGE_SIZE = 50;

    public InMemoryTodoService() throws ServiceException, NoSuchAlgorithmException {
        allTodos = new ConcurrentHashMap<>();
        random = new Random();
        hasher = MessageDigest.getInstance("SHA-256");
        encoder = Base64.getUrlEncoder();
    }

    private String nextId(Todo todo) {
        long nonce = random.nextLong();
        String message = String.format("%d%d", todo.getTimeCreated().toInstant().toEpochMilli(), nonce);
        byte[] digest = hasher.digest(message.getBytes(StandardCharsets.UTF_8));
        String id = new String(encoder.encode(digest));
        return id;
    }

    @Override
    public Todo create(Todo todo) throws ServiceException {
        todo.setTimeCreated(new Date());
        todo.setLasTimeUpdated(new Date());
        todo.setId(nextId(todo));
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

        return allTodos.values().stream().limit(PAGE_SIZE).collect(Collectors.toList());
    }

    @Override
    public Todo get(String id) throws ServiceException{
        if (allTodos.containsKey(id)) {
            return allTodos.get(id);
        }
        throw new ServiceException("todo id: " + id + " not found");
    }
}
