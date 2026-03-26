package org.example.java5nsd20303.repository;

import org.example.java5nsd20303.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository  extends JpaRepository<Todo,Long> {

}
