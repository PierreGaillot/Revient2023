package com.prgaillot.revient.ui.HomeFragment.HorStuffListFragment;

import static java.lang.String.*;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prgaillot.revient.R;
import com.prgaillot.revient.ui.HomeFragment.CollectionAdapter;
import com.prgaillot.revient.ui.HomeFragment.StuffItemAdapterClickListener;
import com.prgaillot.revient.ui.MainActivity.MainActivity;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorStuffListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class HorStuffListFragment extends Fragment {

    private static final String TAG = "HorStuffListFrag";
    private static final String LIST_NAME_ARG_PARAM = "listName";
    private static final String ARG_PARAM2 = "collection";
    CollectionAdapter collectionAdapter;

    RecyclerView listRecyclerView;
    TextView listNameTextView;

    private List<StuffItemUiModel> itemUiModels;
    private String listName;

    public static HorStuffListFragment newInstance(List<StuffItemUiModel> itemUiModels, String listName) {
        HorStuffListFragment fragment = new HorStuffListFragment();
        Bundle args = new Bundle();
        args.putString(LIST_NAME_ARG_PARAM, listName);
        args.putSerializable(ARG_PARAM2, (Serializable) itemUiModels);
        fragment.setArguments(args);
        return fragment;
    }

    public HorStuffListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hor_stuff_list, container, false);
        listNameTextView = view.findViewById(R.id.HorStuffList_name_textView);
        listRecyclerView = view.findViewById(R.id.HorStuffList_RecyclerView);
        initCollectionList();

        if (getArguments() != null) {
            listName = getArguments().getString(LIST_NAME_ARG_PARAM);
            itemUiModels = (List<StuffItemUiModel>) getArguments().getSerializable(ARG_PARAM2);
            listNameTextView.setText(format("%s (%d)", listName, itemUiModels.size()));
            collectionAdapter.update(itemUiModels);
        }


        return view;
    }

    private void initCollectionList() {
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        collectionAdapter = new CollectionAdapter(new ArrayList<>(), new StuffItemAdapterClickListener() {
            @Override
            public void onItemClick( StuffItemUiModel stuffItem) {
                ((MainActivity) getActivity()).openStuffDetailsFragment(stuffItem);            }
        });
        listRecyclerView.setAdapter(collectionAdapter);
    }
}