package com.example.demo_library.controller;

import com.example.demo_library.jpa.Book;
import com.example.demo_library.jpa.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
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
     * @return
     */
    @GetMapping(path = "/book")
    public ModelAndView getBook() {
        return new ModelAndView("index", Map.of("books", bookRepository.findAll()));
    }

    /**
     * 新規登録
     *
     * @param book
     * @return
     */
    @PostMapping(path = "/book", consumes = "application/x-www-form-urlencoded")
    public ModelAndView postBook(@RequestParam String title, @RequestParam String author) {
        bookRepository.save(new Book(title, author));
        return new ModelAndView("index", Map.of("books", bookRepository.findAll()));
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
        return new ModelAndView("index", Map.of("books", bookRepository.findAll()));
    }

    /**
     * 更新
     * TODO: キーはこれでいいのか
     */
    @PostMapping(path = "/book/edit/{id}", consumes = "application/x-www-form-urlencoded")
    public ModelAndView updateBook(@PathVariable Long id, @RequestParam String title, @RequestParam String author) {
        Optional<Book> byId = bookRepository.findById(id);
        if (byId.isEmpty()) {
            return new ModelAndView("index", Map.of("books", bookRepository.findAll()));
        }
        Book update = byId.get();
        update.setTitle(title);
        update.setAuthor(author);
        bookRepository.save(update);
        return new ModelAndView("index", Map.of("books", bookRepository.findAll()));
    }
}
