package com.demo.adi.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.adi.R;
import com.demo.adi.model.MovieInfo;
import com.demo.adi.model.VideoQuery;
import com.demo.adi.network.RetroFitInstance;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailFragment extends Fragment {

    private MovieInfo singleMovie;

    private static final String TAG = "AA_";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            singleMovie = bundle.getParcelable("movie");
        }
        Button button = view.findViewById(R.id.bt_trailer);
        button.setOnClickListener(v -> getKeyAndYouTubeLink());
        ImageView imageView = view.findViewById(R.id.iv_poster);
        String baseURL = "https://image.tmdb.org/t/p/";
        String imageSize = "w185/";
        String fullPath = baseURL + imageSize + singleMovie.getPosterPath();
        Picasso.get().load(fullPath).placeholder(R.drawable.ic_launcher_background).into(imageView);

        TextView textViewTitle = view.findViewById(R.id.tv_title);
        textViewTitle.setText(singleMovie.getTitle());
        TextView textViewReleaseDate = view.findViewById(R.id.tv_release_date);
        textViewReleaseDate.setText(singleMovie.getReleaseDate());
        TextView textViewVoteAvg = view.findViewById(R.id.tv_vote_average);
        String text = singleMovie.getVoteAverage().toString();
        textViewVoteAvg.setText(text);
        TextView textViewPlot = view.findViewById(R.id.tv_plot);
        textViewPlot.setText(singleMovie.getOverview());
    }

    private void getKeyAndYouTubeLink() {
        RetroFitInstance.getRetrofitService().getVideoForMovie(singleMovie.getId()).enqueue(new Callback<VideoQuery>() {
            @Override
            public void onResponse(Call<VideoQuery> call, Response<VideoQuery> response) {
                Log.i(TAG, "onResponse: "+response.body().toString());
                //LOAD Response
//                https://www.youtube.com/watch?v=0lzccY1-PNs
//                https://www.youtube.com/watch?v=<key>
                watchYoutubeVideo(response.body().getResults().get(0).getKey());

            }

            @Override
            public void onFailure(Call<VideoQuery> call, Throwable t) {

            }
        });
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}
