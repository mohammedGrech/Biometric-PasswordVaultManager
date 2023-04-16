package com.example.secure;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> implements Filterable {

    List<WebsiteModel> websiteModels;
    List<WebsiteModel> getWebsiteModelsFilter = new ArrayList<>();
    Context context;

    Biometric biometric;
    public BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;

    public RecycleViewAdapter(List<WebsiteModel> websiteModels, Context context) {
        this.websiteModels = websiteModels;
        this.context = context;
        this.getWebsiteModelsFilter = websiteModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_credential,parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // get the website details at the correct position
        final WebsiteModel webModel = websiteModels.get(position);
        // get position of the row
        Integer index = websiteModels.indexOf(webModel);
        // update the UI with the data
        holder.recordName.setText(websiteModels.get(position).getName());

        // Strings to pass in the display logo function
        String logoName = websiteModels.get(position).getWeb_logo();
        String webName = websiteModels.get(position).getName();

        //Display logo
        displayLogo(logoName, webName, holder);

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

                                // Notify user with their deletion
                                Toast.makeText(context, webModel.getName() + " deleted.", Toast.LENGTH_SHORT).show();
                                //If the database has items
                                if(websiteModels.size()!=0){
                                    //remove database at this position
                                    websiteModels.remove(position);
                                    // inform the recycle view with the changes
                                    notifyItemRemoved(position);
                                    // inform the recycle view with the changes based on the range of data
                                    notifyItemRangeChanged(position,websiteModels.size());
                                }
                            }
                        })
                        // Record deletion will not succeed
                        .setNegativeButton("No", null)
                        //Display the alert
                        .show();
            }
        });

        //Update record
        holder.recordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Open update activity and pass data to it
                Intent intent = new Intent(context, UpdateRecord.class);
                intent.putExtra("id", Integer.valueOf(websiteModels.get(index).getId()));
                intent.putExtra("name", String.valueOf(websiteModels.get(index).getName()));
                intent.putExtra("email", String.valueOf(websiteModels.get(index).getusername()));
                intent.putExtra("password", String.valueOf(websiteModels.get(index).getPassword()));
                intent.putExtra("url", String.valueOf(websiteModels.get(index).getUrl()));
                intent.putExtra("note", String.valueOf(websiteModels.get(index).getNote()));
                intent.putExtra("webLogo", String.valueOf(websiteModels.get(index).getWeb_logo()));

                context.startActivity(intent);
            }
        });
    }

    // Set logo based on spinner value
    private void displayLogo(String logoName, String webName, MyViewHolder holder) {

        // Set logo on the row
        switch (logoName) {
            case "Facebook":
                Glide.with(this.context).load(R.drawable.facebook).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "WhatsApp":
                Glide.with(this.context).load(R.drawable.whatsapp).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Twitter":
                Glide.with(this.context).load(R.drawable.twitter).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Pinterest":
                Glide.with(this.context).load(R.drawable.pinterest).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Youtube":
                Glide.with(this.context).load(R.drawable.youtube).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Linkedin":
                Glide.with(this.context).load(R.drawable.linkedin).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Github":
                Glide.with(this.context).load(R.drawable.github).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Gmail":
                Glide.with(this.context).load(R.drawable.gmail).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Instagram":
                Glide.with(this.context).load(R.drawable.instagram).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Outlook":
                Glide.with(this.context).load(R.drawable.outlook).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Netflix":
                Glide.with(this.context).load(R.drawable.netflix).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Viber":
                Glide.with(this.context).load(R.drawable.viber).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Reddit":
                Glide.with(this.context).load(R.drawable.reddit).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Amazon":
                Glide.with(this.context).load(R.drawable.amazon).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Spotify":
                Glide.with(this.context).load(R.drawable.spotify).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            case "Amazon Prime":
                Glide.with(this.context).load(R.drawable.amazon_prime).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
            default:
                Glide.with(this.context).load(R.drawable.account_24).into(holder.recordLogo);
                holder.textLogo.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return websiteModels.size();
    }


    public Filter getSearch() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = getWebsiteModelsFilter;
                    filterResults.count = getWebsiteModelsFilter.size();
                }else{
                    String searchStr = constraint.toString().toLowerCase();
                    List<WebsiteModel> filterWebsiteModels = new ArrayList<>();
                    for (WebsiteModel websiteModel: getWebsiteModelsFilter){
                        if (websiteModel.getWeb_logo().toLowerCase().contains(searchStr)){
                            filterWebsiteModels.add(websiteModel);
                        }
                    }
                    filterResults.values = filterWebsiteModels;
                    filterResults.count = filterWebsiteModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                websiteModels = (List<WebsiteModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null || constraint.length() == 0){
                    filterResults.values = getWebsiteModelsFilter;
                    filterResults.count = getWebsiteModelsFilter.size();
                }else{
                    String searchStr = constraint.toString().toLowerCase();
                    List<WebsiteModel> filterWebsiteModels = new ArrayList<>();
                    for (WebsiteModel websiteModel: getWebsiteModelsFilter){
                        if (websiteModel.getName().toLowerCase().contains(searchStr)){
                            filterWebsiteModels.add(websiteModel);
                        }
                    }
                    filterResults.values = filterWebsiteModels;
                    filterResults.count = filterWebsiteModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                websiteModels = (List<WebsiteModel>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView recordName;
        ImageView recordLogo;
        TextView textLogo;
        ImageButton btn_deleteRecord;
        ConstraintLayout recordLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recordName = itemView.findViewById(R.id.recordNameLayout);
            recordLogo = itemView.findViewById(R.id.logoLayout);
            textLogo = itemView.findViewById(R.id.textLogo);
            btn_deleteRecord = itemView.findViewById((R.id.deleteIcon));
            btn_deleteRecord.findViewById(R.id.deleteIcon);
            recordLayout = itemView.findViewById(R.id.record_layout);
        }
    }
}