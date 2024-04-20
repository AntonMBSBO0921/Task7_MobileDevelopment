package ru.mirea.maiorovaa.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FileOperationsFragment extends Fragment {

    private TextView textViewFileContent;
    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_file_operations, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab_create_record);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateRecordDialog();
            }
        });

        textViewFileContent = root.findViewById(R.id.text_view_file_content);

        sharedPreferences = requireContext().getSharedPreferences("MyFiles", Context.MODE_PRIVATE);

        loadSavedContent();

        return root;
    }

    private void showCreateRecordDialog() {
        CreateRecordDialogFragment dialogFragment = new CreateRecordDialogFragment();
        dialogFragment.setOnFileSavedListener(new CreateRecordDialogFragment.OnFileSavedListener() {
            @Override
            public void onFileSaved(String fileName, String fileContent) {
                String currentContent = sharedPreferences.getString("fileContent", "");
                String newContent = currentContent + "\n\n" + "File Name: " + fileName + "\n" + "File Content: " + fileContent;

                sharedPreferences.edit().putString("fileContent", newContent).apply();

                textViewFileContent.setText(newContent);
            }
        });
        dialogFragment.show(getChildFragmentManager(), "CreateRecordDialogFragment");
    }

    private void loadSavedContent() {
        String savedContent = sharedPreferences.getString("fileContent", "");
        textViewFileContent.setText(savedContent);
    }
}
