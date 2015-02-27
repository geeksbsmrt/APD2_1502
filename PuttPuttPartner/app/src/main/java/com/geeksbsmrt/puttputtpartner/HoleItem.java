package com.geeksbsmrt.puttputtpartner;

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

    public String getHoleNumber() {
        return getNumber(HOLENUMBER).toString();
    }
    public void setHoleNumber(String number) {
        put(HOLENUMBER, Integer.valueOf(number));
    }

    public String getHolePar() {
        return getNumber(HOLEPAR).toString();
    }
    public void setHolePar(String par) {
        put(HOLEPAR, Integer.valueOf(par));
    }

    public static ParseQuery<HoleItem> getQuery() throws ParseException {
        Log.i("HI", "getting query");
        return ParseQuery.getQuery(HoleItem.class);
    }
}
