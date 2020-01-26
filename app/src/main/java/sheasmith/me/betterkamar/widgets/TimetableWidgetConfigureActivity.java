/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 31/05/19 9:12 PM
 */

package sheasmith.me.betterkamar.widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.util.ArrayList;
import java.util.HashSet;
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
        SharedPreferences preferences = context.getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE);

        Set<String> jsonString;
        if (!preferences.contains("salt")) {
            // There are two possible salts, Build.SERIAL and Unknown. If it is neither we will nuke the data.

            SharedPreferences serialPrefs = new SecurePreferences(context, Build.SERIAL);
            jsonString = serialPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

            String salt = null;

            if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                jsonString = null;
            }
            else {
                salt = Build.SERIAL;
            }

            if (jsonString == null) {
                SharedPreferences unknownPrefs = new SecurePreferences(context, "UNKNOWN");

                jsonString = unknownPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

                if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                    jsonString = null;
                }
                else {
                    salt = "UNKNOWN";
                }
            }

            if (jsonString == null) {
                SharedPreferences unknownPrefs = new SecurePreferences(context, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

                jsonString = unknownPrefs.getStringSet("sheasmith.me.betterkamar.portals", null);

                if (jsonString == null || jsonString.isEmpty() || jsonString.toArray()[0] == null) {
                    jsonString = null;
                }
                else {
                    salt = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            }

            if (jsonString == null) {
                preferences.edit().putString("serial", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)).apply();
                new SecurePreferences(context).edit().remove("sheasmith.me.betterkamar.portals").apply();
                jsonString = new HashSet<>();
            }
            else {
                preferences.edit().putString("serial", salt).apply();
            }
        }
        else {

            jsonString = new SecurePreferences(context, preferences.getString("salt", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))).getStringSet("sheasmith.me.betterkamar.portals", null);
        }

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
        SharedPreferences preferences = getSharedPreferences("sheasmith.me.betterkamar", MODE_PRIVATE);

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
                new SecurePreferences(this).edit().remove("sheasmith.me.betterkamar.portals").apply();
                jsonString = new HashSet<>();
            }
            else {
                preferences.edit().putString("serial", salt).apply();
            }
        }
        else {

            jsonString = new SecurePreferences(this, preferences.getString("salt", Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))).getStringSet("sheasmith.me.betterkamar.portals", null);
        }
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

