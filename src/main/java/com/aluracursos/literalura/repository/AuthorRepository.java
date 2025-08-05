package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByNameIgnoreCase(String name);  // para buscar sin libros

    @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE UPPER(a.name) = UPPER(:name)")
    Optional<Author> findByNameIgnoreCaseWithBooks(@Param("name") String name);  // para buscar con libros

    @Query("SELECT a FROM Author a JOIN FETCH a.books WHERE a.id = :id")
    Optional<Author> findByIdWithBooks(@Param("id") Long id);

    @Query("""
    SELECT DISTINCT a FROM Author a
    JOIN FETCH a.books b
    JOIN FETCH b.languages
    WHERE a.birthYear <= :endYear
    AND (a.deathYear IS NULL OR a.deathYear >= :startYear)
""")
    List<Author> findAuthorsAliveBetween(@Param("startYear") int startYear,
                                         @Param("endYear") int endYear);



}
