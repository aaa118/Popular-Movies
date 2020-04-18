package com.demo.adi.views;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.PersistableBundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AA_";
    private static final String VIEW_ID = "viewId";
    MovieDatabase movieDatabase;
    MoviesRepository moviesRepository;
    private int viewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        moviesRepository = new MoviesRepository(movieDatabase);

        Handler handler = getHandler();
        handler.post(this::doNetWorkStuff);
    }

    /**
     * Used for Background Task
     */
    private Handler getHandler() {
        HandlerThread handlerThread = new HandlerThread("BGThread");
        handlerThread.start();
        return new Handler(handlerThread.getLooper());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.meni_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        viewId = item.getItemId();
        Log.i(TAG, "onOptionsItemSelected: "+viewId);
        getFragmentView();
        return super.onOptionsItemSelected(item);
    }

    private void getFragmentView() {
        if (viewId == R.id.sort_popular || viewId == 0) {
            getMostPopular();
        } else {
            getTopRated();
        }
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
//        Log.i(TAG, "onSaveInstanceState: "+viewId);
//        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putInt(VIEW_ID, viewId);
//    }

//    @Override
//    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        viewId = savedInstanceState.getInt(VIEW_ID);
//        Log.i(TAG, "onRestoreInstanceState: "+viewId);
//    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        getFragmentView();
    }

    public void doNetWorkStuff() {
        RetroFitInstance retroFitInstance = new RetroFitInstance();
        retroFitInstance.getRetrofitService().getMoviesList().enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.body() != null) {
                    List<MovieInfo> moviesList = response.body().getResults();
                    for (MovieInfo movieInfo : moviesList) {
                        MovieInfo movieInfoToSaveToDB = new MovieInfo(movieInfo.getId(), movieInfo.getPopularity(),
                                movieInfo.getPosterPath(), movieInfo.getOriginalTitle(), movieInfo.getTitle(),
                                movieInfo.getVoteAverage(), movieInfo.getOverview(), movieInfo.getReleaseDate());

                        AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(movieInfoToSaveToDB));
//                        getMostPopular();
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
                    List<MovieInfo> moviesList = response.body().getResults();
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

    private void getMostPopular() {
        Handler handler = getHandler();
        handler.post(() -> {
            final ArrayList<MovieInfo> listMovies = moviesRepository.getMostPopularMoviesFromDB();
            runOnUiThread(() -> {
                GridFragment gridFragment = new GridFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", listMovies);
                gridFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
            });
        });
    }

    private void getTopRated() {
        Handler handler = getHandler();
        handler.post(() -> {
            final ArrayList<MovieInfo> listMovies = moviesRepository.getTopRatedMoviesFromDB();
            runOnUiThread(() -> {
                GridFragment gridFragment = new GridFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("key", listMovies);
                gridFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
            });
        });
    }
}
