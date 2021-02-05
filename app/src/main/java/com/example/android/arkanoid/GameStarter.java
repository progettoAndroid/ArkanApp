package com.example.android.arkanoid;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GameStarter extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int Controller;
    private SoundPlayer sound2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // nastavi orientaciu obrazovky
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // vytvori novu hru
        Controller = getIntent().getIntExtra("EXTRA_CONTROLLER", 0);
        game = new Game(this, 3, 0, Controller);

        setContentView(game);

        // vytvori handler a thread
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    private void VytvorHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }

    protected void onPause() {
        super.onPause();
        game.zastavSnimanie();
    }

    protected void onResume() {
        super.onResume();
        game.spustiSnimanie();
    }

}
