package com.example.adminapp.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.adminapp.R;
import com.example.adminapp.SeatActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SeatAdapter extends FirebaseRecyclerAdapter<SeatModel, SeatAdapter.myViewHolder> {

    private final SeatActivity seatActivity;
    public String status;
        public String seatNo;
        Context context;
        ProgressDialog loadingBar;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param seatActivity
     * @param options
     */
    public SeatAdapter(SeatActivity seatActivity, @NonNull FirebaseRecyclerOptions<SeatModel> options) {
        super(options);
        this.seatActivity = seatActivity;

    }

    @Override
    protected void onBindViewHolder(@NonNull final SeatAdapter.myViewHolder holder, final int position, @NonNull final SeatModel model) {
       status= model.getAvailability();
        holder.tableStatus.setText(status);

        if(holder.tableStatus.getText().toString().equals("available")) {
            holder.seatAvail.setBackgroundColor(R.drawable.seatcolor);

        }



        holder.seatAvail.setText(model.getTableNo());
        holder.seatAvail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.seatAvail.setBackgroundColor(R.drawable.seat_alloted);
                Log.i("msg",holder.seatAvail.getText().toString());

               seatNo= holder.seatAvail.getText().toString();
               final String currentStatus = holder.tableStatus.getText().toString();

                pleaseWait();
                final DatabaseReference seatPath = FirebaseDatabase.getInstance().getReference().child("Seats").child(SeatActivity.restaurant_name).child(seatNo);
                HashMap<String,Object> seatData = new HashMap<>();
                if(currentStatus.equals("available")){
                    seatData.put("availability","booked");
//                    holder.seatAvail.setBackgroundColor(R.drawable.seat_booked);
                }
                else {
                    seatData.put("availability","available");
                }

                seatPath.updateChildren(seatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(seatActivity, "Seat status updated", Toast.LENGTH_SHORT).show();
                        //    SeatAdapter.this.notifyDataSetChanged();
                            loadingBar.dismiss();
//                            notifyItemChanged((int) holder.getAdapterPosition());
//                            Log.i("id",holder.getItemId() + "  " + holder.getAdapterPosition()+ holder.getLayoutPosition());
                        }

                    }

                });
            }
        });



    }

    private void pleaseWait(){
        loadingBar = new ProgressDialog(seatActivity);
        loadingBar.setTitle("Login Account");
        loadingBar.setMessage("Please Wait...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
    }

    @NonNull
    @Override
    public SeatAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new SeatAdapter.myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        Button seatAvail;
       TextView tableStatus;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            seatAvail = (Button)itemView.findViewById(R.id.seat);
            tableStatus = (TextView)itemView.findViewById(R.id.status);
        }
    }
}
