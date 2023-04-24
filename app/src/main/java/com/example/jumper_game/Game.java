package com.example.jumper_game;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private StartThread thread;
    private Bitmap background;
    private MediaPlayer gameOver;
    private MediaPlayer gameMusic;
    private Player player;
    private Bricks bricks;
    private Brick brick;
    private Collisions collisions;
    private float x1,x2;
    int touch = 0;
    private Context mContext;


    public Game(Context context) {
        super(context);
        this.mContext = context;
        getHolder().addCallback(this);

        thread = new StartThread(getHolder(),this);
        gameOver = MediaPlayer.create(Variables.context, R.raw.game_over);
        gameMusic = MediaPlayer.create(Variables.context, R.raw.game_music);

        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        setFocusable(true);
        prepare();

    }

    private void prepare() {

        Variables.sound.stop();
        Variables.sound.release();
        Variables.sound = MediaPlayer.create(Variables.context, R.raw.jump);
        Point middle = new Point(Variables.width/2, Variables.height/2);
        this.player = new Player(middle);
        this.bricks = new Bricks();
        this.collisions = new Collisions();

        //Init platforms...
        int numberOfBricks = Variables.width / 160;
        int spaceLeft = Variables.width % 160;

        if(Variables.isSoundGame) {
            gameMusic.start();
            gameMusic.setLooping(true);
        }

        //Spawn bottom platforms
        for(int i = 0; i < numberOfBricks; i++) {
            this.bricks.addBrick(new Brick((new Point(i*160 + spaceLeft, Variables.height - 300))));
        }

        //Spawns the first instance of platforms
        this.bricks.addBrick( new Brick(new Point(middle.x - 400, Variables.height - 700)));
        this.bricks.addBrick( new Brick(new Point(middle.x, Variables.height - 1000)));
        this.bricks.addBrick( new Brick(new Point(middle.x - 400, Variables.height - 1300)));
        this.bricks.addBrick( new Brick(new Point(middle.x + 300, Variables.height - 1500)));
        this.bricks.addBrick( new Brick(new Point(middle.x + 300, Variables.height - 1900)));
        this.bricks.addBrick( new Brick(new Point(middle.x + 160, Variables.height - 2300)));


    }


    public void update() {
        //Progressively getting harder
        if(Variables.score > 40000) {
            this.bricks.setNumber(2);
            this.bricks.setSpacing(350);
            Variables.brickWidth = Variables.width/12;
        }
        else if (Variables.score > 15000) {
            this.bricks.setNumber(2);
            this.bricks.setSpacing(220);
            Variables.brickWidth = Variables.width/10;
        }
        else if (Variables.score > 10000) {
            this.bricks.setNumber(3);
            Variables.brickWidth = Variables.width/8;
        }
        else {
            this.bricks.setNumber(3);
            this.bricks.setSpacing(180);
            Variables.brickWidth = Variables.width/7;
        }
        player.update();

        //Move platforms and increase score
        if(player.getY() < Variables.height/2){
            int diff =  Variables.height/2 - player.getY();
            bricks.updatePlatformsY(diff);
            player.minusY(diff);
            Variables.score += diff;
        }

        bricks.update();

        //CheckCollision
        if(!player.isPlayerJumping()){
            for(int i = 0;i < bricks.getSize();i++){
                if(collisions.isBrickCollidedPlayer(player,bricks.getBrick(i))){
                    player.setJumping(true);
                    if(Variables.isVFX) {
                        Variables.sound.start();
                    }
                }
            }
        }

        if (player.isDead()) {
            if(Variables.isSoundGame) {
                gameMusic.stop();
                gameMusic.release();
            }
            if(Variables.isVFX) {
                Variables.sound.release();
                gameOver.start();
            }
            gameOver();
        }



    }
    public void draw(Canvas canvas) {
            super.draw(canvas);
            canvas.drawBitmap(background, null,new Rect(0,0,Variables.width,Variables.height),null);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);

            player.draw(canvas);

            bricks.draw(canvas);

            paint.setTextSize(64);
            canvas.drawText("Score: " + Variables.score, 0, 60, paint);
        }
    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void resume () {
        thread.start();
    }

    public void pause () {
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //The movement system is pretty janky, It works fine as long as the thumb is on the screen
    //When the player stops touching the screen and touch again, the player teleports to the X coordiantes of the players thumb...
    //Could solve it but requires time.
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch = x;
                break;
            case MotionEvent.ACTION_MOVE:
                int hold = x;
               // if (touch < hold) {
               //     player.moveIncrease(hold-touch);

                //} else {
                 //   player.moveDecrease(touch-hold);
                //}
                player.movePlayer(hold);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new StartThread(getHolder(),this);
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //NOTHING
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while(retry ){
            try{
                thread.setRunning(false);
                thread.join();
            } catch (Exception e){
                e.printStackTrace();
            }
            retry = false;
        }
    }

    public void drawBackground(Canvas canvas) {
        canvas.drawBitmap(background, null,new Rect(0,0,Variables.width,Variables.height),null);
    }

    //Saves score in the SharedPreferences which is a low information storage that can store integers.
    //Better than implementing a complete database (SQLite)
    public void gameOver () {
        SharedPreferences prefs = Variables.context.getSharedPreferences("score", Context.MODE_PRIVATE);
        int score = prefs.getInt("scorekey", 0);
        if(Variables.score > score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("scorekey", Variables.score);
            editor.commit();
        }
        thread.setRunning(false);
        Intent intent = new Intent(mContext, GameOverActivity.class);
        mContext.startActivity(intent);
    }

}
