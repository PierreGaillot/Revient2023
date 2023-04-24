package com.prgaillot.revient.ui.CollectionFragment.CollectionFragmentContent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prgaillot.revient.R;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class CollectionContentFragment extends Fragment {

    RecyclerView recyclerView;
    CollectionContentFragmentAdapter adapter;
    private List<StuffItemUiModel> itemUiModelsList;
    private final String ARG_STUFF_UI_MODEL_LIST = "stuffUiModelListKey";
    private final String TAG = "CollectionContentFragment";

    public CollectionContentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection_content, container, false);
        recyclerView = view.findViewById(R.id.collectionFragmentContent_recyclerView);
        initCollectionList();

        if (getArguments() != null){
            Bundle itemListBundle = getArguments();
            Log.d(TAG, itemListBundle.getSerializable(ARG_STUFF_UI_MODEL_LIST).toString());

            itemUiModelsList = (List<StuffItemUiModel>) itemListBundle.getSerializable(ARG_STUFF_UI_MODEL_LIST);
            adapter.update(itemUiModelsList);
        }

        return view;
    }

    private void initCollectionList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new CollectionContentFragmentAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
    }
}