package ru.mirea.reznikap.mireaproject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class FileWorkFragment extends Fragment {

    private TextView fileStatusText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_file_work, container, false);

        fileStatusText = view.findViewById(R.id.fileStatusText);

        NavController navController = NavHostFragment.findNavController(this);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.nav_file_work) {
                requireActivity().findViewById(R.id.fab).setOnClickListener(v -> showEncryptDialog());
            }
        });

        return view;
    }

    private void showEncryptDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_encrypt_text);

        EditText textToEncryptEditText = dialog.findViewById(R.id.textToEncryptEditText);
        Button encryptButton = dialog.findViewById(R.id.encryptButton);

        encryptButton.setOnClickListener(v -> {
            String text = textToEncryptEditText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(requireContext(), "Введите текст", Toast.LENGTH_SHORT).show();
                return;
            }

            String encryptedText = encryptText(text);
            saveToFile(encryptedText);
            dialog.dismiss();
        });

        dialog.show();
    }

    private String encryptText(String text) {
        int shift = 3;
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                encrypted.append((char) (base + (c - base + shift) % 26));
            } else {
                encrypted.append(c);
            }
        }
        return encrypted.toString();
    }

    private void saveToFile(String text) {
        try {
            File file = new File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "encrypted_text.txt");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(text.getBytes());
            fos.close();
            fileStatusText.setText("Статус: Файл сохранён в " + file.getAbsolutePath());
            Toast.makeText(requireContext(), "Файл сохранён", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            fileStatusText.setText("Статус: Ошибка сохранения файла");
            Toast.makeText(requireContext(), "Ошибка сохранения файла", Toast.LENGTH_SHORT).show();
        }
    }
}