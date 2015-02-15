package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;


public class Fragment_ManualCheckIn extends Fragment {

    public Fragment_ManualCheckIn() {
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

        ListView favList = (ListView) rootView.findViewById(R.id.MCI_FavCourses);
        ListView nearList = (ListView) rootView.findViewById(R.id.MCI_NearCourses);



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