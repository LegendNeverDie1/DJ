package com.example.myapplication.util;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class EndlessScrollListener extends RecyclerView.OnScrollListener {
    private final LinearLayoutManager layoutManager;
    private final int visibleThreshold;
    private boolean loading = false;


    public EndlessScrollListener(LinearLayoutManager layoutManager, int visibleThreshold) {
        this.layoutManager = layoutManager;
        this.visibleThreshold = visibleThreshold;
    }


    public void setLoading(boolean loading) { this.loading = loading; }


    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy <= 0 || loading) return;


        int totalItemCount = layoutManager.getItemCount();
        int lastVisible = layoutManager.findLastVisibleItemPosition();
        if (totalItemCount <= lastVisible + visibleThreshold) {
            loading = true;
            onLoadMore();
        }
    }


    public abstract void onLoadMore();
}