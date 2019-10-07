package com.library.api.repository;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Book collection repository interface
 */
@Repository
public interface BookCollectionDAO extends JpaRepository<BookCollection, Long> {

    /**
     * Checks if the name is already used
     * @param name name
     * @return true if exists by name
     */
    boolean existsByName(String name);

    /**
     * Retrieves the book collections that contain this book
     * @param book book
     * @return list of book collections
     */
    List<BookCollection> findBookCollectionsByBooksContains(Book book);
}
