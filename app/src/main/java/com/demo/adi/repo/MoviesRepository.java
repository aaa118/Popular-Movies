package com.demo.adi.repo;

import android.os.AsyncTask;

import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;

import java.util.ArrayList;

public class MoviesRepository {

    private MovieDatabase movieDatabase;
    private ArrayList<MovieInfo> movieInfoList;
    private ArrayList<MovieInfo> movieTopRated;

    public MoviesRepository(MovieDatabase movieDatabase) {
        this.movieDatabase = movieDatabase;
    }

    public ArrayList<MovieInfo> getTopRatedMoviesFromDB() {
        AsyncTask.execute(() -> movieTopRated = (ArrayList<MovieInfo>) movieDatabase.moviesDao().getTopRatedFromDB());
        return movieTopRated;
    }

    public ArrayList<MovieInfo> getMostPopularMoviesFromDB() {
        AsyncTask.execute(() -> movieInfoList = (ArrayList<MovieInfo>) movieDatabase.moviesDao().getMoviesInfo());
        return movieInfoList;
    }
}
