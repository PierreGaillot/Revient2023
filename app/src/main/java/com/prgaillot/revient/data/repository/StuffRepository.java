package com.prgaillot.revient.data.repository;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

public class StuffRepository {
    private static volatile StuffRepository instance;
    public final String STUFF_COLLECTION = "stuff";

    public static StuffRepository getInstance() {
        StuffRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (StuffRepository.class) {
            if (instance == null) {
                instance = new StuffRepository();
            }
            return instance;
        }
    }


    public void createStuff(Stuff stuff, Callback<Void> success) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(STUFF_COLLECTION)
                .add(stuff)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        stuff.setUid(documentReference.getId());
                        db.collection(STUFF_COLLECTION)
                                .document(documentReference.getId())
                                .set(stuff)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        success.onCallback(unused);
                                    }
                                });
                    }
                });
    }
}
