package com.example.android.arkanoid;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import static com.example.android.arkanoid.MainActivity.CONTROLLER;
import static com.example.android.arkanoid.MainActivity.MUSIC;

public class Settings extends AppCompatActivity {
    private MediaPlayer player;
    Switch sw;
    Switch sw2;
    private Context mContext;
    private int selectedController;
    private int soundOn;
    SharedPreferences controllerSettings;
    SharedPreferences soundPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mContext = getApplicationContext();
        player = MusicCache.getInstance().getMp();
        sw = (Switch) findViewById(R.id.switch1);
        soundPreferences = mContext.getSharedPreferences(MUSIC, mContext.MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);
        //controllo se la musica è on o off per valorizzare lo switch
        if(soundOn == 0) { sw.setChecked(false); }
        if( sw.isChecked() ) { player.start(); }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sendMessageMusicaOn();
                } else {
                    sendMessageMusicaOff();
                }
            }
        });

        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);
        sw2 = (Switch) findViewById(R.id.switch2);
        //controllo quale sia il pad per valorizzare lo switch
        if(selectedController==1){ sw2.setChecked(false);}
        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    sendMessageAccelerometroOn();
                } else {
                    sendMessageTouchOn();
                }
            }
        });
    }

    public void sendMessageMusicaOn() {
        //Accedo alle preferenze riguardanti la musica
        soundPreferences = mContext.getSharedPreferences(MUSIC, mContext.MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);
        //Se soundOn è 1 vuol dire che la musica è attiva
        if(soundOn == 0) {
            //Effettuo lo scambio
            SharedPreferences.Editor editor = soundPreferences.edit();
            soundOn = 1;
            editor.putInt("Music", soundOn);
            editor.commit();
        }
        String text;
        if(player == null){
            player= MediaPlayer.create(this,R.raw.song);
            MusicCache.getInstance().setMp(player);
        }
        if (!player.isPlaying()){
            Toast.makeText(this, text=getApplicationContext().getResources().getString(R.string.musicaOn), Toast.LENGTH_SHORT).show();
            player.start();
        } else  Toast.makeText(this, text="La musica è già attiva", Toast.LENGTH_SHORT).show();

    }

    public void sendMessageMusicaOff() {
        //Accedo alle preferenze riguardanti la musica
        soundPreferences = mContext.getSharedPreferences(MUSIC, mContext.MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);
        //Se soundOn è 1 vuol dire che la musica è attiva
        if(soundOn == 1) {
            //Effettuo lo scambio
            SharedPreferences.Editor editor = soundPreferences.edit();
            soundOn = 0;
            editor.putInt("Music", soundOn);
            editor.commit();
        }
        if(player != null){
            player.pause();
            MusicCache.getInstance().setMp(player);
            String text;
            Toast.makeText(this, text=getApplicationContext().getResources().getString(R.string.musicaOff), Toast.LENGTH_SHORT).show();
        }

    }


    public void sendMessageAccelerometroOn(){
        //Accedo alle preferenze riguardanti il controller
        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);
        //Se il selectedController è 0 o 2 ossia touch o default, ci metto 1, ossia l'accelerometro
        if(selectedController == 0 || selectedController == 2) {
            //Effettuo lo scambio
            SharedPreferences.Editor editor = controllerSettings.edit();
            selectedController = 1;
            editor.putInt("SelectedController", selectedController);
            editor.commit();
            String text;
            Toast.makeText(this, text=getApplicationContext().getResources().getString(R.string.accelerometro_on), Toast.LENGTH_SHORT).show();
        }
    }

    public void sendMessageTouchOn(){
        //Accedo alle preferenze riguardanti il controller
        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);
        //Se il selectedController è 1 o 2 ossia accelerometro o default, ci metto 0, ossia il touch
        if(selectedController == 1 || selectedController == 2) {
            SharedPreferences.Editor editor = controllerSettings.edit();
            selectedController = 0;
            editor.putInt("SelectedController", selectedController);
            editor.commit();
            String text;
            Toast.makeText(this, text = getApplicationContext().getResources().getString(R.string.tocco_on), Toast.LENGTH_SHORT).show();
        }
    }



}
