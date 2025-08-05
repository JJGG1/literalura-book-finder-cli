package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String title;
    private Integer id;

    @JsonAlias("authors")
    private List<Author> authors;
    private List<String> languages;

    @JsonAlias("download_count")
    private int downloadCount;

    // Getters y Setters
    public Integer getId(){
        return id;
    }
    public String getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    @Override
    public String toString() {
        return "\n📘 Título: " + title +
                "\n👤 Autor(es): " + authors +
                "\n🌐 Idioma(s): " + languages +
                "\n⬇️ Descargas: " + downloadCount + "\n";
    }
}
