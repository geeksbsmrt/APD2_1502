package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.geeksbsmrt.puttputtpartner.parse_items.CourseItem;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;


public class Fragment_ManualCheckIn extends Fragment implements AdapterView.OnItemClickListener {

    ListView favList;
    ParseUser user;
    TextView noFavs;
    ParseQueryAdapter<CourseItem> courseAdapter;

    public Fragment_ManualCheckIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = ParseUser.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment__manual__check_in, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        Button unlisted = (Button) rootView.findViewById(R.id.MCI_Unlisted);
        unlisted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment_AddCourse ac = new Fragment_AddCourse();
                getFragmentManager().beginTransaction().replace(R.id.container, ac).addToBackStack(null).commit();
            }
        });

        favList = (ListView) rootView.findViewById(R.id.MCI_FavCourses);
        favList.setOnItemClickListener(this);
        ListView nearList = (ListView) rootView.findViewById(R.id.MCI_NearCourses);
        noFavs = (TextView) rootView.findViewById(R.id.MCI_noFavs);

        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            queryFavs();
        }

        return rootView;
    }

    private void queryFavs() {
        ParseQueryAdapter.QueryFactory<CourseItem> factory = new ParseQueryAdapter.QueryFactory<CourseItem>() {
            @Override
            public ParseQuery<CourseItem> create() {
                Log.i("MCI", "in query favs");
                ParseQuery<CourseItem> query = null;
                try {
                    query = CourseItem.getQuery();
                    query.whereContainedIn("objectId", user.getList("Favorites"));
                    Log.i("MCI", String.valueOf(query.count()));
                    if (query.count() > 0){
                        favList.setVisibility(View.VISIBLE);
                    } else {
                        query = null;
                        noFavs.setVisibility(View.VISIBLE);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }
                return query;
            }
        };

        courseAdapter = new ParseQueryAdapter<CourseItem>(getActivity(), factory) {
            @Override
            public View getItemView(CourseItem course, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getContext(), R.layout.item_course, null);
                }
                TextView nameView = (TextView) view.findViewById(R.id.courseListName);
                TextView addressView = (TextView) view.findViewById(R.id.courseListLoc);
                nameView.setText(course.getCourseName());
                addressView.setText(course.getCourseAddress() + " " + course.getCourseCity() + ", " + course.getCourseState() + " " + course.getCourseZip());
                return view;
            }
        };
        if (courseAdapter.getCount() > 0){
            favList.setAdapter(courseAdapter);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        CourseItem course = courseAdapter.getItem(i);
        Fragment_CourseDesc cd = new Fragment_CourseDesc();
        Bundle courseBundle = new Bundle();
        courseBundle.putSerializable("course", course);
        cd.setArguments(courseBundle);
        getFragmentManager().beginTransaction().replace(R.id.container, cd).addToBackStack(null).commit();
    }
}
