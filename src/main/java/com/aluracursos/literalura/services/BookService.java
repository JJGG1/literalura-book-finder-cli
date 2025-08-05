package com.aluracursos.literalura.services;

import com.aluracursos.literalura.api.BookApi;
import com.aluracursos.literalura.model.Author;
import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookApi bookApi;

    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAllBooksFullyLoaded() {
        return bookRepository.findAllWithAuthorsAndLanguages();
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findBooksByLanguage(language);
    }


    @Transactional
    public List<Book> getBooksByLanguageWithAuthors(String language) {
        List<Book> books = bookRepository.findBooksByLanguage(language);

        // Forzar carga de autores para evitar LazyInitializationException
        books.forEach(book -> book.getAuthors().size());

        return books;
    }

    @Transactional
    public List<Book> searchAndSaveBooks(String search) {
        List<Book> apiBooks = bookApi.searchBooks(search);
        List<Book> savedBooks = new ArrayList<>();


        for (Book apiBook : apiBooks) {
            Optional<Book> existingBookOpt = bookRepository.findByTitleIgnoreCase(apiBook.getTitle().trim());

            Book book = existingBookOpt.orElseGet(() -> {
                Book newBook = new Book();
                newBook.setTitle(apiBook.getTitle().trim());

                // Aquí copias downloadCount
                Integer downloads = apiBook.getDownloadCount();
                newBook.setDownloadCount(downloads != null ? downloads : 0);

                // Aquí copias idiomas si existen
                if (apiBook.getLanguages() != null && !apiBook.getLanguages().isEmpty()) {
                    newBook.setLanguages(new HashSet<>(apiBook.getLanguages()));
                }

                newBook.setAuthors(new HashSet<>());
                return newBook;
            });

            if (apiBook.getAuthors() != null) {
                for (Author apiAuthor : apiBook.getAuthors()) {
                    Optional<Author> existingAuthorOpt = authorRepository.findByNameIgnoreCase(apiAuthor.getName().trim());

                    Author author = existingAuthorOpt.orElseGet(() -> {
                        Author newAuthor = new Author();
                        newAuthor.setName(apiAuthor.getName().trim());
                        newAuthor.setBirthYear(apiAuthor.getBirthYear());
                        newAuthor.setDeathYear(apiAuthor.getDeathYear());
                        return authorRepository.save(newAuthor);
                    });

                    book.addAuthor(author);
                }
            }

            bookRepository.save(book);
            savedBooks.add(book);
        }

        return savedBooks;
    }

}
