package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.IncidentDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends FragmentActivity {
    private static final String TAG = "WelcomeActivity";
    SettingsManager mSettingsManager;

    private ViewPager mViewPager;
    private FragmentStatePagerAdapter mViewPagerAdapter;
    private Fragment[] mLayouts;
    private LinearLayout mDotsLayout;
    private Button mSkipButton, mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        mSettingsManager = new SettingsManager(getApplication());

        if(mSettingsManager.isFirstLaunch())
        {
            Log.i(TAG, "First launch");
            mSettingsManager.setupDefaultDetails();

            mViewPager = findViewById(R.id.view_pager);
            mDotsLayout = findViewById(R.id.layoutDots);
            mSkipButton = findViewById(R.id.btn_skip);
            mNextButton = findViewById(R.id.btn_next);

            mLayouts = new Fragment[]{
                    new WelcomeScreenSettingsFragment(),
                    new WelcomeScreenEntryFragment(),
                    new WelcomeScreenCalendarFragment()};

            // adding bottom mDots
            addBottomDots(0);

            mViewPagerAdapter = new WelcomeViewPageAdapter(getSupportFragmentManager(), mLayouts);
            mViewPager.setAdapter(mViewPagerAdapter);
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    addBottomDots(position);

                    // changing the next button text 'NEXT' / 'GOT IT'
                    if (position == mLayouts.length - 1) {
                        // last page. make button text to GOT IT
                        mNextButton.setText(getString(R.string.start));
                        mSkipButton.setVisibility(View.GONE);
                    } else {
                        // still pages are left
                        mNextButton.setText(getString(R.string.next));
                        mSkipButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            mSkipButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchHomeScreen();
                }
            });

            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // checking for last page
                    // if last page home screen will be launched
                    int current = getItem(+1);
                    if (current < mLayouts.length) {
                        // move to next screen
                        mViewPager.setCurrentItem(current);
                    } else {
                        launchHomeScreen();
                    }
                }
            });
        } else {
            launchHomeScreen();
        }
    }

    private void addBottomDots(int currentPage) {
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        mDotsLayout.removeAllViews();
        for (int i = 0; i < mLayouts.length; i++) {
            ImageView imageView = new ImageView(this);
            GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.circle);
            imageView.setMinimumHeight(31);
            imageView.setMinimumWidth(35);
            imageView.setPadding(2,0,2,0);
            drawable.setColor(colorsInactive[currentPage]);
            imageView.setImageDrawable(drawable);
            mDotsLayout.addView(imageView);
        }

        if (mLayouts.length > 0) {
            ImageView view = (ImageView) mDotsLayout.getChildAt(currentPage);
            GradientDrawable drawables = (GradientDrawable)  view.getDrawable();
            drawables.setColor(colorsActive[currentPage]);
        }
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        mSettingsManager.setFirstLaunch(false);
        Intent intent = new Intent(this, EntryCalendarActivity.class);
        startActivity(intent);
        finish();
    }
}

