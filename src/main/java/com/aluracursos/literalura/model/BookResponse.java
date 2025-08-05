package com.aluracursos.literalura.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookResponse {
    private List<Book> results;

    public List<Book> getResults(){
        return results;
    }

    public void setResults(List<Book> results){
        this.results = results;
    }

    @Override
    public String toString(){
        return "BookResponse {" +
                "results = " + results +
                "}";
    }
}
