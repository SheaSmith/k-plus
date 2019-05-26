/*
 * Created by Shea Smith on 26/05/19 9:35 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 26/05/19 9:35 PM
 */

package sheasmith.me.betterkamar.pages.about;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;

public class AboutActivity extends AppCompatActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About");

        KamarPlusApplication application = (KamarPlusApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("About");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "About", null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }
}
