package com.library.api.service;

import com.library.api.domain.Book;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookService {

    /**
     * Returns a list of all books, sorted by title
     * @return list of all books, sorted by title
     */
    List<Book> findAllBooksSortedByTitle();

    /**
     * Saves the book if it does not exist yet
     * @param book book
     * @return the saved book entity
     * @throws ResourceAlreadyExistsException if the book isbn is already used
     */
    Book createBook(Book book);

    /**
     * Updates the book with the given id.
     * @param bookId book id
     * @param book book data
     * @return updated book entity
     * @throws ResourceNotFoundException if bookId does not link to a book
     */
    Book updateBook(String bookId, Book book);

    /**
     * Deletes the book linked to this id
     * @param bookId book id
     * @throws ResourceNotFoundException if bookId does not link to a book
     */
    void deleteBook(String bookId);

    /**
     * Finds the book by this id.
     * @param bookId book id
     * @return book linked to the id
     * @throws ResourceNotFoundException if bookId does not link to a book
     */
    Book findBookById(String bookId);
}
