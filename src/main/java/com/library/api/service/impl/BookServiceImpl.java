////////////////////////////////////////////////////////////////////
//
// File: BookServiceImpl.java
// Created: 6/10/2019 16:18
// Author: EAG496
// Electrabel n.v./s.a., Regentlaan 8 Boulevard du Régent, BTW BE 0403.107.701 - 1000 Brussel/Bruxelles, Belgium.
//
// Proprietary Notice:
// This software is the confidential and proprietary information of Electrabel s.a./n.v. and/or its licensors. 
// You shall not disclose this Confidential Information to any third parties
// and any use thereof shall be subject to the terms and conditions of use, as agreed upon with Electrabel in writing.
//
////////////////////////////////////////////////////////////////////
package com.library.api.service.impl;

import com.library.api.domain.Book;
import com.library.api.repository.BookDAO;
import com.library.api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDAO bookDAO;

    @Override
    public List<Book> findAllBooksSortedByTitle() {
        //TODO sorting
        return bookDAO.findAll();
    }

    @Override
    public boolean bookExists(Long isbn) {
        return bookDAO.existsById(isbn);
    }

    @Override
    public Book saveBook(Book book) {
        return bookDAO.save(book);
    }

    @Override
    public Book updateBook(long bookId, Book book) {
        return bookDAO.findById(bookId)
                .map(existingBook -> {
                    existingBook.setTitle(book.getTitle());
                    existingBook.setAuthor(book.getAuthor());
                    return bookDAO.save(existingBook);
                }).orElse(null);
    }

    @Override
    public boolean deleteBook(long bookId) {
        Optional<Book> existingBook = bookDAO.findById(bookId);
        if(existingBook.isPresent()) {
            bookDAO.delete(existingBook.get());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Book findBookById(long bookId) {
        return bookDAO.findById(bookId).orElse(null);
    }
}
