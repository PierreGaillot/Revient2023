package com.prgaillot.revient.domain.usecases.repository;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestRepository {

    final String TAG = "FriendRequestRepository";
    public final String FRIEND_REQUEST_COLLECTION = "friendsRequest";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static volatile FriendRequestRepository instance;

    public static FriendRequestRepository getInstance() {
        FriendRequestRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (FriendRequestRepository.class) {
            if (instance == null) {
                instance = new FriendRequestRepository();
            }
            return instance;
        }
    }

    public void createFriendRequest(FriendRequest friendRequest, Callback<Void> callback) {
        db.collection(FRIEND_REQUEST_COLLECTION)
                .add(friendRequest)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        friendRequest.setId(documentReference.getId());
                        db.collection(FRIEND_REQUEST_COLLECTION)
                                .document(friendRequest.getId())
                                .set(friendRequest)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        callback.onCallback(unused);
                                    }
                                });
                    }
                });
    }

    public void getFriendRequestSend(String userId, Callback<List<FriendRequest>> callback) {
        db.collection(FRIEND_REQUEST_COLLECTION)
                .whereEqualTo("userSendId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<FriendRequest> frList = queryDocumentSnapshots.toObjects(FriendRequest.class);
                            callback.onCallback(frList);
                        }

                    }
                });
    }


    public void getFriendsRequests(String userId, Callback<List<FriendRequest>> callback){
        getFriendRequestReceived(userId, new Callback<List<FriendRequest>>() {
            private final List<FriendRequest> friendsRequestList = new ArrayList<>();

            @Override
            public void onCallback(List<FriendRequest> friendRequestList) {
                if(friendRequestList != null) friendsRequestList.addAll(friendRequestList);
                getFriendRequestSend(userId, new Callback<List<FriendRequest>>() {
                    @Override
                    public void onCallback(List<FriendRequest> result) {
                        if(result != null)  friendsRequestList.addAll(result);
                        callback.onCallback(friendsRequestList);;
                    }
                });
            }
        });
    }

    public void getFriendRequestReceived(String userId, Callback<List<FriendRequest>> callback) {
        db.collection(FRIEND_REQUEST_COLLECTION)
                .whereEqualTo("userReceivedId", userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null) {
                            List<FriendRequest> frList = queryDocumentSnapshots.toObjects(FriendRequest.class);
                            callback.onCallback(frList);
                        }

                    }
                });
    }


    public void deleteRequest(String friendRequestId, Callback<Void> callback){
        db.collection(FRIEND_REQUEST_COLLECTION)
                .document(friendRequestId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onCallback(unused);
                    }
                });
    }


}
