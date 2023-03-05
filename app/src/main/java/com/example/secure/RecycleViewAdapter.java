package com.example.secure;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {

    List<WebsiteModel> websiteModels;
    Context context;

    public RecycleViewAdapter(List<WebsiteModel> websiteModels, Context context) {
        this.websiteModels = websiteModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_credential,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recordName.setText(websiteModels.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return websiteModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recordName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            recordName = itemView.findViewById(R.id.recordNameLayout);
        }
    }
}
