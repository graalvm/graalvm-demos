package org.graalvm.demos.micronaut.service.todo;

public class ServiceException extends Exception{
    public ServiceException(String message) {
        super(message);
    }

    interface ServiceExceptingSupplier<T> {
        T get() throws ServiceException;
    }

    interface ServiceExceptingAction {
        void get() throws ServiceException;
    }
}


