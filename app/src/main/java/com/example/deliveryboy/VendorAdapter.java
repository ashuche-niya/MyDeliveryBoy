package com.example.deliveryboy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {

    private List<Vendor> vendorlist;
    private Context context;
    private OnStateChangeListener onStateChangeListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView vname;
        public CheckBox isChecked;

        public ViewHolder(View view) {
            super(view);

            vname = (TextView) view.findViewById(R.id.vendorName);
            isChecked = (CheckBox) view.findViewById(R.id.partiCheck);

        }

    }

    public interface OnStateChangeListener{
        void newStateChanged(int position);
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener){
        this.onStateChangeListener = onStateChangeListener;
    }

    public VendorAdapter(List<Vendor> vendorlist, Context context){
        this.vendorlist = vendorlist;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Vendor vendor = vendorlist.get(position);

        holder.vname.setText(vendor.getVname());

        if (holder.isChecked.isChecked()){
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Nanana", Toast.LENGTH_SHORT).show();
        }

        holder.isChecked.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(onStateChangeListener != null){
                    onStateChangeListener.newStateChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorlist.size();
    }
}
