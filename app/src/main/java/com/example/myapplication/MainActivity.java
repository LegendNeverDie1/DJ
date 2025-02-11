package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView jewelleryRecyclerView;
    private JewelleryAdapter adapter;
    private List<DocumentSnapshot> jewelleryList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jewelleryRecyclerView = findViewById(R.id.jewelleryRecyclerView);
        jewelleryRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        jewelleryList = new ArrayList<>();
        adapter = new JewelleryAdapter(this, jewelleryList);
        jewelleryRecyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        fetchJewelleryData();
    }

    private void fetchJewelleryData() {
        db.collection("Jewellery")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            jewelleryList.add(document);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w("MainActivity", "Error getting documents.", task.getException());
                    }
                });
    }
}
