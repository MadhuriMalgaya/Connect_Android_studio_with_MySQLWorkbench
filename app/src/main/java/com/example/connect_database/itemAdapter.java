package com.example.connect_database;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder> {

    private final Context context;
    private final List<userModel> userList;

    public itemAdapter(Context context, List<userModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.retrival_item, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        userModel user = userList.get(position);

        // Decode image from byte[]
        if (user.photo != null && user.photo.length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(user.photo, 0, user.photo.length);
            holder.photo.setImageBitmap(bmp);
        } else {
            // Show a placeholder image if photo is missing
            holder.photo.setImageResource(R.drawable.baseline_account_box_24); // replace with an actual drawable you have
        }


        // Set data
        holder.name.setText("Name: " + user.name);
        holder.age.setText("Age: " + user.age);
        holder.height.setText("Height: " + user.height);
        holder.weight.setText("Weight: " + user.weight);
        holder.bloodGroup.setText("Blood Group: " + user.bloodGroup);
        holder.medicalIssue.setText("Medical Issue: " + user.medicalIssue);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class itemViewHolder extends RecyclerView.ViewHolder {
        ImageView photo;
        TextView name, age, height, weight, bloodGroup, medicalIssue;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            name = itemView.findViewById(R.id.name);
            age = itemView.findViewById(R.id.age);
            height = itemView.findViewById(R.id.height);
            weight = itemView.findViewById(R.id.weight);
            bloodGroup = itemView.findViewById(R.id.bloodGroupSpinner);
            medicalIssue = itemView.findViewById(R.id.medicalIssue);
        }
    }
}
