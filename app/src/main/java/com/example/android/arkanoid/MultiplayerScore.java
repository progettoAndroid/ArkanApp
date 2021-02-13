package com.example.android.arkanoid;

import android.content.SharedPreferences;
import android.os.Bundle;
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
    private ArrayList<String> users;
    private ArrayList<String> scoreMio;
    private ArrayList<String> scoreAvversario;
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
                    int trovato = -1;
                    namePlayerPreferences = getApplicationContext().getSharedPreferences(NICKNAME, getApplicationContext().MODE_PRIVATE);
                    nickname = namePlayerPreferences.getString("nickname", "");
                    if (dataSnapshot != null) {
                        users = new ArrayList();
                        scoreMio = new ArrayList();
                        scoreAvversario = new ArrayList();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                nicknameCompara1 = ds.child("giocatore1").getValue().toString();
                                nicknameCompara2 = ds.child("giocatore2").getValue().toString();
                                if (nickname.compareTo(nicknameCompara1) == 0) {
                                    if (ds.child("giocatore2").getValue() != null) {
                                        users.add(ds.child("giocatore2").getValue().toString());
                                        scoreMio.add((ds.child("score1").getValue().toString()));
                                        scoreAvversario.add((ds.child("score2").getValue().toString()));
                                        trovato++;
                                    }
                                } else if (nickname.compareTo(nicknameCompara2) == 0) {
                                    if (ds.child("giocatore1").getValue() != null) {
                                        users.add(ds.child("giocatore1").getValue().toString());
                                        scoreMio.add((ds.child("score2").getValue().toString()));
                                        scoreAvversario.add((ds.child("score1").getValue().toString()));
                                        trovato++;
                                    }
                                }
                            }

                        if (users != null && users.size()>0) {
                            if (users.get(trovato).compareTo(nickname) != 0) {
                                TextView viewnick1 = findViewById(R.id.nick1);
                                viewnick1.setText(nickname);
                                TextView viewnick2 = findViewById(R.id.nick2);
                                viewnick2.setText(users.get(trovato));
                                TextView viewscore1 = findViewById(R.id.score1);
                                viewscore1.setText("" + scoreMio.get(trovato));
                                TextView viewscore2 = findViewById(R.id.score2);
                                viewscore2.setText("" + scoreAvversario.get(trovato));
                            } else {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.errore_risultato), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.errore_risultato), Toast.LENGTH_SHORT).show();
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
