/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 14/03/19 8:36 PM
 */

package sheasmith.me.betterkamar.pages.portals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        SharedPreferences prefs = new SecurePreferences(this);

        Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
        if (jsonString != null) {
            for (String s : jsonString) {
                Gson gson = new Gson();
                PortalObject s1 = gson.fromJson(s, PortalObject.class);
                servers.add(s1);
            }
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
        }
        else {
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
        SharedPreferences.Editor editor = new SecurePreferences(this).edit();
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
