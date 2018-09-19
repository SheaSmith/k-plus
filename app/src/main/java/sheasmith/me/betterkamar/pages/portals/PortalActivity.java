package sheasmith.me.betterkamar.pages.portals;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.DataActivity;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.RecyclerItemClickListener;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.addPortal.AddPortalActivity;
import sheasmith.me.betterkamar.pages.notices.NoticesFragment;

public class PortalActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PortalObject> servers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PortalActivity.this, AddPortalActivity.class);
                startActivityForResult(i, 1);
            }
        });

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PortalActivity.this, mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(PortalActivity.this, DataActivity.class);
                i.putExtra("portal", servers.get(position));
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        servers.add((PortalObject) data.getSerializableExtra("portal"));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
