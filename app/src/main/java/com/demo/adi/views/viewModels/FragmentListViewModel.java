package com.demo.adi.views.viewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;

import java.util.List;

public class FragmentListViewModel extends ViewModel {
    private LiveData<List<MovieInfo>> movieListLiveData;
    private LiveData<List<MovieInfo>> topRatedMoviesListLiveData;
    private LiveData<List<MovieInfo>> mostPopularMoviesListLiveData;
    private static final String TAG = "AA_";

    public FragmentListViewModel(Context context) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(context);
        movieListLiveData = movieDatabase.moviesDao().getAllFavMovieList();
        mostPopularMoviesListLiveData = movieDatabase.moviesDao().getPopularMoviesLimitTo20();
        topRatedMoviesListLiveData = movieDatabase.moviesDao().getTopRatedFromDB();
    }

    public LiveData<List<MovieInfo>> getFavMovieList() {
        return movieListLiveData;
    }

    public LiveData<List<MovieInfo>> getTopRatedMoviesListLiveData() {
        return topRatedMoviesListLiveData;
    }

    public LiveData<List<MovieInfo>> getMostPopularMoviesListLiveData() {
        return mostPopularMoviesListLiveData;
    }
}
