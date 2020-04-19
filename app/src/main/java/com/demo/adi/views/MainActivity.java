package com.demo.adi.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.demo.adi.R;
import com.demo.adi.databinding.ActivityMainBinding;
import com.demo.adi.db.MovieDatabase;
import com.demo.adi.model.MovieInfo;
import com.demo.adi.model.MoviesList;
import com.demo.adi.network.RetroFitInstance;
import com.demo.adi.views.viewModels.FragmentListViewModel;
import com.demo.adi.views.viewModels.FragmentListViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "AA_";
    private static final String IS_SYNCED = "IsSynced";
    MovieDatabase movieDatabase;
    private int viewId;
    private ActivityMainBinding binding;
    List<MovieInfo> topRatedMoviesList;
    List<MovieInfo> mostPopularMoviesList;
    List<MovieInfo> favMoviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupViewModelAndObservers();
        movieDatabase = MovieDatabase.getInstance(getApplicationContext());
        SharedPreferences sharedPref = getSharedPreferences("Network Sync", Context.MODE_PRIVATE);
        boolean isSynced = sharedPref.getBoolean(IS_SYNCED, false);
        if (!isSynced) {
            Log.i(TAG, "onCreate: "+isSynced);
            doNetWorkStuff();
        }
    }

    private void setupViewModelAndObservers() {
        FragmentListViewModelFactory factory =  FragmentListViewModelFactory.getInstance(getApplicationContext());
        FragmentListViewModel fragmentListViewModel = ViewModelProviders.of(this, factory).get(FragmentListViewModel.class);

        fragmentListViewModel.getTopRatedMoviesListLiveData().observe(this, movieInfoList -> {
            Log.i(TAG, "onChanged: Top" + movieInfoList);
            topRatedMoviesList = movieInfoList;
        });
        fragmentListViewModel.getMostPopularMoviesListLiveData().observe(this, new Observer<List<MovieInfo>>() {
            @Override
            public void onChanged(List<MovieInfo> movieInfoList) {
                Log.i(TAG, "onChanged: Pop " + movieInfoList);
                mostPopularMoviesList = movieInfoList;
            }
        });

        fragmentListViewModel.getFavMovieList().observe(this, movieInfoList -> {
            Log.i(TAG, "onChanged: Favs " + movieInfoList);
            favMoviesList = movieInfoList;
        });
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
        Log.i(TAG, "onOptionsItemSelected: " + viewId);
        getFragmentView();
        return super.onOptionsItemSelected(item);
    }

    private void getFragmentView() {
        if (viewId == R.id.sort_popular || viewId == 0) {
            getMostPopular();
//            openFragment((ArrayList<MovieInfo>) mostPopularMoviesList);
        } else if (viewId ==R.id.sort_top_rated){
            getTopRated();
//            openFragment((ArrayList<MovieInfo>) topRatedMoviesList);

        } else {
            getFavs();
//            openFragment((ArrayList<MovieInfo>) favMoviesList);
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void doNetWorkStuff() {
        RetroFitInstance.getRetrofitService().getMoviesList().enqueue(new Callback<MoviesList>() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.body() != null) {
                    List<MovieInfo> moviesList = response.body().getResults();
                    saveNetworkDataToDb(moviesList);
                    SharedPreferences sharedPref = getSharedPreferences("Network Sync", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putBoolean(IS_SYNCED, true);
                    // Added to commit synchronously instead of asynchronously
                    editor.commit();
                    getMostPopular();
//                    openFragment((ArrayList<MovieInfo>) mostPopularMoviesList);
                } else {
                    Log.i(TAG, "Null");
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {
                Log.i(TAG + "Fail", call.toString());
            }
        });

        RetroFitInstance.getRetrofitService().getTopRated().enqueue(new Callback<MoviesList>() {
            @Override
            public void onResponse(Call<MoviesList> call, Response<MoviesList> response) {
                if (response.body() != null) {
                    List<MovieInfo> moviesList = response.body().getResults();
                    saveNetworkDataToDb(moviesList);
                } else {
                    Log.i(TAG, "Null");
                }
            }

            @Override
            public void onFailure(Call<MoviesList> call, Throwable t) {

            }
        });

    }

    private void saveNetworkDataToDb(List<MovieInfo> moviesList) {
        for (MovieInfo movieInfo : moviesList) {
            MovieInfo movieInfoToSaveToDB = new MovieInfo(movieInfo.getId(), movieInfo.getPopularity(),
                    movieInfo.getPosterPath(), movieInfo.getOriginalTitle(), movieInfo.getTitle(),
                    movieInfo.getVoteAverage(), movieInfo.getOverview(), movieInfo.getReleaseDate());

            AsyncTask.execute(() -> movieDatabase.moviesDao().insertMovies(movieInfoToSaveToDB));
        }
    }



    private GridFragment getGridFragment() {
        binding.btShowPopular.setVisibility(View.GONE);
        binding.btShowTopRated.setVisibility(View.GONE);
        return new GridFragment();
    }

        private void getMostPopular() {
        GridFragment gridFragment = getGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", (ArrayList<MovieInfo>) mostPopularMoviesList);
        gridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
    }

    private void getTopRated() {
        GridFragment gridFragment = getGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", (ArrayList<MovieInfo>) topRatedMoviesList);
        gridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
    }

    private void getFavs() {
        GridFragment gridFragment = getGridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key", (ArrayList<MovieInfo>) favMoviesList);
        gridFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
    }

//    private void openFragment(ArrayList<MovieInfo> moviesToDisplay) {
//        GridFragment gridFragment = getGridFragment();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList("key", moviesToDisplay);
//        gridFragment.setArguments(bundle);
//        getSupportFragmentManager().beginTransaction().replace(R.id.your_placeholder, gridFragment).commit();
//    }
}
