package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.geeksbsmrt.puttputtpartner.parse_items.CourseItem;
import com.geeksbsmrt.puttputtpartner.parse_items.GameItem;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class Fragment_JoinGame extends Fragment {


    public Fragment_JoinGame() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment__join_game, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        final EditText gameID = (EditText) rootView.findViewById(R.id.JG_query);

        Button join = (Button) rootView.findViewById(R.id.JG_Join);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(), 0);

                ParseQuery<GameItem> gameQuery = GameItem.getQuery();
                gameQuery.whereEqualTo(GameItem.GAMEID, gameID.getText().toString());
                try {
                    GameItem game = gameQuery.getFirst();
                    Log.i("JG", game.getObjectId());
                    ParseUser user = ParseUser.getCurrentUser();
                    game.addPlayer(user);
                    game.saveInBackground();

                    Fragment_PlayGame pg = new Fragment_PlayGame();
                    Bundle gameBundle = new Bundle();
                    gameBundle.putString("game", game.getGameId());
                    ParseQuery<CourseItem> courseQuery = CourseItem.getQuery();
                    courseQuery.whereEqualTo("objectId", game.getCourse().getObjectId());
                    CourseItem course = courseQuery.getFirst();
                    gameBundle.putSerializable("course", course);
                    pg.setArguments(gameBundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, pg).addToBackStack(null).commit();
                } catch (ParseException e) {
                    Toast.makeText(getActivity(), getString(R.string.noGameFound) + " " + gameID.getText().toString(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
            }
        });

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
}
