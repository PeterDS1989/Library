package com.library.api.service;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.exception.ResourceAlreadyExistsException;
import com.library.api.exception.ResourceNotFoundException;
import com.library.api.repository.BookCollectionDAO;
import com.library.api.service.impl.BookCollectionServiceImpl;
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

public class BookCollectionServiceTest {

    @Mock
    private BookCollectionDAO bookCollectionDAO;

    @Mock
    private BookService bookService;

    @Mock
    private TrieSortingService trieSortingService;

    @InjectMocks
    private BookCollectionService bookCollectionService = new BookCollectionServiceImpl();

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    //Rule instead of after to only do this if the test was successful
    @Rule
    public TestWatcher watchman = new TestWatcher() {
        @Override
        protected void succeeded(Description description) {
            verifyNoMoreInteractions(bookCollectionDAO, trieSortingService, bookService);
        }
    };

    @Test
    public void testFindAllBookCollectionsSortedByName() {
        List<BookCollection> collectionList = new ArrayList<>();
        BookCollection collection = new BookCollection();
        collection.setName("Collection");
        collectionList.add(collection);

        when(bookCollectionDAO.findAll()).thenReturn(collectionList);
        when(trieSortingService.sort(eq(collectionList), any())).thenReturn(collectionList);

        List<BookCollection> result = bookCollectionService.findAllBookCollectionsSortedByName();
        verify(bookCollectionDAO).findAll();
        verify(trieSortingService).sort(eq(collectionList), any());
        assertThat(result).isNotNull().isEqualTo(collectionList);
    }

    @Test
    public void testFindBookCollectionById() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(collection));

        BookCollection result = bookCollectionService.findBookCollectionById(5L);
        verify(bookCollectionDAO).findById(5L);
        assertThat(result).isNotNull().isEqualTo(collection);
    }

    @Test
    public void testFindBookCollectionByIdException() {
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.empty());

        try {
            bookCollectionService.findBookCollectionById(5L);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book collection 5 not found");
        }
        verify(bookCollectionDAO).findById(5L);
    }

    @Test
    public void testFindBookCollectionsByBook() {
        List<BookCollection> collectionList = new ArrayList<>();
        BookCollection collection1 = new BookCollection();
        collection1.setName("Collection1");
        collectionList.add(collection1);

        BookCollection collection2 = new BookCollection();
        collection2.setName("Collection2");
        collectionList.add(collection2);

        Book book = new Book();
        book.setIsbn("ISBN-6");

        when(bookCollectionDAO.findBookCollectionsByBooksContains(book)).thenReturn(collectionList);
        List<BookCollection> result = bookCollectionService.findBookCollectionsByBook(book);
        verify(bookCollectionDAO).findBookCollectionsByBooksContains(book);
        assertThat(result).isNotNull().isEqualTo(collectionList);
    }

    @Test
    public void testCreateBookCollection() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        when(bookCollectionDAO.existsByName("Collection")).thenReturn(false);
        when(bookCollectionDAO.save(collection)).thenReturn(collection);
        BookCollection result = bookCollectionService.createBookCollection(collection);
        verify(bookCollectionDAO).existsByName("Collection");
        verify(bookCollectionDAO).save(collection);
        assertThat(result).isNotNull().isEqualTo(collection);
    }

    @Test
    public void testCreateBookCollectionException() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        when(bookCollectionDAO.existsByName("Collection")).thenReturn(true);
        try {
            bookCollectionService.createBookCollection(collection);
            Assert.fail();
        } catch(ResourceAlreadyExistsException raee) {
            assertThat(raee.getMessage()).isNotNull().isEqualTo("Name 'Collection' is already used");
        }
        verify(bookCollectionDAO).existsByName("Collection");
    }

    @Test
    public void testUpdateBookCollection() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        BookCollection savedCollection = new BookCollection();
        savedCollection.setName("Saved collection name");

        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(savedCollection));
        when(bookCollectionDAO.existsByName("Collection")).thenReturn(false);
        when(bookCollectionDAO.save(savedCollection)).thenReturn(savedCollection);

        BookCollection result = bookCollectionService.updateBookCollection(5L, collection);
        verify(bookCollectionDAO).findById(5L);
        verify(bookCollectionDAO).existsByName("Collection");
        verify(bookCollectionDAO).save(savedCollection);
        assertThat(result).isNotNull().isEqualTo(savedCollection);
        assertThat(result.getName()).isNotNull().isEqualTo("Collection");
    }

    @Test
    public void testUpdateBookCollectionExceptionIdNotFound() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.empty());

        try {
            bookCollectionService.updateBookCollection(5L, collection);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book collection 5 not found");
        }
        verify(bookCollectionDAO).findById(5L);
    }

    @Test
    public void testUpdateBookCollectionExceptionNameAlreadyUsed() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        BookCollection savedCollection = new BookCollection();
        savedCollection.setName("Saved collection name");

        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(savedCollection));
        when(bookCollectionDAO.existsByName("Collection")).thenReturn(true);

        try {
            bookCollectionService.updateBookCollection(5L, collection);
            Assert.fail();
        } catch(ResourceAlreadyExistsException raee) {
            assertThat(raee.getMessage()).isNotNull().isEqualTo("Name 'Collection' is already used");
        }
        verify(bookCollectionDAO).findById(5L);
        verify(bookCollectionDAO).existsByName("Collection");
    }

    @Test
    public void testDeleteBookCollection() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(collection));

        bookCollectionService.deleteBookCollection(5L);

        verify(bookCollectionDAO).findById(5L);
        verify(bookCollectionDAO).delete(collection);
    }

    @Test
    public void testDeleteBookCollectionException() {
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.empty());
        try {
            bookCollectionService.deleteBookCollection(5L);
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book collection 5 not found");
        }
        verify(bookCollectionDAO).findById(5L);
    }

    @Test
    public void testAddBookToBookCollection() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        Book bookToAdd = new Book();
        bookToAdd.setIsbn("ISBN-5");

        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(collection));
        when(bookService.findBookById("ISBN-5")).thenReturn(bookToAdd);
        when(bookCollectionDAO.save(collection)).thenReturn(collection);

        BookCollection result = bookCollectionService.addBookToBookCollection(5L, "ISBN-5");

        verify(bookCollectionDAO).findById(5L);
        verify(bookService).findBookById("ISBN-5");
        verify(bookCollectionDAO).save(collection);

        assertThat(result).isNotNull().isEqualTo(collection);
        assertThat(result.getBooks()).hasSize(1).contains(bookToAdd);
        assertThat(result.getLastEditTime()).isNotNull();
    }

    @Test
    public void testAddBookToBookCollectionException() {
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.empty());
        try {
            bookCollectionService.addBookToBookCollection(5L, "ISBN");
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book collection 5 not found");
        }
        verify(bookCollectionDAO).findById(5L);
    }

    @Test
    public void testDeleteBookFromBookCollection() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        Book bookToDelete = new Book();
        bookToDelete.setIsbn("ISBN-5");
        collection.getBooks().add(bookToDelete);

        Book otherBook = new Book();
        otherBook.setIsbn("ISBN-6");
        collection.getBooks().add(otherBook);


        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.of(collection));
        when(bookService.findBookById("ISBN-5")).thenReturn(bookToDelete);
        when(bookCollectionDAO.save(collection)).thenReturn(collection);

        BookCollection result = bookCollectionService.deleteBookFromBookCollection(5L, "ISBN-5");

        verify(bookCollectionDAO).findById(5L);
        verify(bookService).findBookById("ISBN-5");
        verify(bookCollectionDAO).save(collection);

        assertThat(result).isNotNull().isEqualTo(collection);
        assertThat(result.getBooks()).hasSize(1).contains(otherBook);
        assertThat(result.getLastEditTime()).isNotNull();
    }

    @Test
    public void testDeleteBookFromBookCollectionException() {
        when(bookCollectionDAO.findById(5L)).thenReturn(Optional.empty());
        try {
            bookCollectionService.deleteBookFromBookCollection(5L, "ISBN");
            Assert.fail();
        } catch(ResourceNotFoundException rnfe) {
            assertThat(rnfe.getMessage()).isNotNull().isEqualTo("Book collection 5 not found");
        }
        verify(bookCollectionDAO).findById(5L);
    }

    @Test
    public void testDeleteBookFromBookCollectionWithObjects() {
        BookCollection collection = new BookCollection();
        collection.setName("Collection");

        Book bookToDelete = new Book();
        bookToDelete.setIsbn("ISBN-5");
        collection.getBooks().add(bookToDelete);

        Book otherBook = new Book();
        otherBook.setIsbn("ISBN-6");
        collection.getBooks().add(otherBook);

        when(bookCollectionDAO.save(collection)).thenReturn(collection);
        BookCollection result = bookCollectionService.deleteBookFromBookCollection(collection, bookToDelete);

        verify(bookCollectionDAO).save(collection);

        assertThat(result).isNotNull().isEqualTo(collection);
        assertThat(result.getBooks()).hasSize(1).contains(otherBook);
        assertThat(result.getLastEditTime()).isNotNull();
    }
}
