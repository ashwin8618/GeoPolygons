package com.example.mapflyzones;

/**
 * Created by svc on 1/8/17.
 */
public class Polyline {

    private String id;
    private LatLon[] lines;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLon[] getLines() {
        return lines;
    }

    public void setLines(LatLon[] lines) {
        this.lines = lines;
    }
}
