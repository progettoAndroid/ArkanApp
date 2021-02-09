package com.example.android.arkanoid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class MainActivity extends AppCompatActivity {
    public static final String CONTROLLER ="ControllerFile";    //serve a salvare la preferenza sul controller
    public static final String NICKNAME ="NamePlayerFile";
    private static final String TAG = "DB";
    private MediaPlayer player;
    private String nickname = "";
    private String inputName = "";
    SharedPreferences controllerSettings;
    SharedPreferences namePlayerPreferences;
    String[] controllers = {"Touch", "Accelerometro"};
    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int selectedController = 2;
    private Context mContext;
    private SoundPlayer sound2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MusicCache.getInstance();
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);

        sound2 = new SoundPlayer(this);
        player= MediaPlayer.create(this,R.raw.song);
        MusicCache.getInstance().setMp(player);


    }
    @Override
    protected void onStart() {

        super.onStart();

        //Uso le preferenze per salvare e recuperare i comandi preferiti dell'utente
        controllerSettings = mContext.getSharedPreferences(CONTROLLER, mContext.MODE_PRIVATE);
        selectedController = controllerSettings.getInt("SelectedController", 2);

        //se la preferenza sul controller non è ancora stata settata, gli chiedo quale controller preferisca.
        if (selectedController == 2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.styledialog);
            builder.setTitle(mContext.getResources().getString(R.string.configurazione_iniziale) + "\n" + mContext.getResources().getString(R.string.scelta));
            builder.setCancelable(false);
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
            AlertDialog dialogController = builder.show();
            dialogController.getWindow().setBackgroundDrawableResource(R.drawable.popup_style);
            ListView listView =dialogController.getListView();
            listView.setDivider(new ColorDrawable(Color.LTGRAY));
            listView.setDividerHeight(2);
        }

        namePlayerPreferences = mContext.getSharedPreferences(NICKNAME, mContext.MODE_PRIVATE);
        nickname = namePlayerPreferences.getString ("nickname","");


        if ( nickname.isEmpty()) {
            final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setTitle(R.string.benvenuto);
            builder1.setMessage(R.string.username);
            builder1.setCancelable(false);

             final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT );
            builder1.setView(input);
            builder1.setPositiveButton("OK", null);
            final AlertDialog dialog = builder1.show();
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.popup_style);

//
//            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            button.setOnClickListener(new View.OnClickListener() {
//
//                @SuppressLint("RestrictedApi")
//                @Override
//                public void onClick(View view1) {
//                    if (TextUtils.isEmpty(input.getText().toString().trim())) {
//                        input.setError(mContext.getResources().getString(R.string.non_vuoto));
//                    } else {
//                        inputName = input.getText().toString().trim();
//
//                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
//                        final DatabaseReference userNameRef = rootRef.child("Users").child(inputName).child("username");
//                          final DatabaseReference punteggioRef = userNameRef.getParent().child("points");
//
//                       final  ValueEventListener eventListener = new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if(!dataSnapshot.exists()) {
//
//                                    SharedPreferences.Editor editor = namePlayerPreferences.edit();
//                                    editor.putString("nickname", inputName);
//
//                                    editor.commit();
//
//                                    userNameRef.setValue(inputName);
//                                    punteggioRef.setValue("0");
//
//                                    dialog.dismiss();
//                                 }
//                                else{
//                                    input.setError(mContext.getResources().getString(R.string.username_esistente));
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//                                Log.d(TAG, databaseError.getMessage()); //Don't ignore errors!
//                            }
//                        };
//                        userNameRef.addListenerForSingleValueEvent(eventListener);
//                    }
//                }
//            });
        }
    }

    public void sendMessageSingleplayer(View view) {
        Intent gameStarter = new Intent(this, GameStarter.class);
        int orientation = this.getResources().getConfiguration().orientation;
        if(selectedController==2){
            selectedController=0;
        }
        gameStarter.putExtra("EXTRA_CONTROLLER",selectedController);
        gameStarter.putExtra("EXTRA_ORIENTATION", orientation);
        if(selectedController != 2) {
            sound2.playButton();
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
    public void sendMessageRanking(View view) {
//        Intent rank = new Intent(this, Ranking.class);
        sound2.playButton();
//        startActivity(rank);
    }

    @Override
    protected void onPause(){
        super.onPause();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null)
            player.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        MediaPlayer player = MusicCache.getInstance().getMp();
        if(player!=null && !player.isPlaying())
            player.start();

    }

}