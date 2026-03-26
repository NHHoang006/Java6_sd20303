package org.example.java5nsd20303.service;

import org.example.java5nsd20303.dto.BookRequest;
import org.example.java5nsd20303.dto.BookResponse;
import org.example.java5nsd20303.entity.Book;

import java.util.List;

public interface BookService {

    List<BookResponse> findAll();

    BookResponse findById(Long id);
    BookResponse add(BookRequest bookRequest);
    BookResponse update(BookRequest bookRequest, Long id);
    void delete(Long id);

}
