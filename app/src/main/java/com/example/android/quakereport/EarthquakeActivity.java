/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

    private static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static final String REQUEST_URL = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    private static final int EARTHQUAKE_LOADER_ID = 1;

    private EarthquakeAdapter mAdapter;
    private TextView mEmptyView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "OnCreate: Acticity is created");
        setContentView(R.layout.earthquake_activity);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        mEmptyView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyView);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        // Create a new {@link ArrayAdapter} of earthquakes
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake currentEarthquake = (Earthquake) parent.getItemAtPosition(position);
                Uri webpage = Uri.parse(currentEarthquake.getUrl());
                Intent newIntent = new Intent(Intent.ACTION_VIEW, webpage);
                if (newIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(newIntent);
                }
            }
        });

        //new EarthquakeAsyncTask().execute(REQUEST_URL);
        if (isNetworkConnected()) {
            getLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyView.setText(R.string.no_internet_connection);
        }
        Log.i(LOG_TAG, "InitLoader: Loader in initialized");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG, "onStart: Activity is started");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG, "onStop: Activity is stopped");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "onDestroy: Activity is destroyed");
    }

    @Override
    public android.content.Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "OnCreateLoader: Loader in created");
        return new EarthquakeLoader(getApplicationContext(), REQUEST_URL);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Earthquake>> loader, List<Earthquake> data) {
        Log.i(LOG_TAG, "OnLoadFinished: called on LoadFinished");
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setText(R.string.no_earthquakes_found);
        mAdapter.clear();
        if (data != null && data.size() > 0) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "OnLoaderReset: Loader is reset");
        mAdapter.clear();
    }

    private static class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {

        String mUrlString = null;
        public EarthquakeLoader(Context context, String url) {
            super(context);
            mUrlString = url;
        }

        @Override
        protected void onStartLoading() {
            Log.i(LOG_TAG, "onStartLoading: called earthquake loader onStartLoading");
            forceLoad();
        }

        @Override
        public List<Earthquake> loadInBackground() {
            Log.i(LOG_TAG, "loadInBackground: called earthquake loader loadInBackground");
            if (TextUtils.isEmpty(mUrlString))  return null;
            return QueryUtils.fetchEarthquakeData(mUrlString);
        }
    }

/*    private class EarthquakeAsyncTask extends AsyncTask<String, Void, List<Earthquake>> {

        @Override
        protected List<Earthquake> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) return null;
            return QueryUtils.fetchEarthquakeData(urls[0]);
        }

        @Override
        protected void onPostExecute(List<Earthquake> earthquakes) {
            mAdapter.clear();

            if (earthquakes != null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
            }
        }
    }*/
}
