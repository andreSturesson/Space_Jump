package com.example.jumper_game;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bricks implements GameObject {

    private List<Brick> brickList;
    private Point lastCreatedBrick;
    private Random rand;
    private int spacing;
    private int number;
    private Collisions collisions;

    public Bricks() {
        this.brickList = new ArrayList<>();
        this.collisions = new Collisions();
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public Brick getBrick(int i) {
        return brickList.get(i);
    }
    public int getSize() {
        return this.brickList.size();
    }

    @Override
    public void draw(Canvas canvas) {
        for (int i = 0; i < brickList.size(); i++) {
            brickList.get(i).draw(canvas);
        }
    }

    @Override
    public void update() {
        this.genNewBricks();
        for (int i = 0; i < brickList.size(); i++) {
            brickList.get(i).update();
        }
    }

    public void addBrick(Brick brick) {
        this.brickList.add(brick);
        this.lastCreatedBrick = brick.getBrickPos();
    }

    public void updatePlatformsY(int diff) {
        for (int i = 0; i < brickList.size(); i++) {
            brickList.get(i).minusY(diff);
        }

    }

    //Movable platforms later on...
    public void updatePlatformsX(int diff) {
        for (int i = 0; i < brickList.size(); i++) {
            brickList.get(i).minusY(diff);
        }
    }

    public void genNewBricks () {
        if (this.lastCreatedBrick.y >= spacing) {
            int gen = genRandomInt(1,number);
            for (int i = 0; i < gen; i++) {
                boolean create = false;
                Brick nBrick = null;
                int x = generateX();

                create = true;
                nBrick = new Brick(new Point(x, -100));

                for (int j = 0; j <= i; j++) {
                    Brick brickComp = brickList.get(brickList.size()-1-j);
                    if (collisions.isBrickCollidedBrick(nBrick,brickComp)) {
                        create = false;
                        x +=100;
                        if(x > Variables.width - 80) {
                            x = 80;
                        }
                    }
                }
                if(create) {
                    this.addBrick(nBrick);
                }
            }
        }
    }
    private int generateX() {
        return genRandomInt(80, Variables.width - 80);
    }

    private int genRandomInt(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

}
