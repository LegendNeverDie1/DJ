package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class JewelleryDetailActivity extends AppCompatActivity {
    private RecyclerView photosRecyclerView;
    private PhotosAdapter adapter;
    private List<DocumentSnapshot> photosList;
    private FirebaseFirestore db;
    private String jewelleryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jewellery_detail);

        jewelleryId = getIntent().getStringExtra("jewelleryId");
        if (jewelleryId == null) {
            Log.e("JewelleryDetailActivity", "No jewelleryId passed in intent");
            finish();
            return;
        }

        photosRecyclerView = findViewById(R.id.photosRecyclerView);
        photosRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        photosList = new ArrayList<>();
        adapter = new PhotosAdapter(this, photosList, jewelleryId);
        photosRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchPhotos();
    }

    private void fetchPhotos() {
        db.collection("Jewellery").document(jewelleryId)
                .collection("photos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            String photoUrl = document.getString("url");
                            photosList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("JewelleryDetailActivity", "Error getting documents.", task.getException());
                    }
                });
    }
}
