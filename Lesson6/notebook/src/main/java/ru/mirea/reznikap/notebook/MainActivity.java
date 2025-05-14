package ru.mirea.reznikap.notebook;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView filePath;
    private TextView quote;

    private Button saveButton;
    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        filePath = findViewById(R.id.fileName);
        quote = findViewById(R.id.quote);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);

        saveButton.setOnClickListener(v->{
            if(isExternalStorageWritable()){
                writeFileToExternalStorage(filePath.getText().toString(),  quote.getText().toString());
            }
        });
        loadButton.setOnClickListener(v->{
            if(isExternalStorageReadable()){
                var data = readFileFromExternalStorage(filePath.getText().toString());
                quote.setText(data);
            }
        });
    }
    public String readFileFromExternalStorage(String fileName)	{
        File path	=	Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File	file	=	new	File(path,	fileName + ".txt");
        try	{
            FileInputStream fileInputStream	=	new	FileInputStream(file.getAbsoluteFile());
            InputStreamReader inputStreamReader	=	new	InputStreamReader(fileInputStream,	StandardCharsets.UTF_8);
            List<String> lines	=	new ArrayList<String>();
            BufferedReader reader	=	new	BufferedReader(inputStreamReader);
            String	line	=	reader.readLine();
            while	(line	!=	null)	{
                lines.add(line);
                line	=	reader.readLine();
            }
            Log.w("ExternalStorage",	String.format("Read	from	file	%s	successful",	lines.toString()));
            return String.join(", ", lines);
        }	catch	(Exception	e)	{
            Log.w("ExternalStorage",	String.format("Read	from	file	%s	failed",	e.getMessage()));
            return "";
        }
    }

    public	void	writeFileToExternalStorage(String fileName, String data)	{
        File	path	=	Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File	file	=	new	File(path,	fileName + ".txt");
        try	{
            FileOutputStream fileOutputStream	=	new	FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output	=	new	OutputStreamWriter(fileOutputStream);
            //	Запись строки в файл
            output.write(data);
            //	Закрытие потока записи
            output.close();

        }	catch	(IOException e)	{
            Log.w("ExternalStorage",	"Error	writing	"	+	file,	e);
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
    /* Проверяем внешнее хранилище на доступность чтения */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}