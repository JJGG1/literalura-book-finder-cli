package com.aluracursos.literalura.menu;

import com.aluracursos.literalura.api.BookApi;
import com.aluracursos.literalura.model.Author;
import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.repository.AuthorRepository;
import com.aluracursos.literalura.repository.BookRepository;
import com.aluracursos.literalura.services.AuthorService;
import com.aluracursos.literalura.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final BookApi bookApi;

    private final List<Book> searchedBooks = new ArrayList<>();

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;


    public ConsoleMenu(BookApi bookApi) {
        this.bookApi = bookApi;
    }

    public void showMenu(){
        int option = -1;

        while (option != 0) {
            System.out.println("\n📚 === MENÚ PRINCIPAL ===");
            System.out.println("1. Buscar libros por autor/título");
            System.out.println("2. Ver libros guardados");
            System.out.println("3. Listar autores de búsquedas registradas");
            System.out.println("4. Listar autores vivos en un período determinado");
            System.out.println("5. Listar libros por idioma");
            System.out.println("6. Listar Top 10 libros mas descargados");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Debes de ingresar un número.");
                continue;
            }

            switch (option) {
                case 1 -> searchBooks();
                case 2 -> showSavedBooks();
                case 3 -> showAllAuthors();
                case 4 -> showBooksByAuthorsAliveInPeriod();
                case 5 -> showBooksByLanguage();
                case 6 -> showTop10MostDownloadedBooks();
                case 0 -> System.out.println("👋 Saliendo de la aplicación.");
                default -> System.out.println("❌ Opción no válida. Debes de ingresar un múmero de las opciones del menú");
            }
        }
    }

    private void showTop10MostDownloadedBooks() {
        List<Book> topBooks = bookService.getTop10MostDownloadedBooks();
        System.out.println("📚 Los 10 libros más descargados:");

        for (Book book : topBooks) {
            System.out.printf("• %s (Descargas: %d)\n", book.getTitle(), book.getDownloadCount());
        }
    }

    private void showBooksByLanguage() {
        System.out.print("🌐 Ingresa el idioma (por ejemplo: 'en', 'es', 'fr'): ");
        String language = scanner.nextLine().trim();

        List<Book> books = bookService.getBooksByLanguageWithAuthors(language);

        if (books.isEmpty()) {
            System.out.println("❌ No se encontraron libros en el idioma '" + language + "'.");
            return;
        }

        System.out.println("📚 Libros en idioma '" + language + "':");
        books.forEach(book -> {
            String authors = book.getAuthors().stream()
                    .map(Author::getName)
                    .collect(Collectors.joining(", "));
            System.out.println("• " + book.getTitle() + " - Autor(es): " + authors);
        });
    }


    public void showBooksByAuthorsAliveInPeriod() {
        System.out.print("🔢 Año de inicio: ");
        int startYear = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("🔢 Año de fin: ");
        int endYear = Integer.parseInt(scanner.nextLine().trim());

        List<Author> authors = authorRepository.findAuthorsAliveBetween(startYear, endYear);

        if (authors.isEmpty()) {
            System.out.println("❌ No se encontraron autores vivos en ese período.");
            return;
        }

        Set<Book> books = new HashSet<>();
        for (Author author : authors) {
            books.addAll(author.getBooks());
        }

        if (books.isEmpty()) {
            System.out.println("ℹ️ No se encontraron libros de esos autores.");
        } else {
            System.out.println("📚 Libros de autores vivos entre " + startYear + " y " + endYear + ":");
            books.forEach(book -> {
                System.out.println("📖 " + book.getTitle() + " | 📥 " + book.getDownloadCount() +
                        " descargas | 🌐 Idiomas: " + String.join(", ", book.getLanguages()));
            });
        }
    }


    public void searchBooks() {
        System.out.print("🔍 Ingresa el nombre del libro o autor a buscar: ");
        String search = scanner.nextLine().trim();

        List<Book> savedBooks = bookService.searchAndSaveBooks(search); // Llama al servicio

        if (savedBooks.isEmpty()) {
            System.out.println("❌ No se encontraron libros para esa búsqueda.");
            return;
        }

        int addedCount = 0;

        for (Book book : savedBooks) {
            boolean alreadyInMemory = searchedBooks.stream()
                    .anyMatch(b -> b.getTitle().equalsIgnoreCase(book.getTitle()));

            if (!alreadyInMemory) {
                searchedBooks.add(book);
                System.out.println("✅ Libro agregado y guardado: " + book.getTitle());
                addedCount++;
            } else {
                System.out.println("⚠️ El libro '" + book.getTitle() + "' ya está en la lista temporal.");
            }
        }

        if (addedCount == 0) {
            System.out.println("ℹ️ No se agregaron libros nuevos.");
        }
    }







    public void showSavedBooks() {
        List<Book> savedBooks = bookService.findAllBooksFullyLoaded();

        if (savedBooks.isEmpty()) {
            System.out.println("ℹ️ No hay libros guardados en la base de datos.");
            return;
        }

        for (Book book : savedBooks) {
            System.out.println("- " + book.getTitle() + " | Descargas: " + book.getDownloadCount());

            if (book.getAuthors() != null && !book.getAuthors().isEmpty()) {
                String authorsNames = book.getAuthors().stream()
                        .map(Author::getName)
                        .collect(Collectors.joining(", "));
                System.out.println("  Autores: " + authorsNames);
            }

            if (book.getLanguages() != null && !book.getLanguages().isEmpty()) {
                String languages = String.join(", ", book.getLanguages());
                System.out.println("  Idiomas: " + languages);
            }
        }
    }


    public void showAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        System.out.println("Listado de autores registrados:");
        for (Author author : authors) {
            System.out.printf("- %s (Nacimiento: %d, Fallecimiento: %d)\n",
                    author.getName(),
                    author.getBirthYear() != null ? author.getBirthYear() : 0,
                    author.getDeathYear() != null ? author.getDeathYear() : 0);
        }
    }

}
