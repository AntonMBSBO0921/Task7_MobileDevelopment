package ru.mirea.maiorovaa.mireaproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import java.io.FileOutputStream;
import java.io.IOException;

public class CreateRecordDialogFragment extends DialogFragment {

    private EditText editTextFileName;
    private EditText editTextFileContent;
    private OnFileSavedListener onFileSavedListener;

    public interface OnFileSavedListener {
        void onFileSaved(String fileName, String fileContent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_record, null);

        editTextFileName = view.findViewById(R.id.edit_text_file_name);
        editTextFileContent = view.findViewById(R.id.edit_text_file_content);

        builder.setView(view)
                .setTitle("Create Record")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fileName = editTextFileName.getText().toString();
                        String fileContent = editTextFileContent.getText().toString();

                        saveToFile(fileName, fileContent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void saveToFile(String fileName, String fileContent) {
        FileOutputStream fos = null;
        try {
            fos = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
            String formattedData = fileContent + ";" + System.getProperty("line.separator");
            fos.write(formattedData.getBytes());

            Toast.makeText(getActivity(), "Файл успешно сохранен", Toast.LENGTH_SHORT).show();
            if (onFileSavedListener != null) {
                onFileSavedListener.onFileSaved(fileName, fileContent);
            }
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getActivity(), "Ошибка при сохранении файла", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setOnFileSavedListener(OnFileSavedListener listener) {
        this.onFileSavedListener = listener;
    }
}
