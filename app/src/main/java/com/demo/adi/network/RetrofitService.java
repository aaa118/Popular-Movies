package com.demo.adi.network;

import com.demo.adi.model.MoviesList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {
    //TODO add API Key
    String API_KEY = "Enter Key Here";
//    String API_KEY = BuildConfig.API_KEY;

    @GET("3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getMoviesList();

    @GET("3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getTopRated();
}
