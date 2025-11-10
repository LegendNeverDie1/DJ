package com.example.myapplication.data;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.*;

public class FirestoreRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    // Jewellery list — ordered by name (or change to createdAt if you add it)
    public Task<QuerySnapshot> getJewelleryPage(int pageSize, DocumentSnapshot lastDoc) {
        Query q = db.collection("Jewellery").orderBy("name").limit(pageSize);
        if (lastDoc != null) q = q.startAfter(lastDoc);
        return q.get();
    }


    // Photos under one jewellery — order by document ID to avoid needing an index/field
    public Task<QuerySnapshot> getPhotosPage(String jewelleryId, int pageSize, DocumentSnapshot lastDoc) {
        Query q = db.collection("Jewellery").document(jewelleryId)
                .collection("photos")
                .orderBy(FieldPath.documentId())
                .limit(pageSize);
        if (lastDoc != null) q = q.startAfter(lastDoc);
        return q.get();
    }
}