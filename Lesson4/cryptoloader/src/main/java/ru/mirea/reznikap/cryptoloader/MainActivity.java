package ru.mirea.reznikap.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public final String TAG = this.getClass().getSimpleName();
    private final int LoaderID = 1234;
    private EditText phraseEditText;

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
        phraseEditText = findViewById(R.id.phraseEditText);
    }

    public void onClickButton(View view) {
        String phrase = phraseEditText.getText().toString().trim();
        if (phrase.isEmpty()) {
            Toast.makeText(this, "Введите фразу", Toast.LENGTH_SHORT).show();
            return;
        }

        SecretKey key = generateKey();
        byte[] encryptedPhrase = encryptMsg(phrase, key);

        Bundle bundle = new Bundle();
        bundle.putByteArray(MyLoader.ARG_WORD, encryptedPhrase);
        bundle.putByteArray("key", key.getEncoded());
        LoaderManager.getInstance(this).restartLoader(LoaderID, bundle, this);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LoaderID) {
            Toast.makeText(this, "onCreateLoader: " + id, Toast.LENGTH_SHORT).show();
            return new MyLoader(this, args);
        }
        throw new InvalidParameterException("Invalid loader id");
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String result) {
        if (loader.getId() == LoaderID) {
            Log.d(TAG, "onLoadFinished: " + result);
            Toast.makeText(this, "Decrypted: " + result, Toast.LENGTH_LONG).show();
        }
    }

    public static SecretKey generateKey() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            return new SecretKeySpec(kg.generateKey().getEncoded(), "AES");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptMsg(String message, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return cipher.doFinal(message.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                 BadPaddingException | IllegalBlockSizeException e) {
            throw new RuntimeException(e);
        }
    }
}