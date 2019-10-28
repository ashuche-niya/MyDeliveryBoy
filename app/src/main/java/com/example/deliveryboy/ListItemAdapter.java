package com.example.deliveryboy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.MyViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    private Bundle bundle;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public Button btnPickup, btnCheckpoint, btnCustomer, btnCancel, btnConfirm;

        public MyViewHolder(View view) {
            super(view);

            btnPickup = (Button) view.findViewById(R.id.btnPickUp);
            btnCheckpoint = (Button) view.findViewById(R.id.btnCheckpoint);
            btnCustomer = (Button) view.findViewById(R.id.btnCustomer);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);


        }

    }

    public ListItemAdapter(List<ListItem> listItems, Context context, Bundle bundle){
        this.listItems = listItems;
        this.context = context;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_task_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final ListItem listItem = listItems.get(position);

        holder.btnPickup.setText(listItem.getPickUpAddr());
        holder.btnPickup.setEnabled(false);
        holder.btnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Pickup.class);
                intent.putExtra("position", String.valueOf(position));
                context.startActivity(intent);
            }
        });

        holder.btnCheckpoint.setText(listItem.getCheckpointAddr());
        String ss=null;
        if(bundle != null){
            ss = bundle.getString("position");
        }

        holder.btnCheckpoint.setEnabled(true);

//        if(ss != null){
//            if(String.valueOf(position).equals(ss)){
//                if(bundle.getString("pickedUp").equals("true")){
//                    holder.btnCheckpoint.setEnabled(true);
//                    notifyItemChanged(position);
//                }else{
//                    Toast.makeText(context, "hfkd", Toast.LENGTH_SHORT).show();
//                    holder.btnCheckpoint.setEnabled(false);
//                    notifyItemChanged(position);
//                }
//            }else{
//                holder.btnCheckpoint.setEnabled(false);
//                notifyItemChanged(position);
//            }
//        }else {
//            holder.btnCheckpoint.setEnabled(false);
//        }

        holder.btnCheckpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Checkpoint.class);
                context.startActivity(intent);
                Toast.makeText(context, listItem.getCheckpointAddr(), Toast.LENGTH_LONG).show();
            }
        });

        holder.btnCustomer.setText(listItem.getCustomerAddr());
        holder.btnCustomer.setEnabled(false);
        holder.btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, listItem.getCustomerAddr(), Toast.LENGTH_LONG).show();
            }
        });

        holder.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Confirmed", Toast.LENGTH_LONG).show();
                holder.btnPickup.setEnabled(true);
            }
        });

        holder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show();
                listItems.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public void updateItem(int position){
        ListItem listItem = listItems.get(position);
    }

}
