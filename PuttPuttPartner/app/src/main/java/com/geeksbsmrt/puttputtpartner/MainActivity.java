package com.geeksbsmrt.puttputtpartner;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseTwitterUtils;
import com.parse.ui.ParseLoginBuilder;


public class MainActivity extends Activity {

    public static ActionBar actionBar;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        res = getResources();

        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);
        Parse.initialize(this, res.getString(R.string.parse_app_id), res.getString(R.string.parse_client_key));
        ParseTwitterUtils.initialize(res.getString(R.string.twitterConsumerKey), res.getString(R.string.twitterConsumerSecret));

        ParseLoginBuilder builder = new ParseLoginBuilder(this);
        startActivityForResult(builder.build(), 0);

        actionBar = getActionBar();
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            Fragment_MainActivity fma = new Fragment_MainActivity();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fma)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Fragment_Options opts = new Fragment_Options();
                getFragmentManager().beginTransaction().replace(R.id.container, opts).addToBackStack(null).commit();
                return false;
            }
            default: {
                return false;
            }
        }


    }
}
