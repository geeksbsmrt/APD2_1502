package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.geeksbsmrt.puttputtpartner.parse_items.CourseItem;
import com.geeksbsmrt.puttputtpartner.parse_items.GameItem;
import com.geeksbsmrt.puttputtpartner.parse_items.HoleItem;
import com.geeksbsmrt.puttputtpartner.parse_items.ScoreItem;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Fragment_PlayGame extends Fragment implements View.OnClickListener, Dialogs.onDialogFinished {

    View rootView;
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
    List<String> holes;
    List<HoleItem> holeItemList = new ArrayList<HoleItem>();
    int densityDPI;
    GameItem game;

    public Fragment_PlayGame() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        course = (CourseItem) getArguments().get("course");
        gameId = getArguments().getString("game");
        mContext = getActivity().getApplicationContext();
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityDPI = (int)(metrics.density * 160f);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_play_game, container, false);


        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        playerTable = (TableLayout) rootView.findViewById(R.id.PG_playerTable);
        scoreTable = (TableLayout) rootView.findViewById(R.id.PG_scoreTable);
        totalTable = (TableLayout) rootView.findViewById(R.id.PG_totalTable);
        holeRow = (TableRow) rootView.findViewById(R.id.PG_holeRow);
        parRow = (TableRow) rootView.findViewById(R.id.PG_parRow);
        totalPar = (TextView) rootView.findViewById(R.id.PG_totalPar);

        Button addPlayer = (Button) rootView.findViewById(R.id.PG_addPlayer);
        Button clearScore = (Button) rootView.findViewById(R.id.PG_clearScore);

        addPlayer.setOnClickListener(this);
        clearScore.setOnClickListener(this);

        holes = course.getCourseHoles();
        try {
            ParseQuery<GameItem> gameQuery = GameItem.getQuery();
            gameQuery.whereEqualTo(GameItem.GAMEID, gameId);
            game = gameQuery.getFirst();
            Log.i("PG", game.getGameId());
            MainActivity.actionBar.setTitle(getString(R.string.gameID) + " " + game.getGameId());

            ParseQuery<HoleItem> holeQuery = HoleItem.getQuery();
            holeQuery.whereContainedIn("objectId", holes);
            holeQuery.addAscendingOrder(HoleItem.HOLENUMBER);
            List<HoleItem> holeItems = holeQuery.find();

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

            players = game.getPlayers();
            for (String player : players){
                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                userQuery.whereEqualTo("objectId", player);
                ParseUser user = userQuery.getFirst();
                addPlayer(user);
            }

            getScores();

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return rootView;
    }

    private void addPlayer(ParseUser user){
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
            playerNameView.setText(getString(R.string.guest));
        }
        playerNameRow.addView(playerNameView);
        playerTable.addView(playerNameRow);

        TableRow playerScoreRow = new TableRow(mContext);

        TableRow.LayoutParams psrView = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        psrView.setMargins(getPx(8), getPx(5), getPx(8), getPx(5));
        playerScoreRow.setBackgroundColor(Color.parseColor("#003300"));
        playerScoreRow.setDividerDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dark));
        Log.i("Player Index", String.valueOf(players.indexOf(user.getObjectId())));
        playerScoreRow.setId(View.generateViewId());
        playerScoreRow.setTag(user.getObjectId());
        playerScoreRow.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING | TableRow.SHOW_DIVIDER_MIDDLE | TableRow.SHOW_DIVIDER_END);
        playerScoreRow.setLayoutParams(psrView);

        for (String hole : holes){
            final TextView playerScoreText = new TextView(mContext);
            playerScoreText.setMinWidth(getPx(20));
            playerScoreText.setLayoutParams(psrView);
            playerScoreText.setGravity(Gravity.CENTER);
            playerScoreText.setId(View.generateViewId());
            playerScoreText.setOnClickListener(this);
            playerScoreRow.addView(playerScoreText);
        }
        scoreTable.addView(playerScoreRow, scoreTable.getChildCount());

        TableRow playerTotalRow = new TableRow(mContext);
        TableRow.LayoutParams ptrView = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ptrView.setMargins(px5, px5, px5, px5);
        TextView playerTotalView = new TextView(mContext);
        playerTotalView.setGravity(Gravity.END);
        playerTotalView.setLayoutParams(params);
        playerTotalView.setId(View.generateViewId());
        playerTotalView.setTag(user.getObjectId());
        playerTotalRow.addView(playerTotalView);
        playerTotalRow.setDividerDrawable(getResources().getDrawable(android.R.drawable.divider_horizontal_dark));
        playerTotalRow.setShowDividers(TableRow.SHOW_DIVIDER_BEGINNING);
        totalTable.addView(playerTotalRow);
    }

    private void getScores() throws ParseException {
        ParseQuery<ScoreItem> scoreQuery = ScoreItem.getQuery();
        scoreQuery.whereEqualTo(ScoreItem.GAME, game);
        scoreQuery.findInBackground(new FindCallback<ScoreItem>() {
            @Override
            public void done(List<ScoreItem> scoreItems, com.parse.ParseException e) {
                for (ScoreItem scoreItem : scoreItems){
                    TableRow playerRow = (TableRow) scoreTable.findViewWithTag(scoreItem.getPlayer().getObjectId());
                    TextView playerTotalView = (TextView) totalTable.findViewWithTag(scoreItem.getPlayer().getObjectId());
                    try {
                        ParseQuery<HoleItem> holeQuery = HoleItem.getQuery();
                        holeQuery.whereEqualTo("objectId", scoreItem.getHole().getObjectId());
                        HoleItem holeItem = holeQuery.getFirst();
                        TextView holeText = (TextView) playerRow.getChildAt(Integer.parseInt(holeItem.getHoleNumber())-1);
                        holeText.setText(String.valueOf(scoreItem.getScore()));
                        Integer playerTotal = (Integer) scoreItem.getScore();
                        if (!playerTotalView.getText().toString().equals("")) {
                            playerTotal += Integer.parseInt(playerTotalView.getText().toString());
                        }
                        playerTotalView.setText(String.valueOf(playerTotal));
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    } catch (com.parse.ParseException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        switch (view.getId()){
            case R.id.PG_addPlayer:{
                if (players.size() < 4) {
                    Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.PLAYER);
                    Bundle args = new Bundle();
                    args.putInt("viewId", view.getId());
                    dialog.setArguments(args);
                    dialog.setTargetFragment(this, 0);
                    dialog.show(getFragmentManager(), "DF");
                } else {
                    Toast.makeText(mContext, getString(R.string.maxPlayers), Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.PG_clearScore:{

                break;
            }
            default:{
                ParseUser user = null;
                TableRow scoreRow = (TableRow) view.getParent();
                String player = String.valueOf(scoreRow.getTag());
                Log.i("PG player", player);
                ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
                userQuery.whereEqualTo("objectId", player);
                try {
                    user = userQuery.getFirst();
                } catch (com.parse.ParseException e) {
                    e.printStackTrace();
                }

                if (user != null) {
                    Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.SCORE);
                    Bundle args = new Bundle();
                    args.putInt("viewId", view.getId());
                    TextView textView = (TextView) scoreTable.findViewById(view.getId());
                    args.putString("text",textView.getText().toString());
                    dialog.setArguments(args);
                    dialog.setTargetFragment(this, 0);
                    dialog.show(getFragmentManager(), "DF");
                }
                break;
            }
        }
    }

    @Override
    public void onDialogOK(String result, Dialogs dialog) {
        try{
            Log.i("Current User: ", ParseUser.getCurrentUser().getObjectId());
            String token = ParseUser.getCurrentUser().getSessionToken();
            ParseUser newUser = new ParseUser();
            newUser.setUsername(String.format("Gen_%s", String.valueOf(new Random().nextInt(Integer.MAX_VALUE))));
            newUser.setPassword(String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
            newUser.put("name", result);
            newUser.signUp();
            //players.add(newUser.getObjectId());
            game.addPlayer(newUser);
            game.saveInBackground();
            addPlayer(newUser);
            Log.i("Current User After: ", ParseUser.getCurrentUser().getObjectId());
            ParseUser.become(token);
            Log.i("Current User Become: ", ParseUser.getCurrentUser().getObjectId());
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogOK(String result, int viewId, Dialogs dialog){
        TextView text = (TextView) scoreTable.findViewById(viewId);
        text.setText(result);
        TableRow playerRow = (TableRow) text.getParent();
        int position = playerRow.indexOfChild(text);
        Log.i("Position: ", String.valueOf(position));
        HoleItem hole = holeItemList.get(position);
        Log.i("HoleObject", hole.getObjectId());

        ParseUser user = null;

        String player = String.valueOf(playerRow.getTag());
        Log.i("PG player", player);
        ParseQuery<ParseUser> userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", player);
        try {
            user = userQuery.getFirst();
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }

        if (user != null) {
            TextView playerTotalView = (TextView) totalTable.findViewWithTag(user.getObjectId());
            Integer playerTotal = Integer.parseInt(result);
            if (!playerTotalView.getText().toString().equals("")) {
                playerTotal += Integer.parseInt(playerTotalView.getText().toString());
            }
            playerTotalView.setText(String.valueOf(playerTotal));

            try {
                ParseQuery<ScoreItem> scoreQuery = ScoreItem.getQuery();
                scoreQuery.whereEqualTo(ScoreItem.GAME, game);
                scoreQuery.whereEqualTo(ScoreItem.HOLE, hole);
                scoreQuery.whereEqualTo(ScoreItem.PLAYER, user);
                ScoreItem score;
                Log.i("Score Count", String.valueOf(scoreQuery.count()));
                if (scoreQuery.count() > 0){
                    score = scoreQuery.getFirst();
                    score.setScore(Integer.parseInt(result));
                } else {
                    score = new ScoreItem();
                    score.setGame(game);
                    score.setHole(hole);
                    score.setPlayer(user);
                    score.setScore(Integer.parseInt(result));
                }
                score.save();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (com.parse.ParseException e) {
                e.printStackTrace();
            }
        }
        dialog.dismiss();
    }

    @Override
    public void onDialogCancel(Dialogs dialog) {
        dialog.dismiss();
    }
}
