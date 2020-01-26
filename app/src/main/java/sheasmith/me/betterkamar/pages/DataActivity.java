/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.palette.graphics.Palette;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.details.DetailsFragment;
import sheasmith.me.betterkamar.pages.groups.GroupFragment;
import sheasmith.me.betterkamar.pages.notices.NoticesFragment;
import sheasmith.me.betterkamar.pages.results.ResultsFragment;
import sheasmith.me.betterkamar.pages.timetable.TimetableFragment;
import sheasmith.me.betterkamar.util.BottomNavigationViewHelper;
import sheasmith.me.betterkamar.util.ThemeColours;

public class DataActivity extends AppCompatActivity {

    Integer lastFragment = null;
    boolean firstRun = true;

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

        SharedPreferences sharedPreferences = getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        setTheme(getResources().getIdentifier("T_" + stringColor, "style", getPackageName()));

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

        SharedPreferences preferences = getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(preferences.getInt("night-mode", 1));

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
                        transaction.commitAllowingStateLoss();
                        return true;
                    }
                });

        if (savedInstanceState == null) {
            final PortalObject portal = (PortalObject) getIntent().getSerializableExtra("portal");
            String schoolPathName = getFilesDir().toString() + "/" + portal.schoolFile;
            File image = new File(schoolPathName);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
            Palette palette = Palette.from(bitmap).generate();
            int color = palette.getVibrantColor(getResources().getColor(R.color.colorAccent));
            ThemeColours.setNewThemeColor(this, Color.red(color), Color.green(color), Color.blue(color));
            firstRun = false;
        }


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

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(preferences.getInt("night-mode", 1));
    }
}