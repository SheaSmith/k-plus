/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 14/03/19 6:41 PM
 */

package sheasmith.me.betterkamar.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.EventsObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;
import sheasmith.me.betterkamar.util.WidgetApiManager;

/**
 * Created by TheDiamondPicks on 18/01/2019.
 */
public class TimetableWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Intent mIntent;
    public List<EventsObject.Event> mEvents = new ArrayList<>();
    public List<EventsObject.Event> dayEvents = new ArrayList<>();
    public List<PortalObject> servers;
    public List<TimetableObject.Week> timetable;
    List<TimetableObject.Class> periods = new ArrayList<>();
    public List<CalendarObject.Day> days = new ArrayList<>();
    public List<GlobalObject.PeriodDefinition> mPeriodDefinitions;
    PortalObject portal = null;

    public String error = null;

    public TimetableWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mIntent = intent;

        String key = "NOTHING_RANDOM_DATA_SHOULD_RETURN_NO_PORTAL";

        try {
            key = TimetableWidgetConfigureActivity.loadTitlePref(mContext, Integer.valueOf(intent.getData().getSchemeSpecificPart()));
        }
        catch (NullPointerException e) {
            // DO NOTHING
        }

        SharedPreferences prefs = new SecurePreferences(mContext);

        servers = new ArrayList<>();

        Set<String> jsonString = prefs.getStringSet("sheasmith.me.betterkamar.portals", null);
        if (jsonString != null) {
            for (String s : jsonString) {
                Gson gson = new Gson();
                PortalObject s1 = gson.fromJson(s, PortalObject.class);
                servers.add(s1);
            }
        }


        for (PortalObject p : servers) {
            if ((p.username + "%" + p.schoolName).equals(key)) {
                portal = p;
                break;
            }
        }
    }

    @Override
    public void onCreate() {


    }

    private void doRequest(final PortalObject portal) {
        try {
            WidgetApiManager.setVariables(portal, mContext);
            WidgetApiManager.login(portal.username, portal.password);
            WidgetApiManager.setVariables(portal, mContext);
            mPeriodDefinitions = WidgetApiManager.getGlobals(false).GlobalsResults.PeriodDefinitions;
            timetable = WidgetApiManager.getTimetable(false).StudentTimetableResults.Student.Timetable;
            days = WidgetApiManager.getCalendar(false).CalendarResults.Days;
            mEvents = WidgetApiManager.getEvents(Calendar.getInstance().get(Calendar.YEAR), false).EventsResults.Events;
        } catch (Exception e) {
            handleError(portal, e);
        }


    }

    private void handleError(final PortalObject portal, Exception e) {
        if (e instanceof Exceptions.ExpiredToken) {
            ApiManager.login(portal.username, portal.password, new ApiResponse<LoginObject>() {
                @Override
                public void success(LoginObject value) {
                    doRequest(portal);
                }

                @Override
                public void error(Exception e) {
                    e.printStackTrace();
                }
            });
        } else if (e instanceof IOException) {
            error = "You do not appear to be connected to the internet. Please check your connection and try again.";
        } else if (e instanceof Exceptions.AccessDenied) {
            error = "Your school has disabled access to the timetable. You may still be able to view it via the web portal.";
        } else {
            error = "An unknown error occurred";
            e.printStackTrace();
        }
    }

    @Override
    public void onDataSetChanged() {


        if (portal != null) {
            error = null;
            doRequest(portal);

            final Date date = new Date();

            Calendar current = Calendar.getInstance();
            current.setTime(date);

            periods = new ArrayList<>();
            Calendar monday = Calendar.getInstance();
            monday.setTime(date);
            monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            int weekNumber = 0;
            final Calendar temp = Calendar.getInstance();

            for (CalendarObject.Day day : days) {
                try {
                    Date start = format.parse(day.Date);
                    temp.setTime(start);
                    if (current.get(Calendar.DAY_OF_YEAR) == temp.get(Calendar.DAY_OF_YEAR) && !day.WeekYear.equals("") && !day.Status.equals("Holidays")) {
                        weekNumber = Integer.parseInt(day.WeekYear);
                        break;
                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            if (weekNumber == 0) {
                error = "No timetable entries for today.";
            } else {

                weekLoop:
                for (TimetableObject.Week week : timetable) {
                    if (week.WeekNumber == weekNumber) {
                        for (Integer day : week.Classes.keySet()) {
                            Calendar cal = Calendar.getInstance();
                            cal.setFirstDayOfWeek(Calendar.SUNDAY);
                            cal.setTime(date);
                            cal.set(Calendar.HOUR_OF_DAY, 0);
                            cal.set(Calendar.MINUTE, 0);
                            cal.set(Calendar.SECOND, 0);
                            cal.set(Calendar.MILLISECOND, 0);
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            cal.add(Calendar.DAY_OF_WEEK, day);

                        if (cal.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
                                periods = week.Classes.get(day);
                                break weekLoop;
                            }
                        }
                    }
                }

                dayEvents = new ArrayList<>();

                for (EventsObject.Event event : mEvents) {
                    try {
                        Date start = format.parse(event.Start);
                        Date end = format.parse(event.Finish);

                        Calendar c = Calendar.getInstance();
                        c.setTime(start);
                        int startDay = c.get(Calendar.DAY_OF_YEAR);

                        c.setTime(end);
                        int endDay = c.get(Calendar.DAY_OF_YEAR);

                        c.setTime(date);
                        int currentDay = c.get(Calendar.DAY_OF_YEAR);

                        if (startDay >= currentDay && endDay <= currentDay) {
                            dayEvents.add(event);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (periods == null) {
                    throw new RuntimeException();
                }

                if (dayEvents.size() == 0 && periods.size() == 0) {
                    error = "No timetable entries for today.";
                }
            }


        } else {
            error = "Portal has been deleted!";
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (dayEvents != null && mPeriodDefinitions != null && days != null && periods != null) {
            if (dayEvents.size() + periods.size() == 0)
                return 1;
            return dayEvents.size() + periods.size();
        } else if (error == null) {
            return 1;
        } else {
            return 1;
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        if (error != null) {
            RemoteViews errorView = new RemoteViews(mContext.getPackageName(), R.layout.timetable_widget_error);
            errorView.setTextViewText(R.id.error, error);
            return errorView;
        } else {

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.adapter_timetable);

            rv.setViewVisibility(R.id.attendance, View.GONE);

            if (position < dayEvents.size()) {
                final EventsObject.Event event = dayEvents.get(position);

                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                rv.setTextViewText(R.id.title, event.Title);
                String time = "All day";
                if (!event.DateTimeStart.equals("") && !event.DateTimeFinish.equals("")) {
                    try {
                        String start = timeFormat.format(hourFormat.parse(event.DateTimeStart));
                        String end = timeFormat.format(hourFormat.parse(event.DateTimeFinish));
                        time = start + " - " + end;

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                } else if (!event.DateTimeStart.equals("")) {
                    try {
                        time = timeFormat.format(hourFormat.parse(event.DateTimeStart));

                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                rv.setTextViewText(R.id.details, time);

                rv.setInt(R.id.item, "setBackgroundColor", Color.parseColor("#5677fc"));


                if (position == 0)
                    rv.setTextViewText(R.id.time, "Events");
                else
                    rv.setTextViewText(R.id.time, "Visible");

            } else {
                final int pos = position - dayEvents.size();
                Log.d("Widget", position + " " + dayEvents.size());

                final TimetableObject.Class period = periods.get(pos);
                final GlobalObject.PeriodDefinition periodDefinition = mPeriodDefinitions.get(pos);
                rv.setTextViewText(R.id.time, periodDefinition.PeriodName);

                if (!period.SubjectCode.equals("")) {
                    rv.setTextViewText(R.id.title, period.SubjectCode);
                    rv.setTextViewText(R.id.details, String.format("%s • %s • %s", periodDefinition.PeriodTime, period.Teacher, period.Room));
                    final TypedArray colors = mContext.getResources().obtainTypedArray(R.array.mdcolor_500);
                    final int number = Math.abs(period.SubjectCode.hashCode()) % colors.length();
                    rv.setInt(R.id.item, "setBackgroundColor", colors.getColor(number, Color.BLACK));
                    rv.setViewVisibility(R.id.item, View.VISIBLE);

                } else {
                    rv.setViewVisibility(R.id.item, View.GONE);
                }
            }

            return rv;
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return new RemoteViews(mContext.getPackageName(), R.layout.timetable_widget_loading);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}