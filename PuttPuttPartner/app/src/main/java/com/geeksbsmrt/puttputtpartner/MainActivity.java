package com.geeksbsmrt.puttputtpartner;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseCrashReporting;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.concurrent.ExecutionException;


public class MainActivity extends Activity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static ActionBar actionBar;
    private Resources res;
    public ParseUser currentUser;
    private NfcAdapter mNfcAdapter;
    static FragmentManager fragMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar = getActionBar();
        setContentView(R.layout.activity_main);

        res = getResources();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        fragMgr = getFragmentManager();

        if (!ParseCrashReporting.isCrashReportingEnabled()) {
            //ParseCrashReporting.enable(this);
        }
        ParseObject.registerSubclass(CourseItem.class);
        ParseObject.registerSubclass(HoleItem.class);
        ParseObject.registerSubclass(GameItem.class);
        Parse.initialize(this, res.getString(R.string.parse_app_id), res.getString(R.string.parse_client_key));
        ParseTwitterUtils.initialize(res.getString(R.string.twitterConsumerKey), res.getString(R.string.twitterConsumerSecret));

        currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            ParseLoginBuilder builder = new ParseLoginBuilder(this);
            startActivityForResult(builder.build(), 0);
        } else if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            Log.i("MA", "Anonymous");
        } else {
            Log.i("MA", (String) currentUser.get("name"));
        }

        if (savedInstanceState == null) {
            Fragment_MainActivity fma = new Fragment_MainActivity();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fma)
                    .commit();
        }

        handleIntent(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(mNfcAdapter == null)) {
            setupForegroundDispatch(this, mNfcAdapter);
        }
    }
    @Override
    protected void onPause() {
        if (!(mNfcAdapter == null)) {
            stopForegroundDispatch(this, mNfcAdapter);
        }
        super.onPause();
    }

    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        // TODO: handle Intent
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)){
                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                try {
                    String courseID = new NdefReaderTask().execute(tag).get();
                    Log.i("MA", courseID);
                    CourseItem course = getCourse(courseID);
                    if (!(course == null)) {
                        Fragment_CourseDesc cd = new Fragment_CourseDesc();
                        Bundle courseBundle = new Bundle();
                        courseBundle.putSerializable("course", course);
                        cd.setArguments(courseBundle);
                        getFragmentManager().beginTransaction().replace(R.id.container, cd).addToBackStack(null).commit();
                    } else {
                        Log.i("MA", "Course nor found?");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static CourseItem getCourse(String courseID) {
        ParseQuery<CourseItem> query;
        CourseItem course = null;
        try {
            query = CourseItem.getQuery();
            query.whereEqualTo("objectId", courseID);

            course = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return course;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 86 || resultCode == RESULT_CANCELED) {
            ParseAnonymousUtils.logIn(new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e != null) {
                        Log.d("PuttPutt Partner", "Anonymous login failed.");
                    } else {
                        Log.d("PuttPutt Partner", "Anonymous user logged in.");
                    }
                }
            });
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
