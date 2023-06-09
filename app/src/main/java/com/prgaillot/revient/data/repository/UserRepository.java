package com.prgaillot.revient.data.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {


    private static String USER_COLLECTION = "user";
    private static String TAG = "UserRepository";
    private static volatile UserRepository instance;

    private final StuffRepository stuffRepository = StuffRepository.getInstance();

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (UserRepository.class) {
            if (instance == null) {
                instance = new UserRepository();
            }
            return instance;
        }
    }

    public void createUser(User user) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = new HashMap<>();

        userMap.put("uid", user.getUid());
        userMap.put("email", user.getEmail());
        userMap.put("displayName", user.getDisplayName());
        userMap.put("imgUrl", user.getImgUrl());

        db.collection(USER_COLLECTION)
                .document(user.getUid())
                .set(userMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    public void userIsRegistered(String userUid, Callback<Boolean> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION).document(userUid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                callback.onCallback(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
                callback.onCallback(false);
            }
        });
    }

    public void getCurrentUserData(Callback<User> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db.collection(USER_COLLECTION).document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    callback.onCallback(userDocSnapshotToUser(result));
                } else {
                    Log.e(TAG, "exception : " + task.getException());
                }
            }
        });
    }

    public void getUser(String userUid, Callback<User> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_COLLECTION)
                .document(userUid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            callback.onCallback(userDocSnapshotToUser(task.getResult()));
                        } else {
                            Log.e(TAG, "fail :" + task.getException());
                        }
                    }
                });
    }

    public void getUserFriendById(String userId, Callback<List<User>> callback) {
        getUser(userId, new Callback<User>() {
            @Override
            public void onCallback(User result) {
                getUserFriendsByIdList(result.getFriendsUid(), callback);
            }
        });
    }

    public void getUserFriendsByIdList(List<String> friendsUid, Callback<List<User>> callback) {
        List<User> friendsList = new ArrayList<>();

        int i = 0;
        while (i < friendsUid.size()) {
            getUser(friendsUid.get(i), new Callback<User>() {
                @Override
                public void onCallback(User user) {
                    friendsList.add(user);
                    if (friendsList.size() == friendsUid.size()) callback.onCallback(friendsList);
                }
            });
            i++;
        }
    }

    private User userDocSnapshotToUser(DocumentSnapshot result) {
        User user = new User(result.getId(), result.getString("displayName"), Uri.parse(result.getString("imgUrl")), result.getString("email"));
        if (result.contains("friendsUid")) {
            List<String> friendList = (List<String>) result.get("friendsUid");
            user.setFriendsUid(friendList);
        } else {
            user.setFriendsUid(new ArrayList<>());
        }

        return user;
    }

    public void getUserStuffCollection(String userId, Callback<List<Stuff>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(stuffRepository.STUFF_COLLECTION)
                .whereEqualTo("ownerId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Stuff> stuffList = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                stuffList.add(snapshot.toObject(Stuff.class));
                            }
                            callback.onCallback(stuffList);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                });
    }

    public void getUserStuffBorrowedCollection(String userId, Callback<List<Stuff>> callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(stuffRepository.STUFF_COLLECTION)
                .whereEqualTo("borrowerId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Stuff> stuffList = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                stuffList.add(snapshot.toObject(Stuff.class));
                            }
                            callback.onCallback(stuffList);
                        }
                    }
                });
    }

    public void getUserLoanedCollection(String userId, Callback<List<Stuff>> callback) {
        List<Stuff> loanedStuffList = new ArrayList<>();
        getUserStuffCollection(userId, new Callback<List<Stuff>>() {
            @Override
            public void onCallback(List<Stuff> stuffList) {
                for (Stuff stuff : stuffList) {
                    if(stuff.getBorrowerId() != null){
                        loanedStuffList.add(stuff);
                    }
                }
                callback.onCallback(loanedStuffList);
            }
        });
    }
}
