package com.thebipolaroptimist.projecttwo.views;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CustomListPreference extends DialogPreference {
    public static final String TAG ="CustomListPreference";
    Set<String> mValues;

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "constructor");
        setDialogLayoutResource(R.layout.custom_list_preference_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        mValues = new HashSet<>();

    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);
        Log.i(TAG, "OnBindViewDialog");
        Button button = view.findViewById(R.id.pref_list_add_button);
        final EditText editText = view.findViewById(R.id.pref_list_edit_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show text view?
                final String newType = editText.getText().toString();
                if(!newType.isEmpty())
                {
                    mValues.add(newType);
                    final LinearLayout layout = view.findViewById(R.id.pref_list);
                    layout.addView(new RemoveableRow(getContext(), newType, new RemoveableRow.OnClickListener() {
                        @Override
                        public void onClick() {
                            mValues.remove(newType);
                            reloadList(layout);
                        }
                    }));
                }
            }
        });

        final LinearLayout layout = view.findViewById(R.id.pref_list);

        final List<String> array = new ArrayList<>(mValues);

        for (final String s : array) {
            layout.addView(new RemoveableRow(getContext(), s, new RemoveableRow.OnClickListener() {
                @Override
                public void onClick() {
                    mValues.remove(s);
                    reloadList(layout);
                }
            }));
        }
    }

    protected void reloadList(View view)
    {
        LinearLayout layout = view.findViewById(R.id.pref_list);

        layout.removeAllViews();

        for (final String s : mValues) {
            layout.addView(new RemoveableRow(getContext(), s, new RemoveableRow.OnClickListener() {
                @Override
                public void onClick() {
                    mValues.remove(s);
                }
            }));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        Log.i(TAG, "OnDialogClosed");
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                persistStringSet(mValues);
            } else
            {
                persistString(""); //TODO fill this out
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        Log.i(TAG, "Set initial value");
        if (restorePersistedValue) {
            // Restore existing state
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mValues = this.getPersistedStringSet(new HashSet<String>());
            } else
            {
                mValues = new HashSet<>(); //TODO fill this out
            }
        } else {
            // Set default state from the XML attribute
            mValues = (Set<String>) defaultValue;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                persistStringSet(mValues);
            } else
            {
                persistString(""); //TODO fill this out
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        Log.i(TAG, "ONGetDefaultVAlue");
        return super.onGetDefaultValue(a, index);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Log.i(TAG, "OnSaveInstanceState");
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        myState.value = (String []) mValues.toArray();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Log.i(TAG, "OnRestoreInstanceState");
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        mValues = new HashSet<String>(Arrays.asList(myState.value));
    }

    private static class SavedState extends BaseSavedState {
        String[] value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            source.readStringArray(value);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeStringArray(value);
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}


