package com.prgaillot.revient.domain.usecases.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.utils.Callback;

public class StuffRepository {
    private static final String TAG = "StuffRepository";
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


    public void createStuff(Stuff stuff, Callback<String> success) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(STUFF_COLLECTION)
                .add(stuff)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        stuff.setUid(documentReference.getId());
                        long currentTimestamp = System.currentTimeMillis();
                        if (stuff.getBorrowerId() != null) {
                            stuff.setInitialLoanDateTimestamp(currentTimestamp);
                        }

                        stuff.setCreationTimeStamp(currentTimestamp);
                        db.collection(STUFF_COLLECTION)
                                .document(documentReference.getId())
                                .set(stuff)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        success.onCallback(documentReference.getId());
                                    }
                                });
                    }
                });
    }

    public void addDelay(String stuffId, long delay, Callback<Void> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(STUFF_COLLECTION)
                .document(stuffId)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    long newDelay = delay;

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        long currentAddDelay = 0;
                        if (documentSnapshot.getLong("additionalDelay") != null) {
                            currentAddDelay += documentSnapshot.getLong("additionalDelay");
                        }

                        if (currentAddDelay > 0) {
                            newDelay += currentAddDelay;
                            Log.d(TAG, "currentAddDelay : " + currentAddDelay);
                            Log.d(TAG, "newDelay : " + newDelay);
                        }
                        db.collection(STUFF_COLLECTION)
                                .document(stuffId)
                                .update("additionalDelay", newDelay)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callback.onCallback(unused);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }

    public void delete(String stuffId, Callback<Void> callback){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(STUFF_COLLECTION)
                .document(stuffId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onCallback(unused);
                    }
                });
    }
}
