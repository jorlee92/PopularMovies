package com.dant2.popularmovies;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Utilities {
    //TODO: Properly handle exceptions
    //API key has been removed before uploading to GitHub
    private final static String API_KEY = "REMOVED";
    private static URL fullMoviesURL = null;
    private static HttpURLConnection connection = null;

    public static String downloadMoviesAsJson(boolean popSorted) {
        String listOfMovies = null;
        try {
            if (popSorted) {
                fullMoviesURL = new URL("https://api.themoviedb.org/3/movie/popular?api_key=" + API_KEY + "&language=en-US&page=1");
            } else {
                fullMoviesURL = new URL("https://api.themoviedb.org/3/movie/top_rated?api_key=" + API_KEY + "&language=en-US&page=1");
            }

        } catch (MalformedURLException e) {

            Log.v("MalformedURLException", e.toString());

        }
        if (fullMoviesURL != null) {

            try {

                connection = (HttpURLConnection) fullMoviesURL.openConnection();

            } catch (IOException e) {

                Log.v("IOException", e.toString());
            }

        }
        if (connection != null) {
            Log.v("Utilities", "Made connection");
            InputStream moviesResponse;
            try {
                moviesResponse = connection.getInputStream();
                listOfMovies = inputStreamToString(moviesResponse);

            } catch (IOException e) {
                Log.v("IOException", e.toString());
            }

        } else {
            Log.v("Utilities", "Failed to open connection");
        }

        return listOfMovies;
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {

        StringBuilder resultString = new StringBuilder();
        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader inputReader = new BufferedReader(inputStreamReader);
            String line = inputReader.readLine();
            while (line != null) {

                resultString.append(line);
                line = inputReader.readLine();

            }


        }
        Log.v("test", resultString.toString());
        return resultString.toString();
    }

    public static ArrayList<Movie> parseMoviesFromJSON(String jsonString) {
        JSONObject fullMovies;
        JSONArray individualMovies;
        ArrayList<Movie> movieArrayList = new ArrayList<Movie>();
        try {
            fullMovies = new JSONObject(jsonString);
            individualMovies = fullMovies.getJSONArray("results");

            for (int i = 0; i < individualMovies.length(); i++) {
                JSONObject currentMovie = individualMovies.getJSONObject(i);
                String name = currentMovie.getString("title");
                String releaseDate = currentMovie.getString("release_date");
                String plotSummary = currentMovie.getString("overview");
                String rating = currentMovie.optString("vote_average", "none");
                String posterURL = currentMovie.getString("poster_path");
                Log.v("parseMoviesFromJSON", posterURL);
                movieArrayList.add(i, new Movie(name, releaseDate, plotSummary, rating, posterURL));
            }
        } catch (JSONException e) {
            Log.v("parseMoviesFromJSON", "failed to create JSON object");
        }

        return movieArrayList;

    }


}