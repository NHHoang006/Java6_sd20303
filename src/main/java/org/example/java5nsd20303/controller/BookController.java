package org.example.java5nsd20303.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.java5nsd20303.dto.BookRequest;
import org.example.java5nsd20303.dto.BookResponse;
import org.example.java5nsd20303.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findAll());
    }
    @GetMapping("{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest bookRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.add(bookRequest));
    }
    @PutMapping("{id}")
    public ResponseEntity<BookResponse> updateBook(@Valid @RequestBody BookRequest bookRequest, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.update(bookRequest, id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.noContent().build();
    }
}