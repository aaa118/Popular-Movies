package com.demo.adi.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.demo.adi.model.MovieInfo;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM MovieInfo ORDER BY popularity DESC LIMIT 20")
    List<MovieInfo> getPopularMoviesLimitTo20();

    @Query("SELECT * FROM MovieInfo ORDER BY voteAverage DESC LIMIT 20")
    List<MovieInfo> getTopRatedFromDB();

    @Query("SELECT * FROM MovieInfo")
    List<MovieInfo> getAllStoredMovieInfo();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(MovieInfo movies);

    @Update
    void updateMovies(MovieInfo movies);

    @Delete
    void deleteMovies(MovieInfo movies);


}
