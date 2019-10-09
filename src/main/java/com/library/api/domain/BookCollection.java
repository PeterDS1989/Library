package com.library.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Book collection entity
 */
@Entity
@Table(name = "BOOK_COLLECTION")
@JsonIgnoreProperties(
        value = {"id"},
        allowGetters = true
)
public class BookCollection extends AuditData {

    private static final long serialVersionUID = -4877865750639618895L;

    @Id
    @GeneratedValue(generator = "BOOK_COLLECTION_ID_GEN")
    @SequenceGenerator(name = "BOOK_COLLECTION_ID_GEN",
        sequenceName = "BOOK_COLLECTION_ID_SEQ", allocationSize = 1)
    @Column(name = "BOOK_COLLECTION_ID")
    private Long id;

    @NotBlank(message = "Name must be filled in")
    @Size(max = 256, message = "Name can have max 256 characters")
    @Column(name = "NAME", nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOOK_COLLECTION_LINK",
            joinColumns = @JoinColumn(name = "BOOK_COLLECTION_ID"),
            inverseJoinColumns = @JoinColumn(name = "ISBN"))
    @JsonIgnore
    private Set<Book> books = new HashSet<>();

    public BookCollection() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) {
            return true;
        }

        if (object == null || !getClass().isAssignableFrom(object.getClass())) {
            return false;
        }

        BookCollection rhs = (BookCollection) object;
        return new EqualsBuilder()
                .append(this.getName(), rhs.getName())
                .isEquals();
    }

    @Override
    public int hashCode()  {
        return new HashCodeBuilder()
                .append(this.getName())
                .toHashCode();
    }
}
