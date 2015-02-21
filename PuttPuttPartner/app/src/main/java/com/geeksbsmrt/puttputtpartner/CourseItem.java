package com.geeksbsmrt.puttputtpartner;


import android.util.Log;

import com.parse.ParseClassName;
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
    public static String COURSEPAR = "TotalPar";

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

    public  String getCoursePar() {
        return getString(COURSEPAR);
    }
    public void setCoursePar(String coursePar) {
        put(COURSEPAR, coursePar);
    }

    public static ParseQuery<CourseItem> getQuery(){
        Log.i("CI", "getting query");
        return ParseQuery.getQuery(CourseItem.class);
    }
}
