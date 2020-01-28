package com.example.demo_library.jpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface BookRepository extends CrudRepository<Book, Long> {

    @Query(value = "SELECT * FROM BOOK WHERE AUTHOR = :keyword", nativeQuery = true)
    Collection<Book> findByAuthor(@Param("keyword") String keyword);

    @Query(value = "SELECT * FROM BOOK WHERE TITLE = :keyword", nativeQuery = true)
    Collection<Book> findByTitle(@Param("keyword") String keyword);
}
