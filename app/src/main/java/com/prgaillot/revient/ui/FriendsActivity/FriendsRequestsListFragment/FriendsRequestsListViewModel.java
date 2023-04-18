package com.prgaillot.revient.ui.FriendsActivity.FriendsRequestsListFragment;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.prgaillot.revient.domain.models.FriendRequest;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.domain.usecases.DeleteFriendRequestUseCase;
import com.prgaillot.revient.domain.usecases.GetCurrentUserDataUseCase;
import com.prgaillot.revient.domain.usecases.GetUserByIdUserCase;
import com.prgaillot.revient.domain.usecases.GetUserFriendsRequestsSend;
import com.prgaillot.revient.domain.usecases.ValidateFriendRequestUseCase;
import com.prgaillot.revient.ui.uiModels.FriendRequestItemUiModel;
import com.prgaillot.revient.utils.Callback;

import java.nio.file.Watchable;
import java.util.ArrayList;
import java.util.List;


public class FriendsRequestsListViewModel extends ViewModel {
    private static final String TAG = "FriendsRequestsListViewModel";
    private final GetCurrentUserDataUseCase getCurrentUserDataUseCase = GetCurrentUserDataUseCase.instance;
    private final GetUserFriendsRequestsSend getUserFriendsRequestsSend = GetUserFriendsRequestsSend.instance;
    private final GetUserByIdUserCase getUserByIdUserCase = GetUserByIdUserCase.instance;
    private final GetCurrentUserDataUseCase getCurrentUserUseCase = GetCurrentUserDataUseCase.instance;

    private  final ValidateFriendRequestUseCase validateFriendRequestUseCase = ValidateFriendRequestUseCase.instance;

    DeleteFriendRequestUseCase deleteFriendRequestUseCase = DeleteFriendRequestUseCase.instance;

    public static List<FriendRequest> friendRequests;

    public void getUserFriendsRequestsUi(Callback<List<FriendRequestItemUiModel>> callback) {
        getCurrentUserDataUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User user) {
                getUserFriendsRequestsSend.getUserFriendsRequestsSend(user.getUid(), new Callback<List<FriendRequest>>() {
                    @Override
                    public void onCallback(List<FriendRequest> friendRequestList) {

                        List<FriendRequestItemUiModel> friendRequestItemUiModelList = new ArrayList<>();

                        for (FriendRequest friendRequest : friendRequestList) {

                            getUserByIdUserCase.getUserById(friendRequest.getUserReceivedId(), new Callback<User>() {
                                @Override
                                public void onCallback(User result) {
                                    FriendRequestItemUiModel frUi = new FriendRequestItemUiModel(friendRequest.getUserSendId(), friendRequest.getUserReceivedId(), friendRequest.getRequestTimestamp(), result.getImgUrl(), result.getDisplayName());
                                    frUi.setId(friendRequest.getId());
                                    friendRequestItemUiModelList.add(frUi);
                                    if (friendRequestList.size() == friendRequestItemUiModelList.size()) callback.onCallback(friendRequestItemUiModelList);
                                }
                            });

                        }

                    }
                });
            }
        });
    }



    public void getCurrentUser(Callback<User> callback){
        getCurrentUserUseCase.getCurrentUserData(new Callback<User>() {
            @Override
            public void onCallback(User result) {
                callback.onCallback(result);
            }
        });
    }

    public void deleteFriendRequest(String friendRequestId,Callback<Void> callback) {
        deleteFriendRequestUseCase.deleteFriendRequest(friendRequestId, callback);
    }

    public void validateFriendRequest(FriendRequest friendRequest, Callback<Void> callback) {
        validateFriendRequestUseCase.validateFriendRequest(friendRequest, callback);
    }
}
