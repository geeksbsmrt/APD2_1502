package com.geeksbsmrt.puttputtpartner;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Fragment_PlayGame extends Fragment {

    TableLayout playerTable;
    TableLayout scoreTable;
    TableLayout totalTable;
    CourseItem course;
    TableRow holeRow;
    TableRow parRow;
    Context mContext;

    public Fragment_PlayGame() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        course = (CourseItem) getArguments().get("course");
        mContext = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment__play_game, container, false);

        playerTable = (TableLayout) rootView.findViewById(R.id.PG_playerTable);
        scoreTable = (TableLayout) rootView.findViewById(R.id.PG_scoreTable);
        totalTable = (TableLayout) rootView.findViewById(R.id.PG_totalTable);
        holeRow = (TableRow) rootView.findViewById(R.id.PG_holeRow);
        parRow = (TableRow) rootView.findViewById(R.id.PG_parRow);

        List holes = course.getCourseHoles();
        try {
            ParseQuery<HoleItem> query = HoleItem.getQuery();
            query.whereContainedIn("objectId", holes);
            query.addAscendingOrder(HoleItem.HOLENUMBER);
            List<HoleItem> holeItems = query.find();
            for (HoleItem hole : holeItems) {
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(25,15,25,15);

                TextView holeText = new TextView(mContext);
                holeText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                holeText.setLayoutParams(params);
                holeText.setGravity(Gravity.CENTER);
                Log.i("PG", hole.getHoleNumber());
                holeText.setText(hole.getHoleNumber());
                holeRow.addView(holeText);

                TextView parText = new TextView(mContext);
                parText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                parText.setLayoutParams(params);
                parText.setGravity(Gravity.CENTER);
                Log.i("PG", hole.getHolePar());
                parText.setText(hole.getHolePar());
                parRow.addView(parText);
            }
//            query.findInBackground(new FindCallback<HoleItem>() {
//                @Override
//                public void done(List<HoleItem> holeItems, com.parse.ParseException e) {
//                    for (HoleItem hole : holeItems) {
//                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        params.setMargins(5, 5, 5, 5);
//                        TextView test = new TextView(mContext);
//                        test.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
//                        test.setMinWidth(30);
//                        test.setLayoutParams(params);
//                        test.setGravity(Gravity.CENTER);
//                        Log.i("PG", hole.getHoleNumber());
//                        test.setText(hole.getHoleNumber());
//                        holeRow.addView(test);
//                    }
//                }
//            });
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}
