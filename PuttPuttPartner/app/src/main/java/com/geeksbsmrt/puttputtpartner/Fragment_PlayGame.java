package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;


public class Fragment_PlayGame extends Fragment implements View.OnClickListener {

    TableLayout playerTable;
    TableLayout scoreTable;
    TableLayout totalTable;
    CourseItem course;
    TableRow holeRow;
    TableRow parRow;
    Context mContext;
    String gameId;
    TextView totalPar;
    List<String> players;
    List<HoleItem> holeItemList;
    int densityDPI;

    public Fragment_PlayGame() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        course = (CourseItem) getArguments().get("course");
        gameId = getArguments().getString("game");
        mContext = getActivity().getApplicationContext();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityDPI = (int)(metrics.density * 160f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment__play_game, container, false);


        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        playerTable = (TableLayout) rootView.findViewById(R.id.PG_playerTable);
        scoreTable = (TableLayout) rootView.findViewById(R.id.PG_scoreTable);
        totalTable = (TableLayout) rootView.findViewById(R.id.PG_totalTable);
        holeRow = (TableRow) rootView.findViewById(R.id.PG_holeRow);
        parRow = (TableRow) rootView.findViewById(R.id.PG_parRow);
        totalPar = (TextView) rootView.findViewById(R.id.PG_totalPar);



        final List<String> holes = course.getCourseHoles();
        try {
            ParseQuery<GameItem> gameQuery = GameItem.getQuery();
            gameQuery.whereEqualTo(GameItem.GAMEID, gameId);
            GameItem game = gameQuery.getFirst();
            Log.i("PG", game.getGameId());
            MainActivity.actionBar.setTitle(getString(R.string.gameID) + " " + game.getGameId());

            ParseQuery<HoleItem> holeQuery = HoleItem.getQuery();
            holeQuery.whereContainedIn("objectId", holes);
            holeQuery.addAscendingOrder(HoleItem.HOLENUMBER);
            holeQuery.findInBackground(new FindCallback<HoleItem>() {
                @Override
                public void done(List<HoleItem> holeItems, com.parse.ParseException e) {

                    holeItemList = holeItems;

                    int parTotal = 0;
                    for (HoleItem hole : holeItems) {
                        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(getPx(8), getPx(5), getPx(8), getPx(5));

                        TextView holeText = new TextView(mContext);
                        holeText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                        holeText.setLayoutParams(params);
                        holeText.setMinWidth(getPx(20));
                        holeText.setGravity(Gravity.CENTER);
                        holeText.setText(hole.getHoleNumber());
                        holeRow.addView(holeText);

                        TextView parText = new TextView(mContext);
                        parText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
                        parText.setLayoutParams(params);
                        parText.setMinWidth(getPx(20));
                        parText.setGravity(Gravity.CENTER);
                        parText.setText(hole.getHolePar());
                        parRow.addView(parText);

                        parTotal += Integer.parseInt(hole.getHolePar());
                        totalPar.setText(String.valueOf(parTotal));
                    }
                }
            });
            players = game.getPlayers();
            for (String player : players){
                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                userQuery.whereEqualTo("objectId", player);
                ParseUser user = userQuery.getFirst();

                TableRow playerNameRow = new TableRow(mContext);
                TextView playerNameView = new TextView(mContext);
                TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                int px5 = getPx(5);
                params.setMargins(px5,px5,px5,px5);
                playerNameView.setLayoutParams(params);

                if (user.containsKey("name")){
                    Log.i("PG", user.getString("name"));
                    playerNameView.setText(user.getString("name"));
                } else {
                    Log.i("PG", "Unknown");
                    playerNameView.setText("Guest");
                }
                playerNameRow.addView(playerNameView);
                playerTable.addView(playerNameRow);

                TableRow playerScoreRow = new TableRow(mContext);

                TableRow.LayoutParams psrView = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                psrView.setMargins(getPx(8), getPx(5), getPx(8), getPx(5));
                playerScoreRow.setBackgroundColor(Color.parseColor("#003300"));
                playerScoreRow.setDividerDrawable(getResources().getDrawable(R.drawable.custom_divider_vertical));
                playerScoreRow.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING|TableRow.SHOW_DIVIDER_MIDDLE|TableRow.SHOW_DIVIDER_END);
                playerScoreRow.setLayoutParams(psrView);

                for (HoleItem hole : holeItemList){
                    TextView playerScoreText = new TextView(mContext);
                    playerScoreText.setMinWidth(getPx(20));
                    playerScoreText.setLayoutParams(psrView);
                    playerScoreText.setGravity(Gravity.CENTER);
                    playerScoreText.setText("0");
                    playerScoreText.setOnClickListener(this);
                    playerScoreRow.addView(playerScoreText);
                }

                scoreTable.addView(playerScoreRow, scoreTable.getChildCount());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                getFragmentManager().popBackStack();
            }
            default: {
                return false;
            }
        }
    }

    private int getPx(int dpi){
        return dpi * (densityDPI/160);
    }

    @Override
    public void onClick(View view) {

    }
}
