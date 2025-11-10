package com.example.myapplication.ui.main;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.data.FirestoreRepository;
import com.example.myapplication.model.Jewellery;
import com.example.myapplication.util.EndlessScrollListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PAGE_SIZE = 20;


    private RecyclerView rv;
    private JewelleryAdapter adapter;
    private LinearLayoutManager lm;
    private final FirestoreRepository repo = new FirestoreRepository();
    private DocumentSnapshot lastDoc; // for pagination
    private boolean reachedEnd = false;
    private EndlessScrollListener scrollListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rv = findViewById(R.id.jewelleryRecyclerView);
        lm = new LinearLayoutManager(this);
        rv.setLayoutManager(lm);
        adapter = new JewelleryAdapter(this);
        rv.setAdapter(adapter);


        scrollListener = new EndlessScrollListener(lm, 4) {
            @Override public void onLoadMore() { loadNextPage(); }
        };
        rv.addOnScrollListener(scrollListener);


        loadNextPage();
    }


    private void loadNextPage() {
        if (reachedEnd) return;
        scrollListener.setLoading(true);
        repo.getJewelleryPage(PAGE_SIZE, lastDoc)
                .addOnSuccessListener(this::onJewelleryPage)
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error loading page", e);
                    scrollListener.setLoading(false);
                });
    }


    private void onJewelleryPage(QuerySnapshot qs) {
        List<Jewellery> page = new ArrayList<>();
        for (QueryDocumentSnapshot d : qs) {
            String id = d.getId();
            String name = d.getString("name");
            String image = d.getString("image");
            page.add(new Jewellery(id, name, image));
        }
        if (page.isEmpty()) {
            reachedEnd = true;
        } else {
            adapter.addPage(page);
            lastDoc = qs.getDocuments().get(qs.size() - 1);
        }
        scrollListener.setLoading(false);
    }
}