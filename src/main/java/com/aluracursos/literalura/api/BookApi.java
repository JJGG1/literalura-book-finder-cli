package com.aluracursos.literalura.api;

import com.aluracursos.literalura.model.Book;
import com.aluracursos.literalura.model.BookResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Component
public class BookApi {
    private static final String API_URL = "https://gutendex.com/books/";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Book> searchBooks(String query) {
        String url = API_URL + "?search=" + URLEncoder.encode(query, StandardCharsets.UTF_8);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());
            //return response.body(); // JSON crudo
            if (response.statusCode() == 200) {
                BookResponse bookResponse = mapper.readValue(response.body(), BookResponse.class);
                return bookResponse.getResults();
            } else {
                System.out.println("⚠️ Error al conectar con la API. Código: " + response.statusCode());
            }

        } catch (IOException | InterruptedException e) {
            System.out.println("❌ Excepción al realizar la solicitud: " + e.getMessage());
        }
        return Collections.emptyList();
    }
}
