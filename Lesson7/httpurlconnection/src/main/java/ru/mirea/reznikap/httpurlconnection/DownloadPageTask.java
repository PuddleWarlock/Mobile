package ru.mirea.reznikap.httpurlconnection;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.mirea.reznikap.httpurlconnection.databinding.ActivityMainBinding;

public class DownloadPageTask extends AsyncTask<String, Void, String[]> {

    private ActivityMainBinding binding;
    private String latitude, longitude;

    public DownloadPageTask(ActivityMainBinding binding) {
        this.binding = binding;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        binding.textView.setText("Загружаем...");
    }

    @Override
    protected String[] doInBackground(String... urls) {
        try {
            String ipData = downloadIpInfo(urls[0]);
            JSONObject ipJson = new JSONObject(ipData);
            latitude = ipJson.getString("loc").split(",")[0];
            longitude = ipJson.getString("loc").split(",")[1];

            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude + "&current_weather=true";
            String weatherData = downloadIpInfo(weatherUrl);

            return new String[]{ipData, weatherData};
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return new String[]{"error", "error"};
        }
    }

    @Override
    protected void onPostExecute(String[] results) {
        try {
            JSONObject ipJson = new JSONObject(results[0]);
            binding.ipTextView.setText("IP-адрес: " + ipJson.getString("ip"));
            binding.cityTextView.setText("Город: " + ipJson.getString("city"));
            binding.regionTextView.setText("Регион: " + ipJson.getString("region"));
            binding.countryTextView.setText("Страна: " + ipJson.getString("country"));
            binding.locTextView.setText("Координаты (широта,долгота): " + ipJson.getString("loc"));

            JSONObject weatherJson = new JSONObject(results[1]);
            JSONObject currentWeather = weatherJson.getJSONObject("current_weather");
            binding.temperatureTextView.setText("Температура: " + currentWeather.getString("temperature") + " °C");
            binding.windSpeedTextView.setText("Скорость ветра: " + currentWeather.getString("windspeed") + " км/ч");
            binding.weatherCodeTextView.setText("Код погоды (WMO): " + currentWeather.getString("weathercode"));

            binding.textView.setText("Загружено");
        } catch (JSONException e) {
            e.printStackTrace();
            binding.textView.setText("Ошибка обработки данных");
        }
        super.onPostExecute(results);
    }

    private String downloadIpInfo(String address) throws IOException {
        InputStream inputStream = null;
        String data = "";
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(100000);
            connection.setConnectTimeout(100000);
            connection.setRequestMethod("GET");
            connection.setInstanceFollowRedirects(true);
            connection.setUseCaches(false);
            connection.setDoInput(true);
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int read = 0;
                while ((read = inputStream.read()) != -1) {
                    bos.write(read);
                }
                bos.close();
                data = new String(bos.toByteArray(), "UTF-8");
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