package com.example.jumper_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;


public class Player implements GameObject{
    boolean isMovingRight = false;
    boolean isMovingLeft = false;
    int x,y, width, height, state = 0;
    private final Bitmap texture1 = BitmapFactory.decodeResource(Variables.context.getResources(),R.drawable.player);
    private final Bitmap jumping = BitmapFactory.decodeResource(Variables.context.getResources(),R.drawable.player_jump);
    boolean jump = true;

    //LOGIC
    private int playerSpeed = Math.round(8 * Variables.density);
    private int jumpHeight = Variables.height/59*15;
    private int jumpProgress = 0;

    private Bitmap currentBitmap;
    //Colliders
    private Rect playerCollider;
    private Point playerPos;
    private int textureWidth;
    private int textureHeight;

    //States
    private boolean isJumping = false;

    Player (Point position) {

        this.currentBitmap = texture1;
        this.textureWidth = currentBitmap.getWidth();
        this.textureHeight = currentBitmap.getHeight();
        System.out.println("Player speed " + playerSpeed);

        //POS
        this.playerPos = position;
        this.setCollision(playerPos);
    }

    @Override
    public void draw(Canvas canvas) {

        System.out.println("Player speed " + playerSpeed);
        canvas.drawBitmap(currentBitmap, null, playerCollider, null);

    }
    @Override
    public void update () {
        System.out.println("Player speed " + playerSpeed);
        System.out.println("Width " + Variables.width);
        System.out.println("height " + Variables.height);
        if(jumpProgress < 100) {
            currentBitmap = jumping;
        }
        else {
            currentBitmap = texture1;
        }
        //Check if player not reach maxPointOfJump
        if (this.jumpProgress >= jumpHeight) {
            this.isJumping = false;
            this.jumpProgress = 0;
        }

        if (playerPos.x <= 0) {
            playerPos.x = Variables.width;
        } else if (playerPos.x > Variables.width) {
            playerPos.x = 0;
        }

        if (this.isJumping) {
            this.playerPos.y -= playerSpeed;
            jumpProgress += this.playerSpeed;
        } else {
            this.playerPos.y += playerSpeed * 1.1;
        }
        this.setCollision(playerPos);

    }

    private void setCollision(Point pos) {
        this.playerCollider = new Rect(
                pos.x - textureWidth / 2,
                pos.y - textureHeight / 2,
                pos.x + textureWidth / 2,
                pos.y + textureHeight / 2);
    }

    public boolean isDead() {
        if (this.playerPos.y > (Variables.height) + textureHeight) {
            System.out.println("dead");
            return true;
        }
        return false;
    }

    public Rect getPlayerCollision() { return this.playerCollider;}
    void moveLeft () {
        this.playerPos.x -= playerSpeed + 20;
    }
    void moveRight () {
        this.playerPos.x += playerSpeed + 20;
    }

    public boolean isPlayerJumping() {
        return this.isJumping;
    }
    public void setJumping(boolean jumping) {
        this.isJumping = jumping;
    }
    public int getY() {
        return this.playerPos.y;
    }
    public void minusY(int diff) {
        this.playerPos.y += diff;
    }

    public void movePlayer(int x) {
        playerPos.x = x;
    }

    public void moveIncrease(int i) {
        playerPos.x += i;
    }

    public void moveDecrease(int i) {
        playerPos.x -= i;
    }
}
