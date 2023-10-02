package com.example.kapitanov1;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GridView mGrid;
    private GridAdapter mAdapter;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private TextView timerTextView;
    private int seconds = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.field);
        timerTextView = findViewById(R.id.timerTextView);

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Увеличиваем счетчик секунд и обновляем текстовое поле
                seconds++;
                timerTextView.setText("Время: " + seconds + " секунд");

                // Постоянно запускаем этот Runnable каждую секунду
                timerHandler.postDelayed(this, 1000);
            }
        };

        // Получаем переданные значения размеров поля
        int rows = getIntent().getIntExtra("rows", 4);
        int columns = getIntent().getIntExtra("columns", 4);
        String collection = getIntent().getStringExtra("collection");

        mGrid.setNumColumns(columns); // Устанавливаем количество столбцов
        mGrid.setEnabled(true);

        mAdapter = new GridAdapter(this, rows, columns, collection); // Используем выбранные размеры, набор картинок
        mGrid.setAdapter(mAdapter);

        timerHandler.postDelayed(timerRunnable, 1000);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.handleCardClick(position))
                {
                    timerHandler.removeCallbacks(timerRunnable);

                    Intent intent = new Intent(MainActivity.this, EndGame.class);
                    startActivity(intent);
                }
            }
        });
    }
}
