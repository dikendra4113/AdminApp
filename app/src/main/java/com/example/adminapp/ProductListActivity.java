package com.example.adminapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.adminapp.model.model;
import com.example.adminapp.model.myAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ProductListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter adapter;
    static public String phoneNumber;
    static public ProgressDialog loadingBar;
    int flag = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        recyclerView =  findViewById(R.id.cardView);

        loadingBar = new ProgressDialog(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product").child("All"), model.class)
                        .build();
        adapter =new myAdapter(options);
        recyclerView.setAdapter(adapter);


    }
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}