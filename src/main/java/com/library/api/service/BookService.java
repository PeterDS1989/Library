package com.library.api.service;

import com.library.api.domain.Book;
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
     * Checks if the book exists by its isbn
     * @param isbn isbn of the book
     * @return true if it exists
     */
    boolean bookExists(Long isbn);

    /**
     * Saves the book
     * @param book book to save
     * @return the saved book entity
     */
    Book saveBook(Book book);

    /**
     * Updates the book with the given id.
     * @param bookId book id
     * @param book book data
     * @return updated book entity, null if bookId does not link to a book
     */
    Book updateBook(long bookId, Book book);

    /**
     * Deletes the book linked to this id
     * @param bookId book id
     * @return true if bookId exists and was deleted, false otherwise
     */
    boolean deleteBook(long bookId);

    /**
     * Finds the book by this id.
     * @param bookId book id
     * @return book linked to the id, null if no book
     */
    Book findBookById(long bookId);
}
