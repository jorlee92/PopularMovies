package com.dant2.popularmovies;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        TextView detailSummary = (TextView) findViewById(R.id.tv_detail_summary);
        TextView releaseText = (TextView) findViewById(R.id.tv_detail_release_date);
        TextView detailRating = (TextView) findViewById(R.id.tv_detail_rating);
        ImageView detailImage = (ImageView) findViewById(R.id.iv_detail_movie_image);
        TextView detailName = (TextView) findViewById(R.id.tv_detail_name);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            detailName.setText(extras.getString("MOVIE_NAME"));
            detailSummary.setText(extras.getString("MOVIE_SUMMARY"));
            detailRating.setText(extras.getString("MOVIE_RATING"));
            releaseText.setText(extras.getString("MOVIE_RELEASE_DATE"));
            Picasso.with(detailImage.getContext()).load("http://image.tmdb.org/t/p/w500/" + extras.getString("MOVIE_IMAGE")).into(detailImage);
        }



    }

}
