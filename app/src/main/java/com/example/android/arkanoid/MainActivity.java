package com.example.android.arkanoid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

public class MainActivity<SPLASH_TIME_OUT> extends Activity {
    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int selectedController;
    String[] controllers = {"Touch", "Accelerometro"};
    private Button button;

    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openActivitymenu();
            }
        });


    }

    public void openActivitymenu(){


        // crea un nuovo gioco
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scegli come Controllare il Paddle");
        builder.setItems(controllers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedController = which;
            }
        });
        builder.show();
        game = new Game(this, 3, 0, selectedController);
        setContentView(game);

        // imposta l'orientamento dello schermo
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // crea un gestore e un thread
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }
    /**
     * creaHandler
     */
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
