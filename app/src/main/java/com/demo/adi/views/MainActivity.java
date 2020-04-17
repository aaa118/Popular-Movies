package com.demo.adi.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.demo.adi.R;
import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;
import com.demo.adi.model.MoviesList;
import com.demo.adi.network.RetroFitInstance;
import com.demo.adi.repo.MoviesRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AA_";
    MovieDatabase movieDatabase;
    MoviesRepository moviesRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        moviesRepository = new MoviesRepository(movieDatabase);

        HandlerThread handlerThread = new HandlerThread("BGThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(this::doNetWorkStuff);
        moviesRepository.getMostPopularMoviesFromDB();
        moviesRepository.getTopRatedMoviesFromDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meni_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            getData();
        } else {
            getMostPopular();
        }

        return super.onOptionsItemSelected(item);
    }

    public void doNetWorkStuff() {
        RetroFitInstance retroFitInstance = new RetroFitInstance();
        retroFitInstance.getRetrofitService().getMoviesList().enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.body() != null) {
                    List<MovieInfo> moviesList = response.body().getResults();
                    Log.i(TAG, "onResponse: " + moviesList.size());

                    for (MovieInfo movieInfo : moviesList) {
                        MovieInfo movieInfoToSaveToDB = new MovieInfo(movieInfo.getId(), movieInfo.getPopularity(),
                                movieInfo.getPosterPath(), movieInfo.getOriginalTitle(), movieInfo.getTitle(),
                                movieInfo.getVoteAverage(), movieInfo.getOverview(), movieInfo.getReleaseDate());

                        AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(movieInfoToSaveToDB));
                        AsyncTask.execute(() -> getData());
                    }
                } else {
                    Log.i(TAG, "Null");
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.i(TAG + "Fail", call.toString());
            }
        });

        retroFitInstance.getRetrofitService().getTopRated().enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.body() != null) {
                    Log.i(TAG + "Top Rated", response.body().toString());
                    List<MovieInfo> moviesList = response.body().getResults();
                    Log.i(TAG, "onResponse: " + moviesList.size());
                    for (MovieInfo movieInfo : moviesList) {
                        MovieInfo movieInfoToSaveToDB = new MovieInfo(movieInfo.getId(), movieInfo.getPopularity(),
                                movieInfo.getPosterPath(), movieInfo.getOriginalTitle(), movieInfo.getTitle(),
                                movieInfo.getVoteAverage(), movieInfo.getOverview(), movieInfo.getReleaseDate());

                        AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(movieInfoToSaveToDB));
                    }
                } else {
                    Log.i(TAG, "Null");
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

            }
        });
    }


    private void getData() {
        new Handler(Looper.getMainLooper()).post(() -> {
            GridFragment gridFragment = new GridFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", moviesRepository.getMostPopularMoviesFromDB());
            Log.i(TAG, "run: "+moviesRepository.getMostPopularMoviesFromDB());
            gridFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commitAllowingStateLoss();
        });

    }

//
//    /**
//     * Go back to previous activity
//     * This will work only when using getSupportFragmentManger to start the fragments
//     */
//    @Override
//    public synchronized void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
//            super.onBackPressed();
//        }
//    }

    private void getMostPopular() {
            GridFragment gridFragment = new GridFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("key", moviesRepository.getTopRatedMoviesFromDB());
            gridFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commitAllowingStateLoss();
    }
}
