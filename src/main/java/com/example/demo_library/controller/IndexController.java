package com.example.demo_library.controller;

import com.example.demo_library.jpa.Book;
import com.example.demo_library.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * ビュー表示用ルーティング
 */
@RestController
public class IndexController {
    @Autowired
    private BookRepository bookRepository;

    /**
     * リスト取得
     *
     * @return model
     */
    @GetMapping(path = "/book")
    public ModelAndView getBook(@RequestParam(required = false) String keyword) {
        if (Objects.isNull(keyword) || keyword.isBlank()) {
            // 無指定：全件を取得
            return indexModelAndView(bookRepository.findAll());
        }
        Collection<Book> findResult;
        findResult = bookRepository.findByTitle(keyword);
        if (findResult.isEmpty()) {
            findResult = bookRepository.findByAuthor(keyword);
        }
        return indexModelAndView(findResult);
    }

    private ModelAndView indexModelAndView(Iterable<Book> books) {
        return new ModelAndView("index", Map.of("books", books));
    }

    /**
     * 新規登録
     *
     * @param title
     * @param author
     * @return
     */
    @PostMapping(path = "/book", consumes = "application/x-www-form-urlencoded")
    public ModelAndView postBook(@RequestParam String title, @RequestParam String author) {
        bookRepository.save(new Book(title, author));
        return indexModelAndView(bookRepository.findAll());
    }

    /**
     * 書籍情報の削除
     * TODO: キーはこれでいいのか
     *
     * @param id
     */
    @PostMapping(path = "/book/delete/{id}")
    public ModelAndView deleteBook(@PathVariable Long id) {
        bookRepository.findById(id)
                .ifPresent(book -> bookRepository.deleteById(id));
        return indexModelAndView(bookRepository.findAll());
    }

    /**
     * 更新
     * TODO: キーはこれでいいのか
     */
    @PostMapping(path = "/book/edit/{id}", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam String author) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isEmpty()) {
            return indexModelAndView(bookRepository.findAll());
        }
        Book update = byId.get();
        update.setTitle(title);
        update.setAuthor(author);
        bookRepository.save(update);
        return indexModelAndView(bookRepository.findAll());
    }
}
