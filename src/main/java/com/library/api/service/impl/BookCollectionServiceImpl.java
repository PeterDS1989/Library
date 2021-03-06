package com.library.api.service.impl;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.repository.BookCollectionDAO;
import com.library.api.service.BookCollectionService;
import com.library.api.service.BookService;
import com.library.api.service.TrieSortingService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of {@link BookCollectionService}
 */
@Service
public class BookCollectionServiceImpl implements BookCollectionService {

    private static final String RESOURCE_NOT_FOUND_ERROR = "Book collection {0} not found";
    private static final String NAME_ALREADY_USED_ERROR = "Name ''{0}'' is already used";

    @Autowired
    private BookCollectionDAO bookCollectionDAO;

    @Autowired
    private BookService bookService;

    @Autowired
    private TrieSortingService trieSortingService;

    @Override
    public List<BookCollection> findAllBookCollectionsSortedByName() {
        return trieSortingService.sort(bookCollectionDAO.findAll(), BookCollection::getName);
    }

    @Override
    public BookCollection findBookCollectionById(Long bookCollectionId) {
        return bookCollectionDAO.findById(bookCollectionId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(RESOURCE_NOT_FOUND_ERROR, bookCollectionId)));
    }

    @Override
    public List<BookCollection> findBookCollectionsByBook(Book book) {
        return bookCollectionDAO.findBookCollectionsByBooksContains(book);
    }

    @Override
    public BookCollection createBookCollection(BookCollection bookCollection) {
        if(bookCollectionDAO.existsByName(bookCollection.getName())) {
            throw new ResourceAlreadyExistsException(MessageFormat.format(NAME_ALREADY_USED_ERROR, bookCollection.getName()));
        }
        return bookCollectionDAO.save(bookCollection);
    }

    @Override
    public BookCollection updateBookCollection(Long bookCollectionId, BookCollection bookCollection) {
        BookCollection existingBookCollection = findBookCollectionById(bookCollectionId);
        if(!StringUtils.equals(bookCollection.getName(), existingBookCollection.getName())
                && bookCollectionDAO.existsByName(bookCollection.getName())) {
            throw new ResourceAlreadyExistsException(MessageFormat.format(NAME_ALREADY_USED_ERROR, bookCollection.getName()));
        }
        existingBookCollection.setName(bookCollection.getName());
        return bookCollectionDAO.save(existingBookCollection);
    }

    @Override
    public void deleteBookCollection(Long bookCollectionId) {
        BookCollection existingBookCollection = findBookCollectionById(bookCollectionId);
        bookCollectionDAO.delete(existingBookCollection);
    }

    @Override
    public BookCollection addBookToBookCollection(Long bookCollectionId, String bookId) {
        BookCollection existingBookCollection = findBookCollectionById(bookCollectionId);
        Book bookToAdd = bookService.findBookById(bookId);
        existingBookCollection.getBooks().add(bookToAdd);
        existingBookCollection.setLastEditTime(LocalDateTime.now());
        return bookCollectionDAO.save(existingBookCollection);
    }

    @Override
    public BookCollection deleteBookFromBookCollection(Long bookCollectionId, String bookId) {
        BookCollection existingBookCollection = findBookCollectionById(bookCollectionId);
        Book bookToRemove = bookService.findBookById(bookId);
        return deleteBookFromBookCollection(existingBookCollection, bookToRemove);
    }

    @Override
    public BookCollection deleteBookFromBookCollection(BookCollection bookCollection, Book book) {
        bookCollection.getBooks().remove(book);
        bookCollection.setLastEditTime(LocalDateTime.now());
        return bookCollectionDAO.save(bookCollection);
    }

}
