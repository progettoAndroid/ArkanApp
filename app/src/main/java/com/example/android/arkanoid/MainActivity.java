package com.example.android.arkanoid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    public static final String CONTROLLER ="ControllerFile";    //serve a salvare la preferenza sul controller
    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int selectedController = 2;
    private Context mContext;
    private SoundPlayer sound2;
    String[] controllers = {"Touch", "Accelerometro"};
    SharedPreferences controllerSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sound2 = new SoundPlayer(this);

        //Uso le preferenze per salvare e recuperare i comandi preferiti dell'utente
        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);

        //se la preferenza sul controller non è ancora stata settata, gli chiedo quale controller preferisca.
        if (selectedController == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.styledialog);
            builder.setTitle("CONFIGURAZIONE INIZIALE \nScegli come controllare il paddle");
            builder.setItems(controllers, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    selectedController = which;
                    //salvo la preferenza per non chiederla più
                    SharedPreferences.Editor editor = controllerSettings.edit();
                    editor.putInt("SelectedController", selectedController);
                    editor.commit();
                }
            });
            //sistemo la parte grafica dell'alertdialog
            AlertDialog dialog = builder.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_style);
            ListView listView =dialog.getListView();
            listView.setDivider(new ColorDrawable(Color.LTGRAY));
            listView.setDividerHeight(2);
        }
    }


    public void sendMessageSingleplayer(View view) {
        Intent gameStarter = new Intent(this, GameStarter.class);
        sound2.playStarting();
        if(selectedController==2){
            selectedController=0;
        }
        gameStarter.putExtra("EXTRA_CONTROLLER",selectedController);
        if(selectedController != 2) {
            startActivity(gameStarter);
        }
    }

    public void sendMessageMultiplayer(View view) {
        sound2.playButton();
    }
    public void sendMessageImpostazioni(View view) {
        Intent settings = new Intent(this, Settings.class);
        sound2.playButton();
        startActivity(settings);
    }
    public void sendMessageClassifica(View view) {
        sound2.playButton();
    }
}