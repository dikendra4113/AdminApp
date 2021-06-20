package com.example.adminapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.adminapp.model.OrderAdapter;
import com.example.adminapp.model.OrderModel;
import com.example.adminapp.utils.Prevelents;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView order_recyclerView;
    public OrderAdapter adapter;
    public String saveCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        order_recyclerView = findViewById(R.id.orderlist_recycler);
        order_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String phone = Paper.book().read(Prevelents.phone);
        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd,yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());
        FirebaseRecyclerOptions<OrderModel> options =
                new FirebaseRecyclerOptions.Builder<OrderModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Order").child("Admins View"), OrderModel.class)
                        .build();


        adapter =new OrderAdapter(OrdersActivity.this,options);
        order_recyclerView.setAdapter(adapter);
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