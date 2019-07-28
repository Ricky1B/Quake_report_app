package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private String mUrl;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        mUrl =url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(mUrl == null){
            return null;
        }
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }


}
