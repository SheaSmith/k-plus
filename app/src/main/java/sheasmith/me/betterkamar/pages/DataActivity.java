package sheasmith.me.betterkamar.pages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.pages.details.DetailsFragment;
import sheasmith.me.betterkamar.pages.groups.GroupFragment;
import sheasmith.me.betterkamar.pages.notices.NoticesFragment;
import sheasmith.me.betterkamar.pages.results.ResultsFragment;
import sheasmith.me.betterkamar.pages.timetable.TimetableFragment;
import sheasmith.me.betterkamar.util.BottomNavigationViewHelper;

public class DataActivity extends AppCompatActivity {

    Integer lastFragment = null;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, getFragment(savedInstanceState.getInt("lastFragment")));
            transaction.commit();
        } else {
            //Manually displaying the first fragment - one time only
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, NoticesFragment.newInstance());
            transaction.commit();
            lastFragment = R.id.nav_notices;
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_data);
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = getFragment(item.getItemId());

                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });


        //Used to select an item programmatically
        //bottomNavigationView.getMenu().getItem(2).setChecked(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (lastFragment != null)
            outState.putInt("lastFragment", lastFragment);
        super.onSaveInstanceState(outState);
    }

    private Fragment getFragment(int id) {
        lastFragment = id;
        switch (id) {
            case R.id.nav_notices:
                return NoticesFragment.newInstance();
            case R.id.nav_calender:
                return TimetableFragment.newInstance();
            case R.id.nav_ncea:
                return ResultsFragment.newInstance();
            case R.id.nav_groups:
                return GroupFragment.newInstance();
            case R.id.nav_details:
                return DetailsFragment.newInstance();
            default:
                return null;
        }
    }
}