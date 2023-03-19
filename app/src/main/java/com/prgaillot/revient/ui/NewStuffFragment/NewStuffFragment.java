package com.prgaillot.revient.ui.NewStuffFragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.prgaillot.revient.R;
import com.prgaillot.revient.domain.models.Stuff;
import com.prgaillot.revient.domain.models.User;
import com.prgaillot.revient.utils.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewStuffFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewStuffFragment extends Fragment {

    private static final String ARG_OWNER_ID = "ownerId";

    private String ownerIdParam;

    AutoCompleteTextView borrowerACTextView;
    EditText displayNameEditText;
    Button submitBtn;
    private NewStuffFragmentViewModel viewModel;
    private HashMap<String, String> friendsHashMap;

    public NewStuffFragment() {
    }

    public static NewStuffFragment newInstance(String ownerIdParam, String ownerFriendsParam) {
        NewStuffFragment fragment = new NewStuffFragment();
        Bundle args = new Bundle();
        args.putString(ARG_OWNER_ID, ownerIdParam);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewStuffFragmentViewModel.class);

        if (getArguments() != null) {
            ownerIdParam = getArguments().getString(ARG_OWNER_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_stuff, container, false);
        displayNameEditText = view.findViewById(R.id.newStuff_editTextText_displayName);
        submitBtn = view.findViewById(R.id.newStuff_btn_submit);
        borrowerACTextView = view.findViewById(R.id.newStuff_borrower_AutoCompleteTV);


        viewModel.getUserFriend(ownerIdParam, new Callback<List<User>>() {
            @Override
            public void onCallback(List<User> result) {
                viewModel.prepareFriendsSearchList(result, new Callback<HashMap<String, String>>() {
                    @Override
                    public void onCallback(HashMap<String, String> result) {
                        friendsHashMap = result;
                        List<String> friendNameList = new ArrayList<>(result.keySet());
                        ArrayAdapter<String> friendListAdapter = new ArrayAdapter<>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, friendNameList);
                        Log.d(TAG, friendNameList.toString());
                        borrowerACTextView.setAdapter(friendListAdapter);
                    }
                });
            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (displayNameEditText.getText() != null) {
                    Stuff stuff = new Stuff(ownerIdParam, displayNameEditText.getText().toString());
                    if(!borrowerACTextView.getText().toString().isEmpty() && !friendsHashMap.isEmpty()){
                        stuff.setBorrowerId(friendsHashMap.get(borrowerACTextView.getText().toString()));
                    }
                    viewModel.createStuff(stuff, new Callback<Void>() {
                        @Override
                        public void onCallback(Void result) {
                            NavHostFragment navHostFragment = (NavHostFragment) getParentFragment().getParentFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
                            NavController navController = navHostFragment.getNavController();
                            navController.navigate(R.id.action_newStuffFragment_to_HomeFragment);
                            Toast.makeText(getContext(), stuff.getDisplayName() + "has been added", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        return view;
    }
}