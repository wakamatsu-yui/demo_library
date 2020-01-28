package com.example.demo_library.controller.api;

import com.example.demo_library.jpa.Book;
import com.example.demo_library.jpa.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIndexControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll(); // applicationの初期化時にdemoデータが入ってしまうため
    }

    @Test
    void getBook_全件取得() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/book")
                .contentType("application/json")
                .accept("application/json");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        Book hon1 = bookRepository.save(new Book("ほん１", "さくしゃ１"));
        Book hon2 = bookRepository.save(new Book("ほん２", "さくしゃ２"));
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":" + hon1.getId() + ", \"title\": \"ほん１\", \"author\": \"さくしゃ１\"}" +
                        ", {\"id\":" + hon2.getId() + ", \"title\": \"ほん２\", \"author\": \"さくしゃ２\"}]")); // id は自動採番のため以降は検証に含めない
    }

    @Test
    void getBook_キーワード指定() throws Exception {

        Book hon1 = bookRepository.save(new Book("ほん１", "さくしゃ１"));
        Book hon2 = bookRepository.save(new Book("ほん２", "さくしゃ２"));
        Book hon3 = bookRepository.save(new Book("ほん１その２", "さくしゃ１"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/book?keyword=ほん１")
                .contentType("application/json")
                .accept("application/json");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\": \"ほん１\", \"author\": \"さくしゃ１\"}]"));

        request = MockMvcRequestBuilders.get("/api/book?keyword=さくしゃ２")
                .contentType("application/json")
                .accept("application/json");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\": \"ほん２\", \"author\": \"さくしゃ２\"}]"));

        request = MockMvcRequestBuilders.get("/api/book?keyword=さくしゃ１")
                .contentType("application/json")
                .accept("application/json");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"title\": \"ほん１\", \"author\": \"さくしゃ１\"}, {\"title\": \"ほん１その２\", \"author\": \"さくしゃ１\"}]"));

    }

    @Test
    void postBook() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/book")
                .contentType("application/json")
                .accept("application/json");
        request.content("{\"title\": \"ほん１\", \"author\": \"さくしゃ１\"}");

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\": \"ほん１\", \"author\": \"さくしゃ１\"}"));

        // 余計なプロパティがあってもOKらしい
        request.content("{\"title\": \"ほん２\", \"author\": \"さくしゃ１\", \"hoge\": true}");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\": \"ほん２\", \"author\": \"さくしゃ１\"}"));

        // 不足なものはnullとされる
        request.content("{\"title\": \"ほん３\", \"hoge\": \"さくしゃ１\"}");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\": \"ほん３\", \"author\": null}"));

        // 不足なものはnullとされる
        request.content("{\"hage\": \"ほん\", \"hoge\": \"さくしゃ\"}");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().json("{\"title\": null, \"author\": null}"));
    }

    @Test
    void deleteBook() throws Exception {
        Book hon1 = bookRepository.save(new Book("ほん１", "さくしゃ１"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/api/book/" + hon1.getId())
                .contentType("application/json")
                .accept("application/json");

        assertThat(bookRepository.count()).isEqualTo(1);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        assertThat(bookRepository.count()).isEqualTo(0);
    }

    @Test
    void updateBook() throws Exception {
        Book hon1 = bookRepository.save(new Book("ほん１", "さくしゃ１"));
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/api/book/" + hon1.getId())
                .contentType("application/json")
                .accept("application/json");
        request.content(String.format("{\"id\": %d, \"title\": \"ほん更新\", \"author\": \"さくしゃUPDATE\"}", hon1.getId()));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        Book hon = bookRepository.findById(hon1.getId()).get();
        assertThat(hon.getTitle()).isEqualTo("ほん更新");
        assertThat(hon.getAuthor()).isEqualTo("さくしゃUPDATE");

        // 不足なものはnullとされる
        request.content(String.format("{\"id\": %d, \"hage\": \"ほん\", \"hoge\": \"さくしゃ\"}", hon.getId()));

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(""));

        hon = bookRepository.findById(hon1.getId()).get();
        assertThat(hon.getTitle()).isEqualTo(null);
        assertThat(hon.getAuthor()).isEqualTo(null);
    }
}