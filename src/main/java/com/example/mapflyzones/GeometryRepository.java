package com.example.mapflyzones;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by svc on 1/8/17.
 */
public class GeometryRepository {

    private ArrayList<Polygon> _localRep = new ArrayList<>();
    private ArrayList<Polyline> _localLineRep = new ArrayList<>();
    private Client client;

    public GeometryRepository(){
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.86.74"), 9300));
        }
        catch(Exception ex){}
    }
    //lines
    public Polyline[] getLines()
    {
        return _localLineRep.toArray(new Polyline[_localLineRep.size()] );
    }

    public Polygon[] getPolygons()
    {
        SearchResponse response = client.prepareSearch("hyderabad")
                .setTypes("polygons").get();

        ArrayList<Polygon> polygons = new ArrayList<>();
        for(SearchHit sh : response.getHits() )
        {
            Polygon polygon = new Polygon();
            ArrayList<LatLon> latlons = new ArrayList<>();
            String source = sh.getSourceAsString();
            JSONObject jsonObject = new JSONObject(source);
            JSONArray ja = jsonObject.getJSONObject("location").getJSONArray("coordinates").getJSONArray(0);
            for(int i = 0; i < ja.length(); i++)
            {
                JSONArray japoint = ja.getJSONArray(i);
                LatLon ltln = new LatLon();
                ltln.setLon(japoint.getDouble(0));
                ltln.setLat(japoint.getDouble(1));
                latlons.add(ltln);
            }
            polygon.setEdges(latlons.toArray(new LatLon[latlons.size()]));
            polygon.setId(sh.getId());
            polygons.add(polygon);
        }


        return polygons.toArray(new Polygon[polygons.size()] );
        //return _localRep.toArray(new Polygon[_localRep.size()] );

    }

    public void savePolygon(Polygon polygon)
    {
        //_localRep.add(polygon);

        try {
            //prepare the coordinates
            XContentBuilder jb = jsonBuilder()
                    .startObject()
                    .field("name", "First polygon")
                    .field("location")
                    .startObject()
                    .field("type", "polygon")
                    .startArray("coordinates").startArray();
            for(LatLon ltln : polygon.getEdges()) {
                jb.startArray().value(ltln.getLon())
                        .value(ltln.getLat()).endArray();
            }

            //first and last latlon should be same to store in elasticsearch
            LatLon ltln = polygon.getEdges()[0];
            jb.startArray().value(ltln.getLon())
                    .value(ltln.getLat()).endArray();

            jb.endArray().endArray().endObject().endObject();


            IndexResponse response = client.prepareIndex("hyderabad", "polygons")
                    .setSource(jb)
                    .get();
            System.out.println(response.toString());
        }
        catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void savePolyline(Polyline line)
    {
        //_localLineRep.add(line);
    }

    public Polygon findPolygonById(String id)
    {
        return null;
    }

    public Polyline findPolylineById(String id)
    {
        return null;
    }



}
