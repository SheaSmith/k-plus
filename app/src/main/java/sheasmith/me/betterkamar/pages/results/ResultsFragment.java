package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.net.Uri;
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

public class ResultsFragment extends Fragment {

    private View mView;
    private TabLayout tabLayout;
    private ViewPager firstViewPager;
    private TabViewPagerAdapter mAdapter;

    public static ResultsFragment newInstance() {
        return new ResultsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        getActivity().setTitle("Results");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_results, container, false);
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
        mAdapter.addFragment(AllResultsFragment.newInstance(), "All Results");
        mAdapter.addFragment(NCEAFragment.newInstance(), "NCEA");
        mAdapter.addFragment(NZQAFragment.newInstance(), "Qualifications");
        mAdapter.addFragment(ReportFragment.newInstance(), "Reports");
        viewPager.setAdapter(mAdapter);
    }
}
