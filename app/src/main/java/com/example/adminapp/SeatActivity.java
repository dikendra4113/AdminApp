package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.adminapp.model.SeatAdapter;
import com.example.adminapp.model.SeatModel;
import com.example.adminapp.utils.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import io.paperdb.Paper;

public class SeatActivity extends AppCompatActivity {

    public SeatAdapter adapter;
    public RecyclerView seat_recycler;
    public static  String restaurant_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);
        seat_recycler = findViewById(R.id.seat_recycler);
        Paper.init(this);
        restaurant_name = Paper.book().read(Prevelents.restaurant);
        seat_recycler.setLayoutManager(new GridLayoutManager(this, 4));
        FirebaseRecyclerOptions<SeatModel> options =
                new FirebaseRecyclerOptions.Builder<SeatModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Seats").child(restaurant_name), SeatModel.class)
                        .build();


        adapter =new SeatAdapter(SeatActivity.this,options);
        seat_recycler.setAdapter(adapter);
    }

    @Override
    public void onStart() {

        super.onStart();
        adapter.startListening();


    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();

    }
}