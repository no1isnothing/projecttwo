package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeScreenCalendarFragment extends Fragment {
    Button mCalendarButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_screen_calendar, container, false);
        mCalendarButton = view.findViewById(R.id.welcome_calendar_button);

        mCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EntryCalendarActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
