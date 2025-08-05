package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // O usa @GeneratedValue si no tienes id externo

    private String title;

    @JsonProperty("download_count")
    @Column(name = "download_count")
    private Integer downloadCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    private Set<String> languages = new HashSet<>();

    // Getter y setter para languages
    public Set<String> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<String> languages) {
        this.languages = languages;
    }


    // Constructor vacío obligatorio
    public Book() {}

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    // Método para mantener relación bidireccional
    public void addAuthor(Author author) {
        if (authors == null) {
            authors = new HashSet<>();
        }
        if (!authors.contains(author)) {
            authors.add(author);
        }

        if (author.getBooks() == null) {
            author.setBooks(new HashSet<>());
        }
        if (!author.getBooks().contains(this)) {
            author.getBooks().add(this);
        }
    }

    public void removeAuthor(Author author) {
        if (authors != null && authors.contains(author)) {
            authors.remove(author);
            author.getBooks().remove(this);
        }
    }
}
