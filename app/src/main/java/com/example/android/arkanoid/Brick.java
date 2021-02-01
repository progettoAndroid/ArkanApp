package com.example.android.arkanoid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.HashMap;


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
