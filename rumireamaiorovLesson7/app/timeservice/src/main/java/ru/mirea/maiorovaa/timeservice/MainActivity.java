package ru.mirea.maiorovaa.timeservice;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

import ru.mirea.maiorovaa.timeservice.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String host = "time.nist.gov"; // или time-a.nist.gov
    private final int port = 13;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetTimeTask timeTask = new GetTimeTask();
                timeTask.execute();
            }
        });
    }
    private class GetTimeTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String timeResult = "";
            try {
                Socket socket = new Socket(host, port);
                BufferedReader reader = SocketUtils.getReader(socket);
                reader.readLine(); // игнорируем первую строку
                timeResult = reader.readLine(); // считываем вторую строку
                Log.d(TAG,timeResult);
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return timeResult;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null && !result.isEmpty()) {
                String[] parts = result.split("\\s+");
                if (parts.length >= 4) {
                    String dateString = parts[1];
                    String[] dateParts = dateString.split("-");
                    String year = dateParts[0];
                    String month = dateParts[1];
                    String day = dateParts[2];
                    String date = day + '.' + month + '.' + year + " г.";
                    String dateTime = date + "\nвремя:" + parts[2];
                    binding.textView.setText(dateTime);
                } else {
                    binding.textView.setText("Ошибка разбора времени");
                }
            } else {
                binding.textView.setText("Строка времени пуста");
            }
        }
    }
}