package com.prgaillot.revient.ui.CollectionFragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.prgaillot.revient.R;
import com.prgaillot.revient.ui.CollectionFragment.CollectionFragmentContent.CollectionContentFragment;
import com.prgaillot.revient.ui.uiModels.StuffItemUiModel;

import java.io.Serializable;
import java.util.List;

public class CollectionFragment extends Fragment {

    private static final String COLLECTION_NAME_PARAM = "collectionNameKey";
    private static final String COLLECTION_LIST_PARAM = "collectionListKey";
    private static final String TAG = "CollectionFragment";
    private String collectionName;
    private List<StuffItemUiModel> stuffs;
    TextView collectionTitleTextView;



    public CollectionFragment() {
        // Required empty public constructor
    }

    public static CollectionFragment newInstance(String collectionName, List<StuffItemUiModel> stuffs) {
        CollectionFragment fragment = new CollectionFragment();
        Bundle args = new Bundle();
        args.putString(COLLECTION_NAME_PARAM, collectionName);
        args.putSerializable(COLLECTION_LIST_PARAM, (Serializable) stuffs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            collectionName = getArguments().getString(COLLECTION_NAME_PARAM);
            stuffs = (List<StuffItemUiModel>) getArguments().getSerializable( COLLECTION_LIST_PARAM);

            CollectionContentFragment contentFragment = new CollectionContentFragment();
            Bundle collectionBundle = new Bundle();
            collectionBundle.putSerializable("stuffUiModelListKey", (Serializable) stuffs);
            contentFragment.setArguments(collectionBundle);

            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.collectionFrag_fragmentContent, contentFragment, null).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collection, container, false);

        CollapsingToolbarLayout collToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout);
        collToolbar.setExpandedTitleTypeface(Typeface.create(collToolbar.getExpandedTitleTypeface(), Typeface.BOLD));
        collToolbar.setTitle(collectionName);
        return view;
    }
}