package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    private AudioManager manager;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void sendMessageMusicaOn(View view) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.adjustVolume(AudioManager.ADJUST_UNMUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        String text;
        player = MusicCache.getInstance().getMp();
        if (!player.isPlaying()){
            Toast.makeText(this, text="Musica ON", Toast.LENGTH_SHORT).show();
            player.start();
        } else  Toast.makeText(this, text="La musica è già attiva", Toast.LENGTH_SHORT).show();

    }

    public void sendMessageMusicaOff(View view) {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.adjustVolume(AudioManager.ADJUST_MUTE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        stopPlayer();
    }

    private void stopPlayer() {
        player = MusicCache.getInstance().getMp();
        if(player != null){
            player.pause();
            MusicCache.getInstance().setMp(player);
            String text;
            Toast.makeText(this, text="Musica OFF", Toast.LENGTH_SHORT).show();
        }

    }
}
