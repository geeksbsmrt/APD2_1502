package com.geeksbsmrt.puttputtpartner;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;



public class MainActivity extends Activity {

    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
