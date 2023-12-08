package com.example.booksearch;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {
    public static final String BOOK_TITLE = "com.example.BOOK_TITLE";
    public static final String BOOK_AUTHOR = "com.example.BOOK_AUTHOR";
    public static final String BOOK_COVER_ID = "com.example.BOOK_COVER_ID";

    private TextView bookAuthor, bookTitle;
    private ImageView coverBook;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details);

        bookAuthor = findViewById(R.id.book_author);
        bookTitle = findViewById(R.id.book_title);
        coverBook = findViewById(R.id.img_cover);

        Intent intent = getIntent();
        bookTitle.setText(intent.getStringExtra(BOOK_TITLE));
        bookAuthor.setText(intent.getStringExtra(BOOK_AUTHOR));
        String coverId = intent.getStringExtra(BOOK_COVER_ID);

        if (coverId != null) {
            Picasso.with(this)
                    .load(MainActivity.IMAGE_URL_BASE + coverId + "-L.jpg")
                    .placeholder(R.drawable.ic_baseline_book_24)
                    .into(coverBook);
        }
        else {
            coverBook.setImageResource(R.drawable.ic_baseline_book_24);
        }
    }
}
