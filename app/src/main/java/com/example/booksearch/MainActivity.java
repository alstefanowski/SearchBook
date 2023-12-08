package com.example.booksearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    private BookAdapter adapter;
    private List<Book> books;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private String prepareQuery(String query) {
        String[] queryParts = query.split("\\s+");
        return TextUtils.join("+", queryParts);
    }

    private void setupBookListView(List<Book> books) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final BookAdapter adapter = new BookAdapter();
        adapter.setBooks(books);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public boolean checkNullOrEmpty(String text) {
        return text != null && !TextUtils.isEmpty(text);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                fetchBooksData(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    private void fetchBooksData(String query) {
        String finalQuery = prepareQuery(query);
        BookService bookService = RetrofitInstance.getRetrofitInstance().create(BookService.class);

        Call<BookContainer> bookApiCall = bookService.findBooks(finalQuery);

        bookApiCall.enqueue(new Callback<BookContainer>() {
            @Override
            public void onResponse(@NonNull Call<BookContainer> call, @NonNull Response<BookContainer> response) {
                if (response.body() != null) {
                    setupBookListView(response.body().getBookList());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BookContainer> call, @NonNull Throwable t) {
                Snackbar.make(findViewById(R.id.main_view), "Błąd",
                        BaseTransientBottomBar.LENGTH_LONG).show();
            }
        });
    }

    private class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView bookCover;
        private TextView bookTitle, bookAuthor, numberOfPages;
        private Book book;

        public BookHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            super(layoutInflater.inflate(R.layout.book_list_item, viewGroup, false));
            itemView.setOnClickListener(this);

            bookCover = itemView.findViewById(R.id.img_cover);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            numberOfPages = itemView.findViewById(R.id.number_of_pages);
        }
        public void bind(Book book) {
            this.book = book;
            if (book != null && checkNullOrEmpty(book.getTitle()) && book.getAuthors() != null) {
                bookTitle.setText(book.getTitle());
                bookAuthor.setText(TextUtils.join(", ", book.getAuthors()));
                numberOfPages.setText(book.getNumberOfPages());

                if (book.getCover() != null) {
                    Picasso.with(itemView.getContext())
                            .load(IMAGE_URL_BASE + book.getCover() + "-S.jpg")
                            .placeholder(R.drawable.ic_baseline_book_24).into(bookCover);
                }
                else {
                    bookCover.setImageResource(R.drawable.ic_baseline_book_24);
                }
            }
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, BookDetails.class);
            intent.putExtra(BookDetails.BOOK_TITLE, bookTitle.getText());
            intent.putExtra(BookDetails.BOOK_AUTHOR, bookAuthor.getText());
            intent.putExtra(BookDetails.BOOK_COVER_ID, book.getCover());
            startActivity(intent);
        }
    }

    private class BookAdapter extends RecyclerView.Adapter<BookHolder> {

        private List<Book> books;

        @NonNull
        @Override
        public BookHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookHolder(getLayoutInflater(), parent);
        }

        @Override
        public int getItemCount() {
            if(books != null)
                return books.size();
            return 0;
        }

        @Override
        public void onBindViewHolder(@NonNull BookHolder holder, int position) {
            if (books != null) {
                Book book = books.get(position);
                holder.bind(book);
            }
            else {
                Log.d("MainActivity", "No books");
            }
        }
        void setBooks(List<Book> books) {
            this.books = books;
            notifyDataSetChanged();
        }

    }
}