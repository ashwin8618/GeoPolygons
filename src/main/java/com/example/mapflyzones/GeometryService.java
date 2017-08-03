package com.example.mapflyzones;

import com.vividsolutions.jts.geom.Coordinate;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.builders.ShapeBuilders;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.common.geo.ShapeRelation;
import org.elasticsearch.common.geo.builders.ShapeBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import static org.elasticsearch.index.query.QueryBuilders.geoShapeQuery;

/**
 * Created by svc on 1/8/17.
 */
public class GeometryService {

    private Client client;

    public GeometryService()
    {
        try {
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.86.74"), 9300));
        }
        catch(Exception ex){}
    }
    public List<String> findIntersections(Polyline polyline)
    {
        List<String> list = new ArrayList<String>();
        try {

            SearchResponse response;

            List<Coordinate> points = new ArrayList<>();
            for(LatLon ltln : polyline.getLines())
            {
                points.add(new Coordinate(ltln.getLon(), ltln.getLat()));
            }

            QueryBuilder qb = geoShapeQuery(
                    "location",
                    ShapeBuilders.newLineString(points))
                    .relation(ShapeRelation.INTERSECTS);


            response = client.prepareSearch("hyderabad")
        .setTypes("polygons")
        .setQuery(qb)
        .execute().actionGet();

            ArrayList<Polygon> polygons = new ArrayList<>();
            for (SearchHit sh : response.getHits()) {
                list.add(sh.getId());
            }

        }
        catch(Exception ex)
        {

        }

        return list;
    }
}
