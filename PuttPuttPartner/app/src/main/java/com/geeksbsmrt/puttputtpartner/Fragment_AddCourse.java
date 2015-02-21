package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;

public class Fragment_AddCourse extends Fragment implements View.OnClickListener {

    private LinearLayout playButtons;
    CourseItem course;
    Spinner stateSpinner;
    EditText courseName;
    EditText courseAddress;
    EditText courseCity;
    EditText courseZip;

    public Fragment_AddCourse() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        course = new CourseItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment__add_course, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        courseName = (EditText) rootView.findViewById(R.id.UC_CourseName);
        courseAddress = (EditText) rootView.findViewById(R.id.UC_CourseAddress);
        courseCity = (EditText) rootView.findViewById(R.id.UC_CourseCity);
        courseZip = (EditText) rootView.findViewById(R.id.UC_CourseZip);
        stateSpinner = (Spinner) rootView.findViewById(R.id.UC_CourseState);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.states, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        Button createScorecard = (Button) rootView.findViewById(R.id.AC_CreateScorecard);
        createScorecard.setOnClickListener(this);
        Button afp = (Button) rootView.findViewById(R.id.AC_AddFavAndPlay);
        afp.setOnClickListener(this);
        Button play = (Button) rootView.findViewById(R.id.AC_PlayOnce);
        play.setOnClickListener(this);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            afp.setVisibility(View.GONE);
        }

        playButtons = (LinearLayout) rootView.findViewById(R.id.AC_PlayButtons);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AC_CreateScorecard: {

                if (courseName.getText().toString().equals("") ||
                    courseAddress.getText().toString().equals("") ||
                    courseCity.getText().toString().equals("") ||
                    courseZip.getText().toString().equals("") ||
                    stateSpinner.getSelectedItem().toString().equals("State")){
                    Toast.makeText(getActivity(), getString(R.string.allFields), Toast.LENGTH_LONG).show();
                } else {
                    course.setCourseName(courseName.getText().toString());
                    course.setCourseAddress(courseAddress.getText().toString());
                    course.setCourseCity(courseCity.getText().toString());
                    course.setCourseZip(courseZip.getText().toString());
                    course.setCourseState(stateSpinner.getSelectedItem().toString());
                    //TODO: Move this functionality to the play buttons in M3
                    try {
                        course.save();
                        playButtons.setVisibility(View.VISIBLE);
                    } catch (ParseException e) {
                        Toast.makeText(getActivity(), getString(R.string.courseNotSaved), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                    //TODO: Remove toast message and complete Parse DB integration.
                    //Static set text in toast message as it will not be present in final build.
                    Toast.makeText(getActivity(), "Scorecard functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.AC_AddFavAndPlay: {

                Log.i("AC", course.getObjectId());

                ParseUser user = ParseUser.getCurrentUser();
                user.addUnique("Favorites", course.getObjectId());
                user.saveInBackground();
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Favorite saved. Play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.AC_PlayOnce: {
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            default:
                break;
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