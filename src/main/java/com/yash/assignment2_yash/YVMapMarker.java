package com.yash.assignment2_yash;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by yash on 26/09/16.
 */

public class YVMapMarker {

    private String title;
    private String snippet;
    private LatLng position;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
}
