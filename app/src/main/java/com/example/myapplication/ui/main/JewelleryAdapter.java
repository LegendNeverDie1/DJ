package com.example.myapplication.ui.main;


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
import com.example.myapplication.R;
import com.example.myapplication.model.Jewellery;
import com.example.myapplication.ui.detail.JewelleryDetailActivity;
import java.util.ArrayList;
import java.util.List;


public class JewelleryAdapter extends RecyclerView.Adapter<JewelleryAdapter.VH> {
    private final Context context;
    private final List<Jewellery> items = new ArrayList<>();


    public JewelleryAdapter(Context context) { this.context = context; }


    public void addPage(List<Jewellery> page) {
        int start = items.size();
        items.addAll(page);
        notifyItemRangeInserted(start, page.size());
    }


    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_jewellery, parent, false);
        return new VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        Jewellery j = items.get(position);
        h.name.setText(j.getName());
        Glide.with(h.itemView).load(j.getImage()).into(h.thumb);
        h.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, JewelleryDetailActivity.class);
            i.putExtra("jewelleryId", j.getId());
            context.startActivity(i);
        });
    }


    @Override public int getItemCount() { return items.size(); }


    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb; TextView name;
        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.jewelleryImageView);
            name = itemView.findViewById(R.id.jewelleryNameTextView);
        }
    }
}