package com.demo.adi.network;

import com.demo.adi.model.MoviesList;
import com.demo.adi.model.ReviewList;
import com.demo.adi.model.VideoQuery;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitService {
    //TODO add API Key
    String API_KEY = "Enter Key Here";
//    String API_KEY = BuildConfig.API_KEY;

    @GET("3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getMoviesList();

    @GET("3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1")
    Call<MoviesList> getTopRated();

    @GET("/3/movie/{movie_id}/videos?api_key=" + API_KEY + "&language=en-US")
    Call<VideoQuery> getVideoForMovie(@Path("movie_id") int movieId);

    @GET("/3/movie/{movie_id}/reviews?api_key=" + API_KEY + "&language=en-US")
    Call<ReviewList> getReviewForMovie(@Path("movie_id") int movieId);
}
