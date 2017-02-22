package com.example.android.quakereport;

/**
 * {@link Earthquake} represents an earthquake occured. It consists of
 * magnitude of earthquake, place and time of occurence, link to webpage
 */
public class Earthquake {
    // Magnitude of earthquake
    private double mMagnitude;
    // Place of occurence
    private String mPlace;
    // Time of occurence in milliseconds(Unix time)
    private long mTime;
    // Link to website
    private String mUrl;

    public Earthquake(double magnitude, String place, long time, String url) {
        mMagnitude = magnitude;
        mPlace = place;
        mTime = time;
        mUrl = url;
    }

    /**
     * Returns the magnitude of the earthquake
     */
    public double getMagnitude() {
        return mMagnitude;
    }

    /**
     * Returns the place of occurence of earthquake
     */
    public String getPlace() {
        return mPlace;
    }

    /**
     * Returns the date of occurence of earthquake
     */
    public long getTime() {
        return mTime;
    }

    /**
     * Returns the web url of earthquake
     */
    public String getUrl() {
        return mUrl;
    }
}
