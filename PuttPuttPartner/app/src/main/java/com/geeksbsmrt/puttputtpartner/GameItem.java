package com.geeksbsmrt.puttputtpartner;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by Adam on 2/21/2015.
 */

@ParseClassName("Game")
public class GameItem extends ParseObject implements Serializable {

    public static String GAMEID = "GameID";
    public static String PLAYERS = "Players";
    public static String COURSE = "Course";

    public String getGAMEID() {
        return getString(GAMEID);
    }
    public void setGAMEID(String gameID) {
       put(GAMEID, gameID);
    }

    public JSONArray getPLAYERS() {
        return getJSONArray(PLAYERS);
    }
    public void setPLAYERS(JSONArray players) {
        put(PLAYERS, players);
    }

    public ParseObject getCourse() {
        return getParseObject(COURSE);
    }
    public void setCourse(ParseObject course) {
       put(COURSE, course);
    }

    public Number generateGameID(){
        return new Random().nextInt(Integer.MAX_VALUE);
    }

    public static ParseQuery<GameItem> getQuery(){
        Log.i("GI", "getting query");
        return ParseQuery.getQuery(GameItem.class);
    }
}
