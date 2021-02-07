package com.example.android.arkanoid;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        ViewGroup.LayoutParams params;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            numberRanking = (TextView) itemView.findViewById(R.id.number);
            username = (TextView) itemView.findViewById(R.id.username);
            points = (TextView) itemView.findViewById(R.id.points);
            params = (LinearLayout.LayoutParams)username.getLayoutParams();
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
        public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {

        DataSnapshot snapshot =  mDataSnapshots.get(position);
        UsersModal user  = snapshot.getValue(UsersModal.class);

        holder.username.setText(user.getUsername());
        holder.points.setText(user.getPoints());
        holder.numberRanking.setText(String.valueOf(position));
        spinner.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return mDataSnapshots.size();
    }
    public void  clean() {
        mData.removeEventListener(mListener);
    }
}





