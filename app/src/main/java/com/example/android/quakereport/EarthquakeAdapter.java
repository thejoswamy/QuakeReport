package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
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

public class EarthquakeAdapter extends ArrayAdapter<Earthquake>{

    private static final String LOCATION_SEPARATOR = " of";

    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }
        Earthquake earthquake = getItem(position);


        TextView magnitudeTv = (TextView) convertView.findViewById(R.id.magnitude);
        DecimalFormat formatter = new DecimalFormat("0.0");
        magnitudeTv.setText(formatter.format(earthquake.getMagnitude()));
        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTv.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(earthquake.getMagnitude());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


        String locationOffset, primaryLocation, location = earthquake.getPlace();
        if (location.contains(LOCATION_SEPARATOR)) {
            String[] parts = location.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }
        TextView offsetTv = (TextView) convertView.findViewById(R.id.location_offset);
        TextView primaryTv = (TextView) convertView.findViewById(R.id.primary_location);
        offsetTv.setText(locationOffset);
        primaryTv.setText(primaryLocation);


        Date date = new Date(earthquake.getTime());
        TextView dateTv = (TextView) convertView.findViewById(R.id.date);
        dateTv.setText(formatDate(date));
        TextView timeTv = (TextView) convertView.findViewById(R.id.time);
        timeTv.setText(formatTime(date));

        return convertView;
    }

    private int getMagnitudeColor(double magnitude) {
        int decimalMagnitude = (int) magnitude;
        int colorResId = -1;
        switch (decimalMagnitude) {
            case 0:
            case 1:
                colorResId = R.color.magnitude1;
                break;
            case 2:
                colorResId = R.color.magnitude2;
                break;
            case 3:
                colorResId = R.color.magnitude3;
                break;
            case 4:
                colorResId = R.color.magnitude4;
                break;
            case 5:
                colorResId = R.color.magnitude5;
                break;
            case 6:
                colorResId = R.color.magnitude6;
                break;
            case 7:
                colorResId = R.color.magnitude7;
                break;
            case 8:
                colorResId = R.color.magnitude8;
                break;
            case 9:
                colorResId = R.color.magnitude9;
                break;
            default:
                colorResId = R.color.magnitude10plus;
        }

        return ContextCompat.getColor(getContext(), colorResId);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
