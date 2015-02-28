package com.geeksbsmrt.puttputtpartner.parse_items;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.text.ParseException;


/**
 * Created by Adam on 2/21/2015.
 */

@ParseClassName("Hole")
public class HoleItem extends ParseObject implements Serializable {

    public static String HOLENUMBER = "Number";
    public static String HOLEPAR = "Par";

    public static ParseQuery<HoleItem> getQuery() throws ParseException {
        Log.i("HI", "getting query");
        return ParseQuery.getQuery(HoleItem.class);
    }

    public String getHoleNumber() {
        return String.valueOf(getNumber(HOLENUMBER));
    }

    public void setHoleNumber(String number) {
        put(HOLENUMBER, Integer.valueOf(number));
    }

    public String getHolePar() {
        return String.valueOf(getNumber(HOLEPAR));
    }

    public void setHolePar(String par) {
        put(HOLEPAR, Integer.valueOf(par));
    }
}
