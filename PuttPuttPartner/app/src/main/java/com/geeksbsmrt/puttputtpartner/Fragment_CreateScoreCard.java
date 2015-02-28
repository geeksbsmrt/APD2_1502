package com.geeksbsmrt.puttputtpartner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.geeksbsmrt.puttputtpartner.parse_items.HoleItem;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Adam on 2/28/2015.
 */
public class Fragment_CreateScoreCard extends Fragment implements View.OnClickListener, Dialogs.createDialogFinished {

    public static final String NUM_HOLES = "numHoles";
    TableRow holeRow;
    TableRow parRow;
    TextView totalPar;
    int densityDPI;
    Context mContext;
    int parTotal;
    Integer numHoles;
    List<String> holeList;


    public interface CreateScoreCardCallback{
        public void onScorecardCreated(List<String> holes, String totalPar);
    }
    public Fragment_CreateScoreCard() {
        // Required empty public constructor
    }

    CreateScoreCardCallback mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (CreateScoreCardCallback) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onDialogFinished");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_create_scorecard, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        densityDPI = (int)(metrics.density * 160f);
        mContext = getActivity().getApplicationContext();

        holeRow = (TableRow) rootView.findViewById(R.id.PG_holeRow);
        parRow = (TableRow) rootView.findViewById(R.id.PG_parRow);
        totalPar = (TextView) rootView.findViewById(R.id.PG_totalPar);
        holeList = new ArrayList<String>();

        numHoles = getArguments().getInt(NUM_HOLES);
        parTotal = 0;
        for (int i=0,j=numHoles; i<j; i++){
            TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(getPx(8), getPx(5), getPx(8), getPx(5));

            TextView holeText = new TextView(mContext);
            holeText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
            holeText.setLayoutParams(params);
            holeText.setMinWidth(getPx(20));
            holeText.setGravity(Gravity.CENTER);
            holeText.setText(String.valueOf(i+1));
            holeRow.addView(holeText);

            TextView parText = new TextView(mContext);
            parText.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
            parText.setLayoutParams(params);
            parText.setMinWidth(getPx(20));
            parText.setGravity(Gravity.CENTER);
            parText.setId(View.generateViewId());
            parText.setOnClickListener(this);
            parRow.addView(parText);
        }

        return rootView;
    }

    private int getPx(int dpi){
        return dpi * (densityDPI/160);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SC_Save:{
                for (int i=0, j = numHoles; i<j; i++){
                    TextView textView = (TextView) parRow.getChildAt(i);
                    if (textView.getText().toString().equals("")){
                        Toast.makeText(mContext, getString(R.string.allHoles), Toast.LENGTH_LONG);
                        break;
                    }
                }
                mListener.onScorecardCreated(holeList, totalPar.getText().toString());
                break;
            }
            default:{
                Dialogs dialog = Dialogs.newInstance(Dialogs.DialogType.PAR);
                Bundle args = new Bundle();
                args.putInt(Dialogs.VIEW_ID, view.getId());
                TextView textView = (TextView) parRow.findViewById(view.getId());
                args.putString(Dialogs.TEXT,textView.getText().toString());
                dialog.setArguments(args);
                dialog.setTargetFragment(this, 0);
                dialog.show(getFragmentManager(), "DF");
                break;
            }
        }
    }

    @Override
    public void onDialogOK(String result, int viewId, Dialogs dialog) {

        TextView textView = (TextView) parRow.findViewById(viewId);
        textView.setText(result);
        parTotal += Integer.parseInt(result);
        totalPar.setText(String.valueOf(parTotal));
        int position = parRow.indexOfChild(textView);

        final HoleItem hole = new HoleItem();
        hole.setHoleNumber(String.valueOf(position+1));
        hole.setHolePar(result);
        hole.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                holeList.add(hole.getObjectId());
            }
        });
        dialog.dismiss();
    }

    @Override
    public void onDialogCancel(Dialogs dialog) {
        dialog.dismiss();
    }
}
