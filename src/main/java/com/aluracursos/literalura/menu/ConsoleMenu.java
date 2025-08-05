package com.aluracursos.literalura.menu;

import com.aluracursos.literalura.api.BookApi;
import com.aluracursos.literalura.model.Author;
import com.aluracursos.literalura.model.Book;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final BookApi bookApi;

    private final List<Book> searchedBooks = new ArrayList<>();

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
                case 2 -> showSearchedBooks();
                case 3 -> listAuthorsFromSearches();
                case 4 -> listAuthorsAliveInPeriod();
                case 5 -> listBooksByLanguage();
                case 0 -> System.out.println("👋 Saliendo de la aplicación.");
                default -> System.out.println("❌ Opción no válida. Debes de ingresar un múmero de las opciones del menú");
            }
        }
    }

    private void listBooksByLanguage() {
        if (searchedBooks.isEmpty()){
            System.out.println("⚠️ No hay libros buscados para filtrar por idioma.");
            return;
        }

        System.out.println("Ingrese el código del idioma (por ejemplo: en, es, fr): ");
        String inputLang = scanner.nextLine().trim().toLowerCase();

        List<Book> filteredBooks = new ArrayList<>();

        for (Book book : searchedBooks) {
            if (book.getLanguages() != null && book.getLanguages().contains(inputLang)){
                filteredBooks.add(book);
            }
        }

        if (filteredBooks.isEmpty()){
            System.out.println("❌ No se encontraron libros en el idioma '" + inputLang + "'.");
        } else {
            System.out.println("📚 Libros en idioma '" + inputLang + "':");
            for (Book book : filteredBooks) {
                System.out.println("🔹 " + book.getTitle());
            }
        }
    }

    private void listAuthorsAliveInPeriod() {
        if (searchedBooks.isEmpty()) {
            System.out.println("⚠️ No hay libros buscados para analizar autores.");
            return;
        }

        try {
            System.out.print("Ingrese el año de inicio: ");
            int startYear = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el año de fin: ");
            int endYear = Integer.parseInt(scanner.nextLine());

            Set<String> authorsAlive = new TreeSet<>();

            for (Book book : searchedBooks) {
                for (Author author : book.getAuthors()) {
                    Integer birth = author.getBirth_year();
                    Integer death = author.getDeath_year();

                    // Consideramos autores que estaban vivos en algún punto del rango
                    boolean wasAliveInPeriod = (birth != null && birth <= endYear) &&
                            (death == null || death >= startYear);

                    if (wasAliveInPeriod) {
                        authorsAlive.add(author.getName() + " (" + birth + " - " + (death != null ? death : "¿vivo?") + ")");
                    }
                }
            }

            if (authorsAlive.isEmpty()) {
                System.out.println("❌ No se encontraron autores vivos en ese período.");
            } else {
                System.out.println("✅ Autores vivos entre " + startYear + " y " + endYear + ":");
                authorsAlive.forEach(System.out::println);
            }

        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida. Por favor ingrese números válidos.");
        }
    }

    private void searchBooks() {
        System.out.print("🔍 Ingresa el nombre del libro o autor a buscar: ");
        String search = scanner.nextLine().trim();

        List<Book> books = bookApi.searchBooks(search);

        if (books.isEmpty()) {
            System.out.println("❌ No se encontraron libros para esa búsqueda.");
            return;
        }

        int addedCount = 0;

        for (Book book : books) {
            boolean alreadyExists = searchedBooks.stream()
                    .anyMatch(b -> b.getTitle().trim().equalsIgnoreCase(book.getTitle().trim()));

            if (!alreadyExists) {
                searchedBooks.add(book);
                System.out.println("✅ Libro agregado: " + book.getTitle());
                addedCount++;
            } else {
                System.out.println("⚠️ El libro '" + book.getTitle() + "' ya está registrado. No se añadió de nuevo.");
            }
        }

        if (addedCount == 0) {
            System.out.println("ℹ️ No se agregaron libros nuevos.");
        }
    }


    private void showSearchedBooks(){
        if (searchedBooks.isEmpty()) {
            System.out.println("⚠️ No hay libros guardados de búsquedas anteriores.");
        } else {
            System.out.println("📚 Libros buscados anteriormente:");
            for (Book book : searchedBooks) {
                System.out.println("📘 Título: " + book.getTitle());
                System.out.println("👤 Autor(es): " + book.getAuthors());
                System.out.println("🌐 Idioma(s): " + book.getLanguages());
                System.out.println("⬇️ Descargas: " + book.getDownloadCount());
                System.out.println("-------------------------------");
            }
        }
    }

    public void listAuthorsFromSearches() {
        if (searchedBooks.isEmpty()){
            System.out.println("⚠\uFE0F No hay libros registrados para mostrar autores.");
            return;
        }

        Set<String> authorsSet = new TreeSet<>(); //TreeSet para ordenar alfabeticamente y eliminar duplicados

        for (Book book : searchedBooks) {
            List<Author> authors = book.getAuthors();
            for (Author author : authors) {
                authorsSet.add(author.getName());
            }
        }

        if (authorsSet.isEmpty()) {
            System.out.println("⚠️ No se encontraron autores en los libros buscados.");
        } else {
            System.out.println("🖋️ Autores registrados en las búsquedas:");
            for (String author : authorsSet) {
                System.out.println("👤 " + author);
            }
        }
    }
}
