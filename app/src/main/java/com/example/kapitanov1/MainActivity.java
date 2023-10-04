package com.example.kapitanov1;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GridView mGrid;
    private GridAdapter mAdapter;
    private Button toMainMenu;

    private Handler timerHandler = new Handler();
    private Runnable timerRunnable;
    private TextView timerTextView;
    private int seconds = 0;
    private int numberOfTurns = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.field);
        timerTextView = findViewById(R.id.timerTextView);
        toMainMenu = findViewById(R.id.returnToMainMenu);

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Увеличиваем счетчик секунд и обновляем текстовое поле
                seconds++;
                timerTextView.setText("Время игры: " + seconds / 60 + " минут " + seconds % 60 + " секунд");

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

        // старт секундомера
        timerHandler.postDelayed(timerRunnable, 1000);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                numberOfTurns++;

                if (mAdapter.handleCardClick(position))
                {
                    timerHandler.removeCallbacks(timerRunnable);

                    Intent intent = new Intent(MainActivity.this, EndGame.class);
                    intent.putExtra("time", seconds);
                    intent.putExtra("turns", numberOfTurns);
                    startActivity(intent);
                    finish();
                }
            }
        });

        toMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
