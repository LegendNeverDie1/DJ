package com.example.myapplication.ui.viewer;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.example.myapplication.R;
import com.example.myapplication.data.FirestoreRepository;
import com.example.myapplication.model.Photo;
import com.example.myapplication.PhotosPagerAdapter; // reuse your existing adapter
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;


public class GridItemActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PhotosPagerAdapter adapter;
    private final List<com.google.firebase.firestore.DocumentSnapshot> photosList = new ArrayList<>();
    private final FirestoreRepository repo = new FirestoreRepository();
    private String jewelleryId;
    private String jewelleryName;
    private String photoId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);


        jewelleryId = getIntent().getStringExtra("jewelleryId");
        photoId = getIntent().getStringExtra("photoId");
        if (jewelleryId == null || photoId == null) { finish(); return; }

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        jewelleryName = getIntent().getStringExtra("jewelleryName");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(jewelleryName != null ? jewelleryName : "Photos");         // 🔹 title
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 🔹 back
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        viewPager = findViewById(R.id.viewPager);
        adapter = new PhotosPagerAdapter(this, photosList);
        viewPager.setAdapter(adapter);


// For simplicity here we still load all photos once; you can adapt pagination similarly if needed.
        repo.getPhotosPage(jewelleryId, 200, null)
                .addOnSuccessListener(qs -> {
                    for (QueryDocumentSnapshot d : qs) photosList.add(d);
                    adapter.notifyDataSetChanged();
                    setInitialPhotoPosition();
                })
                .addOnFailureListener(e -> Log.w("GridItemActivity", "Error", e));
    }


    private void setInitialPhotoPosition() {
        for (int i = 0; i < photosList.size(); i++) {
            if (photosList.get(i).getId().equals(photoId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
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