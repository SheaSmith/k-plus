/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.portals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.fabric.sdk.android.Fabric;
import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.about.AboutActivity;
import sheasmith.me.betterkamar.pages.addPortal.AddPortalActivity;

public class PortalActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PortalObject> servers = new ArrayList<>();
    private Tracker mTracker;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_portal, menu);
        return true;
    }

    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Intent i = new Intent(this, AboutActivity.class);
            startActivity(i);
        } else if (id == R.id.action_theme) {
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }

            SharedPreferences.Editor editor = getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE).edit();
            editor.putInt("night-mode", AppCompatDelegate.getDefaultNightMode());
            editor.apply();

            recreate();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

        KamarPlusApplication application = (KamarPlusApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Portal List");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        mTracker.enableAdvertisingIdCollection(true);

        setTitle("Portals");

        mRecyclerView = (RecyclerView) findViewById(R.id.servers);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        SharedPreferences preferences = getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE);
        AppCompatDelegate.setDefaultNightMode(preferences.getInt("night-mode", 1));

        SharedPreferences prefNoSalt = new SecurePreferences(this, "");

        Set<String> jsonString;
        if (!preferences.contains("salt")) {
            // There are two possible salts, Build.SERIAL and Unknown. If it is neither we will nuke the data.

            SharedPreferences serialPrefs = new SecurePreferences(this, Build.SERIAL);
            jsonString = serialPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

            String salt = null;

            if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                jsonString = null;
            }
            else {
                salt = Build.SERIAL;
            }

            if (jsonString == null) {
                SharedPreferences unknownPrefs = new SecurePreferences(this, "UNKNOWN");

                jsonString = unknownPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

                if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                    jsonString = null;
                }
                else {
                    salt = "UNKNOWN";
                }
            }

            if (jsonString == null) {
                SharedPreferences unknownPrefs = new SecurePreferences(this, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

                jsonString = unknownPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

                if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                    jsonString = null;
                }
                else {
                    salt = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }

            if (jsonString == null) {
                preferences.edit().putString("serial", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).apply();
                prefNoSalt.edit().remove("sheasmith.me.betterkamar.portals").apply();
                jsonString = new HashSet<>();
            }
            else {
                preferences.edit().putString("serial", salt).apply();
            }
        }
        else {

            jsonString = new SecurePreferences(this, preferences.getString("salt", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))).getStringSet("sheasmith.me.betterkamar.portals", null);
        }

//        // TODO remove this in future version & target api level 28
//        // TODO Dialog should be shown for direct upgrades from 3.1.x -> new unsalted version (3.2.x?)
//        String serial = Build.SERIAL;
//        if (!serial.equals("UNKNOWN") && prefNoSalt.contains("sheasmith.me.betterkamar.portals") && !preferences.contains("serial"))
//            preferences.edit().putString("serial", serial).apply();
//
//        String salt = null;
//
//        if (preferences.contains("serial"))
//            salt = preferences.getString("serial", null);
//
//
//        if (salt == null)
//            salt = Settings.Secure.getString(getContentResolver(),
//                    Settings.Secure.ANDROID_ID);
//
//
//        SharedPreferences prefs = new SecurePreferences(this, salt);
//
//        Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
//        if (jsonString != null && !jsonString.isEmpty() && jsonString.toArray()[0] == null) {
//            prefs = new SecurePreferences(this, "UNKNOWN");
//            jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
//        }

        for (String s : jsonString) {
            Gson gson = new Gson();
            PortalObject s1 = gson.fromJson(s, PortalObject.class);
            servers.add(s1);
        }

        // specify an adapter (see also next example)
        mAdapter = new PortalAdapter(servers, this);
        mRecyclerView.setAdapter(mAdapter);

        if (servers.size() == 0) {
            findViewById(R.id.empty).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.empty).setVisibility(View.GONE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PortalActivity.this, AddPortalActivity.class);
                startActivityForResult(i, 1);
            }
        });

        Fabric.with(this, new Crashlytics());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && data != null) {
            servers.add((PortalObject) data.getSerializableExtra("portal"));
            mAdapter.notifyDataSetChanged();

            if (servers.size() == 0) {
                findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else {
                findViewById(R.id.empty).setVisibility(View.GONE);
            }

            save();
        } else {
            if (data != null) {
                if (!data.getBooleanExtra("isDeleted", false))
                    servers.set(data.getIntExtra("index", -1), (PortalObject) data.getSerializableExtra("portal"));
                else
                    servers.remove(data.getIntExtra("index", -1));
                mAdapter.notifyDataSetChanged();
                save();

                if (servers.size() == 0) {
                    findViewById(R.id.empty).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.empty).setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void save() {
        SharedPreferences.Editor editor = new SecurePreferences(this, getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE).getString("serial", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))).edit();
        Set<String> jsonSet = new HashSet<>();
        for (PortalObject portal : servers) {
            Gson gson = new Gson();
            String json = gson.toJson(portal);
            jsonSet.add(json);
        }
        editor.putStringSet("sheasmith.me.betterkamar.portals", jsonSet);
        editor.apply();
    }
}
