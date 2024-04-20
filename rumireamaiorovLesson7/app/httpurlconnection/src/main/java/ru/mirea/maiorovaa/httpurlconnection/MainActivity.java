package ru.mirea.maiorovaa.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView cityTextView;
    private TextView regionTextView;
    private TextView weatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityTextView = findViewById(R.id.textViewCity);
        regionTextView = findViewById(R.id.textViewRegion);
        weatherTextView = findViewById(R.id.textViewWeather);
    }

    public void getLocationInfo(View view) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute("https://ipinfo.io/json"); // запуск нового потока
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject responseJson = new JSONObject(result);
                Log.d(MainActivity.class.getSimpleName(), "Response: " + responseJson);
                String city = responseJson.getString("city");
                String region = responseJson.getString("region");

                // Отображаем полученные данные о городе и регионе
                cityTextView.setText("Город: " + city);
                regionTextView.setText("Регион: " + region);

                // Получаем координаты
                String loc = responseJson.getString("loc");
                String[] coordinates = loc.split(",");
                String latitude = coordinates[0];
                String longitude = coordinates[1];

                // Создаем URL запроса к сервису погоды
                String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude + "&longitude=" + longitude + "&current_weather=true";
                new GetWeatherTask().execute(weatherUrl);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadWeatherInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Отображаем данные о погоде на экране
            weatherTextView.setText(result);
        }
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder stringBuilder = new StringBuilder();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    stringBuilder.append(new String(buffer, 0, bytesRead));
                }
                data = stringBuilder.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }

    private String downloadWeatherInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                inputStream = connection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder stringBuilder = new StringBuilder();
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    stringBuilder.append(new String(buffer, 0, bytesRead));
                }
                data = stringBuilder.toString();
            } else {
                data = connection.getResponseMessage() + ". Error Code: " + responseCode;
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return data;
    }
}

