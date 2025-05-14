package ru.mirea.reznikap.mireaproject;

import android.Manifest;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MicrophoneFragment extends Fragment {
    private MediaRecorder mediaRecorder;
    private String outputFile;
    private boolean isRecording = false;
    private Button startRecordingButton, stopRecordingButton;
    private ActivityResultLauncher<String> requestMicrophonePermissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_microphone, container, false);

        startRecordingButton = view.findViewById(R.id.startRecordingButton);
        stopRecordingButton = view.findViewById(R.id.stopRecordingButton);


        requestMicrophonePermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (!isGranted) {
                        Toast.makeText(requireContext(), "Разрешение на микрофон отклонено", Toast.LENGTH_SHORT).show();
                    }
                });

        startRecordingButton.setOnClickListener(v -> {
            requestMicrophonePermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
            if (!isRecording) {
                try {
                    outputFile = createOutputFile();
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(outputFile);
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                    isRecording = true;
                    startRecordingButton.setEnabled(false);
                    stopRecordingButton.setEnabled(true);
                    Toast.makeText(requireContext(), "Запись начата", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(requireContext(), "Ошибка записи", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopRecordingButton.setOnClickListener(v -> {
            if (isRecording) {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                startRecordingButton.setEnabled(true);
                stopRecordingButton.setEnabled(false);
                Toast.makeText(requireContext(), "Заметка сохранена", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private String createOutputFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        return requireActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/note_"+ timeStamp +".3gp";
    }
}