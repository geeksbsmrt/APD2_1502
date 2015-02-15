package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


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

        Button facebook = (Button) rootView.findViewById(R.id.OPT_FB);
        facebook.setOnClickListener(this);
        Button twitter = (Button) rootView.findViewById(R.id.OPT_Twitter);
        twitter.setOnClickListener(this);
        Button act = (Button) rootView.findViewById(R.id.OPT_ACT);
        act.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.OPT_ACT:{
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Account functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.OPT_FB:{
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Facebook functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.OPT_Twitter:{
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Twitter functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
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
