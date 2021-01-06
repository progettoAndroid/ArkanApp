package com.example.android.arkanoid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String CONTROLLER ="ControllerFile";    //serve a salvare la preferenza sul controller
    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int selectedController = 2;
    private Context mContext;
    String[] controllers = {"Touch", "Accelerometro"};
    SharedPreferences controllerSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Uso le preferenze per salvare e recuperare i comandi preferiti dell'utente
        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);

        //se la preferenza sul controller non è ancora stata settata, gli chiedo quale controller preferisca.
        if (selectedController == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Scegli come Controllare il Paddle");
            builder.setItems(controllers, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedController = which;
                    SharedPreferences.Editor editor = controllerSettings.edit();
                    editor.putInt("SelectedController", selectedController);
                    editor.commit();
                }
            });
            builder.show();
        }
    }


    public void sendMessageSingleplayer(View view) {
        Intent gameStarter = new Intent(this, GameStarter.class);
        gameStarter.putExtra("EXTRA_CONTROLLER",selectedController);
        if(selectedController != 2) {
            startActivity(gameStarter);
        }
    }

    public void sendMessageMultiplayer(View view) {

    }
    public void sendMessageImpostazioni(View view) {

    }
    public void sendMessageClassifica(View view) {

    }
}