package com.example.jumper_game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    boolean click = false;
    boolean musicclick = false;
    boolean menuSound = true;
    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //Display properties

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Variables.width = dm.widthPixels;
        Variables.height = dm.heightPixels;
        Variables.context = getApplicationContext();
        Variables.sound = new MediaPlayer();
        Variables.density = getResources().getDisplayMetrics().density;
        Variables.sound = MediaPlayer.create(this, R.raw.menu_music);
        Variables.brickWidth = 160;
        ImageView musicImage = findViewById(R.id.musicCtrl);
        ImageView soundImage = findViewById(R.id.volumeCtrl);
        SharedPreferences prefs1 = Variables.context.getSharedPreferences("VFX", Context.MODE_PRIVATE);
        int vfx = prefs1.getInt("VFXVAL", 0);

        if(vfx == 1) {
            Variables.isVFX = true;
            soundImage.setImageResource(R.drawable.ic_baseline_volume_up_24);
            click = true;
        } else if (vfx == 2) {
            Variables.isVFX = false;
            soundImage.setImageResource(R.drawable.ic_baseline_volume_off_24);
            click = false;
        }
        else {
            Variables.isVFX = true;
            soundImage.setImageResource(R.drawable.ic_baseline_volume_up_24);
        }
        SharedPreferences prefs2 = Variables.context.getSharedPreferences("music", Context.MODE_PRIVATE);
        int music = prefs2.getInt("musickey", 0);
        if(music == 1) {
            Variables.isSoundGame = true;
            menuSound = true;
            musicImage.setImageResource(R.drawable.ic_baseline_music_note_24);
            musicclick = true;
        } else if (music == 2) {
            Variables.isSoundGame = false;
            menuSound = false;
            musicImage.setImageResource(R.drawable.ic_baseline_music_off_24);
            musicclick = false;
        }
        else {
            Variables.isSoundGame = true;
            menuSound = true;
            musicImage.setImageResource(R.drawable.ic_baseline_music_note_24);
        }

        TextView highScore = findViewById(R.id.highScoreText);
        SharedPreferences prefs = Variables.context.getSharedPreferences("score", Context.MODE_PRIVATE);
        int score = prefs.getInt("scorekey", 0);
        highScore.setText("HighScore: " + score);

        if(menuSound) {
            Variables.sound.start();
            Variables.sound.setLooping(true);
            first = false;
        }


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Variables.context, GameActivity.class));
            }
        });
    }
    public void soundRegulator(View view) {
        if (click == false) {
            findViewById(R.id.volumeCtrl).setBackgroundResource(R.drawable.ic_baseline_volume_off_24);
            click = true;
            //Sound off
            changeVFX(2);
            Variables.isVFX = false;

        }
        else {
            findViewById(R.id.volumeCtrl).setBackgroundResource(R.drawable.ic_baseline_volume_up_24);
            click = false;
            changeVFX(1);
            Variables.isVFX = true;
        }
    }

    public void musicRegulator(View view) {
        if(!musicclick) {
            findViewById(R.id.musicCtrl).setBackgroundResource(R.drawable.ic_baseline_music_off_24);
            musicclick = true;
            menuSound = false;
            if(first) {
                Variables.sound.start();
                first = false;
            }
            changeMusic(2);
            Variables.isSoundGame = false;
            pause();
        } else {
            findViewById(R.id.musicCtrl).setBackgroundResource(R.drawable.ic_baseline_music_note_24);
            musicclick = false;
            menuSound = true;
            changeMusic(1);
            Variables.isSoundGame = true;
            resume();
        }
    }

    private void pause() {
        Variables.sound.pause();
    }

    private void resume() {
        Variables.sound.start();
    }

    public void changeVFX(int num) {
        SharedPreferences prefs = Variables.context.getSharedPreferences("VFX", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("VFXVAL", num);
        editor.commit();
    }
    public void changeMusic(int num) {
        SharedPreferences prefs = Variables.context.getSharedPreferences("music", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("musickey", num);
        editor.commit();
    }
}