package com.example.android.arkanoid;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity2 extends Activity {

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        /*@Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.layout.main,menu);
            return true;
        }*/
    }
}