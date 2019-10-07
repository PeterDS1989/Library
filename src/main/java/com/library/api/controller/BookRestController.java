package com.library.api.controller;

import com.library.api.domain.Book;
import com.library.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest controller for books
 */
@RestController
@RequestMapping("/api/books")
public class BookRestController extends AbstractRestController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getBooks() {
        return bookService.findAllBooksSortedByTitle();
    }

    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable String bookId) {
        return bookService.findBookById(bookId);
    }

    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable String bookId,
                           @Valid @RequestBody Book book) {
        return bookService.updateBook(bookId, book);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok().build();
    }
}
