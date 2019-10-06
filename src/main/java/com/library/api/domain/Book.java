package com.library.api.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Book entity
 */
@Entity
@Table(name = "BOOK")
public class Book extends AuditData {

    private static final long serialVersionUID = 4935089404617913837L;

    @Id
    @Column(name = "ISBN", nullable = false)
    @NotNull(message = "ISBN must be filled in")
    private Long isbn;

    @Column(name = "TITLE", nullable = false)
    @NotBlank(message = "Title must be filled in")
    @Size(max = 256, message = "Title can have max 256 characters")
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    @NotBlank(message = "Author must be filled in")
    @Size(max = 256, message = "Author can have max 256 characters")
    private String author;

    /**
     * Constructor
     */
    public Book() {
        super();
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) {
            return true;
        }

        if (object == null || !getClass().isAssignableFrom(object.getClass())) {
            return false;
        }

        Book rhs = (Book) object;
        return new EqualsBuilder()
                .append(this.getIsbn(), rhs.getIsbn())
                .isEquals();
    }

    @Override
    public int hashCode()  {
        return new HashCodeBuilder()
                .append(this.getIsbn())
                .toHashCode();
    }


}
