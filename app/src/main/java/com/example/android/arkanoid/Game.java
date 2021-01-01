package com.example.android.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class Game extends View implements SensorEventListener, View.OnTouchListener {

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

    private SensorManager sManager = null;
    private Sensor accelerometer = null;

    private int lifes;
    private int score;
    private int level;
    private int controller;
    private boolean start;
    private boolean gameOver;
    private Context context;
    private WorkerThread controlsThread;


    public Game(Context context, int lifes, int score, int controller) {
        super(context);
        paint = new Paint();

        // impostare contesto, vite, punteggi e livelli
        this.context = context;
        this.lifes = lifes;
        this.score = score;
        this.controller = controller;
        level = 0;

        // start a gameOver per scoprire se il gioco è finito e se il giocatore non l'ha perso
        start = false;
        gameOver = false;

        //Il giocatore sceglie quale controller utilizzare

            sManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            accelerometer = sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        nacitajPozadie(context);

        //crea una bitmap per la palla e la pagaia
        redBall = BitmapFactory.decodeResource(getResources(), R.drawable.redball);
        paddle_p = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);

        //
        //crea una nuova palla, una nuova base e un elenco di mattoni
        ball = new Ball(size.x / 2, size.y - 480);
        paddle = new Paddle(size.x / 2 - 100, size.y - 400);
        zoznam = new ArrayList<Brick>();

        vygenerujBricks(context);
        this.setOnTouchListener(this);

    }

    /**
     * riempire l'elenco con i mattoni
     * @param context
     */
    private void vygenerujBricks(Context context) {
        for (int i = 3; i < 7; i++) {
            for (int j = 1; j < 6; j++) {
                zoznam.add(new Brick(context, j * 150, i * 100, Brick.Level.ONE));
            }
        }
    }

    /**
     * impostare lo sfondo
     * @param context
     */
    private void nacitajPozadie(Context context) {
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
        canvas.drawText("" + lifes, 400, 100, paint);
        canvas.drawText("" + score, 700, 100, paint);

        // in caso di perdita "Game over!"
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(100);
            canvas.drawText("Game over!", size.x / 4, size.y / 2, paint);
        }
    }


    /**controllare che la palla non abbia toccato il bordo
     *
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

    /**controlla lo stato del gioco. se le mie vite o se il gioco è finito
     *
     */
    private void skontrolujZivoty() {
        if (lifes == 1) {
            gameOver = true;
            start = false;
            invalidate();
        } else {
            lifes--;
            ball.setX(size.x / 2);
            ball.setY(size.y - 480);
            ball.vytvorRychlost();
            ball.zvysRychlost(level);
            start = false;
        }
    }

    // ogni passaggio controlla se c'è una collisione, una sconfitta o una vittoria, ecc.
    public void update() {
        if (start) {
            vyhra();
            skontrolujOkraje();
            ball.NarazPaddle(paddle.getX(), paddle.getY());
            for (int i = 0; i < zoznam.size(); i++) {
                Brick b = zoznam.get(i);
                if (ball.NarazBrick(b.getX(), b.getY())) {
//                    Bitmap brickCurrent = zoznam.get(i).getBrick();
                    if (zoznam.get(i).getLevel() == Brick.Level.ONE){
                        zoznam.remove(i);

                        zoznam.add(i,new Brick(context, b.getX(),  b.getY(), Brick.Level.TWO) );
                    }
                    else if (zoznam.get(i).getLevel() == Brick.Level.TWO){
                        zoznam.remove(i);

                    }



                    score = score + 80;
                }
            }
            ball.pohni();
        }
    }

    /**
     * stopSensing
     */
    public void zastavSnimanie() {
        sManager.unregisterListener(this);
    }

    /**
     * runScanning
     */
    public void spustiSnimanie() {
        if(sManager != null) {
            sManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }
    }

    //
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
     serve a sospendere il gioco in caso di un nuovo gioco
     **/
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gameOver == true && start == false) {
            score = 0;
            lifes = 3;
            resetLevel();
            gameOver = false;

        } else {
            start = true;

                switch(event.getAction()){
                    //se l'azione è di tipo down o move richiamo il thread
                    case MotionEvent.ACTION_DOWN: case MotionEvent.ACTION_MOVE:
                        if (controlsThread == null){
                            controlsThread = new WorkerThread(context, event, paddle, size);
                            controlsThread.start();
                        }
                      return true;
                     //se è di tipo up, stoppo il thread
                    case MotionEvent.ACTION_UP:
                        if (controlsThread != null){
                            controlsThread.terminate();
                            controlsThread = null;
                        }
                        return true;
                }
            }

        return false;
    }

    /** imposta il gioco per iniziare
     *
     */
    private void resetLevel() {
        ball.setX(size.x / 2);
        ball.setY(size.y - 480);
        ball.vytvorRychlost();
        zoznam = new ArrayList<Brick>();
        vygenerujBricks(context);
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
