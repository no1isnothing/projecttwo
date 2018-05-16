package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

public class ConfirmDeleteDialog extends DialogFragment {

    public interface ConfirmDeleteDialogListener
    {
        void onConfirmDeletePositiveClick();
    }

    private ConfirmDeleteDialogListener mListener;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);

        try
        {
            mListener = (ConfirmDeleteDialogListener) context;
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString()
                    + "must implement ConfirmDeleteDialogListener");
        }
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to permanently delete this entry?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onConfirmDeletePositiveClick();
                    }
                });
        return builder.create();
    }
}
