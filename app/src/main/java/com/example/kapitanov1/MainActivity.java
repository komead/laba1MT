package com.example.kapitanov1;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private GridView mGrid;
    private GridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGrid = findViewById(R.id.field);

        // Получаем переданные значения размеров поля
        int rows = getIntent().getIntExtra("rows", 4);
        int columns = getIntent().getIntExtra("columns", 4);
        String collection = getIntent().getStringExtra("collection");

        mGrid.setNumColumns(columns); // Устанавливаем количество столбцов
        mGrid.setEnabled(true);

        mAdapter = new GridAdapter(this, rows, columns, collection); // Используем выбранные размеры, но без указания набора картинок
        mGrid.setAdapter(mAdapter);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.handleCardClick(position);
            }
        });
    }
}
