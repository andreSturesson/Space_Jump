package com.example.jumper_game;

import android.graphics.Rect;

public class Collisions {

    public boolean isBrickCollidedPlayer(Player player, Brick brick) {
        Rect playerCollider = player.getPlayerCollision();
        Rect brickCollider = brick.getBrickCollider();
        if (playerCollider.bottom <= brickCollider.bottom && playerCollider.bottom >= brickCollider.top &&
                ((playerCollider.left <= brickCollider.right && playerCollider.left >= brickCollider.left) || (playerCollider.right <= brickCollider.right && playerCollider.right >= brickCollider.left))) {
            return true;
        }
        return false;
    }

    public boolean isBrickCollidedBrick(Brick jumper1, Brick jumper2) {
        Rect jumperBox1 = jumper1.getBrickCollider();
        Rect jumperBox2 = jumper2.getBrickCollider();
        if (
                (
                        jumperBox1.left >= jumperBox2.left &&
                                jumperBox1.left <= jumperBox2.right
                )
                        ||
                        (
                                jumperBox1.right >= jumperBox2.left &&
                                        jumperBox1.right <= jumperBox2.right
                        )
        ) {
            return true;
        }
        return false;
    }


}