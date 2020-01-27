package com.example.demo_library;

import com.example.demo_library.jpa.Book;
import com.example.demo_library.jpa.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoLibraryApplication {

    private static final Logger logger = LoggerFactory.getLogger(DemoLibraryApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DemoLibraryApplication.class, args);
    }

    /**
     * 初期データの投入
     *
     * @param repository
     * @return
     */
    @Bean
    public CommandLineRunner setUpDemoData(BookRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Book("初期タイトル１", "著者一郎"));
                repository.save(new Book("初期タイトル２", "著者二郎"));
                for (Book book : repository.findAll()) {
                    logger.debug("初期データ... {}", book.toString());
                }
            }
        };
    }

}
