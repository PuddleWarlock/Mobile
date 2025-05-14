package ru.mirea.reznikap.mireaproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkInfo;

public class BackgroundTaskFragment extends Fragment {

    private TextView statusTextView;
    private Button startButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background_task, container, false);

        statusTextView = view.findViewById(R.id.statusTextView);
        startButton = view.findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> startBackgroundTask());

        return view;
    }

    private void startBackgroundTask() {
        statusTextView.setText("Задача запущена...");
        startButton.setEnabled(false);

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(BackgroundWorker.class)
                .build();

        WorkManager.getInstance(requireContext())
                .enqueue(workRequest);

        WorkManager.getInstance(requireContext())
                .getWorkInfoByIdLiveData(workRequest.getId())
                .observe(getViewLifecycleOwner(), workInfo -> {
                    if (workInfo != null && workInfo.getState().isFinished()) {
                        startButton.setEnabled(true);
                        if (workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            Data outputData = workInfo.getOutputData();
                            String result = outputData.getString(BackgroundWorker.KEY_RESULT);
                            statusTextView.setText(result != null ? result : "Результат не получен");
                        } else {
                            statusTextView.setText("Ошибка при выполнении задачи");
                        }
                    }
                });
    }
}