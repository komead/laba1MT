package com.example.kapitanov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EndGame extends AppCompatActivity {
    private TextView tvTime;
    //private TextView textView;
    private TextView tvTurns;
    private Button btnMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        tvTime = findViewById(R.id.gameTime);
        tvTurns = findViewById(R.id.turns);
        btnMainMenu = findViewById(R.id.toMainMenu);

        int gameTime = getIntent().getIntExtra("time", 0);
        int numberOfTurns = getIntent().getIntExtra("turns", 0);

        tvTime.setText(gameTime / 60 + " minutes " + gameTime % 60 + " seconds");
        tvTurns.setText(Integer.toString(numberOfTurns));

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndGame.this, MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
