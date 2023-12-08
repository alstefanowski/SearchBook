package com.example.booksearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Book {
    @SerializedName("title")
    private String title;
    @SerializedName("author_name")
    private List<String> authors;
    @SerializedName("cover_i")
    private String cover;
    @SerializedName("numbers_of_pages_median")
    private String numberOfPages;

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }
    public List<String> getAuthors() {
        return authors;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getCover() {
        return cover;
    }
    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
    public String getNumberOfPages() {
        return numberOfPages;
    }
}
