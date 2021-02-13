package com.example.android.arkanoid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.android.arkanoid.MainActivity.NICKNAME;

public class MultiplayerScore extends AppCompatActivity {
    private int score1;
    private String score2;
    private String nick1;
    private String nick2;
    SharedPreferences namePlayerPreferences;
    private DatabaseReference rootRef;
    private ArrayList<String> Users;
    private ArrayList<String> ScoreMio;
    private ArrayList<String> ScoreAvversario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multiplayer_score);
        if(getIntent().getStringExtra("EXTRA_NICKNAME1")!=null) {
            nick1 = getIntent().getStringExtra("EXTRA_NICKNAME1");
            score1 = getIntent().getIntExtra("EXTRA_SCORE1", 0);
            nick2 = getIntent().getStringExtra("EXTRA_NICKNAME2");
            score2 = getIntent().getStringExtra("EXTRA_SCORE2");
            TextView viewnick1 = findViewById(R.id.nick1);
            viewnick1.setText(nick1);
            TextView viewnick2 = findViewById(R.id.nick2);
            viewnick2.setText(nick2);
            TextView viewscore1 = findViewById(R.id.score1);
            viewscore1.setText("" + score1);
            TextView viewscore2 = findViewById(R.id.score2);
            viewscore2.setText("" + score2);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Partite");
        if(getIntent().getStringExtra("EXTRA_NICKNAME1")!=null && getIntent().getStringExtra("EXTRA_NICKNAME2")!=null ) {
            rootRef.child(nick1+" "+nick2).child("giocatore1").setValue(nick1);
            rootRef.child(nick1+" "+nick2).child("giocatore2").setValue(nick2);
            rootRef.child(nick1+" "+nick2).child("score1").setValue(score1);
            rootRef.child(nick1+" "+nick2).child("score2").setValue(score2);
        } else{
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nickname;
                    String nicknameCompara1;
                    String nicknameCompara2;
                    boolean trovato = false;
                    namePlayerPreferences = getApplicationContext().getSharedPreferences(NICKNAME, getApplicationContext().MODE_PRIVATE);
                    nickname = namePlayerPreferences.getString("nickname", "");
                    if (dataSnapshot != null) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                Users = new ArrayList();
                                ScoreMio = new ArrayList();
                                ScoreAvversario = new ArrayList();
                                nicknameCompara1 = ds.child("giocatore1").getValue().toString();
                                nicknameCompara2 = ds.child("giocatore2").getValue().toString();
                                if (nickname.compareTo(nicknameCompara1) == 0) {
                                    if (ds.child("giocatore2").getValue() != null) {
                                        Users.add(ds.child("giocatore2").getValue().toString());
                                        ScoreMio.add((ds.child("score1").getValue().toString()));
                                        ScoreAvversario.add((ds.child("score2").getValue().toString()));
                                    }
                                } else if (nickname.compareTo(nicknameCompara2) == 0) {
                                    if (ds.child("giocatore1").getValue() != null) {
                                        Users.add(ds.child("giocatore1").getValue().toString());
                                        ScoreMio.add((ds.child("score2").getValue().toString()));
                                        ScoreAvversario.add((ds.child("score1").getValue().toString()));
                                    }
                                }
                            }

                        if (Users != null && Users.size()>1) {
                            if (Users.get(Users.lastIndexOf(Users)).compareTo(nickname) != 0) {
                                TextView viewnick1 = findViewById(R.id.nick1);
                                viewnick1.setText(nickname);
                                TextView viewnick2 = findViewById(R.id.nick2);
                                viewnick2.setText(Users.get(Users.size()-1));
                                TextView viewscore1 = findViewById(R.id.score1);
                                viewscore1.setText("" + ScoreMio.get(ScoreMio.size()-1));
                                TextView viewscore2 = findViewById(R.id.score2);
                                viewscore2.setText("" + ScoreAvversario.get(ScoreAvversario.size()-1));
                            } else if (Users.size() == 1) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.errore_risultato) + "1", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.errore_risultato) + "2", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });

        }

    }
}
