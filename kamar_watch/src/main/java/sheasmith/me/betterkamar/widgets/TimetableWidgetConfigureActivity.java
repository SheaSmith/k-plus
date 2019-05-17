/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * The configuration screen for the {@link TimetableWidget TimetableWidget} AppWidget.
 */
public class TimetableWidgetConfigureActivity extends Activity {

    private static final String PREFS_NAME = "sheasmith.me.betterkamar.widgets.TimetableWidget";
    private static final String PREF_PREFIX_KEY = "timetable_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private List<PortalObject> servers = new ArrayList<>();

    public TimetableWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    public static PortalObject getPortal(Context context, int appWidgetId) {
        SharedPreferences prefs = new SecurePreferences(context);

        Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
        if (jsonString != null) {
            for (String s : jsonString) {
                Gson gson = new Gson();
                PortalObject s1 = gson.fromJson(s, PortalObject.class);
                if ((s1.username + "%" + s1.schoolName).equalsIgnoreCase(loadTitlePref(context, appWidgetId))) {
                    return s1;
                }
            }
        }

        return null;
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.timetable_widget_configure);
        SharedPreferences prefs = new SecurePreferences(this);

        Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
        if (jsonString != null) {
            for (String s : jsonString) {
                Gson gson = new Gson();
                PortalObject s1 = gson.fromJson(s, PortalObject.class);
                servers.add(s1);
            }
        }

        ArrayAdapter<PortalObject> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, servers);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        ListView list = findViewById(R.id.portals);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Context context = TimetableWidgetConfigureActivity.this;

                // When the button is clicked, store the string locally
                String serverId = servers.get(i).username + "%" + servers.get(i).schoolName;
                saveTitlePref(context, mAppWidgetId, serverId);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                TimetableWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }
}

