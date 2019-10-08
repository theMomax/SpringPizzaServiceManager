package restframework.comparison.PizzaServiceManager.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonInclude(Include.NON_NULL)
public class Response<T> {

    private T data;

    private String message;

    public Response(String message, T data) {
        this.data = data;
        this.message = message;
    }

    public Response(String message) {
        this.message = message;
    }

    public Response(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }
}