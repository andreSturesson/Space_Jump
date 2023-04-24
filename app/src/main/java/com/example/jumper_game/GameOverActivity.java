package com.example.jumper_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.gameover_activity);

        TextView gameOver = findViewById(R.id.gameover);
        gameOver.setText("Game Over");

        TextView score = findViewById(R.id.score);
        score.setText("Score: " + Variables.score);


    }

    public void returnMainMenu (View view) {
        startActivity(new Intent(Variables.context, MainActivity.class));

        }

}
