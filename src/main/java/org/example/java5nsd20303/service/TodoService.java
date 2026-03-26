package org.example.java5nsd20303.service;

import org.example.java5nsd20303.entity.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> findAll();
    Todo findByID(long id);
    Todo add(Todo todo);
    Todo update(Todo todo,Long id);
    Todo delete(Long id);
}
