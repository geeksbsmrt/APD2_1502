package com.geeksbsmrt.puttputtpartner;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;

/**
 * Created by Adam on 2/27/2015.
 */
public class Dialogs extends DialogFragment {

    public static final String VIEW_ID = "viewId";
    public static final String TEXT = "text";

    public enum DialogType {
        SCORE,
        PLAYER,
        PAR
    }
    public static DialogType type;

    EditText input;

    public Dialogs(){}

    public interface onDialogFinished{
        void onDialogOK(String result, Dialogs dialog);
        void onDialogOK(String result, int viewId, Dialogs dialog) throws ParseException;
        void onDialogCancel(Dialogs dialog);
    }
    public interface createDialogFinished{
        void onDialogOK(String result, int viewId, Dialogs dialog);
        void onDialogCancel(Dialogs dialog);
    }

    onDialogFinished mListener;
    createDialogFinished cListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (onDialogFinished) getTargetFragment();
            cListener = (createDialogFinished) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onDialogFinished");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_enter, null);
        input = (EditText) view.findViewById(R.id.ED_input);
        switch (type){
            case SCORE:{
                if (getArguments().getString(TEXT).equals("")) {
                    input.setHint(getString(R.string.enterScore));
                } else {
                    input.setText(getArguments().getString(TEXT));
                }
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!input.getText().toString().equals("")) {
                            try {
                                mListener.onDialogOK(input.getText().toString(), getArguments().getInt(VIEW_ID), Dialogs.this);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogCancel(Dialogs.this);
                    }
                });
                break;
            }
            case PLAYER:{
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setHint(getString(R.string.playerName));
                builder.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!input.getText().toString().equals("")) {
                            mListener.onDialogOK(input.getText().toString(), Dialogs.this);
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onDialogCancel(Dialogs.this);
                    }
                });
                break;
            }
            case PAR:{
                if (getArguments().getString(TEXT).equals("")) {
                    input.setHint(getString(R.string.par));
                } else {
                    input.setText(getArguments().getString(TEXT));
                }
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(view).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!input.getText().toString().equals("")) {
                            cListener.onDialogOK(input.getText().toString(), getArguments().getInt(VIEW_ID), Dialogs.this);
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cListener.onDialogCancel(Dialogs.this);
                    }
                });
            }
            default:break;
        }
        return builder.create();
    }
    public static Dialogs newInstance(DialogType dType){
        type = dType;
        return new Dialogs();
    }
}
