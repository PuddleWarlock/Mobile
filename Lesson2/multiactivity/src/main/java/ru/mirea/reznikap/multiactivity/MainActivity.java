package ru.mirea.reznikap.multiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        View button = findViewById(R.id.button);
        TextView sendText = findViewById(R.id.sendText);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("key", sendText.getText().toString());
            startActivity(intent);
        });
        Log.d(TAG, "onCreate:");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart:");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume:");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause:");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart:");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy:");
    }
}