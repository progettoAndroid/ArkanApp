package com.example.android.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.android.arkanoid.MainActivity.MUSIC;

public class Multiplayer extends AppCompatActivity {

    private Integer selectedController;
    private SoundPlayer sound2;
    SharedPreferences soundPreferences;
    private int soundOn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer);
        sound2 = new SoundPlayer(this);
        selectedController = getIntent().getIntExtra("selectedController",2);;
        soundPreferences = getApplicationContext().getSharedPreferences(MUSIC, getApplicationContext().MODE_PRIVATE);
        soundOn = soundPreferences.getInt("Music", 1);
    }

    public void sendMessageRanking(View view) {
        Intent rank = new Intent(this, Ranking.class);
        if(soundOn==1) {
            sound2.playButton();
        }
        startActivity(rank);
    }

    public void sendMessagePlay(View view){
        Intent gameStarter = new Intent(this, GameStarter.class);
        int orientation = this.getResources().getConfiguration().orientation;
        if(selectedController==2){
            selectedController=0;
        }
        gameStarter.putExtra("EXTRA_CONTROLLER",selectedController);
        gameStarter.putExtra("EXTRA_ORIENTATION", orientation);
        gameStarter.putExtra("isMultiplayer", 1);
        if(selectedController != 2) {
            if(soundOn==1) {
                sound2.playButton();
            }
            startActivity(gameStarter);
        }

    }
    public void sendMessageResult(View view){
        Intent multiplayerScore = new Intent(this, MultiplayerScore.class);
        startActivity(multiplayerScore);
    }

}
