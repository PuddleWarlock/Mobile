package ru.mirea.reznikap.mireaproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class NetworkFragment extends Fragment {

    private static final String TAG = "NetworkFragment";
    private ProgressBar progressBar;
    private TextView timeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        timeTextView = view.findViewById(R.id.timeTextView);

        new FetchTimeTask().execute();

        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class FetchTimeTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("https://api.api-ninjas.com/v1/worldtime?timezone=Europe/Moscow");
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Api-Key", "qHS2b2ThdWGNlxtZCTZt2g==jd4IcoZvl3w25SoI");
                connection.setConnectTimeout(10000); // Увеличен до 10 секунд
                connection.setReadTimeout(10000);    // Увеличен до 10 секунд

                int responseCode = connection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e(TAG, "Ошибка HTTP: " + responseCode);
                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                Log.d(TAG, jsonObject.getString("datetime"));
                return jsonObject.getString("datetime");
            } catch (Exception e) {
                Log.e(TAG, "Ошибка в doInBackground: " + e.getMessage(), e);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String datetime) {
            progressBar.setVisibility(View.GONE);
            timeTextView.setVisibility(View.VISIBLE);

            if (datetime != null) {
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = inputFormat.parse(datetime);

                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                    String formattedDate = outputFormat.format(date);

                    timeTextView.setText("Текущая дата и время: " + formattedDate);
                } catch (Exception e) {
                    Log.e(TAG, "Ошибка форматирования даты: " + e.getMessage(), e);
                    timeTextView.setText("Текущая дата и время: " + datetime);
                }
            } else {
                Toast.makeText(requireContext(), "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                timeTextView.setText("Не удалось загрузить дату и время");
            }
        }
    }
}