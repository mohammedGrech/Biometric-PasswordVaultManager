package com.example.secure;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
        final WebsiteModel webModel = websiteModels.get(position);
        Integer index = websiteModels.indexOf(webModel);
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

        //Delete rows
        holder.btn_deleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Confirmation dialog box to delete a record
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setCancelable(false)
                        .setMessage("Are you sure to delete " + webModel.getName() + "?")
                        //If user press Yes button
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Database database = new Database(context);
                                boolean result = database.deleteRecord(webModel.getId());
                                // If deletion succeed

                                // Notify user with their deletion
                                Toast.makeText(context, webModel.getName() + " deleted.", Toast.LENGTH_SHORT).show();
                                websiteModels.remove(webModel);
                                notifyItemRemoved(index);
                            }
                        })
                        // Record deletion will not succeed
                        .setNegativeButton("No", null)
                        //Display the alert
                        .show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return websiteModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recordName;
        ImageView recordLogo;
        TextView textLogo;
        ImageButton btn_deleteRecord;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.recordNameLayout);
            recordLogo = itemView.findViewById(R.id.logoLayout);
            textLogo = itemView.findViewById(R.id.textLogo);
            btn_deleteRecord = itemView.findViewById((R.id.deleteIcon));
            btn_deleteRecord.findViewById(R.id.deleteIcon);

        }
    }
}
