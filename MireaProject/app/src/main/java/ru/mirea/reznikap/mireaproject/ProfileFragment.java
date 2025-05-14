package ru.mirea.reznikap.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText nameEditText, ageEditText, hobbyEditText;
    private TextView profileInfoText;
    private SharedPreferences preferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        nameEditText = view.findViewById(R.id.nameEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        hobbyEditText = view.findViewById(R.id.hobbyEditText);
        profileInfoText = view.findViewById(R.id.profileInfoText);
        Button saveButton = view.findViewById(R.id.saveButton);

        preferences = requireActivity().getSharedPreferences("mirea_settings", Context.MODE_PRIVATE);

        loadProfileData();

        saveButton.setOnClickListener(v -> saveProfileData());

        return view;
    }

    private void saveProfileData() {
        String name = nameEditText.getText().toString().trim();
        String ageStr = ageEditText.getText().toString().trim();
        String hobby = hobbyEditText.getText().toString().trim();

        if (name.isEmpty() || ageStr.isEmpty() || hobby.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Возраст должен быть числом", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("PROFILE_NAME", name);
        editor.putInt("PROFILE_AGE", age);
        editor.putString("PROFILE_HOBBY", hobby);
        editor.apply();

        Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show();
        loadProfileData();
    }

    private void loadProfileData() {
        String name = preferences.getString("PROFILE_NAME", "Не указано");
        int age = preferences.getInt("PROFILE_AGE", 0);
        String hobby = preferences.getString("PROFILE_HOBBY", "Не указано");

        String profileInfo = String.format("Имя: %s\nВозраст: %d\nХобби: %s", name, age, hobby);
        profileInfoText.setText(profileInfo);
    }
}