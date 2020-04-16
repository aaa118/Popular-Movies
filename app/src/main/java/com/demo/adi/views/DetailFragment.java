package com.demo.adi.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.adi.R;
import com.demo.adi.model.MovieInfo;
import com.squareup.picasso.Picasso;

public class DetailFragment extends Fragment {

    private MovieInfo singleMovie;
//
//    public DetailFragment(MovieInfo singleMovie) {
//        this.singleMovie = singleMovie;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            singleMovie = bundle.getParcelable("movie");
        }
        ImageView imageView = view.findViewById(R.id.imageView);

        String baseURL = "https://image.tmdb.org/t/p/";
        String imageSize = "w185/";
        String fullPath = baseURL + imageSize + singleMovie.getPosterPath();
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_launcher_background).into(imageView);

        TextView textViewTitle = view.findViewById(R.id.tv_title);
        textViewTitle.setText(singleMovie.getTitle());
        TextView textViewReleaseDate = view.findViewById(R.id.tv_release_date);
        textViewReleaseDate.setText(singleMovie.getReleaseDate());
        TextView textViewVoteAvg = view.findViewById(R.id.tv_vote_average);
        textViewVoteAvg.setText(singleMovie.getVoteAverage().toString());
        TextView textViewPlot = view.findViewById(R.id.tv_plot);
        textViewPlot.setText(singleMovie.getOverview());
    }
}
