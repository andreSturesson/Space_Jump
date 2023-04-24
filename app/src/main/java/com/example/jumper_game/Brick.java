package com.example.jumper_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

public class Brick implements GameObject {
    private final Bitmap brick = Bitmap.createBitmap(BitmapFactory.decodeResource(Variables.context.getResources(), R.drawable.brick),0,0,Variables.brickWidth,Variables.height/48,null,true);

    private Rect brickCollider;
    private Point brickPos;
    private int brickWidth;
    private int brickHeight;

    public Brick(Point position) {
        this.brickWidth = brick.getWidth();
        this.brickHeight = brick.getHeight();

        this.brickPos = position;
        this.setBrickCollider(brickPos);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(brick,null,brickCollider,null);

    }
    @Override
    public void update() {
        this.setBrickCollider(brickPos);
    }

    private void setBrickCollider(Point jumperPosition) {
        this.brickCollider = new Rect(
                jumperPosition.x - brickWidth/2,
                jumperPosition.y - brickHeight/2,
                jumperPosition.x + brickWidth/2,
                jumperPosition.y + brickHeight/2

        );
    }

    public Rect getBrickCollider(){
        return this.brickCollider;
    }

    public void minusY(int diff) {
        this.brickPos.y += diff;
    }

    public Point getBrickPos(){
        return this.brickPos;
    }
}
