package com.example.adminapp.model;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.OrdersActivity;
import com.example.adminapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class OrderAdapter extends FirebaseRecyclerAdapter<OrderModel,OrderAdapter.myViewHolder> {

    private final OrdersActivity orderViewActivity;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param orderViewActivity
     * @param options
     */
    public OrderAdapter(OrdersActivity orderViewActivity, @NonNull FirebaseRecyclerOptions<OrderModel> options) {
        super(options);
        this.orderViewActivity = orderViewActivity;
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderAdapter.myViewHolder holder, int position, @NonNull OrderModel model) {

        holder.order_seat.setText("Seat No.: "+model.getSeatNo());
        holder.order_price.setText("Rs.: "+model.getPrice());
//        holder.order_des.setText("Des: "+model.getDescription());
        holder.order_pname.setText(model.getPname()+" x "+model.getQuantity());
        holder.restaurant_name.setText(model.getRestaurant_name());
        holder.order_date.setText("Date: "+model.getDate());
        if(model.getStatus().equals("Done")){
            holder.deliveryStatus.setChecked(true);
        }else {
            holder.deliveryStatus.setChecked(false);
        }
        holder.deliveryStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                final DatabaseReference orderPath = FirebaseDatabase.getInstance().getReference().child("Order");

                final HashMap<String,Object> data = new HashMap<>();
                if(holder.deliveryStatus.isChecked()){


                    data.put("status","Done");
                    orderPath.child("Admins View").child(model.getPid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            orderPath.child("Users View").child(model.getUser_contact()).child(model.getPid()).updateChildren(data);
                        }
                    });
                }else {
                    data.put("status","false");
                    orderPath.child("Admins View").child(model.getPid()).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            orderPath.child("Users View").child(model.getUser_contact()).child(model.getPid()).updateChildren(data);
                        }
                    });
                }
            }
        });
    }


    @NonNull
    @Override
    public OrderAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_view,parent,false);
        return new OrderAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView order_pname,restaurant_name,order_des,order_price,order_seat,order_date,order_quantity,order_payment,delivery_status;
        Switch deliveryStatus;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurant_name = (TextView)itemView.findViewById(R.id.restaurant_name_tv);
            order_pname = (TextView)itemView.findViewById(R.id.order_item_tv);
//            order_des = (TextView)itemView.findViewById(R.id.delivery_status_tv);
            order_price = (TextView)itemView.findViewById(R.id.total_amount_tv);
            order_seat = (TextView)itemView.findViewById(R.id.order_no_tv);
            order_date = (TextView)itemView.findViewById(R.id.order_date_tv);
            order_quantity = (TextView)itemView.findViewById(R.id.order_quantity);
//            order_payment = (TextView)itemView.findViewById(R.id.order_payment);
            delivery_status = (TextView)itemView.findViewById(R.id.delivery_status_tv);
            deliveryStatus = (Switch) itemView.findViewById(R.id.switchButton);

            Boolean status = deliveryStatus.isChecked();



        }
    }
}