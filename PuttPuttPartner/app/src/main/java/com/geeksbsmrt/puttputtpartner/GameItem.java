package com.geeksbsmrt.puttputtpartner;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Adam on 2/21/2015.
 */

@ParseClassName("Game")
public class GameItem extends ParseObject implements Serializable {

    public static String GAMEID = "GameID";
    public static String PLAYERS = "Players";
    public static String COURSE = "Course";

    public String getGameId() {
        return getString(GAMEID);
    }
    public void setGameId(String gameID) {
       put(GAMEID, gameID);
    }

    public List<String> getPlayers() {
        return getList(PLAYERS);
    }
    public void setPlayers(List players) {
        put(PLAYERS, players);
    }

    public void addPlayer(String player){
        List<String> players =  getPlayers();
        if (players == null){
            players = new ArrayList<String>();
            players.add(player);
        } else {
            players.add(player);
        }
        setPlayers(players);
    }

    public ParseObject getCourse() {
        return getParseObject(COURSE);
    }
    public void setCourse(CourseItem course) {
       put(COURSE, course);
    }

    public String generateGameID(){
        return String.valueOf(new Random().nextInt(Integer.MAX_VALUE));
    }

    public static ParseQuery<GameItem> getQuery(){
        Log.i("GI", "getting query");
        return ParseQuery.getQuery(GameItem.class);
    }
}
