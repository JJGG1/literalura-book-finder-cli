package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleIgnoreCase(String title);
    @Query("SELECT DISTINCT b FROM Book b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.languages")
    List<Book> findAllWithAuthorsAndLanguages();

    @Query("SELECT b FROM Book b JOIN b.languages l WHERE l = :language")
    List<Book> findBooksByLanguage(String language);





}
