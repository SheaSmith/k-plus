/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 18/02/19 7:50 PM
 */

package sheasmith.me.betterkamar.pages.details;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.util.TabViewPagerAdapter;

public class DetailsFragment extends Fragment {

    private View mView;
    private TabLayout tabLayout;
    private ViewPager firstViewPager;
    private TabViewPagerAdapter mAdapter;

    public static DetailsFragment newInstance() {
        return new DetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setElevation(0);
            requireActivity().setTitle("Details");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isAdded()) {
            // Inflate the layout for this fragment
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");
            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
            mView = localInflator.inflate(R.layout.fragment_details, container, false);
            firstViewPager = mView.findViewById(R.id.pager);

            tabLayout = mView.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(firstViewPager);
            tabLayout.setTabTextColors(getResources().getColor(R.color.colorHintTextLight),
                    getResources().getColor(R.color.colorPrimaryTextLight));
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = contextThemeWrapper.getTheme();
            theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
            tabLayout.setBackgroundColor(typedValue.data);

            setupViewPager(firstViewPager);
        }

        return mView;
    }

    private void setupViewPager(ViewPager viewPager) {
        if (isAdded()) {
            mAdapter = new TabViewPagerAdapter(requireActivity().getSupportFragmentManager());
            mAdapter.addFragment(StudentDetailsFragment.newInstance(), "Student");
            mAdapter.addFragment(CaregiverDetailsFragment.newInstance(), "Caregivers");
            mAdapter.addFragment(EmergencyDetailsFragment.newInstance(), "Emergency");
            mAdapter.addFragment(MedicalDetailsFragment.newInstance(), "Medical");
            viewPager.setAdapter(mAdapter);
        }
    }
}