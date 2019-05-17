/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.DiskCache;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sheasmith.me.betterkamar.BuildConfig;
import sheasmith.me.betterkamar.dataModels.AttendanceObject;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.EventsObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 5/09/2018.
 */

public class WidgetApiManager {

    public static String TOKEN;
    public static String ID;
    public static String PASSWORD;
    public static String URL;

    private static DiskCache diskCache;
    private static com.iainconnor.objectcache.CacheManager cacheManager;

    public static void setVariables(final PortalObject portal, Context context) {
        TOKEN = portal.key;
        ID = portal.username;
        PASSWORD = portal.password;
        URL = portal.hostname + "/api/api.php";

        String cachePath = context.getCacheDir().getPath();
        File cacheFile = new File(cachePath + File.separator + BuildConfig.APPLICATION_ID + File.separator);

        try {
            diskCache = new DiskCache(cacheFile, BuildConfig.VERSION_CODE, 1024 * 1024 * 10);
            cacheManager = com.iainconnor.objectcache.CacheManager.getInstance(diskCache);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Logon to KAMAR
     *
     * @param username Username to use
     * @param password Password to use
     */
    public static LoginObject login(final String username, final String password) throws Exception {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "Command=Logon&Key=vtku&Username=" + username + "&Password=" + password);
        try {
            Request request = new Request.Builder()
                    .url(URL)
                    .post(body)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "KAMAR+ " + BuildConfig.VERSION_NAME)
                    .addHeader("X-Requested-With", "nz.co.KAMAR")
                    .addHeader("Origin", "file://")
                    .build();


            try {
                Response response = client.newCall(request).execute();
                String xml = response.body().string();

                LoginObject loginObject = new LoginObject(xml);
//                       TOKEN = login.getKey();
                ID = username;
                PASSWORD = password;
                TOKEN = loginObject.LogonResults.Key;
                return loginObject;

            } catch (Exception e) {
                throw e;
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }
    }

    public static GlobalObject getGlobals(boolean ignoreCache) throws Exception {
        GlobalObject cache = (GlobalObject) cacheManager.get("GlobalObject" + ID, GlobalObject.class, new TypeToken<GlobalObject>() {
        }.getType());
        if (cache != null && !ignoreCache) {
            return cache;
        }

        if (TOKEN != null) {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=GetGlobals&Key=" + TOKEN);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR+ " + BuildConfig.VERSION_NAME)
                        .addHeader("X-Requested-With", "nz.co.KAMAR")
                        .addHeader("Origin", "file://")
                        .build();

                Response response = client.newCall(request).execute();

                String xml = response.body().string();
                GlobalObject globals = new GlobalObject(xml);
                cacheManager.put("GlobalObject" + ID, globals, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_MONTH.asSeconds(), false);
                return globals;

            } catch (Exception e) {
                throw e;
            }

        } else {
            throw new Exceptions.InvalidToken();
        }
    }

    public static EventsObject getEvents(final int year, boolean ignoreCache) throws Exception {
        EventsObject cache = (EventsObject) cacheManager.get("EventsObject" + year + ID, EventsObject.class, new TypeToken<EventsObject>() {
        }.getType());
        if (cache != null && !ignoreCache) {
            return cache;
        }

        Log.d("Events", year + "");

        if (TOKEN != null) {

            try {
                OkHttpClient client = new OkHttpClient();

                SimpleDateFormat kamarDate = new SimpleDateFormat("dd/MM/yyyy");

                Calendar start = Calendar.getInstance();
                start.set(Calendar.YEAR, year);
                start.set(Calendar.DAY_OF_MONTH, 1);
                start.set(Calendar.MONTH, 1);
                String startDate = kamarDate.format(start.getTime());

                Calendar end = Calendar.getInstance();
                end.set(Calendar.YEAR, year);
                end.set(Calendar.DAY_OF_MONTH, 31);
                end.set(Calendar.MONTH, 12);
                String endDate = kamarDate.format(end.getTime());

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=GetEvents&Key=" + TOKEN + "&DateStart=" + startDate + "&DateEnd=" + endDate + "&ShowAll=YES");
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR+ " + BuildConfig.VERSION_NAME)
                        .addHeader("X-Requested-With", "nz.co.KAMAR")
                        .addHeader("Origin", "file://")
                        .build();

                Response response = client.newCall(request).execute();

                String xml = response.body().string();
                EventsObject eventsObject = new EventsObject(xml);
                cacheManager.put("EventsObject" + year + ID, eventsObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);
                return eventsObject;

            } catch (Exception e) {
                throw e;
            }

        } else {
            throw new Exceptions.InvalidToken();
        }
    }

    public static CalendarObject getCalendar(boolean ignoreCache) throws Exception {
        CalendarObject cache = (CalendarObject) cacheManager.get("CalObject" + ID, AttendanceObject.class, new TypeToken<CalendarObject>() {
        }.getType());
        if (cache != null && !ignoreCache) {
            return cache;
        }

        if (TOKEN != null) {
            try {
                OkHttpClient client = new OkHttpClient();

                SimpleDateFormat kamarDate = new SimpleDateFormat("yyyy");
                String date = kamarDate.format(new Date(System.currentTimeMillis())) + "TT";

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=GetCalendar&Key=" + TOKEN + "&Year=" + date + "&StudentID=" + ID);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR+ " + BuildConfig.VERSION_NAME)
                        .addHeader("X-Requested-With", "nz.co.KAMAR")
                        .addHeader("Origin", "file://")
                        .build();

                Response response = client.newCall(request).execute();

                String xml = response.body().string();
                CalendarObject calendarObject = new CalendarObject(xml);
                cacheManager.put("CalObject" + ID, calendarObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);
                return calendarObject;

            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new Exceptions.InvalidToken();
        }
    }

    public static TimetableObject getTimetable(boolean ignoreCache) throws Exception {
        TimetableObject cache = (TimetableObject) cacheManager.get("TimetableObject" + ID, TimetableObject.class, new TypeToken<TimetableObject>() {
        }.getType());
        if (cache != null && !ignoreCache) {
            return cache;
        }

        if (TOKEN != null) {
            try {
                OkHttpClient client = new OkHttpClient();

                SimpleDateFormat kamarDate = new SimpleDateFormat("yyyy");
                String date = kamarDate.format(new Date(System.currentTimeMillis())) + "TT";

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=GetStudentTimetable&Key=" + TOKEN + "&StudentID=" + ID + "&Grid=" + date);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR+ " + BuildConfig.VERSION_NAME)
                        .addHeader("X-Requested-With", "nz.co.KAMAR")
                        .addHeader("Origin", "file://")
                        .build();

                Response response = client.newCall(request).execute();

                String xml = response.body().string();
                TimetableObject timetableObject = new TimetableObject(xml);
                cacheManager.put("TimetableObject" + ID, timetableObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);
                return timetableObject;

            } catch (Exception e) {
                throw e;
            }
        } else {
            throw new Exceptions.InvalidToken();
        }
    }

}
