package com.example.demo_library.jpa;

import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
    // 基本的な操作のみなら定義不要
}
