package com.library.api.service;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.repository.BookDAO;
import com.library.api.service.impl.BookServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookDAO bookDAO;

    @Mock
    private BookCollectionService bookCollectionService;

    @Mock
    private TrieSortingService trieSortingService;

    @InjectMocks
    private BookService bookService = new BookServiceImpl();


    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    //Rule instead of after to only do this if the test was successful
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            verifyNoMoreInteractions(bookDAO, trieSortingService, bookCollectionService);
        }
    };

    @Test
    public void testFindAllBooksSortedByTitle() {

        List<Book> bookList = new ArrayList<>();
        Book book = new Book();
        book.setIsbn("ISBN-5");
        bookList.add(book);

        when(bookDAO.findAll()).thenReturn(bookList);
        when(trieSortingService.sort(eq(bookList), any())).thenReturn(bookList);

        List<Book> result = bookService.findAllBooksSortedByTitle();
        verify(bookDAO).findAll();
        verify(trieSortingService).sort(eq(bookList), any());
        assertThat(result).isNotNull().isEqualTo(bookList);
    }

    @Test
    public void testCreateBook() {
        Book bookToSave = new Book();
        bookToSave.setIsbn("ISBN-5");
        bookToSave.setTitle("Title");
        bookToSave.setAuthor("Author");

        when(bookDAO.existsById("ISBN-5")).thenReturn(false);
        when(bookDAO.save(bookToSave)).thenReturn(bookToSave);

        Book result = bookService.createBook(bookToSave);

        verify(bookDAO).existsById("ISBN-5");
        verify(bookDAO).save(bookToSave);

        assertThat(result).isNotNull().isEqualTo(bookToSave);
        assertThat(result.getIsbn()).isNotNull().isEqualTo("ISBN-5");
        assertThat(result.getTitle()).isNotNull().isEqualTo("Title");
        assertThat(result.getAuthor()).isNotNull().isEqualTo("Author");
    }

    @Test
    public void testCreateBookException() {
        Book bookToSave = new Book();
        bookToSave.setIsbn("ISBN-5");
        bookToSave.setTitle("Title");
        bookToSave.setAuthor("Author");

        when(bookDAO.existsById("ISBN-5")).thenReturn(true);

        try {
            bookService.createBook(bookToSave);
            Assert.fail();
        } catch(ResourceAlreadyExistsException raee) {
            assertThat(raee.getMessage()).isNotNull().isEqualTo("ISBN 'ISBN-5' is already used");
        }
        verify(bookDAO).existsById("ISBN-5");
    }

    @Test
    public void testUpdateBook() {
        String bookId = "ISBN-5";
        Book bookToSave = new Book();
        bookToSave.setIsbn("ISBN-5");
        bookToSave.setTitle("Title");
        bookToSave.setAuthor("Author");

        Book savedBook = new Book();
        savedBook.setIsbn("ISBN-5");
        savedBook.setTitle("Saved title");
        savedBook.setAuthor("Saved author");

        when(bookDAO.findById(bookId)).thenReturn(Optional.of(savedBook));
        when(bookDAO.save(bookToSave)).thenReturn(bookToSave);

        Book result = bookService.updateBook(bookId, bookToSave);

        verify(bookDAO).findById("ISBN-5");
        verify(bookDAO).save(bookToSave);

        assertThat(result).isNotNull().isEqualTo(bookToSave);
        assertThat(result.getIsbn()).isNotNull().isEqualTo("ISBN-5");
        assertThat(result.getTitle()).isNotNull().isEqualTo("Title");
        assertThat(result.getAuthor()).isNotNull().isEqualTo("Author");
    }

    @Test
    public void testUpdateBookException() {
        String bookId = "ISBN-5";
        Book bookToSave = new Book();
        bookToSave.setIsbn("ISBN-5");
        bookToSave.setTitle("Title");
        bookToSave.setAuthor("Author");

        when(bookDAO.findById(bookId)).thenReturn(Optional.empty());

        try {
            bookService.updateBook(bookId, bookToSave);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book with ISBN 'ISBN-5' not found");
        }
        verify(bookDAO).findById(bookId);
    }

    @Test
    public void testDeleteBook() {
        String bookId = "ISBN-5";
        Book bookToDelete = new Book();
        bookToDelete.setIsbn(bookId);

        List<BookCollection> collectionList = new ArrayList<>();
        BookCollection collection1 = new BookCollection();
        collection1.setName("Collection1");
        collectionList.add(collection1);
        BookCollection collection2 = new BookCollection();
        collection2.setName("Collection2");
        collectionList.add(collection2);

        when(bookDAO.findById(bookId)).thenReturn(Optional.of(bookToDelete));
        when(bookCollectionService.findBookCollectionsByBook(bookToDelete)).thenReturn(collectionList);

        bookService.deleteBook(bookId);

        verify(bookDAO).findById(bookId);
        verify(bookCollectionService).findBookCollectionsByBook(bookToDelete);
        verify(bookCollectionService).deleteBookFromBookCollection(collection1, bookToDelete);
        verify(bookCollectionService).deleteBookFromBookCollection(collection2, bookToDelete);
        verify(bookDAO).delete(bookToDelete);
    }

    @Test
    public void testDeleteBookException() {
        String bookId = "ISBN-5";

        when(bookDAO.findById(bookId)).thenReturn(Optional.empty());

        try {
            bookService.deleteBook(bookId);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book with ISBN 'ISBN-5' not found");
        }
        verify(bookDAO).findById(bookId);
    }

    @Test
    public void testFindBookById(){
        String bookId = "ISBN-5";
        Book book = new Book();
        book.setIsbn(bookId);

        when(bookDAO.findById(bookId)).thenReturn(Optional.of(book));

        Book result = bookService.findBookById(bookId);
        assertThat(result).isNotNull().isEqualTo(book);
        verify(bookDAO).findById(bookId);
    }

    @Test
    public void testFindBookByIdException() {
        String bookId = "ISBN-5";

        when(bookDAO.findById(bookId)).thenReturn(Optional.empty());

        try {
            bookService.findBookById(bookId);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book with ISBN 'ISBN-5' not found");
        }
        verify(bookDAO).findById(bookId);
    }
}
