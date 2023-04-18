package com.prgaillot.revient.domain.usecases.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {


    public static String USER_COLLECTION = "user";
    private static String TAG = "UserRepository";
    private static volatile UserRepository instance;

    private static MutableLiveData<User> _currentUser;
    private static LiveData<User> currentUser = _currentUser;

    private final StuffRepository stuffRepository = StuffRepository.getInstance();

    private final FriendRequestRepository friendRequestRepository = FriendRequestRepository.getInstance();

    FirebaseFirestore db = FirebaseFirestore.getInstance();


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

    public void createUser(User user, Callback<Void> callback) {

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
                        callback.onCallback(unused);
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
        db.collection(USER_COLLECTION).document(userUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(task.getResult().exists());
                } else {
                    callback.onCallback(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "User " + userUid + " doesn't exist.");

                Log.e(TAG, e.getLocalizedMessage());
                callback.onCallback(false);
            }
        });
    }

    public void getCurrentUserData(Callback<User> callback) {
        // TODO Add current user MutableLiveData
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            db.collection(USER_COLLECTION).document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot result = task.getResult();
                        callback.onCallback(userDocSnapshotToUser(result));
//                        _currentUser.postValue(userDocSnapshotToUser(result));
                    } else {
                        Log.e(TAG, "exception : " + task.getException());
                    }
                }
            });
        } else {
            callback.onCallback(currentUser.getValue());
        }


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
                if (result.getFriendsUid() != null) {
                    getUserFriendsByIdList(result.getFriendsUid(), callback);
                } else {
                    callback.onCallback(new ArrayList<>());
                }
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

        User user = new User(result.getId(), result.getString("displayName"), result.getString("imgUrl"), result.getString("email"));
        if (result.contains("friendsUid") || result.getString("friendsUid") != null) {
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
                    if (stuff.getBorrowerId() != null) {
                        loanedStuffList.add(stuff);
                    }
                }
                callback.onCallback(loanedStuffList);
            }
        });
    }

    public void checkIfUserIsFriend(String friendId, Callback<Boolean> callback) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        getUser(currentUser.getUid(), new Callback<User>() {
            @Override
            public void onCallback(User result) {
                if (result.getFriendsUid().contains(friendId)) {
                    callback.onCallback(true);
                } else {
                    callback.onCallback(false);
                }
            }
        });
    }

    public void researchUsersByString(String newText, Callback<List<User>> callback) {
        db.collection(USER_COLLECTION)
                .whereGreaterThanOrEqualTo("displayName", newText)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<User> usersList = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                User user = new User(snapshot.getId(), snapshot.getString("displayName"), snapshot.getString("imgUrl"), snapshot.getString("email"));
                                usersList.add(user);
                            }
                            if (queryDocumentSnapshots.size() == usersList.size()) {
                                callback.onCallback(usersList);
                            }
                        }
                    }
                });
    }

    public void sendFriendRequest(String userSendRequest, String userReceivedRequest, Callback<Void> callback) {
        long timestamp = System.currentTimeMillis();
        FriendRequest friendRequest = new FriendRequest(userSendRequest, userReceivedRequest, timestamp);
        friendRequestRepository.createFriendRequest(friendRequest, callback);
    }

    public void validateFriendRequest(FriendRequest friendRequest, Callback<Void> callback) {
        addNewFriend(friendRequest.getUserReceivedId(), friendRequest.getUserSendId(), new Callback<Void>() {
            @Override
            public void onCallback(Void result) {
                addNewFriend(friendRequest.getUserSendId(), friendRequest.getUserReceivedId(), new Callback<Void>() {
                    @Override
                    public void onCallback(Void result) {
                        friendRequestRepository.deleteRequest(friendRequest.getId(), new Callback<Void>() {
                            @Override
                            public void onCallback(Void result) {
                                callback.onCallback(result);
                            }
                        });
                    }
                });
            }
        });
    }

    public void addNewFriend(String userReceivedId, String userSendId, Callback<Void> callback) {
        db.collection(USER_COLLECTION)
                .document(userReceivedId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> userFriends = new ArrayList<>();
                        User user = documentSnapshot.toObject(User.class);
                        if (user.getFriendsUid() != null) {
                            userFriends = user.getFriendsUid();
                        }
                        userFriends.add(userSendId);

                        db.collection(USER_COLLECTION)
                                .document(userReceivedId)
                                .set(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callback.onCallback(unused);
                                    }
                                });
                    }
                });
    }

    public void removeFriend(String friendId, Callback<Void> callback) {

        getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User userResult) {
                User user = userResult;
                if (user.getFriendsUid() != null && user.getFriendsUid().contains(friendId)) {
                    List<String> friendsIdList = user.getFriendsUid();
                    friendsIdList.remove(friendId);
                    user.setFriendsUid(friendsIdList);
                    db.collection(USER_COLLECTION)
                            .document(userResult.getUid())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    getUser(friendId, new Callback<User>() {
                                        @Override
                                        public void onCallback(User result) {
                                            if (result.getFriendsUid() != null && result.getFriendsUid().contains(friendId)){
                                                List<String> userFriendsUid = result.getFriendsUid();
                                                userFriendsUid.remove(result.getUid());
                                                result.setFriendsUid(userFriendsUid);
                                                db.collection(USER_COLLECTION)
                                                        .document(friendId)
                                                        .set(result)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                callback.onCallback(unused);
                                                            }
                                                        });
                                            }


                                        }
                                    });

                                }
                            });


                }
            }
        });

    }

}


