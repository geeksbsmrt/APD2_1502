package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Fragment_AddCourse extends Fragment implements View.OnClickListener {

    private LinearLayout playButtons;

    public Fragment_AddCourse() {
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
        View rootView = inflater.inflate(R.layout.fragment_fragment__add_course, container, false);

        Spinner stateSpinner = (Spinner) rootView.findViewById(R.id.UC_CourseState);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.states, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(adapter);

        Button createScorecard = (Button) rootView.findViewById(R.id.AC_CreateScorecard);
        createScorecard.setOnClickListener(this);
        Button afp = (Button) rootView.findViewById(R.id.AC_AddFavAndPlay);
        afp.setOnClickListener(this);
        Button play = (Button) rootView.findViewById(R.id.AC_PlayOnce);
        play.setOnClickListener(this);

        playButtons = (LinearLayout) rootView.findViewById(R.id.AC_PlayButtons);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.AC_CreateScorecard:{
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Scorecard functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                playButtons.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.AC_AddFavAndPlay:{
                Toast.makeText(getActivity(), "Favorite and play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            case R.id.AC_PlayOnce:{
                Toast.makeText(getActivity(), "Play functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
                break;
            }
            default: break;
        }
    }
}
