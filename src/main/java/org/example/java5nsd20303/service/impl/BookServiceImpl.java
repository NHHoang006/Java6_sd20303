package org.example.java5nsd20303.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.java5nsd20303.dto.BookRequest;
import org.example.java5nsd20303.dto.BookResponse;
import org.example.java5nsd20303.entity.Book;
import org.example.java5nsd20303.exception.CustomResourceNotFoundException;
import org.example.java5nsd20303.repository.BookRepository;
import org.example.java5nsd20303.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;


    @Override
    public List<BookResponse> findAll() {
        return bookRepository.findAll().stream()
                .map(book -> modelMapper.map(book, BookResponse.class)).toList();
    }

    @Override
    public BookResponse findById(Long id) {
        return bookRepository.findById(id).map(book -> modelMapper.map(book,BookResponse.class))
                .orElseThrow(()-> new CustomResourceNotFoundException("Book not found for this id: "+id));
    }

    @Override
    public BookResponse add(BookRequest bookRequest) {
        Book book = modelMapper.map(bookRequest, Book.class);
        book.setIsbn(generateIsbnn());
        bookRepository.save(book);

        BookResponse bookResponse = modelMapper.map(book, BookResponse.class);
        return bookResponse;
    }

    private String generateIsbnn() {
        return "ISBN-" + UUID.randomUUID().toString().substring(0, 13);
    }

    @Override
    public BookResponse update(BookRequest bookRequest, Long id) {
        return bookRepository.findById(id).map(book -> {
            if(bookRequest.getTitle() != null) {
                book.setTitle(bookRequest.getTitle());
            }
            if(bookRequest.getAuthor() != null) {
                book.setAuthor(bookRequest.getAuthor());
            }
            if (bookRequest.getPrice() != 0){
                book.setPrice(bookRequest.getPrice());
            }
            bookRepository.save(book);
            return modelMapper.map(book, BookResponse.class);


        }).orElseThrow(()-> new CustomResourceNotFoundException("Bool not found for this id: "+id));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
