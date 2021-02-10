package com.example.android.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.android.arkanoid.MainActivity.CONTROLLER;
import static com.example.android.arkanoid.MainActivity.MUSIC;


public class Editor extends AppCompatActivity {
    public static final String LEVELPOR ="LevelFile";
    public static final String LEVELLAND ="LevelLandscapeFile";
    CheckBox chkBuffer;
    ArrayList<CheckBox> ArrayCheckbox = new ArrayList<>();  // arraylist che servirà a fare il controllo con l'id per modificare il boolean giusto
    boolean[] Livello = new boolean[21];    // il livello sarà visto come un array di boolean, true c'è blocco, false non c'è.
    private int selectedController;
    private int soundOn;
    private MediaPlayer player;
    SharedPreferences soundPreferences;
    SharedPreferences controllerSettings;
    SharedPreferences levelPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor);
        soundPreferences = getApplicationContext().getSharedPreferences(MUSIC, getApplicationContext().MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);

        //Fisso l'orientamento.
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        //controllo se la musica è on o off per valorizzare lo switch
        if(soundOn == 1) {
            player=MusicCache.getInstance().getMp();
            player.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        PopolaArray();
    }

    // popolo l'array di boolean livello e l'arraylist di checkbox
    private void PopolaArray(){
        for(int i=1;i<=20 ;i++){
           int resID = getResources().getIdentifier("check"+i, "id",getPackageName());
            CheckBox cb = (CheckBox) findViewById(resID);
            ArrayCheckbox.add(cb);
            Livello[i]=cb.isChecked();
        }
    }

    // funzione che cambia il valore del singolo blocco quando viene cliccata la checkbox
    public void BooleanBlocco(View view){
        CheckBox chk = (CheckBox) view;
        for(int i=1;i<=20 ;i++){
            if(chk.getId() == ArrayCheckbox.get(i-1).getId()){
                ArrayCheckbox.set(i-1, chk);
                Livello[i] = chk.isChecked();
            }
        }
        //se il valore è checkato lo faccio diventare verde tenendo la destinazione altrimenti lo faccio diventare bianco.
        //il brick red, in modalità src_in viene sovrascritto pixel a pixel dall'android:buttonTint del layout
        if(chk.isChecked()) {
            ((CheckBox) view).setButtonTintMode(PorterDuff.Mode.DST);
            ((CheckBox) view).setButtonDrawable(R.drawable.brick_selected);
        }
        if(!chk.isChecked()) {
            ((CheckBox) view).setButtonTintMode(PorterDuff.Mode.SRC_IN);
            ((CheckBox) view).setButtonDrawable(R.drawable.brick_red);
        }


    }

    // se il livello ha almeno un blocco selezionato lo faccio giocare altrimenti no.
    public void Gioca(View view) {
        boolean flagGiocabile = false;
        for (int i = 1; i <= 20; i++) {
            if (Livello[i] == true) {
                flagGiocabile = true;
            }
        }
        if (flagGiocabile == true) {
            Intent gameStarter = new Intent(this, GameStarter.class);
            controllerSettings = getApplicationContext().getSharedPreferences(CONTROLLER, getApplicationContext().MODE_PRIVATE);
            selectedController = controllerSettings.getInt("SelectedController", 2);
            int orientation = this.getResources().getConfiguration().orientation;
            SoundPlayer sound2 = new SoundPlayer(this);
            if (selectedController == 2) {
                selectedController = 0;
            }
            gameStarter.putExtra("EXTRA_CONTROLLER", selectedController);
            gameStarter.putExtra("EXTRA_ORIENTATION", orientation);
            gameStarter.putExtra("EXTRA_BOOLEAN", Livello);
            if (selectedController != 2) {
                if (soundOn == 1) {
                    sound2.playButton();
                }
                startActivity(gameStarter);
            }
        } else {
            String text;
            Toast.makeText(this, text = getApplicationContext().getResources().getString(R.string.livello_non_giocabile), Toast.LENGTH_SHORT).show();

        }
    }

    // funzione che, una volta appurato l'orientamento, salva sul file adatto i valori con un separatore.
    public void Salva(View view){
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            levelPreferences = getApplicationContext().getSharedPreferences(LEVELPOR, getApplicationContext().MODE_PRIVATE);
        } else if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            levelPreferences = getApplicationContext().getSharedPreferences(LEVELLAND, getApplicationContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor LevelEditor = levelPreferences.edit();
        String save = "";
        for (int i = 1; i <=20; i++) {
            if(Livello[i]) {
                save = save + "true" + "|$|SEPARATOR|$|";
            } else {
                save = save + "false" + "|$|SEPARATOR|$|";
            }
        }
        LevelEditor.putString("level", save);
        LevelEditor.commit();
    }

    // funzione che dopo aver capito quale sia l'attuale configurazione procede a caricare il livello.
    public void Carica(View view){
        if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            levelPreferences = getApplicationContext().getSharedPreferences(LEVELPOR, getApplicationContext().MODE_PRIVATE);
        } else if (getApplicationContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            levelPreferences = getApplicationContext().getSharedPreferences(LEVELLAND, getApplicationContext().MODE_PRIVATE);
        }
        SharedPreferences.Editor LevelEditor = levelPreferences.edit();
        String mem = levelPreferences.getString("level", "");
        LevelEditor.putString("level", "");
        String[] array = getArray(mem);
        for(int i = 1; i <= array.length; i++){
            if(array[i-1].equals("true")){
                Livello[i] = true;
            }else{
                Livello[i] = false;
            }
        }
        // Con un output grafico si conferma che il livello è caricato, rendendo selezionati i blocchi.
        for(int i=1;i<=20;i++){
            if(Livello[i]==true){
                int resID = getResources().getIdentifier("check"+i, "id",getPackageName());
                CheckBox cb = (CheckBox) findViewById(resID);
                cb.setChecked(true);
                cb.setButtonTintMode(PorterDuff.Mode.DST);
                cb.setButtonDrawable(R.drawable.brick_selected);
            }
        }
    }

    // Funzione che si occupa della divisione al separatore della stringa (come strtok di C)
    public static String[] getArray(String input) {
        return input.split("\\|\\$\\|SEPARATOR\\|\\$\\|");
    }

}




