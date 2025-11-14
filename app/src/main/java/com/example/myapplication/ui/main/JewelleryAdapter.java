package com.example.myapplication.ui.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.model.Jewellery;
import com.example.myapplication.ui.detail.JewelleryDetailActivity;
import java.util.ArrayList;
import java.util.List;


public class JewelleryAdapter extends RecyclerView.Adapter<JewelleryAdapter.VH> {
    private final Context context;
    private boolean isLiked = false;
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
            i.putExtra("jewelleryName", j.getName());
            context.startActivity(i);
        });

        h.favBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLiked){
                    isLiked = false;
                    h.likeBtn.setBackgroundResource(R.drawable.like_btn);
                    h.likeBtn.setColorFilter(
                            ContextCompat.getColor(context,R.color.gold_color),
                            PorterDuff.Mode.SRC_IN
                    );
                }else{
                    isLiked = true;
                    h.likeBtn.setBackgroundResource(R.drawable.unlike_btn);

                }
            }
        });
    }


    @Override public int getItemCount() { return items.size(); }




    static class VH extends RecyclerView.ViewHolder {
        ImageView thumb,likeBtn; TextView name;
        RelativeLayout favBtnView;
        VH(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.jewelleryImageView);
            name = itemView.findViewById(R.id.jewelleryNameTextView);
            favBtnView = itemView.findViewById(R.id.likeUnlikeBtn);
            likeBtn = itemView.findViewById(R.id.favBtn);


        }
    }
}