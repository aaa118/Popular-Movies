package com.demo.adi.views;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.adi.R;
import com.demo.adi.adapters.MyAdapter;
import com.demo.adi.model.MovieInfo;

import java.util.ArrayList;

public class GridFragment extends Fragment {
    private static final String MOVIE_INFO_LIST = "movieInfoList";
    private ArrayList<MovieInfo> movieInfoList;
    private static final String TAG = "AA_";
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);
        return mView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG, "onSaveInstanceState: Fragment " + movieInfoList);
        outState.putParcelableArrayList(MOVIE_INFO_LIST, movieInfoList);
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieInfoList = bundle.getParcelableArrayList("key");
        }
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        Log.i(TAG, "onActivityCreated: "+movieInfoList);
//        super.onActivityCreated(savedInstanceState);
//        if (savedInstanceState != null) {
//            movieInfoList = savedInstanceState.getParcelableArrayList(MOVIE_INFO_LIST);
//        }
//        displayRecyclerView();
//    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            movieInfoList = savedInstanceState.getParcelableArrayList(MOVIE_INFO_LIST);
        }
        Log.i(TAG, "onViewStateRestored: " + movieInfoList);
        displayRecyclerView();
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        displayRecyclerView();
//    }

    private void displayRecyclerView() {
        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        recyclerView = mView.findViewById(R.id.rl_movies_list);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getContext(), calculateNoOfColumns());
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieInfoList = bundle.getParcelableArrayList("key");
        }
        mAdapter = new MyAdapter(movieInfoList, getFragmentManager());
        recyclerView.setAdapter(mAdapter);
    }

    private int calculateNoOfColumns() {
        if (getContext() != null) {
            DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
            float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
            return (int) (dpWidth / 160);
        }
        // default value
        return 2;
    }
}
