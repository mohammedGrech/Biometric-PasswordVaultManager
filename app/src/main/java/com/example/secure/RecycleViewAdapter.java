package com.example.secure;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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

    //    @Override


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.recordName.setText(websiteModels.get(position).getName());
        String logoName = websiteModels.get(position).getName();
        if (logoName.toLowerCase().contains("facebook")){
            Glide.with(this.context).load(R.drawable.facebook).into(holder.recordLogo);
            holder.textLogo.setVisibility(View.GONE);
        }
        else {
             char firstLetter = logoName.charAt(0);
             char secondLetter = logoName.charAt(logoName.length()-1);
             String TextCombination = firstLetter +""+ secondLetter;
             holder.textLogo.setText(TextCombination.toUpperCase());
        }


    }

    @Override
    public int getItemCount() {
        return websiteModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recordName;
        ImageView recordLogo;
        TextView textLogo;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.recordNameLayout);
            recordLogo = itemView.findViewById(R.id.logoLayout);
            textLogo = itemView.findViewById(R.id.textLogo);

        }
    }
}
