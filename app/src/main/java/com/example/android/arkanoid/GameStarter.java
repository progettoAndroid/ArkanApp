package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GameStarter extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int controller;
    private int orientation;
    private SoundPlayer sound2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // nastavi orientaciu obrazovky

        // vytvori novu hru
        controller = getIntent().getIntExtra("EXTRA_CONTROLLER", 0);
        orientation = getIntent().getIntExtra("EXTRA_ORIENTATION", 0);
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(getIntent().getBooleanExtra("isMultiplayer", false)) {
            game = new Game(this, 3, 0, controller, true);
        } else {
            game = new Game(this, 3, 0, controller);
        }

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

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null)
            player.pause();
        game.zastavSnimanie();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null && !player.isPlaying())
            player.start();
        game.spustiSnimanie();
    }


    @Override
    public void onBackPressed() {
        String text;
        Toast.makeText(this, text="clicca un'altra volta per tornare al menù", Toast.LENGTH_SHORT).show();
        onDestroy();
    }


}
