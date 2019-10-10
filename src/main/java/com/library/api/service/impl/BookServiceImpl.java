package com.library.api.service.impl;

import com.library.api.domain.Book;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.repository.BookDAO;
import com.library.api.service.BookCollectionService;
import com.library.api.service.BookService;
import com.library.api.service.TrieSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final String RESOURCE_NOT_FOUND_ERROR = "Book with ISBN ''{0}'' not found";
    private static final String ISBN_ALREADY_USED_ERROR = "ISBN ''{0}'' is already used";

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookCollectionService bookCollectionService;

    @Autowired
    private TrieSortingService trieSortingService;

    @Override
    public List<Book> findAllBooksSortedByTitle() {
        return trieSortingService.sort(bookDAO.findAll(), Book::getTitle);
    }

    @Override
    public Book createBook(Book book) {
        if(!bookDAO.existsById(book.getIsbn())) {
            return bookDAO.save(book);
        }
        throw new ResourceAlreadyExistsException(MessageFormat.format(ISBN_ALREADY_USED_ERROR, book.getIsbn()));
    }

    @Override
    public Book updateBook(String bookId, Book book) {
        Book existingBook = findBookById(bookId);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        return bookDAO.save(existingBook);
    }

    @Override
    public void deleteBook(String bookId) {
        Book existingBook = findBookById(bookId);
        bookCollectionService.findBookCollectionsByBook(existingBook)
                .forEach(bookCollection -> bookCollectionService.deleteBookFromBookCollection(bookCollection, existingBook));
        bookDAO.delete(existingBook);
    }

    @Override
    public Book findBookById(String bookId) {
        return bookDAO.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(RESOURCE_NOT_FOUND_ERROR, bookId)));
    }
}
