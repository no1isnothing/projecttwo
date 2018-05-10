package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.thebipolaroptimist.projecttwo.R;

public class ConfirmDiscardDialog extends DialogFragment {

    public interface ConfirmDiscardDialogListener {
        void onConfirmDialogPositiveClick(DialogFragment dialog);
        void onConfirmDialogNegativeClick(DialogFragment dialog);
    }

    ConfirmDiscardDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (ConfirmDiscardDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ConfirmDiscardDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.discard_message)
                .setPositiveButton(R.string.discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onConfirmDialogPositiveClick(ConfirmDiscardDialog.this);
                    }
                })
                .setNegativeButton(R.string.save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onConfirmDialogNegativeClick(ConfirmDiscardDialog.this);
                    }
                });

        return builder.create();
    }

}
