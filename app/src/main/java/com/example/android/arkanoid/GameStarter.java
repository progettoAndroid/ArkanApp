package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.android.arkanoid.MainActivity.MUSIC;

public class GameStarter extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int controller;
    private int orientation;
    private SoundPlayer sound2;
    private boolean DoppioBackPerUscire = false;
    SharedPreferences soundPreferences;
    private int soundOn;
    private boolean[] livello = new boolean[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Prendo dagli intent i valori passati che riguardano la scelta del controller e l'orientamento del cellulare
        controller = getIntent().getIntExtra("EXTRA_CONTROLLER", 0);
        orientation = getIntent().getIntExtra("EXTRA_ORIENTATION", 0);
        livello = getIntent().getBooleanArrayExtra("EXTRA_BOOLEAN");
        // Fisso l'orientamento durante la partita
        if (orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        if(livello == null) {
            game = new Game(this, 3, 0, controller);
        }else if(livello != null){
            game = new Game(this, 3, 0, controller, livello);
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


    //Se l'activity va in onpause() stoppo la musica e smetto di fare il sensing
    /*
      Il gioco continua ad andare per scelta progettuale (tap per sbaglio su home o overview
      comporterebbe la fine della partita).
    */
    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null)
            player.pause();
        game.zastavSnimanie();
    }

    //Riprendo a suonare la musica una volta tornato dall'onpause
    @Override
    protected void onResume() {
        super.onResume();
        MediaPlayer player = MusicCache.getInstance().getMp();
        soundPreferences = getApplicationContext().getSharedPreferences(MUSIC, getApplicationContext().MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);
        if((player!=null && !player.isPlaying()) && soundOn == 1)
            player.start();
        game.spustiSnimanie();
    }

    /*
      Se il pulsante back è premuto due volte faccio tornare al menù, sempre per gestire l'errore
      di un ipotetico tocco singolo
     */
    @Override
    public void onBackPressed() {
        if (DoppioBackPerUscire) {
            super.onBackPressed();
            return;
        }
        this.DoppioBackPerUscire = true;
        Toast.makeText(this, this.getResources().getString(R.string.doppio_back), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DoppioBackPerUscire=false;
            }
        }, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null)
            player.pause();
    }


}
