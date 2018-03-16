package com.thebipolaroptimist.projecttwo.views;

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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * Custom Preference Class
 * Contains a list of string values
 * The user can add items to the list with an edit text and a button
 * The user can remove items from the list with a button
 */
public class CustomListPreference extends DialogPreference {
    public static final String TAG ="CustomListPreference";
    Set<String> mValues;
    LinearLayout mLayout;

    public CustomListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.custom_list_preference_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        mValues = new HashSet<>();
    }

    @Override
    protected void onBindDialogView(final View view) {
        super.onBindDialogView(view);
        Button addItemButton = view.findViewById(R.id.pref_list_add_button);
        final EditText editText = view.findViewById(R.id.pref_list_edit_text);
        mLayout = view.findViewById(R.id.pref_list);

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = editText.getText().toString();
                if(!item.isEmpty())
                {
                    mValues.add(item);
                    mLayout.addView(new ActionRow(getContext(), item, new ActionRow.OnClickListener() {
                        @Override
                        public void onClick() {
                            mValues.remove(item);
                            reloadList();
                        }
                    }));
                }
            }
        });
        reloadList();
    }

    protected void reloadList()
    {
        mLayout.removeAllViews();

        for (final String s : mValues) {
            mLayout.addView(new ActionRow(getContext(), s, new ActionRow.OnClickListener() {
                @Override
                public void onClick() {
                    mValues.remove(s);
                    reloadList();
                }
            }));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                persistStringSet(mValues);
            } else
            {
                //TODO add java 8 replace with join
                StringBuilder builder = new StringBuilder();
                for (String mValue : mValues) {
                    builder.append(mValue).append(",");
                }
                boolean result = persistString(builder.toString());
                Log.i(TAG, "REsult " + result);
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            // Restore existing state
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mValues = this.getPersistedStringSet(new HashSet<String>());
            } else
            {
                String values = this.getPersistedString("");
                mValues = new HashSet<String>(Arrays.asList(values.split(",")));
            }
        } else {
            Log.i(TAG, "Not persisting values");
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return super.onGetDefaultValue(a, index);
    }

    //TODO test this code. Wrote it based on the android docs
    @Override
    protected Parcelable onSaveInstanceState() {
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
        myState.value = mValues.toArray(new String[mValues.size()]);
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
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
        mValues = new HashSet<>(Arrays.asList(myState.value));
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


