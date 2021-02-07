package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Random;


public class Brick extends View {


    public enum Level {
        ONE,
        TWO
    };

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    private Level level;
    private Bitmap brick;
    private float x;
    private float y;
    private boolean isXBrick;
    private int xBrickBreakCounter;

    public int getxBrickBreakCounter() {
        return xBrickBreakCounter;
    }

    public void setxBrickBreakCounter(int xBrickBreakCounter) {
        this.xBrickBreakCounter = xBrickBreakCounter;
    }

    public boolean getisXBrick() {
        return isXBrick;
    }

    public void setXBrick(boolean XBrick) {
        isXBrick = XBrick;
    }

    public Brick(Context context, float x, float y, Level level) {
        super(context);
            this.x = x;
            this.y = y;
        this.setLayoutParams(new ViewGroup.LayoutParams(64,32));
        System.out.println("getHeight: "+this.getHeight()+" getWidth "+this.getWidth());
            if (level == Level.ONE) {
                this.level = level;
                skin();
            } else if (level == Level.TWO) {
                this.level = level;
                skin_level2();
            }
        }
    //costruttore xBrickl
    public Brick(Context context, float x, float y, boolean isXBrick, int gameLevel) {
        super(context);
        this.x = x;
        this.y = y;
        this.isXBrick=isXBrick;
        this.setLayoutParams(new ViewGroup.LayoutParams(64,32));
        System.out.println("getHeight: "+this.getHeight()+" getWidth "+this.getWidth());
        this.level = level;
        skinXBrick(gameLevel);

    }
    // assegna un'immagine casuale al mattone
    private void skinXBrick(int gameLevel) {
        Random randomInt = new Random();
        if (gameLevel < 1) {
            xBrickBreakCounter = randomInt.nextInt(3);
        } else if(gameLevel <= 10 && gameLevel != 0){
            xBrickBreakCounter = randomInt.nextInt(5);
        } else if(gameLevel > 10){
            xBrickBreakCounter = randomInt.nextInt(10);
        }
        brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_x);

    }

    // assegna un'immagine casuale al mattone
    private void skin() {
        int a = (int) (Math.random() * 8);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                 break;
            case 2:
                 brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow);
                break;
        }
    }

    // assegna un'immagine casuale al mattone
    private void skin_level2( ) {
        int a = (int) (Math.random() * 8);
        switch (a) {
            case 0:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_aqua_screpolato);
                break;
            case 1:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue_screpolato);
                break;
            case 2:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_blue_screpolato);
                break;
            case 3:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_orange_screpolato);
                break;
            case 4:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_pink_screpolato);
                break;
            case 5:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_purple_screpolato);
                break;
            case 6:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_red_screpolato);
                break;
            case 7:
                brick = BitmapFactory.decodeResource(getResources(), R.drawable.brick_yellow_screpolato);
                break;
        }
    }


    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    public Bitmap getBrick() {
        return brick;
    }
    public void setBrick(Bitmap brick) {
        this.brick=brick;
    }
}
