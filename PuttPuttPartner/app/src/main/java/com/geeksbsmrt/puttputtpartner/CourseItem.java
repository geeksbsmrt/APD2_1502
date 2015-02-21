package com.geeksbsmrt.puttputtpartner;


import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.io.Serializable;

/**
 * Created by Adam on 2/20/2015.
 */

@ParseClassName("Course")
public class CourseItem extends ParseObject implements Serializable {
    public static String COURSENAME = "Name";
    public static String COURSEADDRESS = "Address";
    public static String COURSELOCATION = "Location";
    public static String COURSEHOLES = "Holes";
    public static String COURSECITY = "City";
    public static String COURSESTATE = "State";
    public static String COURSEZIP = "Zip";

    public String getCourseName() {
        return getString(COURSENAME);
    }
    public void setCourseName(String name) {
       put(COURSENAME, name);
    }

    public String getCourseAddress() {
        return getString(COURSEADDRESS);
    }
    public void setCourseAddress(String address) {
        put(COURSEADDRESS, address);
    }

    public String getCourseCity() {
        return getString(COURSECITY);
    }
    public void setCourseCity(String city) {
        put(COURSECITY, city);
    }

    public String getCourseState() {
        return getString(COURSESTATE);
    }
    public void setCourseState(String state) {
        put(COURSESTATE, state);
    }

    public String getCourseZip() {
        return getString(COURSEZIP);
    }
    public void setCourseZip(String zip) {
        put(COURSEZIP, zip);
    }

    public ParseGeoPoint getCourseLocation() {
        return getParseGeoPoint(COURSELOCATION);
    }
    public void setCourseLocation(ParseGeoPoint location) {
        put(COURSELOCATION, location);
    }

    public JSONArray getCourseHoles() {
        return getJSONArray(COURSEHOLES);
    }
    public void setCourseHoles(JSONArray holes) {
        put(COURSEHOLES, holes);
    }

    public static ParseQuery<CourseItem> getQuery(){

        Log.i("CI", "getting query");
        ParseQuery<CourseItem> query = ParseQuery.getQuery(CourseItem.class);
        try {
            Log.i("CI", String.valueOf(query.count()));
            return query;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
