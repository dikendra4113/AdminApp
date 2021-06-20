package com.example.adminapp.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.R;
import com.example.adminapp.UserViewActivity;
import com.example.adminapp.model.model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class FragmentAll extends Fragment {
    View view;
    private RecyclerView recyclerView;
    private List<model> models;
    private DatabaseReference databaseReference;

    public FragmentAll() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.starter_catagory_fragment, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.start_Recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

//
        return view;
//        myAdapter adapter = new myAdapter(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerOptions<model> options =
                new FirebaseRecyclerOptions.Builder<model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product").child("All"), model.class)
                        .build();

        FirebaseRecyclerAdapter<model,StarterViewHolder> adapter = new FirebaseRecyclerAdapter<model, StarterViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull StarterViewHolder holder, int position, @NonNull model model) {
                holder.productName.setText(model.getPname());
                holder.product_description.setText(model.getDescription());
                holder.user_productPrice.setText(model.getPrice());
                holder.productCatagory.setText(model.getCatagary());
                holder.pid = model.getPid();

                Glide.with(holder.productImage.getContext()).load(model.getImage()).into(holder.productImage);
            }

            @NonNull
            @Override
            public StarterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_detail,parent,false);
                StarterViewHolder viewHolder = new StarterViewHolder(view);
                return viewHolder;
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    public static class StarterViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName,product_description,user_productPrice,productCatagory;
        Button removeBtn,editBtn;
        ImageView addQuantity,minusQuantity;
        TextView productQuantity;
        public String pid,seatNo;
        private String saveCurrentDate,saveCurrentTime;
        public StarterViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            productName = (TextView)itemView.findViewById(R.id.productName);
            productCatagory = (TextView)itemView.findViewById(R.id.productCatagory);
            product_description = (TextView)itemView.findViewById(R.id.product_description);
            user_productPrice = (TextView)itemView.findViewById(R.id.user_productPrice);
            removeBtn = (Button)itemView.findViewById(R.id.productRemove);
         //   editBtn = (Button) itemView.findViewById(R.id.productEdit);

            removeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference().child("Product");
                    new AlertDialog.Builder(UserViewActivity.context)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete this entry?")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    cartRef.child(productCatagory.getText().toString()).child(pid)
                                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                cartRef.child("All").child(pid).removeValue();

                                                Log.i("delete","Deleted");
                                            }

                                        }
                                    });
                                }
                            })

                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                }
            });

    }


    }

}