package com.demo.adi.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.adi.R;
import com.demo.adi.databinding.FragmentDetailBinding;
import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;
import com.demo.adi.model.ReviewList;
import com.demo.adi.model.VideoQuery;
import com.demo.adi.network.RetroFitInstance;
import com.demo.adi.views.viewModels.FragmentListViewModel;
import com.demo.adi.views.viewModels.FragmentListViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {
    private MovieInfo singleMovie;
    private FragmentDetailBinding fragmentDetailBinding;
    private static final String TAG = "AA_";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentDetailBinding = FragmentDetailBinding.inflate(getLayoutInflater(), container, false);
        return fragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            singleMovie = bundle.getParcelable("movie");
        }
        if (singleMovie != null && singleMovie.isFavorite()) {
            showfavriteChecked();
        }
        FragmentListViewModelFactory factory = FragmentListViewModelFactory.getInstance(getContext());
        FragmentListViewModel fragmentListViewModel = ViewModelProviders.of(this, factory).get(FragmentListViewModel.class);
        fragmentDetailBinding.btTrailer.setOnClickListener(v -> getKeyAndYouTubeLink());
        Log.i(TAG, "onViewCreated: " + singleMovie.getId());
        MovieDatabase movieDatabase = MovieDatabase.getInstance(getContext());
        fragmentDetailBinding.btReview.setOnClickListener(v -> openReview());
        fragmentDetailBinding.ivFavorite.setOnClickListener(v -> fragmentListViewModel.getFavMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieInfo>>() {
            @Override
            public void onChanged(List<MovieInfo> movieInfoList) {
                fragmentListViewModel.getFavMovieList().removeObserver(this);
                checkFavoritesIcon(movieDatabase);
            }
        }));
        fragmentDetailBinding.ivFavoriteChecked.setOnClickListener(v -> fragmentListViewModel.getFavMovieList().observe(getViewLifecycleOwner(), new Observer<List<MovieInfo>>() {
            @Override
            public void onChanged(List<MovieInfo> movieInfoList) {
                fragmentListViewModel.getFavMovieList().removeObserver(this);
                checkFavoritesIcon(movieDatabase);
            }
        }));
        String baseURL = "https://image.tmdb.org/t/p/";
        String imageSize = "w185/";
        String fullPath = baseURL + imageSize + singleMovie.getPosterPath();
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_launcher_background).into(fragmentDetailBinding.ivPoster);

        fragmentDetailBinding.tvTitle.setText(singleMovie.getTitle());
        fragmentDetailBinding.tvReleaseDate.setText(singleMovie.getReleaseDate());
        String text = singleMovie.getVoteAverage().toString();
        fragmentDetailBinding.tvVoteAverage.setText(text);
        fragmentDetailBinding.tvPlot.setText(singleMovie.getOverview());
    }

    private void checkFavoritesIcon(MovieDatabase movieDatabase) {
        if (!singleMovie.isFavorite()) {
            singleMovie.setFavorite(true);
            showfavriteChecked();
            AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(singleMovie));
        } else {
            singleMovie.setFavorite(false);
            fragmentDetailBinding.ivFavorite.setVisibility(View.VISIBLE);
            fragmentDetailBinding.ivFavoriteChecked.setVisibility(View.GONE);
            AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(singleMovie));
        }
    }

    private void showfavriteChecked() {
        fragmentDetailBinding.ivFavorite.setVisibility(View.GONE);
        fragmentDetailBinding.ivFavoriteChecked.setVisibility(View.VISIBLE);
    }

    private void getKeyAndYouTubeLink() {
        Log.i(TAG, "getKeyAndYouTubeLink: " + singleMovie.getId());
        RetroFitInstance.getRetrofitService().getVideoForMovie(singleMovie.getId()).enqueue(new Callback<VideoQuery>() {
            @Override
            public void onResponse(Call<VideoQuery> call, Response<VideoQuery> response) {
                if (response.body() != null) {
                    watchYoutubeVideo(response.body().getResults().get(0).getKey());
                }
            }

            @Override
            public void onFailure(Call<VideoQuery> call, Throwable t) {

            }
        });

    }

    private void openReview() {
        RetroFitInstance.getRetrofitService().getReviewForMovie(singleMovie.getId()).enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if (response.body() != null) {
                    String url = response.body().getResults().get(0).getUrl();
                    Log.i(TAG, "onResponse: " + url);

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {

            }
        });
    }

    private void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
