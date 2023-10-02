package com.example.kapitanov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Установка макета для этой активности
        setContentView(R.layout.activity_main_menu);

        // Находим элементы интерфейса по их ID
        EditText etRows = findViewById(R.id.etRows);
        EditText etColumns = findViewById(R.id.etColumns);
        Button btnStartGame = findViewById(R.id.btnApplySize);
        Spinner spinnerPictureCollection = findViewById(R.id.spinnerPictureCollection);

        // ставим значение по умолчанию
        etRows.setText("4");
        etColumns.setText("4");

        // Получение массива строк из ресурсов
        String[] dataArray = getResources().getStringArray(R.array.picture_collections);

        // Создание адаптера и заполнение Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dataArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPictureCollection.setAdapter(adapter);

        // Устанавливаем обработчик кликов на кнопку
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем введенные пользователем значения количества строк и столбцов
                int rows = Integer.parseInt(etRows.getText().toString());
                int columns = Integer.parseInt(etColumns.getText().toString());

                // получаем выбранное значение из списка
                String collection = spinnerPictureCollection.getSelectedItem().toString();

                // Передаем выбранные размеры поля в активность игры (MainActivity)
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                intent.putExtra("rows", rows);
                intent.putExtra("columns", columns);
                intent.putExtra("collection", collection);
                startActivity(intent);
            }
        });
    }
}
