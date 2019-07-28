package com.example.android.quakereport;

public class Earthquake {
    private double mMagnitude;
    private String mLocation;
    private String mDate;
    private long mTime;
    private String mUrl;

    public Earthquake(double magnitude, String location, String date, long time, String url){
         mMagnitude = magnitude;
         mLocation = location;
         mDate = date;
         mTime = time;
         mUrl =url;
    }

    public double getMagnitude(){return mMagnitude;}

    public String getLocation(){return mLocation;}

    public String getDate(){ return mDate;}

    public long getTime(){return mTime;}

    public String getUrl(){return mUrl;}
}