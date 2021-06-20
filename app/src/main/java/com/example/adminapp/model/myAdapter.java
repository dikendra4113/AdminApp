package com.example.adminapp.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;



public class myAdapter extends FirebaseRecyclerAdapter<model,myAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public myAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull model model) {

        holder.productName.setText(model.getPname());
        holder.product_description.setText(model.getDescription());
        holder.user_productPrice.setText(model.getPrice());
        holder.productCatagory.setText(model.getCatagary());



        Glide.with(holder.productImage.getContext()).load(model.getImage()).into(holder.productImage);

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_cart,parent,false);
        return new myViewHolder(view);
    }

    static class myViewHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productName,product_description,user_productPrice,productCatagory;
//        Button addToCart;
        ImageView addQuantity,minusQuantity;
        TextView productQuantity;
        public String pid,seatNo;
        private String saveCurrentDate,saveCurrentTime;


        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = (ImageView)itemView.findViewById(R.id.productImage);
            productName = (TextView)itemView.findViewById(R.id.productName);
            productCatagory = (TextView)itemView.findViewById(R.id.productCatagory);
            product_description = (TextView)itemView.findViewById(R.id.product_description);
            user_productPrice = (TextView)itemView.findViewById(R.id.user_productPrice);
//            addToCart = (Button) itemView.findViewById(R.id.addToCart);



        }


    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }
}
