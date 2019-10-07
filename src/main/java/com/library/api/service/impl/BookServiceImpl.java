package com.library.api.service.impl;

import com.library.api.domain.Book;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.repository.BookDAO;
import com.library.api.service.BookCollectionService;
import com.library.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private static final String RESOURCE_NOT_FOUND_ERROR = "Book with ISBN ''{0}'' not found";
    private static final String ISBN_ALREADY_USED_ERROR = "ISBN ''{0}'' is already used";

    @Autowired
    private BookDAO bookDAO;

    @Autowired
    private BookCollectionService bookCollectionService;

    @Override
    public List<Book> findAllBooksSortedByTitle() {
        //TODO sorting
        return bookDAO.findAll();
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
        return bookDAO.findById(bookId)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setAuthor(book.getAuthor());
                    return bookDAO.save(existingBook);
                }).orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(RESOURCE_NOT_FOUND_ERROR, bookId)));
    }

    @Override
    public void deleteBook(String bookId) {
        Optional<Book> existingBookOpt = bookDAO.findById(bookId);
        if(existingBookOpt.isPresent()) {
            Book existingBook = existingBookOpt.get();
            bookCollectionService.findBookCollectionsByBook(existingBook)
                    .forEach(bookCollection -> bookCollectionService.deleteBookFromBookCollection(bookCollection, existingBook));
            bookDAO.delete(existingBook);
        } else {
            throw new ResourceNotFoundException(MessageFormat.format(RESOURCE_NOT_FOUND_ERROR, bookId));
        }
    }

    @Override
    public Book findBookById(String bookId) {
        return bookDAO.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageFormat.format(RESOURCE_NOT_FOUND_ERROR, bookId)));
    }
}
