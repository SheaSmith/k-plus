package sheasmith.me.betterkamar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class Servers extends AppCompatActivity {
    private static List<ServersObject> serversList = new ArrayList<>();

    public static List<ServersObject> getServersList() {
        return serversList;
    }
    private static List<String> titleList = new ArrayList<>();

    public static List<String> getTitleList() {
        return titleList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Portals");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servers);
        View empty = findViewById(R.id.empty);
        ListView list = (ListView) findViewById(R.id.servers);
        list.setEmptyView(empty);
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.addButton);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Servers.this, AddServer.class);
                startActivity(k);
            }
        });
        if (serversList.isEmpty()) {
            SharedPreferences prefs = new SecurePreferences(this);

            Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.servers", null);
            if (jsonString != null) {
                for (String s : jsonString) {
                    Gson gson = new Gson();
                    ServersObject s1 = gson.fromJson(s, ServersObject.class);
                    getServersList().add(s1);
                }
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView list = (ListView) findViewById(R.id.servers);
        titleList.clear();
        for (ServersObject k : serversList) {
            titleList.add(k.title);
        }
        list.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, titleList));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                Intent k = new Intent(Servers.this, NoticesPage.class);
                k.putExtra("listID", pos);
                startActivity(k);
            }});

    }
}
