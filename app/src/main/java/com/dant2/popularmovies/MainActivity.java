package com.dant2.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieRecyclerAdapter.ListItemClickListener{
    private RecyclerView moviesView;
    private MovieRecyclerAdapter movieAdapter;
    private ArrayList<Movie> listOfMovies = new ArrayList<Movie>();
    private Boolean popularSort = true;
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Setup the RecyclerView */
        moviesView = (RecyclerView) findViewById(R.id.rv_list_movies);
        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);
        moviesView.setLayoutManager(layoutManager);
        movieAdapter = new MovieRecyclerAdapter(listOfMovies.size(),listOfMovies, this);
        moviesView.setAdapter(movieAdapter);
        //TODO: Give the user an indicator that they are offline instead of just giving a blank page.
        /*Download the list of Movies using loadMoviesFromWebTask*/
        if(isOnline()) {
            loadMoviesFromWebTask loadMovies = new loadMoviesFromWebTask();
            loadMovies.execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sort:
                switchSort();
                return true;
            default:
                return true;
        }

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Movie movieItem = listOfMovies.get(clickedItemIndex);
        Intent showDetails = new Intent(MainActivity.this, DetailActivity.class);
        showDetails.putExtra("MOVIE_NAME", movieItem.getName());
        showDetails.putExtra("MOVIE_SUMMARY",movieItem.getPlotSummary() );
        showDetails.putExtra("MOVIE_IMAGE", movieItem.getPoster());
        showDetails.putExtra("MOVIE_RATING", movieItem.getRating());
        showDetails.putExtra("MOVIE_RELEASE_DATE", movieItem.getReleaseDate());
        startActivity(showDetails);
    }
    private void switchSort(){
        if(popularSort){
            popularSort = false;
        }
        else if(!popularSort){
            popularSort = true;
        }
        if(isOnline()) {

            loadMoviesFromWebTask loadMovies = new loadMoviesFromWebTask();
            loadMovies.execute();
        }
    }

    private class loadMoviesFromWebTask extends AsyncTask<Boolean, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(Boolean... bools) {
            Log.v("doInBackground", "Attempting to download movies");
            String movies = Utilities.downloadMoviesAsJson(popularSort);
            Log.v("doInBackground", "Attempting to parse movies from JSON");
            ArrayList<Movie> arrayOfMovies = Utilities.parseMoviesFromJSON(movies);
            Log.v("doInBackground","Parsed Movies from Json");
            return arrayOfMovies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            /* Store the new ArrayList of movies in the global movies variable and then create a new movies adapter using it */
            listOfMovies = movies;
            String stringOfMovies = "";
            for (Movie m:movies) {
                stringOfMovies += "Name: " + m.getName() + " Release: " + m.getReleaseDate() + " Summary: " + m.getPlotSummary() + " Rating: " + m.getRating()+ "\n\n";
            }
            Log.v("List of Movies", stringOfMovies);
            Log.d("Size of Movie arrayList", Integer.toString(listOfMovies.size()));
            //TODO: Figure out if there is a different way to do this.
            movieAdapter = new MovieRecyclerAdapter(listOfMovies.size(),listOfMovies, MainActivity.this);
            moviesView.setAdapter(movieAdapter);
        }
    }

}
