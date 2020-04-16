package com.demo.adi.views;

import android.os.Bundle;
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

import java.util.List;

public class GridFragment extends Fragment {
    private List<MovieInfo> movieInfoList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager;
        recyclerView = view.findViewById(R.id.rl_movies_list);
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieInfoList = bundle.getParcelableArrayList("key");
        }
        mAdapter = new MyAdapter(movieInfoList, getFragmentManager());
        recyclerView.setAdapter(mAdapter);
    }
}
