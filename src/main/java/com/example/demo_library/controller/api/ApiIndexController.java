package com.example.demo_library.controller.api;

import com.example.demo_library.jpa.Book;
import com.example.demo_library.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * API用
 */
@RestController
@RequestMapping(path = "/api", consumes = "application/json", produces = "application/json")
public class ApiIndexController {
    @Autowired
    private BookRepository bookRepository;

    /**
     * デバッグ用　全書籍取得
     *
     * @return
     */
    @GetMapping("/")
    public String printBooks() {
        StringBuilder sb = new StringBuilder();
        bookRepository.findAll().forEach(
                book -> sb.append(book.toString()).append("\n")
        );
        return sb.toString();
    }

    /**
     * リスト取得
     *
     * @return
     */
    @GetMapping(path = "/book", produces = "application/json")
    public Iterable<Book> getBook(@RequestParam(required = false) String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            // 無指定：全件を取得
            return bookRepository.findAll();
        }
        Collection<Book> findResult;
        findResult = bookRepository.findByTitle(keyword);
        if (findResult.isEmpty()) {
            findResult = bookRepository.findByAuthor(keyword);
        }
        return findResult;
    }

    /**
     * 新規登録
     *
     * @param book
     * @return
     */
    @PostMapping(path = "/book", consumes = "application/json", produces = "application/json")
    public Long postBook(@RequestBody Book book) {
        Book saved = bookRepository.save(new Book(book.getTitle(), book.getAuthor()));
        return saved.getId();
    }

    /**
     * 書籍情報の削除
     * TODO: キーはこれでいいのか
     *
     * @param id
     */
    @DeleteMapping(path = "/book/{id}", consumes = "application/json")
    public void deleteBook(@PathVariable Long id) {
        bookRepository.findById(id)
                .ifPresent(book -> bookRepository.deleteById(id));
    }

    /**
     * 更新
     * TODO: キーはこれでいいのか
     *
     * @param id
     * @param book
     */
    @PutMapping(path = "/book/{id}", consumes = "application/json")
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        Optional<Book> byId = bookRepository.findById(book.getId());
        if (byId.isEmpty()) {
            return;
        }
        Book update = byId.get();
        update.setTitle(book.getTitle());
        update.setAuthor(book.getAuthor());
        bookRepository.save(update);
    }
}
