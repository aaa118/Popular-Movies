package com.demo.adi.repo;

import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;
import java.util.ArrayList;

public class MoviesRepository {
    private MovieDatabase movieDatabase;

    public MoviesRepository(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    public ArrayList<MovieInfo> getTopRatedMoviesFromDB() {
        return (ArrayList<MovieInfo>) movieDatabase.moviesDao().getTopRatedFromDB();
    }

    public ArrayList<MovieInfo> getMostPopularMoviesFromDB() {
        return (ArrayList<MovieInfo>) movieDatabase.moviesDao().getPopularMoviesLimitTo20();
    }

    public ArrayList<MovieInfo> getAllMovieInfoFromDb() {
        return (ArrayList<MovieInfo>) movieDatabase.moviesDao().getAllStoredMovieInfo();
    }
}
