package sheasmith.me.betterkamar.pages.results;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.util.TabViewPagerAdapter;

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
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getActivity().setTheme(R.style.NoActionBarShadow);
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        getActivity().setTitle("Results");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            new AlertDialog.Builder(getContext())
                    .setTitle("NCEA Information")
                    .setMessage("Stored results may not be confirmed with NZQA and should not be considered official. To attain a Merit or Excellence endorsement 50 merit or excellence credits are needed. To achieve NCEA, 60 credits, plus 20 credits from the previous level are needed (80 credits in Level 1). For more information about NCEA please contact your school or visit NZQA's website: http://nzqa.govt.nz\n\nThe university entrance literacy field indicates if the student has  met the requirements for UE literacy. If \"No\", the current progress is shown as follows: [reading credits/writing credits/reading OR writing credits]. An [M] indicates obtaining UE Literacy with credits in Maori assessments. \n\nThe Level 1 Literacy field indicates if the student has met the requirements for Level 1 Literacy. If \"No\", the current progress is shown as follows: [pre-2011 requirements credits/2012 achievement standard credits/2012 unit standard credits].\n\nThe Numeracy field indicates if the student has met the requirements for Numeracy. If \"No\", the progress towards numeracy is shown, giving a number of credits contributing so far.")
                    .setPositiveButton("Close", null)
                    .create()
                    .show();
        }
        else if (item.getItemId() == 2) {
            new AlertDialog.Builder(getContext())
                    .setTitle("NZQA Qualifications Information")
                    .setMessage("These qualifications and endorsements have been officially awarded by NZQA. They should properly reflect official records.")
                    .setPositiveButton("Close", null)
                    .create()
                    .show();
        }
        else if (item.getItemId() == 3) {
            setStatusBarColor(R.color.colorPrimary);
            String url = "https://nzqa.govt.nz";
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getContext().getResources().getColor(R.color.colorPrimary));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getContext(), Uri.parse(url));

        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatusBarColor(int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getContext(), colorId));
        }
    }


        @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        if (firstViewPager.getCurrentItem() == 1) {
            menu.add(0, 3, 0, "NZQA Website").setIcon(R.drawable.ic_nzqa).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, 1, 0, "NCEA Information").setIcon(R.drawable.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        else if (firstViewPager.getCurrentItem() == 2) {
            menu.add(0, 3, 0, "NZQA Website").setIcon(R.drawable.ic_nzqa).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add(0, 2, 0, "NCEA Information").setIcon(R.drawable.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
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

        firstViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
