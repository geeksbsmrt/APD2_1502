package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;


public class Fragment_CourseDesc extends Fragment implements View.OnClickListener {

    CourseItem course;

    public Fragment_CourseDesc() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        course = (CourseItem) getArguments().get("course");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment__course_desc, container, false);

        MainActivity.actionBar.setTitle(course.getCourseName());
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        TextView address = (TextView) rootView.findViewById(R.id.CD_Address);
        TextView city = (TextView) rootView.findViewById(R.id.CD_City);
        TextView holes = (TextView) rootView.findViewById(R.id.CD_Holes);
        TextView totalPar = (TextView) rootView.findViewById(R.id.CD_Par);
        Button favButton = (Button) rootView.findViewById(R.id.CD_AddFavAndPlay);
        Button playButton = (Button) rootView.findViewById(R.id.CD_Play);
        favButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            favButton.setVisibility(View.GONE);
        }

        address.setText(course.getCourseAddress());
        city.setText(course.getCourseCity() + ", " + course.getCourseState());
        holes.setText(String.valueOf(course.getCourseHoles().length()) + " " + getResources().getString(R.string.holes));
        totalPar.setText(getResources().getString(R.string.totalPar) + " " + course.getCoursePar());

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.CD_AddFavAndPlay:{
                ParseUser user = ParseUser.getCurrentUser();
                user.addUnique("Favorites", course.getObjectId());
                user.saveInBackground();
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Favorite saved. Play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.CD_Play:{
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
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
