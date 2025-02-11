package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {
    private Context context;
    private List<DocumentSnapshot> photosList;
    private String jewelleryId;

    public PhotosAdapter(Context context, List<DocumentSnapshot> photosList, String jewelleryId) {
        this.context = context;
        this.photosList = photosList;
        this.jewelleryId = jewelleryId;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        DocumentSnapshot document = photosList.get(position);
        String imageUrl = document.getString("url");

        Glide.with(holder.itemView.getContext()).load(imageUrl).into(holder.photoImageView);

        holder.itemView.setOnClickListener( v -> {
            Intent intent = new Intent(context, GridItemActivity.class);
            intent.putExtra("jewelleryId", jewelleryId);
            intent.putExtra("photoId", document.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return photosList.size();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}
