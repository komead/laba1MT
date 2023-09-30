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
        Button btnApplySize = findViewById(R.id.btnApplySize);
        Spinner spinnerPictureCollection = findViewById(R.id.spinnerPictureCollection);

        // обработчик кликов на кнопку "Начать игру"
        Button btnStartGame = findViewById(R.id.btnStartGame);
        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // запускаем активность с игровым экраном
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Устанавливаем обработчик кликов на кнопку
        btnApplySize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем введенные пользователем значения количества строк и столбцов
                int rows = Integer.parseInt(etRows.getText().toString());
                int columns = Integer.parseInt(etColumns.getText().toString());

                // Передаем выбранные размеры поля в активность игры (MainActivity)
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                intent.putExtra("rows", rows);
                intent.putExtra("columns", columns);
                startActivity(intent);
            }
        });

        // Создаем адаптер для Spinner, используя массив ресурсов
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.picture_collections,
                android.R.layout.simple_spinner_item
        );

        // Устанавливаем стиль отображения выпадающего списка
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Устанавливаем адаптер для Spinner
        spinnerPictureCollection.setAdapter(adapter);
    }
}
