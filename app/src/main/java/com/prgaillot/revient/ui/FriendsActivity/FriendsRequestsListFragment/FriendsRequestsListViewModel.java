package com.prgaillot.revient.ui.FriendsActivity.FriendsRequestsListFragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.DeleteFriendRequestUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsRequest;
import com.prgaillot.revient.domain.usecases.ValidateFriendRequestUseCase;
import com.prgaillot.revient.ui.uiModels.FriendRequestItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.List;


public class FriendsRequestsListViewModel extends ViewModel {
    private static final String TAG = "FriendsRequestsListViewModel";
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
    private final GetUserFriendsRequest getUserFriendsRequest = GetUserFriendsRequest.instance;
    private final GetUserByIdUserCase getUserByIdUserCase = GetUserByIdUserCase.instance;
    private final GetCurrentUserDataUseCase getCurrentUserUseCase = GetCurrentUserDataUseCase.instance;

    private final ValidateFriendRequestUseCase validateFriendRequestUseCase = ValidateFriendRequestUseCase.instance;
    private final MutableLiveData<List<FriendRequest>> _friendsRequests = new MutableLiveData<>();
    public LiveData<List<FriendRequest>> friendsRequests = _friendsRequests;

    DeleteFriendRequestUseCase deleteFriendRequestUseCase = DeleteFriendRequestUseCase.instance;


    public void refreshFriendsRequest(Callback<Void> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                getUserFriendsRequest.getUserFriendsRequest(result.getUid(), new Callback<List<FriendRequest>>() {
                    @Override
                    public void onCallback(List<FriendRequest> result) {
                        _friendsRequests.postValue(result);
                        callback.onCallback(null);
                    }
                });
            }
        });
    }

    public void friendsRequestToUi(List<FriendRequest> friendRequests, Callback<List<FriendRequestItemUiModel>> callback) {
        List<FriendRequestItemUiModel> friendRequestItemUiModelList = new ArrayList<>();
        getCurrentUserUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User user) {
                for (FriendRequest friendRequest : friendRequests) {
                    String userActorId;
                    if (user.getUid().equals(friendRequest.getUserSendId())) {
                        userActorId = friendRequest.getUserReceivedId();
                    } else {
                        userActorId = friendRequest.getUserSendId();
                    }

                    getUserByIdUserCase.getUserById(userActorId, new Callback<User>() {
                        @Override
                        public void onCallback(User result) {
                            String actorDisplayName = result.getDisplayName();
                            String imgActorUrl = result.getImgUrl();

                            FriendRequestItemUiModel frUi = new FriendRequestItemUiModel(friendRequest.getUserSendId(), friendRequest.getUserReceivedId(), friendRequest.getRequestTimestamp(), imgActorUrl, actorDisplayName);
                            frUi.setId(friendRequest.getId());
                            friendRequestItemUiModelList.add(frUi);
                            if(friendRequestItemUiModelList.size() == friendRequests.size()){
                                callback.onCallback(friendRequestItemUiModelList);
                            }
                        }
                    });
                }
            }

        });
    }

    public void getUserFriendsRequestsUi(Callback<List<FriendRequestItemUiModel>> callback) {
        List<FriendRequestItemUiModel> friendRequestItemUiModelList = new ArrayList<>();
        List<FriendRequest> friendRequestList = new ArrayList<>();

        getCurrentUser(new Callback<User>() {
            @Override
            public void onCallback(User user) {

                if (friendsRequests != null) {

                    String actorDisplayName = user.getDisplayName();
                    String imgActorUrl = user.getImgUrl();
                    List<FriendRequestItemUiModel> friendRequestItemUiModels = new ArrayList<>();
                    for (FriendRequest friendRequest : friendsRequests.getValue()) {
                        FriendRequestItemUiModel frUi = new FriendRequestItemUiModel(friendRequest.getUserSendId(), friendRequest.getUserReceivedId(), friendRequest.getRequestTimestamp(), imgActorUrl, actorDisplayName);
                        friendRequestItemUiModels.add(frUi);
                        if (friendRequestList.size() == friendRequestItemUiModelList.size())
                            callback.onCallback(friendRequestItemUiModelList);
                    }

                } else {

                    getUserFriendsRequest.getUserFriendsRequest(user.getUid(), new Callback<List<FriendRequest>>() {
                        @Override
                        public void onCallback(List<FriendRequest> result) {
                            friendRequestList.addAll(result);
                            _friendsRequests.postValue(friendRequestList);
                            for (FriendRequest friendRequest : friendRequestList) {
                                String userActorId;
                                if (user.getUid().equals(friendRequest.getUserSendId())) {
                                    userActorId = friendRequest.getUserReceivedId();
                                } else {
                                    userActorId = friendRequest.getUserSendId();
                                }

                                getUserByIdUserCase.getUserById(userActorId, new Callback<User>() {
                                    @Override
                                    public void onCallback(User result) {
                                        String actorDisplayName = result.getDisplayName();
                                        String imgActorUrl = result.getImgUrl();

                                        FriendRequestItemUiModel frUi = new FriendRequestItemUiModel(friendRequest.getUserSendId(), friendRequest.getUserReceivedId(), friendRequest.getRequestTimestamp(), imgActorUrl, actorDisplayName);
                                        frUi.setId(friendRequest.getId());
                                        friendRequestItemUiModelList.add(frUi);
                                        if (friendRequestList.size() == friendRequestItemUiModelList.size())
                                            callback.onCallback(friendRequestItemUiModelList);
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });


    }


    public void getCurrentUser(Callback<User> callback) {
        getCurrentUserUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                callback.onCallback(result);
            }
        });
    }

    public void deleteFriendRequest(String friendRequestId, Callback<Void> callback) {
        deleteFriendRequestUseCase.deleteFriendRequest(friendRequestId, callback);
    }

    public void validateFriendRequest(FriendRequest friendRequest, Callback<Void> callback) {
        validateFriendRequestUseCase.validateFriendRequest(friendRequest, callback);
    }
}
