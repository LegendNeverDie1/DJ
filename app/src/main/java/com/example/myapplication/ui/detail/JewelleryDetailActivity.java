package com.example.myapplication.ui.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.FirestoreRepository;
import com.example.myapplication.model.Photo;
import com.example.myapplication.util.EndlessScrollListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class JewelleryDetailActivity extends AppCompatActivity {
    private static final String TAG = "JewelleryDetailAct";
    private static final int PAGE_SIZE = 30;
    private RecyclerView rv;
    private PhotosAdapter adapter;
    private final FirestoreRepository repo = new FirestoreRepository();
    private String jewelleryId;
    private DocumentSnapshot lastDoc;
    private boolean reachedEnd = false;
    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewellery_detail);


        jewelleryId = getIntent().getStringExtra("jewelleryId");
        if (jewelleryId == null) { finish(); return; }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String jewelleryName = getIntent().getStringExtra("jewelleryName");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(jewelleryName != null ? jewelleryName : "Photos");            // 🔹 title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);    // 🔹 back arrow
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        rv = findViewById(R.id.photosRecyclerView);
        GridLayoutManager gm = new GridLayoutManager(this, 2);
        rv.setLayoutManager(gm);
        adapter = new PhotosAdapter(this, jewelleryId, jewelleryName);
        rv.setAdapter(adapter);


        scrollListener = new EndlessScrollListener(new androidx.recyclerview.widget.LinearLayoutManager(this), 6) {
            @Override public void onLoadMore() { loadNextPage(); }
        };
// For GridLayoutManager we still attach the listener; it relies on findLastVisible via LM passed above.
        rv.addOnScrollListener(scrollListener);


        loadNextPage();
    }


    private void loadNextPage() {
        if (reachedEnd) return;
        scrollListener.setLoading(true);
        repo.getPhotosPage(jewelleryId, PAGE_SIZE, lastDoc)
                .addOnSuccessListener(this::onPhotosPage)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error loading photos page", e);
                    scrollListener.setLoading(false);
                });
    }


    private void onPhotosPage(QuerySnapshot qs) {
        List<Photo> page = new ArrayList<>();
        for (QueryDocumentSnapshot d : qs) {
            page.add(new Photo(d.getId(), d.getString("url"), d.getString("name")));
        }
        if (page.isEmpty()) {
            reachedEnd = true;
        } else {
            adapter.addPage(page);
            lastDoc = qs.getDocuments().get(qs.size() - 1);
        }
        scrollListener.setLoading(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}