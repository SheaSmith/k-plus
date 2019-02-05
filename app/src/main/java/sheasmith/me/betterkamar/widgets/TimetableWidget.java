/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TimetableWidgetConfigureActivity TimetableWidgetConfigureActivity}
 */
public class TimetableWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.timetable_widget);
        Intent intent = new Intent(context, TimetableWidgetService.class);
        PortalObject p = TimetableWidgetConfigureActivity.getPortal(context, appWidgetId);
        if (p != null)
            views.setTextViewText(R.id.student, p.student + " (" + p.schoolName + ")");
        intent.setData(Uri.fromParts("content", appWidgetId + "", null));
        views.setRemoteAdapter(R.id.timetable, intent);

//        views.setBoolean(R.id.timetable, "setEnabled", false);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            TimetableWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

