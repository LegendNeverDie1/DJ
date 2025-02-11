package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

public class JewelleryAdapter extends RecyclerView.Adapter<JewelleryAdapter.JewelleryViewHolder> {
    private Context context;
    private List<DocumentSnapshot> jewelleryList;

    public JewelleryAdapter(Context context, List<DocumentSnapshot> jewelleryList) {
        this.context = context;
        this.jewelleryList = jewelleryList;
    }

    @NonNull
    @Override
    public JewelleryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jewellery, parent, false);
        return new JewelleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JewelleryViewHolder holder, int position) {
        DocumentSnapshot document = jewelleryList.get(position);
        String name = document.getString("name");
        String imageUrl = document.getString("image");

        holder.jewelleryNameTextView.setText(name);
        Glide.with(context).load(imageUrl).into(holder.jewelleryImageView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, JewelleryDetailActivity.class);
            intent.putExtra("jewelleryId", document.getId());
            if (!document.getId().equals("1Temp")) context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jewelleryList.size();
    }

    public static class JewelleryViewHolder extends RecyclerView.ViewHolder {
        ImageView jewelleryImageView;
        TextView jewelleryNameTextView;

        public JewelleryViewHolder(@NonNull View itemView) {
            super(itemView);
            jewelleryImageView = itemView.findViewById(R.id.jewelleryImageView);
            jewelleryNameTextView = itemView.findViewById(R.id.jewelleryNameTextView);
        }
    }
}