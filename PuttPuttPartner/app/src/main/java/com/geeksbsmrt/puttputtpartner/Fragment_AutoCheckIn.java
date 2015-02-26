package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Fragment_AutoCheckIn extends Fragment {

    private NfcAdapter nfcAdapter;

    public Fragment_AutoCheckIn() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (nfcAdapter == null){
            Toast.makeText(getActivity(), getString(R.string.noNFC), Toast.LENGTH_LONG).show();
        }

        if (!nfcAdapter.isEnabled()){
            Toast.makeText(getActivity(), getString(R.string.nfcDisabled), Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment__auto_check_in, container, false);

        MainActivity.actionBar.setTitle(R.string.app_name);
        setHasOptionsMenu(true);
        MainActivity.actionBar.setHomeButtonEnabled(true);
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);

        Button qr = (Button) rootView.findViewById(R.id.ACI_barcode);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(Fragment_AutoCheckIn.this);
                integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("ACI", "in results");
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null){
            String contents = result.getContents();
            if (contents != null){
                Log.i("ACI", contents);
                CourseItem course = MainActivity.getCourse(contents);
                if (!(course == null)) {
                    Fragment_CourseDesc cd = new Fragment_CourseDesc();
                    Bundle courseBundle = new Bundle();
                    courseBundle.putSerializable("course", course);
                    cd.setArguments(courseBundle);
                    getFragmentManager().beginTransaction().replace(R.id.container, cd).addToBackStack(null).commit();
                } else {
                    Log.i("ACI", "course null");
                }
            } else {
                Log.i("ACI", "Contents null");
            }
        } else {
            Log.i("ACI", "results null");
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
