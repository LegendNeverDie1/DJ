package com.example.myapplication.ui.detail;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Photo;
import com.example.myapplication.ui.viewer.GridItemActivity;
import java.util.ArrayList;
import java.util.List;


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.VH> {
    private final Context context;
    private final List<Photo> items = new ArrayList<>();
    private final String jewelleryId;


    public PhotosAdapter(Context context, String jewelleryId) {
        this.context = context;
        this.jewelleryId = jewelleryId;
    }


    public void addPage(List<Photo> page) {
        int start = items.size();
        items.addAll(page);
        notifyItemRangeInserted(start, page.size());
    }


    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Photo p = items.get(position);
        Glide.with(h.itemView).load(p.getUrl()).into(h.iv);
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, GridItemActivity.class);
            i.putExtra("jewelleryId", jewelleryId);
            i.putExtra("photoId", p.getId());
            context.startActivity(i);
        });
    }


    @Override public int getItemCount() { return items.size(); }


    static class VH extends RecyclerView.ViewHolder {
        ImageView iv;
        VH(@NonNull View itemView) { super(itemView); iv = itemView.findViewById(R.id.photoImageView); }
    }
}