package com.library.api.repository;

import com.library.api.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Book repository interface
 */
@Repository
public interface BookDAO extends JpaRepository<Book, Long> {
}
