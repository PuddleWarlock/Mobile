package ru.mirea.reznikap.mireaproject;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWorker extends Worker {
    public static final String TAG = "BackgroundWorker";
    public static final String KEY_RESULT = "result";

    public BackgroundWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Thread.sleep(5000);
            String result = "Фоновая задача успешно выполнена!";
            Log.d(TAG, result);

            androidx.work.Data outputData = new androidx.work.Data.Builder()
                    .putString(KEY_RESULT, result)
                    .build();
            return Result.success(outputData);
        } catch (InterruptedException e) {
            Log.e(TAG, "Ошибка в фоновой задаче: " + e.getMessage());
            return Result.failure();
        }
    }
}