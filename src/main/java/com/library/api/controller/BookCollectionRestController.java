package com.library.api.controller;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.service.BookCollectionService;
import com.library.api.service.TrieSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest controller for book collections
 */
@RestController
@RequestMapping("/api/bookCollections")
public class BookCollectionRestController extends AbstractRestController {

    @Autowired
    private BookCollectionService bookCollectionService;

    @Autowired
    private TrieSortingService trieSortingService;

    @GetMapping
    public List<BookCollection> getBookCollections() {
        return bookCollectionService.findAllBookCollectionsSortedByName();
    }

    @GetMapping("/{bookCollectionId}")
    public BookCollection getBookCollection(@PathVariable Long bookCollectionId) {
        return bookCollectionService.findBookCollectionById(bookCollectionId);
    }

    @PostMapping
    public BookCollection createBookCollection(@Valid @RequestBody BookCollection bookCollection) {
        return bookCollectionService.createBookCollection(bookCollection);
    }

    @PutMapping("/{bookCollectionId}")
    public BookCollection updateBookCollection(@PathVariable Long bookCollectionId,
                           @Valid @RequestBody BookCollection bookCollection) {
        return bookCollectionService.updateBookCollection(bookCollectionId, bookCollection);
    }

    @DeleteMapping("/{bookCollectionId}")
    public ResponseEntity<?> deleteBookCollection(@PathVariable Long bookCollectionId) {
        bookCollectionService.deleteBookCollection(bookCollectionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookCollectionId}/books")
    public List<Book> getBooksFromBookCollection(@PathVariable Long bookCollectionId) {
        return trieSortingService.sort(bookCollectionService.findBookCollectionById(bookCollectionId).getBooks(), Book::getTitle);
    }

    @PutMapping("/{bookCollectionId}/books/{bookId}")
    public List<Book> addBookToBookCollection(@PathVariable Long bookCollectionId, @PathVariable String bookId) {
        return trieSortingService.sort(bookCollectionService.addBookToBookCollection(bookCollectionId, bookId).getBooks(), Book::getTitle);
    }

    @DeleteMapping("/{bookCollectionId}/books/{bookId}")
    public List<Book> deleteBookFromCollection(@PathVariable Long bookCollectionId, @PathVariable String bookId) {
        return trieSortingService.sort(bookCollectionService.deleteBookFromBookCollection(bookCollectionId, bookId).getBooks(), Book::getTitle);
    }
}
