package ru.mirea.reznikap.lesson6;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ru.mirea.reznikap.lesson6.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        var group = binding.group;
        var number = binding.number;
        var film = binding.film;

        SharedPreferences preferences	=
                getSharedPreferences("mirea_settings",	Context.MODE_PRIVATE);



        group.setText(preferences.getString("GROUP", "unknown"));
        number.setText(String.valueOf(preferences.getInt("NUMBER", 0)));
        film.setText(preferences.getString("FILM", "unknown"));


        binding.button.setOnClickListener(v->{
            SharedPreferences.Editor	editor	=	preferences.edit();

            editor.putString("GROUP", group.getText().toString());
            editor.putInt("NUMBER", Integer.parseInt(number.getText().toString()));
            editor.putString("FILM", film.getText().toString());
            editor.apply();
        });
    }
}