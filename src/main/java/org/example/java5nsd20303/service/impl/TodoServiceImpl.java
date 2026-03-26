package org.example.java5nsd20303.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.java5nsd20303.entity.Todo;
import org.example.java5nsd20303.exception.CustomResourceNotFoundException;
import org.example.java5nsd20303.repository.TodoRepository;
import org.example.java5nsd20303.service.TodoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements  TodoService {

    private final TodoRepository todoRepository;


    @Override
    public List<Todo> findAll() {
        return todoRepository.findAll();
    }

    @Override
    public Todo findByID(long id) {
        return todoRepository
                .findById(id)
                .orElseThrow(()->new CustomResourceNotFoundException("Todo not found for this id:"+id));


    }

    @Override
    public Todo add(Todo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public Todo update(Todo todo, Long id) {
       return todoRepository
                .findById(id)
                .map(exitsting ->{
                    if (todo.getTitle()!= null){
                        exitsting.setTitle(todo.getTitle());

                    }
                    if (todo.getDescription()!= null){
                        exitsting.setDescription(todo.getDescription());
                    }
                    todo.setCompleted(todo.isCompleted());


                    return  todoRepository.save(exitsting);
                }).orElse(null);

    }

    @Override
    public Todo delete(Long id) {

       Todo deleteTodo = findByID(id);
       if (deleteTodo!= null){
           todoRepository.deleteById(id);
       }
       return deleteTodo;
    }
}
