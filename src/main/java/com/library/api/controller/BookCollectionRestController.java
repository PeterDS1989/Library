////////////////////////////////////////////////////////////////////
//
// File: BookCollectionController.java
// Created: 7/10/2019 8:05
// Author: EAG496
// Electrabel n.v./s.a., Regentlaan 8 Boulevard du Régent, BTW BE 0403.107.701 - 1000 Brussel/Bruxelles, Belgium.
//
// Proprietary Notice:
// This software is the confidential and proprietary information of Electrabel s.a./n.v. and/or its licensors. 
// You shall not disclose this Confidential Information to any third parties
// and any use thereof shall be subject to the terms and conditions of use, as agreed upon with Electrabel in writing.
//
////////////////////////////////////////////////////////////////////
package com.library.api.controller;

import com.library.api.domain.Book;
import com.library.api.domain.BookCollection;
import com.library.api.service.BookCollectionService;
import com.library.api.service.TrieSortingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest controller for book collections
 */
@RestController
@RequestMapping("/api/bookCollections")
public class BookCollectionRestController extends AbstractRestController {

    @Autowired
    private BookCollectionService bookCollectionService;

    @Autowired
    private TrieSortingService trieSortingService;

    @GetMapping
    public List<BookCollection> getBookCollections() {
        return bookCollectionService.findAllBookCollectionsSortedByName();
    }

    @GetMapping("/{bookCollectionId}")
    public BookCollection getBookCollection(@PathVariable Long bookCollectionId) {
        return bookCollectionService.findBookCollectionById(bookCollectionId);
    }

    @PostMapping
    public BookCollection createBookCollection(@Valid @RequestBody BookCollection bookCollection) {
        return bookCollectionService.createBookCollection(bookCollection);
    }

    @PutMapping("/{bookCollectionId}")
    public BookCollection updateBookCollection(@PathVariable Long bookCollectionId,
                           @Valid @RequestBody BookCollection bookCollection) {
        return bookCollectionService.updateBookCollection(bookCollectionId, bookCollection);
    }

    @DeleteMapping("/{bookCollectionId}")
    public ResponseEntity<?> deleteBookCollection(@PathVariable Long bookCollectionId) {
        bookCollectionService.deleteBookCollection(bookCollectionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{bookCollectionId}/books")
    public List<Book> getBooksFromBookCollection(@PathVariable Long bookCollectionId) {
        return trieSortingService.sort(bookCollectionService.findBookCollectionById(bookCollectionId).getBooks(), Book::getTitle);
    }

    @PutMapping("/{bookCollectionId}/books/{bookId}")
    public List<Book> addBookToBookCollection(@PathVariable Long bookCollectionId, @PathVariable String bookId) {
        return trieSortingService.sort(bookCollectionService.addBookToBookCollection(bookCollectionId, bookId).getBooks(), Book::getTitle);
    }

    @DeleteMapping("/{bookCollectionId}/books/{bookId}")
    public List<Book> deleteBookFromCollection(@PathVariable Long bookCollectionId, @PathVariable String bookId) {
        return trieSortingService.sort(bookCollectionService.deleteBookFromBookCollection(bookCollectionId, bookId).getBooks(), Book::getTitle);
    }
}
