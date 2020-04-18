package com.demo.adi.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.adi.R;
import com.demo.adi.model.MovieInfo;
import com.demo.adi.views.DetailFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MovieInfo> movieInfoList;
    private FragmentManager context;

    public MyAdapter(List<MovieInfo> movieInfoList, FragmentManager context) {
        this.movieInfoList = movieInfoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        // set the view's size, margins, padding and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        MovieInfo singleMovie = movieInfoList.get(position);
        final String name = singleMovie.getTitle();
        holder.textViewTitle.setText(name);
        holder.itemView.setOnClickListener(v -> openDetailFragment(singleMovie));

        String baseURL = "https://image.tmdb.org/t/p/";
        String imageSize = "w185/";
        String fullPath = baseURL + imageSize + singleMovie.getPosterPath();
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_launcher_background).into(holder.ivPoster);

    }

    private void openDetailFragment(MovieInfo singleMovie) {
        DetailFragment detailFragment = new DetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", singleMovie);
        detailFragment.setArguments(bundle);
        context.beginTransaction().replace(R.id.your_placeholder, detailFragment).addToBackStack("Test").commit();
    }


    @Override
    public int getItemCount() {
        return movieInfoList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView ivPoster;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_movie_name);
            ivPoster = itemView.findViewById(R.id.iv_image);

        }
    }
}
