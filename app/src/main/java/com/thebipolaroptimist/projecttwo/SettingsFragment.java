package com.thebipolaroptimist.projecttwo;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.models.SelectableWordData;
import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.CustomListPreference;
import com.thebipolaroptimist.projecttwo.views.IncidentDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

import java.util.Set;

public class SettingsFragment extends Fragment implements CustomListPreference.Listener {
    public static final String[] CATEGORIES_ARRAY = {MoodDetailRow.CATEGORY, ActivityDetailRow.CATEGORY, IncidentDetailRow.CATEGORY};
    public static final String PREFERENCE_PREFIX = "preference_";
    private SettingsFragmentViewModel mViewModel;
    public static final String CATEGORY_KEY = "category";

    public static String getLabel(String category)
    {
        if(category.equals(MoodDetailRow.CATEGORY))
        {
            return "Intensity";
        } else if(category.equals(ActivityDetailRow.CATEGORY))
        {
            return "Duration";
        }
        return "";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        LinearLayout layout = view.findViewById(R.id.fragment_settings_layout);
        mViewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel.class);

        for (final String category : CATEGORIES_ARRAY) {
            Button button = new Button(getActivity());
            button.setText(category + " Types");
            layout.addView(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString(CATEGORY_KEY, category);
                    CustomListPreference preference = new CustomListPreference(); //have just one for class?
                    preference.setArguments(bundle);
                    preference.setData(mViewModel.getDetails(category));
                    preference.setListener(SettingsFragment.this);
                    preference.show(getFragmentManager(), category);
                }
            });
        }
            return view;
    }

    @Override
    public void onPositiveResult(String category, Set<SelectableWordData> data) {
        mViewModel.setDetailsAndStore(category, data);
    }
}