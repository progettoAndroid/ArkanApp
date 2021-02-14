package com.example.android.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {
    private Activity mActivity;
    private DatabaseReference mData;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshots;
    ProgressBar spinner;
    SharedPreferences namePlayerPreferences;
    private String nickname = "";

    public static final String NICKNAME ="NamePlayerFile";


    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            Collections.sort(mDataSnapshots, new Comparator<DataSnapshot>() {
                @Override
                public int compare(DataSnapshot d1, DataSnapshot d2) {
                    UsersModal user1  = d1.getValue(UsersModal.class);
                    UsersModal user2  = d2.getValue(UsersModal.class);

                    return  Integer.valueOf(user1.getPoints() ) > Integer.valueOf(user2.getPoints() ) ? -1 :  Integer.valueOf(user1.getPoints() ) <  Integer.valueOf(user2.getPoints() )   ? 1 : 0;
                }
            });
            mDataSnapshots.add(snapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    public RankingAdapter(Activity activity, DatabaseReference ref, ProgressBar spinner) {
        this.mActivity = activity;
        this.mData = ref.child("Users");
        this.mDisplayName = mDisplayName;
        this.mDataSnapshots = new ArrayList<>();
        mData.addChildEventListener(mListener);
        this.spinner = spinner;
     }

    public class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView points;
        TextView numberRanking;
        ImageView imageSend;
        ViewGroup.LayoutParams params;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            numberRanking = (TextView) itemView.findViewById(R.id.number);
            username = (TextView) itemView.findViewById(R.id.username);
            points = (TextView) itemView.findViewById(R.id.points);
            imageSend = (ImageView) itemView.findViewById(R.id.image_send);
//            params = (LinearLayout.LayoutParams)username.getLayoutParams();
        }

    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout Inflater viene utilizzata per istanziare il contenuto dei file XML di layout nei corrispondenti oggetti View.
        //In altre parole, prende un file XML come input e da esso costruisce gli oggetti View.
        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = inflater.inflate(R.layout.row_ranking,parent,false);
        RankingViewHolder rh = new RankingViewHolder(v);
        return rh;
    }

    @Override
        public void onBindViewHolder(@NonNull final RankingViewHolder holder, final int position) {

        DataSnapshot snapshot =  mDataSnapshots.get(position);
        final UsersModal user  = snapshot.getValue(UsersModal.class);

        holder.username.setText(user.getUsername());
        holder.points.setText(user.getPoints());
        holder.numberRanking.setText(String.valueOf(position+1));

        namePlayerPreferences = mActivity.getApplicationContext().getSharedPreferences(NICKNAME, mActivity.getApplicationContext().MODE_PRIVATE);
        nickname = namePlayerPreferences.getString ("nickname","");
        holder.username.setTextColor(  Color.parseColor("#FFFFFF"));
        holder.points.setTextColor(  Color.parseColor("#FFFFFF"));
        holder.numberRanking.setTextColor(  Color.parseColor("#FFFFFF"));
        if (nickname.equals(user.getUsername())){
            holder.imageSend.setVisibility(View.VISIBLE);
            holder.username.setTypeface(null, Typeface.BOLD);
            holder.itemView.setBackgroundColor( Color.parseColor("#FFFFFF"));
            holder.points.setTypeface(null, Typeface.BOLD);
            holder.numberRanking.setTypeface(null, Typeface.BOLD);
            holder.username.setTextColor(  Color.parseColor("#000000"));
            holder.points.setTextColor(  Color.parseColor("#000000"));
            holder.numberRanking.setTextColor(  Color.parseColor("#000000"));
          }
        switch (position+1){
            case 1: holder.username.setTextColor( Color.parseColor("#daa520"));
                holder.points.setTextColor( Color.parseColor("#daa520"));
                holder.numberRanking.setTextColor( Color.parseColor("#daa520"));
                 break;
             case 2: holder.username.setTextColor( Color.parseColor("#8a9597"));
                holder.points.setTextColor( Color.parseColor("#8a9597"));
                holder.numberRanking.setTextColor( Color.parseColor("#8a9597"));

                break;
              case 3: holder.username.setTextColor( Color.parseColor("#cd7f32"));
                 holder.points.setTextColor( Color.parseColor("#cd7f32"));
                 holder.numberRanking.setTextColor( Color.parseColor("#cd7f32"));
               break;
         }

        spinner.setVisibility(View.GONE);

        holder.imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                int pos = position;
                pos +=1;
                sendIntent.putExtra(Intent.EXTRA_TEXT, "---ArkanApp---\n" + "Il miglior punteggio del giocatore " + user.getUsername() + " è: "  + user.getPoints() + " arrivando " + pos+ " in classifica!");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                mActivity.startActivity(shareIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataSnapshots.size();
    }
    public void  clean() {
        mData.removeEventListener(mListener);
    }
}





