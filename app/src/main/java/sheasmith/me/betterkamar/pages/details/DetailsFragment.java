package sheasmith.me.betterkamar.pages.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.TabViewPagerAdapter;
import sheasmith.me.betterkamar.pages.results.AllResultsFragment;
import sheasmith.me.betterkamar.pages.results.NCEAFragment;
import sheasmith.me.betterkamar.pages.results.NZQAFragment;
import sheasmith.me.betterkamar.pages.results.ReportFragment;

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
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        getActivity().setTitle("Details");
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_details, container, false);
        firstViewPager = mView.findViewById(R.id.pager);

        tabLayout = mView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(firstViewPager);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorHintTextLight),
                getResources().getColor(R.color.colorPrimaryTextLight));

        setupViewPager(firstViewPager);

        return mView;
    }

    private void setupViewPager(ViewPager viewPager) {
        mAdapter = new TabViewPagerAdapter(getActivity().getSupportFragmentManager());
        mAdapter.addFragment(StudentDetailsFragment.newInstance(), "Student");
        mAdapter.addFragment(CaregiverDetailsFragment.newInstance(), "Caregivers");
        mAdapter.addFragment(EmergencyDetailsFragment.newInstance(), "Emergency");
        mAdapter.addFragment(MedicalDetailsFragment.newInstance(), "Medical");
        viewPager.setAdapter(mAdapter);
    }
}
