package com.library.api.controller;

import com.library.api.domain.Book;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public List<Book> getBooks() {
        return bookService.findAllBooksSortedByTitle();
    }

    @GetMapping("/{bookId}")
    public Book getBook(@PathVariable Long bookId) {
        return handleMissingBook(() -> bookService.findBookById(bookId), bookId);
    }

    @PostMapping
    public Book createBook(@Valid @RequestBody Book book) {
        if(bookService.bookExists(book.getIsbn())) {
            throw new ResourceAlreadyExistsException("Book ISBN is already in use");
        }
        return bookService.saveBook(book);
    }

    @PutMapping("/{bookId}")
    public Book updateBook(@PathVariable Long bookId,
                           @Valid @RequestBody Book book) {
        return handleMissingBook(() -> bookService.updateBook(bookId, book), bookId);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId) {
        return handleMissingBook(() -> {
            if(bookService.deleteBook(bookId)) {
                return ResponseEntity.ok().build();
            } else {
                return null;
            }
        }, bookId);
    }

    /**
     * Handles the case of a missing book
     * @param supplier supplier method to execute for the normal result
     * @param bookId bookId
     * @return normal result
     * @throws ResourceNotFoundException if supplier returns null (which should indicate an invalid book id)
     */
    private <T> T handleMissingBook(Supplier<T> supplier, long bookId) {
        return Optional.ofNullable(supplier.get())
                .orElseThrow(() -> new ResourceNotFoundException("Book " + bookId + " not found"));
    }

    /**
     * Helper method to handle formatting of automatic spring validation errors
     * @param ex exception
     * @return map of fields and errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
