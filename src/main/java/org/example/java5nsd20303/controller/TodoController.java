package org.example.java5nsd20303.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.java5nsd20303.entity.Todo;
import org.example.java5nsd20303.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequestMapping("/api/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    // ==========================
    // 1️ GET ALL
    // ==========================
    @GetMapping
    //ResponseEntity dùng để trả về trạng thái
    public ResponseEntity<List<Todo>>  getAllTodos() {
       List<Todo> todos = todoService.findAll();
        // c1: return new ResponseEntity<>(todos, HttpStatus.OK);
        // c2:  return  ResponseEntity.ok(todos);
        // c3: return ResponseEntity.status(HttpStatus.OK).body(todos);
        return ResponseEntity
                .ok()
                .header("Custom-Header","Custom_Value")
                .body(todos);
    }

    // ==========================
    // 2 GET BY ID
    // ==========================
    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodo(@PathVariable long id) {
        Todo todo = todoService.findByID(id);

       return  new ResponseEntity<>(todo,HttpStatus.OK);
    }




    //
    @PostMapping
    public ResponseEntity<Todo>  addTodo( @Valid @RequestBody Todo todo){
        Todo saveToDo =todoService.add(todo);
        return  new ResponseEntity<>(saveToDo,HttpStatus.CREATED);
    }
    //
    @PutMapping("{id}")
    public ResponseEntity<Todo> updateTodo( @Valid @RequestBody Todo todo, @PathVariable long id){
        Todo updatedToDo= todoService.update(todo, id);
        return  new ResponseEntity<>(updatedToDo,HttpStatus.OK);
    }

    //
    @DeleteMapping("{id}")
    public ResponseEntity<Todo>  deleteTodo(@PathVariable long id){
        Todo deleteToDo= todoService.delete(id);
        return new ResponseEntity<>(deleteToDo,HttpStatus.OK);
    }
}