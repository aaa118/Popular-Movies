package com.demo.adi.repo;

import androidx.lifecycle.LiveData;

import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;

import java.util.List;

public class MoviesRepository {
    private MovieDatabase movieDatabase;

    public MoviesRepository(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    public LiveData<List<MovieInfo>> getTopRatedMoviesFromDB() {
        return  movieDatabase.moviesDao().getTopRatedFromDB();
    }

    public LiveData<List<MovieInfo>> getMostPopularMoviesFromDB() {
        return  movieDatabase.moviesDao().getPopularMoviesLimitTo20();
    }

    public LiveData<List<MovieInfo>> getAllMovieInfoFromDb() {
        return movieDatabase.moviesDao().getAllFavMovieList();
    }
}
