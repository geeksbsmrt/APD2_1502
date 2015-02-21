package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;


public class Fragment_Options extends Fragment implements View.OnClickListener{

    public Fragment_Options() {
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
        View rootView = inflater.inflate(R.layout.fragment_fragment__options, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        Button act = (Button) rootView.findViewById(R.id.OPT_ACT);
        act.setOnClickListener(this);
        Button logOut = (Button) rootView.findViewById(R.id.OPT_LOGOUT);
        logOut.setOnClickListener(this);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            act.setVisibility(View.VISIBLE);
        } else {
            logOut.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.OPT_ACT:{
                ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
                startActivityForResult(builder.build(), 0);
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.OPT_LOGOUT:{
                Log.i("OPT", "Logging out");
                ParseUser.logOut();
                ParseAnonymousUtils.logIn(new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null) {
                            Log.d("MyApp", "Anonymous login failed.");
                        } else {
                            Log.d("MyApp", "Anonymous user logged in.");
                        }
                    }
                });
                getFragmentManager().popBackStack();
                break;
            }
            default:break;
        }
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
