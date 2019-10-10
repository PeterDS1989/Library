package com.library.api.service;

import com.library.api.domain.Book;
import com.library.api.service.impl.TrieSortingServiceImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class TrieSortingServiceTest {

    private TrieSortingService trieSortingService = new TrieSortingServiceImpl();

    @Test
    public void testSort() {
        List<Book> booksToSort = new ArrayList<>();

        Book book1 = new Book();
        book1.setIsbn("ISBN-5");
        book1.setAuthor("Book author");
        book1.setTitle("Title of book");
        booksToSort.add(book1);

        Book book2 = new Book();
        book2.setIsbn("ISBN-4");
        book2.setAuthor("Book author");
        book2.setTitle("Book Title");
        booksToSort.add(book2);

        Book book3 = new Book();
        book3.setIsbn("ISBN-2");
        book3.setAuthor("Author of book");
        book3.setTitle("Dark");
        booksToSort.add(book3);

        List<Book> sortedBooks = trieSortingService.sort(booksToSort, Book::getIsbn);
        assertThat(sortedBooks).isNotNull().hasSize(3);
        assertThat(sortedBooks.get(0)).isNotNull().isEqualTo(book3);
        assertThat(sortedBooks.get(1)).isNotNull().isEqualTo(book2);
        assertThat(sortedBooks.get(2)).isNotNull().isEqualTo(book1);

        sortedBooks = trieSortingService.sort(booksToSort, Book::getTitle);
        assertThat(sortedBooks).isNotNull().hasSize(3);
        assertThat(sortedBooks.get(0)).isNotNull().isEqualTo(book2);
        assertThat(sortedBooks.get(1)).isNotNull().isEqualTo(book3);
        assertThat(sortedBooks.get(2)).isNotNull().isEqualTo(book1);

        sortedBooks = trieSortingService.sort(booksToSort, Book::getAuthor);
        assertThat(sortedBooks).isNotNull().hasSize(3);
        assertThat(sortedBooks.get(0)).isNotNull().isEqualTo(book3);
        assertThat(sortedBooks.get(1)).isNotNull().isEqualTo(book1);
        assertThat(sortedBooks.get(2)).isNotNull().isEqualTo(book2);
    }

}
