package com.example.mapflyzones;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

/**
 * Created by svc on 26/7/17.
 */
@Controller
public class nfzcontroller {

    private GeometryRepository geometryRepository = new GeometryRepository();
    GeometryService geometryService = new GeometryService();
    @RequestMapping("/noflyzones")
    public String home(Model model)
    {
        //Polygon[] polygons = new Polygon[3];
        Polygon polygon = new Polygon();
        LatLon latLon = new LatLon();
        latLon.setLat(17.442535362724712);
        latLon.setLon(78.36882591247559);
        LatLon latLon1 = new LatLon();
        latLon1.setLat(17.429515337732226);
        latLon1.setLon(78.36496353149414);
        LatLon latLon2 = new LatLon();
        latLon2.setLat(17.430825570972353);
        latLon2.setLon(78.3845329284668);
        LatLon[] latlonarr = {latLon, latLon1, latLon2};
        polygon.setEdges(latlonarr);
        Polygon[] polygons = geometryRepository.getPolygons();

        Polyline[] polylines = geometryRepository.getLines();

        model.addAttribute("polygons", polygons);
        model.addAttribute("polylines", polylines);
        return "default";
    }

    @RequestMapping(value="/savepolygon", method= RequestMethod.POST)
    public String savePolygon(@RequestParam String array)
    {
        System.out.println(array);
        Polygon polygon = new Polygon();
        ArrayList<LatLon> latlngArray = new ArrayList<>();
        JSONArray jarray = new JSONArray(array);

        for(int i = 0; i < jarray.length(); i++) {
            JSONObject jobject = jarray.getJSONObject(i);
            LatLon latLon = new LatLon();
            latLon.setLat( (double)jobject.get("lat"));
            latLon.setLon( (double)jobject.get("lng"));

            latlngArray.add(latLon);
        }

        polygon.setEdges(latlngArray.toArray(new LatLon[latlngArray.size()] ));
        geometryRepository.savePolygon(polygon);
        return "id=1";
    }

    @RequestMapping(value="/savepolyline", method= RequestMethod.POST)
    public String savePolyline(@RequestParam String array)
    {
        System.out.println(array);
        Polyline line = new Polyline();
        ArrayList<LatLon> latlngArray = new ArrayList<>();
        JSONArray jarray = new JSONArray(array);

        for(int i = 0; i < jarray.length(); i++) {
            JSONObject jobject = jarray.getJSONObject(i);
            LatLon latLon = new LatLon();
            latLon.setLat( (double)jobject.get("lat"));
            latLon.setLon( (double)jobject.get("lng"));

            latlngArray.add(latLon);
        }

        line.setLines(latlngArray.toArray(new LatLon[latlngArray.size()] ));
        geometryRepository.savePolyline(line);
        return "id=1";
    }

    @RequestMapping(value = "/findintersections", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object findIntersections(@RequestParam String array)
    {
        System.out.println(array);
        Polyline line = new Polyline();
        ArrayList<LatLon> latlngArray = new ArrayList<>();
        JSONArray jarray = new JSONArray(array);

        for(int i = 0; i < jarray.length(); i++) {
            JSONObject jobject = jarray.getJSONObject(i);
            LatLon latLon = new LatLon();
            latLon.setLat( (double)jobject.get("lat"));
            latLon.setLon( (double)jobject.get("lng"));

            latlngArray.add(latLon);
        }

        line.setLines(latlngArray.toArray(new LatLon[latlngArray.size()] ));
        return geometryService.findIntersections(line);

    }
}
