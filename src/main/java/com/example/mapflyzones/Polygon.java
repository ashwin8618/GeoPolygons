package com.example.mapflyzones;

/**
 * Created by svc on 1/8/17.
 */
public class Polygon {


    private String id;
    private LatLon[] edges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LatLon[] getEdges() {
        return edges;
    }

    public void setEdges(LatLon[] edges) {
        this.edges = edges;
    }
}
