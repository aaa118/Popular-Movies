package com.demo.adi.network;

import com.demo.adi.BuildConfig;
import com.demo.adi.model.MoviesList;
import com.demo.adi.model.RequestToken;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    String API_KEY = BuildConfig.API_KEY;

    @GET("3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getMoviesList();

    @GET("3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getTopRated();
}