package com.yash.assignment2_yash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by yash on 26/09/16.
 */

public class YVMapMarkersListAdapter extends ArrayAdapter {

    public YVMapMarkersListAdapter(Context context, int layoutResId, ArrayList<MarkerOptions> arrMarkers) {
        super(context, layoutResId, arrMarkers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        MarkerOptions marker = (MarkerOptions) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.map_marker_layout, parent, false);
        }

        // Lookup view for data population
        TextView txtVwTitle = (TextView) convertView.findViewById(R.id.marker_title);
        TextView txtVwSnippet = (TextView) convertView.findViewById(R.id.marker_snippet);

        // Populate the data into the template view using the data object
        txtVwTitle.setText(marker.getTitle());
        txtVwSnippet.setText(marker.getSnippet());

        // Return the completed view to render on screen
        return convertView;
    }
}
