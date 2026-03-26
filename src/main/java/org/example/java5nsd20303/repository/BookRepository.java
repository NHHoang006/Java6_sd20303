package org.example.java5nsd20303.repository;

import org.example.java5nsd20303.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
