package ru.mirea.reznikap.favoritebook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> activityResultLauncher;
    static final String BOOK_NAME_KEY = "book_name";
    static final String QUOTES_KEY = "quotes_name";
    static final String USER_MESSAGE="MESSAGE";
    private TextView textViewUserBook;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewUserBook = findViewById(R.id.textViewBook);
        ActivityResultCallback<ActivityResult> callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String userBook = data.getStringExtra(BOOK_NAME_KEY);
                    String userQuote = data.getStringExtra(QUOTES_KEY);
                    textViewUserBook.setText(String.format("Моя любимая книга: %s и цитата %s",
                            userBook, userQuote));

                }
            }
        };
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                callback);

/*        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String book_name = extras.getString(MainActivity.BOOK_NAME_KEY);
            String quotes_name = extras.getString(MainActivity.QUOTES_KEY);
            textViewUserBook.setText(String.format("Моя любимая книга: %s и цитата %s",
                    book_name, quotes_name));
        }*/
    }
    public void getInfoAboutBook(View view) {
        Intent intent = new Intent(this, ShareActivity.class);
        /*intent.putExtra(BOOK_NAME_KEY, "Маленький принц");
        intent.putExtra(QUOTES_KEY, "Мы в ответе за тех кого приручили");*/
        activityResultLauncher.launch(intent);
    }
}