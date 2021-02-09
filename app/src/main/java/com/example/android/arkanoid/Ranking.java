//package com.example.android.arkanoid;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
//public class Ranking extends AppCompatActivity {
//    private ListView rankingLV;
//    private  ArrayList<UsersModal> usersModalArrayList;
//    private DatabaseReference rootRef;
//     private static final String TAG = "DB";
//    private HashMap<String,Integer> userlist;
//    private  ArrayList<UsersModal> usersList ;
//    private RankingAdapter rankingAdapter;
//    private RecyclerView rvRanking;
//    private ProgressBar spinner;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ranking);
//
//        spinner = (ProgressBar)findViewById(R.id.progressBar_ranking);
//        spinner.setVisibility(View.VISIBLE);
//
//
//
//
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        rvRanking = (RecyclerView)findViewById(R.id.ranking);
//        rootRef= FirebaseDatabase.getInstance().getReference();
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((getApplicationContext()));
//        rvRanking.setLayoutManager(linearLayoutManager);
//
//
//        //creo l adapter
//        rankingAdapter = new  RankingAdapter(this,rootRef,spinner);
//
//        //assegno l adapter alla recycler view
//        rvRanking.setAdapter(rankingAdapter);
//
//
//
////        rankingLV = (ListView ) findViewById(R.id.list_ranking);
////        usersModalArrayList = new ArrayList<>();
////        rootRef = FirebaseDatabase.getInstance().getReference().child("Users");
////        loadDatainListview();
//    }
//
//    @Override
//    protected void onStop(   ) {
//        super.onStop();
//        rankingAdapter.clean();
//
//     }
//
////    private void loadDatainListview() {
//////        rankingLV.setAdapter(new RankingAdapter(this, R.layout.card_ranking,usersModalArrayList));
////        // below line is use to get data from Firebase
////        // firestore using collection in android.
////        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
////            @Override
////            public void onDataChange(DataSnapshot dataSnapshot) {
////                userlist = new HashMap<String,Integer>();
////                // Result will be holded Here
////                usersList = new ArrayList<UsersModal>();
////                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
////                    Log.w(TAG, ""+dsp);
//////                    users.add(new UsersModal());
////                     userlist.putAll((HashMap<String,Integer>)dsp.getValue() ); //add result into array list
//////                       usersList.add( dsp.getValue(UsersModal.class));
////
////                  }
////                for (Map.Entry<String, Integer> entry : userlist.entrySet()) {
////                    Log.w(TAG, "Key : " + entry.getKey() + " Value : " + entry.getValue()) ;
////                }
////
////            }
////
////            @Override
////            public void onCancelled(DatabaseError error) {
////                // Failed to read value
////                Log.w(TAG, "Failed to read value.", error.toException());
////            }
////        });
//////                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//////                    @Override
//////                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//////                        // after getting the data we are calling on success method
//////                        // and inside this method we are checking if the received
//////                        // query snapshot is empty or not.
//////                        if (!queryDocumentSnapshots.isEmpty()) {
//////                            // if the snapshot is not empty we are hiding
//////                            // our progress bar and adding our data in a list.
//////                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//////                            for (DocumentSnapshot d : list) {
//////                                // after getting this list we are passing
//////                                // that list to our object class.
//////                                DataModal dataModal = d.toObject(DataModal.class);
//////
//////                                // after getting data from Firebase we are
//////                                // storing that data in our array list
//////                                dataModalArrayList.add(dataModal);
//////                            }
//////                            // after that we are passing our array list to our adapter class.
//////                            CoursesLVAdapter adapter = new CoursesLVAdapter(MainActivity.this, dataModalArrayList);
//////
//////                            // after passing this array list to our adapter
//////                            // class we are setting our adapter to our list view.
//////                            coursesLV.setAdapter(adapter);
//////                        } else {
//////                            // if the snapshot is empty we are displaying a toast message.
//////                            Toast.makeText(MainActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
//////                        }
//////                    }
//////                }).addOnFailureListener(new OnFailureListener() {
//////            @Override
//////            public void onFailure(@NonNull Exception e) {
//////                // we are displaying a toast message
//////                // when we get any error from Firebase.
//////                Toast.makeText(MainActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
//////            }
//////        });
////    }
//
//
//}
