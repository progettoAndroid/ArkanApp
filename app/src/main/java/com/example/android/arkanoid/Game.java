package com.example.android.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class Game extends View implements SensorEventListener, View.OnTouchListener {

    private static final String TAG = "db" ;
    private Bitmap wallpaper;
    private Bitmap redBall;
    private Bitmap screenorientation;
    private Bitmap paddle_p;

    private Display display;
    private Point size;
    private Paint paint;

    private Ball ball;
    private ArrayList<Brick> zoznam;
    private Paddle paddle;

    private RectF r;
    private SoundPlayer sound2;
    private SensorManager sManager = null;
    private Sensor accelerometer = null;

    private int lifes;
    private Integer score;
    private int level;
    private int controller;
    private boolean start;
    private boolean gameOver;
    private Context context;
    private WorkerThread controlsThread;

    SharedPreferences namePlayerPreferences;
    public static final String NICKNAME ="NamePlayerFile";
    private String nickname = "";
    private boolean isScoreMultipleOf400 = false;
    int randomBrick = -1;
    boolean isRandomTNT = false;
    int paddleTouchCounter = 0;
    MediaPlayer ringMiccia;
    private boolean isMultiplayer;

    public Game(Context context, int lifes, int score, int controller) {
        super(context);
        paint = new Paint();

        // impostare contesto, vite, punteggi e livelli
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.controller = controller;
        level = 0;
        sound2 = new SoundPlayer(this.context);

        // start a gameOver per scoprire se il gioco è finito e se il giocatore non l'ha perso
        start = false;
        gameOver = false;

        //Il giocatore sceglie quale controller utilizzare
        if (controller == 1) {
            sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        nacitajPozadie(context);

        //crea una bitmap per la palla e la pagaia
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //
        //crea una nuova palla, una nuova base e un elenco di mattoni
        ball = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2 - 100, size.y - 400);
        zoznam = new ArrayList<Brick>();
        Sottofondo();

        vygenerujBricks(context);
        this.setOnTouchListener(this);

    }
    public Game(Context context, int lifes, int score, int controller, boolean isMultiplayer) {
        super(context);
        paint = new Paint();
        this.isMultiplayer = isMultiplayer;
        // impostare contesto, vite, punteggi e livelli
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.controller = controller;
        level = 0;
        sound2 = new SoundPlayer(this.context);

        // start a gameOver per scoprire se il gioco è finito e se il giocatore non l'ha perso
        start = false;
        gameOver = false;

        //Il giocatore sceglie quale controller utilizzare
        if (controller == 1) {
            sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        nacitajPozadie(context);

        //crea una bitmap per la palla e la pagaia
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //
        //crea una nuova palla, una nuova base e un elenco di mattoni
        ball = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2 - 100, size.y - 400);
        zoznam = new ArrayList<Brick>();
        Sottofondo();

        vygenerujBricks(context);
        this.setOnTouchListener(this);

    }

    /**
     * riempire l'elenco con i mattoni
     *
     * @param context
     */
    private void vygenerujBricks(Context context) {
        Brick b = null;
        if(this.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            for (int i = 3; i < 7; i++) {
                for (int j = 1; j < 6; j++) {
                    if (level == 0 && i == 6 && j == 5) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else if (level != 0 && level <= 10 && ((i == 3 && j == 5) || (i == 6 && j == 1))) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else if (level > 10 && ((i == 3 && j == 5) || (i == 6 && j == 1) || (i == 4 && j == 3))) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else {
                        b = new Brick(context, j * 150, i * 100, Brick.Level.ONE);
                    }
                    zoznam.add(b);
                }
            }
        }else if ( this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            for (int i = 1; i < 3; i++) {
                for (int j = 2; j < 12; j++) {
                    if (level == 0 && i == 2 && j == 5) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else if (level != 0 && level <= 10 && ((i == 2 && j == 5) || (i == 6 && j == 1))) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else if (level > 10 && ((i == 2 && j == 5) || (i == 2 && j == 2) || (i == 4 && j == 3))) {
                        b = new Brick(context, j * 150, i * 100, true, level);
                    } else {
                        b = new Brick(context, j * 150, i * 100, Brick.Level.ONE);
                    }
                    zoznam.add(b);
                }
            }
        }
        }

    private void Sottofondo(){
        MediaPlayer mediaPlayer = MusicCache.getInstance().getMp();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    /**
     * impostare lo sfondo
     * @param context
     */
    private void nacitajPozadie(Context context) {
        // in base all'orientamento creo lo sfondo
        wallpaper = Bitmap.createBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.pozadie_score));
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = wm.getDefaultDisplay();
        size = new Point();
        display.getSize(size);
    }

    protected void onDraw(Canvas canvas) {
        // crea lo sfondo solo una volta
        if (screenorientation == null) {
            screenorientation = Bitmap.createScaledBitmap(wallpaper, size.x, size.y, false);
        }
        canvas.drawBitmap(screenorientation, 0, 0, paint);

        // disegnare la palla
        paint.setColor(Color.RED);
        canvas.drawBitmap(redBall, ball.getX(), ball.getY(), paint);

        // disegnato paddle
        paint.setColor(Color.WHITE);
        r = new RectF(paddle.getX(), paddle.getY(), paddle.getX() + 200, paddle.getY() + 40);
        canvas.drawBitmap(paddle_p, null, r, paint);

        // disegnare mattoni
        paint.setColor(Color.GREEN);
        for (int i = 0; i < zoznam.size(); i++) {
            Brick b = zoznam.get(i);
            r = new RectF(b.getX(), b.getY(), b.getX() + 100, b.getY() + 80);
            canvas.drawBitmap(b.getBrick(), null, r, paint);
        }

        // disegnare il testo
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        if(this.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT) {
            canvas.drawText("" + lifes, 400, 100, paint);
            canvas.drawText("" + score, 700, 100, paint);
        } else if ( this.getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE){
            canvas.drawText("" + lifes, 930, 68, paint);
            canvas.drawText("" + score, 1290, 68, paint);
        }


        // in caso di perdita "Game over!"
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", size.x / 4, size.y / 2, paint);
        }
    }


    /**
     * controllare che la palla non abbia toccato il bordo
     */
    private void skontrolujOkraje() {
        if (ball.getX() + ball.getxRychlost() >= size.x - 60) {
            ball.zmenSmer("prava");
        } else if (ball.getX() + ball.getxRychlost() <= 0) {
            ball.zmenSmer("lava");
        } else if (ball.getY() + ball.getyRychlost() <= 150) {
            ball.zmenSmer("hore");
        } else if (ball.getY() + ball.getyRychlost() >= size.y - 200) {
            skontrolujZivoty();
        }
    }

    /**
     * controlla lo stato del gioco. se le mie vite o se il gioco è finito
     */
    private void skontrolujZivoty() {
        if (lifes == 1) {
            gameOver = true;
            start = false;
            MediaPlayer mediaPlayer = MusicCache.getInstance().getMp();
            mediaPlayer.setLooping(true);
            mediaPlayer.pause();
            if(ringMiccia!= null) {
                ringMiccia.pause();
            }
            sound2.playStarting();
            invalidate();

            namePlayerPreferences = context.getSharedPreferences(NICKNAME, context.MODE_PRIVATE);
            nickname = namePlayerPreferences.getString ("nickname","");
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference userNameRef = rootRef.child("Users");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(nickname).exists()) {
                        try {
                            Integer value = Integer.parseInt(dataSnapshot.child(nickname).child("points").getValue(String.class));
                            if (value < score )
                                userNameRef.child(nickname).child( "points").setValue(score.toString());
                        }catch (Exception e){
                            Log.d(TAG, e.getMessage()); //Don't ignore errors!
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
                }
            };
            userNameRef.addListenerForSingleValueEvent(eventListener);

        } else {
            lifes--;
            ball.setX(size.x / 2);
            ball.setY(size.y - 480);
            ball.vytvorRychlost();
            ball.zvysRychlost(level);
            start = false;
        }
    }
    private boolean randomTNT(){
        boolean returnValue = false;
        Random randomInt = new Random();
        do {
            randomBrick = randomInt.nextInt(zoznam.size());
        } while(zoznam.get(randomBrick).getisXBrick());
        if (isScoreMultipleOf400 &&score!=0) {
            ringMiccia = MediaPlayer.create(context,R.raw.miccia);
            ringMiccia.setLooping(true);
            ringMiccia.start();
            zoznam.get(randomBrick).setBrick(BitmapFactory.decodeResource(getResources(), R.drawable.brick_green));
            isScoreMultipleOf400 = false;
            returnValue=true;
        }
        return returnValue;
    }

    // ogni passaggio controlla se c'è una collisione, una sconfitta o una vittoria, ecc.
    public void update() {
        if (start) {
            vyhra();
            skontrolujOkraje();
            if(ball.NarazPaddle(paddle.getX(), paddle.getY()) == 1){
                sound2.playHitSound();
            }
            if(ball.NarazPaddle(paddle.getX(), paddle.getY()) == 1){
                if(paddleTouchCounter==0) {
                    if(zoznam.size()!=1) {
                        isRandomTNT = randomTNT();
                        if (isRandomTNT) {
                            paddleTouchCounter++;
                        }
                    }
                }
            }
            for (int i = 0; i < zoznam.size(); i++) {
                Brick b = zoznam.get(i);
                if (ball.NarazBrick(b.getX(), b.getY())) {
                    if(zoznam.get(i).getisXBrick()){
                        if(zoznam.get(i).getxBrickBreakCounter()!=0){
                            zoznam.get(i).setxBrickBreakCounter(zoznam.get(i).getxBrickBreakCounter()-1);
                        } else {
                            zoznam.remove(i);
                        }
                    } else if (randomBrick != -1  && isRandomTNT && zoznam.size()!=1) {
                        MediaPlayer ring = MediaPlayer.create(context,R.raw.explosion);
                        ringMiccia.stop();
                        ring.start();
                        zoznam.remove(randomBrick);
                        isRandomTNT = false;
                        paddleTouchCounter=0;
                        randomBrick = -1;
                    } else {
                        if (zoznam.get(i).getLevel() == Brick.Level.ONE && !zoznam.get(i).getBrick().equals(R.drawable.brick_green)) {
                            zoznam.remove(i);
                            sound2.playHitSound();
                            zoznam.add(i, new Brick(context, b.getX(), b.getY(), Brick.Level.TWO));
                        } else if (zoznam.get(i).getLevel() == Brick.Level.TWO) {
                            zoznam.remove(i);
                            sound2.playHitSound();
                        }
                        score = score + 80;
                        if (score % 400 == 0) {
                            isScoreMultipleOf400 = true;
                        }
                    }
                }
            }
            ball.pohni();
        }
    }

    /**
     * stopSensing
     */
    public void zastavSnimanie() {
        if (controller == 1) {
            sManager.unregisterListener(this);
        }
    }

    /**
     * runScanning
     */
    public void spustiSnimanie() {
        if (controller == 1) {
            sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    //cambiare accelerometro
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            paddle.setX(paddle.getX() - event.values[0] - event.values[0]);

            if (paddle.getX() + event.values[0] > size.x - 240) {
                paddle.setX(size.x - 240);
            } else if (paddle.getX() - event.values[0] <= 20) {
                paddle.setX(20);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * serve a sospendere il gioco in caso di un nuovo gioco
     **/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver == true && start == false) {
            score = 0;
            lifes = 3;
            if (isMultiplayer) {
                MainActivity.storeScoreMultiplayer(score);
            }
            resetLevel();
            gameOver = false;

        } else {
            start = true;
            if (controller == 0) {
                switch (event.getAction()) {
                    //se l'azione è di tipo down o move richiamo il thread
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        if (controlsThread == null) {
                            controlsThread = new WorkerThread(context, event, paddle, size);
                            controlsThread.start();
                        }
                        return true;
                    //se è di tipo up, stoppo il thread
                    case MotionEvent.ACTION_UP:
                        if (controlsThread != null) {
                            controlsThread.terminate();
                            controlsThread = null;
                        }
                        return true;
                }
            }
        }
        return false;
    }


    /**
     * imposta il gioco per iniziare
     */
    private void resetLevel() {
        ball.setX(size.x / 2);
        ball.setY(size.y - 480);
        ball.vytvorRychlost();
        zoznam = new ArrayList<Brick>();
        vygenerujBricks(context);
        randomBrick = -1;
        isRandomTNT = false;
    }

    /**
     * scopri se il giocatore ha vinto o meno
     */
    private void vyhra() {
        if (zoznam.isEmpty()) {
            ++level;
            resetLevel();
            ball.zvysRychlost(level);
            start = false;
        }
    }

}
