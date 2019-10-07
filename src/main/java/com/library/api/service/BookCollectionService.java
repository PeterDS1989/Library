package com.library.api.service;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;

import java.util.List;

/**
 * Interface for book collection service
 */
public interface BookCollectionService {

    /**
     * Retrieves all book collections and sorts them by name
     * @return list of sorted book collections
     */
    List<BookCollection> findAllBookCollectionsSortedByName();

    /**
     * Finds a book collection by its id
     * @param bookCollectionId book collection id
     * @return book collection
     * @throws ResourceNotFoundException if book collection id is not linked to a book collection
     */
    BookCollection findBookCollectionById(Long bookCollectionId);

    /**
     * Retrieves the book collections that contain this book
     * @param book book
     * @return list of book collections
     */
    List<BookCollection> findBookCollectionsByBook(Book book);

    /**
     * Creates the book collection
     * @param bookCollection book collection to create
     * @return saved book collection entity
     * @throws ResourceAlreadyExistsException if name of book collection is already used
     */
    BookCollection createBookCollection(BookCollection bookCollection);

    /**
     * Updates the book collection
     * @param bookCollectionId book collection id
     * @param bookCollection updated book collection data
     * @return updated book collection entity
     * @throws ResourceAlreadyExistsException if new name of book collection is already used
     * @throws ResourceNotFoundException if book collection id is not linked to a book collection
     */
    BookCollection updateBookCollection(Long bookCollectionId, BookCollection bookCollection);

    /**
     * Deletes a book collection
     * @param bookCollectionId book collection id
     * @throws ResourceNotFoundException if book collection id is not linked to a book collection
     */
    void deleteBookCollection(Long bookCollectionId);

    /**
     * Adds a book to the book collection
     * @param bookCollectionId book collection id
     * @param bookId book id
     * @return updated book collection
     * @throws ResourceNotFoundException if book collection id or book id is not linked to an entity
     */
    BookCollection addBookToBookCollection(Long bookCollectionId, String bookId);

    /**
     * Deletes a book of the book collection
     * @param bookCollectionId book collection id
     * @param bookId book id
     * @return updated book collection
     * @throws ResourceNotFoundException if book collection id or book id is not linked to an entity
     */
    BookCollection deleteBookFromBookCollection(Long bookCollectionId, String bookId);

    /**
     * Deletes the book from the collection and saves the collection
     * @param bookCollection book collection
     * @param book book
     * @return updated book collection
     */
    BookCollection deleteBookFromBookCollection(BookCollection bookCollection, Book book);
}
