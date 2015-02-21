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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.regex.Pattern;


public class Fragment_CourseSearch extends Fragment {

    ParseQueryAdapter<CourseItem> courseAdapter;
    ListView courseList;

    public Fragment_CourseSearch() {
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
        View rootView = inflater.inflate(R.layout.fragment_course_search, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        final EditText input = (EditText) rootView.findViewById(R.id.CS_query);
        courseList = (ListView) rootView.findViewById(R.id.CS_CourseList);

        Button search = (Button) rootView.findViewById(R.id.CS_Search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String query = input.getText().toString();
                if (!query.equals("")){
                    Log.i("CS", query);
                    String zipRegex = "\\d{5}";
                    String cityStateRegex = ".*[,]? [A-Z][A-Za-z]";
                    if (Pattern.matches(zipRegex, query)){
                        queryCourse(query);
                    } else if (Pattern.matches(cityStateRegex, query)) {
                        String state = query.substring(query.length() - 2).toUpperCase();
                        Log.i("CS", state);
                        String city = query.substring(0, query.length() - 3);
                        Log.i("CS", city);
                        if (city.substring(city.length() - 1).equals(",")) {
                            city = city.substring(0, city.length() - 1);
                        }
                        queryCourse(city, state);
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.searchQuery), Toast.LENGTH_LONG).show();
                    }
                    hideKeyboard();
                } else {
                    Toast.makeText(getActivity(), getResources().getString(R.string.noInput), Toast.LENGTH_LONG).show();
                }
            }
        });
        return rootView;
    }

    private void queryCourse(final String zip){

        Log.i("CS", "In queryCourse ZIP");

        ParseQueryAdapter.QueryFactory<CourseItem> factory = new ParseQueryAdapter.QueryFactory<CourseItem>() {
            @Override
            public ParseQuery<CourseItem> create() {
                Log.i("CS", "in Create");
                ParseQuery<CourseItem> query = CourseItem.getQuery();
                query.whereMatches(CourseItem.COURSEZIP, zip);
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
            courseList.setAdapter(courseAdapter);
            courseList.setVisibility(View.VISIBLE);
    }

    private void queryCourse(final String city, final String state){
        Log.i("CS", "In queryCourse City, ST");

        ParseQueryAdapter.QueryFactory<CourseItem> factory = new ParseQueryAdapter.QueryFactory<CourseItem>() {
            @Override
            public ParseQuery<CourseItem> create() {
                Log.i("CS", "in Create");
                ParseQuery<CourseItem> query = CourseItem.getQuery();
                query.whereContains(CourseItem.COURSECITY, city);
                query.whereContains(CourseItem.COURSESTATE, state);
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
        courseList.setAdapter(courseAdapter);
        courseList.setVisibility(View.VISIBLE);
    }


    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
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
