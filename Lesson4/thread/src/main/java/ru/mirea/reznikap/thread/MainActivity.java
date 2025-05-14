package ru.mirea.reznikap.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Locale;

import ru.mirea.reznikap.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private	int	counter	=	0;


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

        TextView infoTextView = findViewById(R.id.textView);
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());
        // Меняем имя и выводим в текстовом поле
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: БСБО-09-22, НОМЕР ПО СПИСКУ: 23, МОЙ ЛЮБИМЫЙ ФИЛЬМ: Трасса 60");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(),"Stack: "+ Arrays.toString(mainThread.getStackTrace()));



        binding.button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)	{
                new	Thread(new Runnable() {
                    public void run() {
                        int days = Integer.parseInt(binding.days.getText().toString());
                        int pairs = Integer.parseInt(binding.pairs.getText().toString());
                        float pairsPerDay = (float) pairs / days;
                        runOnUiThread(() -> binding.textView.setText(String.format(Locale.ENGLISH,"Среднее кол-во пар в день = %.2f", pairsPerDay)));
                    }
                }).start();
            }
        });
    }
}