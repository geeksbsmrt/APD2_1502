package com.geeksbsmrt.puttputtpartner;

import android.app.Fragment;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
                //TODO: Remove toast message and complete Parse DB integration.
                //Static set text in toast message as it will not be present in final build.
                Toast.makeText(getActivity(), "Barcode functionality will be enabled in a future release.", Toast.LENGTH_LONG).show();
            }
        });

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
