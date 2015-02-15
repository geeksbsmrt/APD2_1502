package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_MainActivity extends Fragment implements View.OnClickListener {

    public Fragment_MainActivity() {
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
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);

        Button findCourse = (Button) rootView.findViewById(R.id.search);
        findCourse.setOnClickListener(this);

        Button aci = (Button) rootView.findViewById(R.id.autoCheckIn);
        aci.setOnClickListener(this);

        Button mci = (Button) rootView.findViewById(R.id.manualCheckIn);
        mci.setOnClickListener(this);

        Button join = (Button) rootView.findViewById(R.id.joinGame);
        join.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search:{
                Fragment_CourseSearch fcs = new Fragment_CourseSearch();
                getFragmentManager().beginTransaction().replace(R.id.container, fcs).addToBackStack(null).commit();
                break;
            }
            case R.id.autoCheckIn:{
                Fragment_AutoCheckIn aci = new Fragment_AutoCheckIn();
                getFragmentManager().beginTransaction().replace(R.id.container, aci).addToBackStack(null).commit();
                break;
            }
            case R.id.manualCheckIn:{
                Fragment_ManualCheckIn mci = new Fragment_ManualCheckIn();
                getFragmentManager().beginTransaction().replace(R.id.container, mci).addToBackStack(null).commit();
                break;
            }
            case R.id.joinGame:{
                Fragment_JoinGame jg = new Fragment_JoinGame();
                getFragmentManager().beginTransaction().replace(R.id.container, jg).addToBackStack(null).commit();
                break;
            }
            default: break;
        }
    }
}