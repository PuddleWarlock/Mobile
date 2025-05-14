package ru.mirea.reznikap.employeedb;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

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

        AppDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();
        Employee employee = new Employee();
        employee.id = 1;
        employee.name = "Super Man";
        employee.superpower = "Speed";
        // запись сотрудников в базу
        employeeDao.insert(employee);
        // Загрузка всех работников
        List<Employee> employees = employeeDao.getAll();
        // Получение определенного работника с id = 1
        employee = employeeDao.getById(1);
        // Обновление полей объекта
        employee.superpower = "Laser sight";
        employeeDao.update(employee);
        Log.d(TAG, employee.name + " " + employee.superpower);
    }
}

