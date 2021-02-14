package com.example.android.arkanoid;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 2;

    private static SoundPool soundPool;
    private static int hitSound;
    private static int starting;
    private static int button;

    public SoundPlayer(Context context){

         audioAttributes = new AudioAttributes.Builder()
                 .setUsage(AudioAttributes.USAGE_GAME)
                 .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                 .build();
         soundPool = new SoundPool.Builder()
                 .setAudioAttributes(audioAttributes)
                 .setMaxStreams(SOUND_POOL_MAX)
                 .build();


        hitSound = soundPool.load(context, R.raw.tasti,1);
        starting = soundPool.load(context, R.raw.tastosp,1);
        button = soundPool.load(context, R.raw.tasti,1);

    }

    public void playHitSound(){
        soundPool.play(hitSound, 1.0f, 1.0f, 1,0, 1.0f);
    }


    public void playStarting(){
        soundPool.play(starting, 1.0f, 1.0f, 1,0, 1.0f);
    }

    public void playButton(){
        soundPool.play(button, 1.0f, 1.0f, 1,0, 1.0f);
    }
    public void pauseButton(){
        soundPool.release( );
    }
}

