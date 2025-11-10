package com.example.myapplication;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GridItemActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private PhotosPagerAdapter adapter;
    private List<DocumentSnapshot> photosList;
    private FirebaseFirestore db;
    private String jewelleryId;
    private String photoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_item);

        jewelleryId = getIntent().getStringExtra("jewelleryId");
        photoId = getIntent().getStringExtra("photoId");

        if (jewelleryId == null || photoId == null) {
            Log.e("GridItemActivity", "No jewelleryId or photoId passed in intent");
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewPager);

        photosList = new ArrayList<>();
        adapter = new PhotosPagerAdapter(this, photosList);
        viewPager.setAdapter(adapter);

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
                            photosList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                        setInitialPhotoPosition();
                    } else {
                        Log.w("GridItemActivity", "Error getting documents.", task.getException());
                    }
                });
    }
//    private void fetchPhotos() {
//        db.collection("Jewellery").document(jewelleryId)
//                .collection("photos").document(photoId)
//                .get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        photosList.add(task.getResult());
//                        adapter.notifyDataSetChanged();
//                        setInitialPhotoPosition();
//                    } else {
//                        Log.w("GridItemActivity", "Error getting document.", task.getException());
//                    }
//                });
//    }

    private void setInitialPhotoPosition() {
        for (int i = 0; i < photosList.size(); i++) {
            if (photosList.get(i).getId().equals(photoId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
