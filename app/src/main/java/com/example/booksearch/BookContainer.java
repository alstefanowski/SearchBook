package com.example.booksearch;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookContainer {
    @SerializedName("docs")
    private List<Book> bookList;

    public void setbookList(List<Book> bookList) {
        this.bookList = bookList;
    }
    public List<Book> getBookList() {
        return bookList;
    }

}
