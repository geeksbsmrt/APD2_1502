package com.geeksbsmrt.puttputtpartner.parse_items;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.text.ParseException;

/**
 * Created by Adam on 2/28/2015.
 */
@ParseClassName("Score")
public class ScoreItem extends ParseObject implements Serializable {
    public static String HOLE = "Hole";
    public static String SCORE = "Score";
    public static String PLAYER = "Player";
    public static String GAME = "Game";

    public ParseObject getHole() {
        return getParseObject(HOLE);
    }
    public void setHole(HoleItem hole) {
        put(HOLE, hole);
    }

    public Number getScore(){
        return getNumber(SCORE);
    }
    public void setScore(Number score){
        put(SCORE, score);
    }

    public ParseUser getPlayer() {
        return getParseUser(PLAYER);
    }
    public void setPlayer(ParseUser player) {
        put(PLAYER, player);
    }

    public ParseObject getGame() {
        return getParseObject(GAME);
    }
    public void setGame(GameItem game) {
        put(GAME, game);
    }

    public static ParseQuery<ScoreItem> getQuery() throws ParseException {
        Log.i("HI", "getting query");
        return ParseQuery.getQuery(ScoreItem.class);
    }
}
