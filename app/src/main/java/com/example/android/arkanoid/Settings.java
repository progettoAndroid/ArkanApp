package com.example.android.arkanoid;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void sendMessageMusicaOn(View view) {
        if (player == null){
            player= MediaPlayer.create(this,R.raw.song);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            });
        }

        player.start();
    }

    public void sendMessageMusicaOff(View view) {
        stopPlayer();
    }

    private void stopPlayer() {
        if(player != null){
            player.release();
            player=null;
            String text;
            Toast.makeText(this, text="Musica OFF", Toast.LENGTH_SHORT).show();
        }

    }
}
