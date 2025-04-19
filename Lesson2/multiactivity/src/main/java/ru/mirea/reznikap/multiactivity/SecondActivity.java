package ru.mirea.reznikap.multiactivity;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.mirea.reznikap.multiactivity.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {

    private String TAG = SecondActivity.class.getSimpleName();
    private AppBarConfiguration appBarConfiguration;
    private ActivitySecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String text = (String) getIntent().getSerializableExtra("key");
        var textView = (TextView)findViewById(R.id.textView2);
        textView.setText(text);
        Log.d(TAG, "onCreate:");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_second);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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