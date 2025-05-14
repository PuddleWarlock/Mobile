package ru.mirea.reznikap.lesson4;

import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import ru.mirea.reznikap.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.trackTitle.setText("Sample Track");
        binding.artistName.setText("Sample Artist");

        binding.playPauseButton.setOnClickListener(v -> {
            isPlaying = !isPlaying;
            binding.playPauseButton.setText(isPlaying ? "Pause" : "Play");
            Log.d(MainActivity.class.getSimpleName(), isPlaying ? "Play clicked" : "Pause clicked");
        });

        binding.previousButton.setOnClickListener(v -> {
            binding.trackTitle.setText("Previous Track");
            Log.d(MainActivity.class.getSimpleName(), "Previous clicked");
        });

        binding.nextButton.setOnClickListener(v -> {
            binding.trackTitle.setText("Next Track");
            Log.d(MainActivity.class.getSimpleName(), "Next clicked");
        });
    }
}