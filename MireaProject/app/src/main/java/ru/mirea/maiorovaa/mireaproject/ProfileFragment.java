package ru.mirea.maiorovaa.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText editTextName;
    private EditText editTextAge;
    private Button btnSave;
    private ProfileManager profileManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        editTextName = root.findViewById(R.id.editTextName);
        editTextAge = root.findViewById(R.id.editTextAge);
        btnSave = root.findViewById(R.id.btnSave);

        profileManager = new ProfileManager(requireContext());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileData();
            }
        });

        loadProfileData();

        return root;
    }

    private void loadProfileData() {
        String name = profileManager.getName();
        int age = profileManager.getAge();

        editTextName.setText(name);
        editTextAge.setText(String.valueOf(age));
    }

    private void saveProfileData() {
        String name = editTextName.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());

        profileManager.saveProfile(name, age);
    }
}
