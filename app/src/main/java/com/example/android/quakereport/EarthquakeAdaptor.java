package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeAdaptor extends ArrayAdapter<Earthquake> {
    private static final String LOCATION_SEPARATOR = " of ";



    public EarthquakeAdaptor(Context context, List<Earthquake> earthquakes) {
        super(context, 0,earthquakes);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Earthquake currentEarthquake = getItem(position);
        View listItemView = convertView;
        //check if the listItem view provided is null, if yes inflate it from scratch
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        double magnitudeFormater = currentEarthquake.getMagnitude();

        TextView magnitude = (TextView)listItemView.findViewById(R.id.magnitude);
        magnitude.setBackgroundResource(R.drawable.magnitude_circle);
        magnitude.setText(formatMagnitude(magnitudeFormater));



        Date dateObject = new Date(currentEarthquake.getTime());

        String originalLocation = currentEarthquake.getLocation();
        String primaryLocation;
        String locationOffset;

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetView =(TextView)listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(locationOffset);

        TextView primaryLocationView = (TextView)listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        TextView date = (TextView)listItemView.findViewById(R.id.date);
        date.setText(currentEarthquake.getDate());

        TextView time =(TextView)listItemView.findViewById(R.id.time);
        time.setText(formatTime(dateObject));


        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);



        return listItemView;
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private String formatMagnitude(double magnitude){
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    private int getMagnitudeColor(double magnitude){
        int magnitudeColorResourceId;
        int magnitudeInt = (int) magnitude;
        switch (magnitudeInt){
            case 0: magnitudeColorResourceId= R.color.magnitude1;
                break;
            case 1: magnitudeColorResourceId= R.color.magnitude1;
                break;
            case 2: magnitudeColorResourceId= R.color.magnitude2;
                break;
            case 3: magnitudeColorResourceId= R.color.magnitude3;
                break;
            case 4: magnitudeColorResourceId= R.color.magnitude4;
                break;
            case 5: magnitudeColorResourceId= R.color.magnitude5;
                break;
            case 6: magnitudeColorResourceId= R.color.magnitude6;
                break;
            case 7: magnitudeColorResourceId= R.color.magnitude7;
                break;
            case 8: magnitudeColorResourceId= R.color.magnitude8;
                break;
            case 9: magnitudeColorResourceId= R.color.magnitude9;
                break;
                default: magnitudeColorResourceId= R.color.magnitude10plus;
                    break;

        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);    }
}
