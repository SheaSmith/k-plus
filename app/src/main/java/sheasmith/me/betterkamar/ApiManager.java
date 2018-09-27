package sheasmith.me.betterkamar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.reflect.TypeToken;
import com.iainconnor.objectcache.DiskCache;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import sheasmith.me.betterkamar.dataModels.AbsenceObject;
import sheasmith.me.betterkamar.dataModels.AttendanceObject;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.EventsObject;
import sheasmith.me.betterkamar.dataModels.DetailsObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.GroupObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NCEAObject;
import sheasmith.me.betterkamar.dataModels.NZQAObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.dataModels.ResultObject;
import sheasmith.me.betterkamar.dataModels.SettingsObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.GroupsObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 5/09/2018.
 */

public class ApiManager {

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

    public static void setVariables(final PortalObject portal, final ApiResponse<PortalObject> callback, final Context context) {
        String cachePath = context.getCacheDir().getPath();
        final String fileName = UUID.randomUUID().toString() + ".jpg";
        File cacheFile = new File(cachePath + File.separator + BuildConfig.APPLICATION_ID + File.separator);

        try {
            diskCache = new DiskCache(cacheFile, BuildConfig.VERSION_CODE, 1024 * 1024 * 10);
            cacheManager = com.iainconnor.objectcache.CacheManager.getInstance(diskCache);
        } catch (IOException e) {
            callback.error(e);
        }

        ID = portal.username;
        PASSWORD = portal.password;
        URL = portal.hostname + "/api/api.php";
        login(ID, PASSWORD, new ApiResponse<LoginObject>() {
            @Override
            public void success(LoginObject value) {
                portal.key = value.LogonResults.Key;

                if (portal.studentFile == null) {
                    getProfilePicture(new ApiResponse<Bitmap>() {
                        @Override
                        public void success(Bitmap value) {
                            String fileName = UUID.randomUUID().toString() + ".jpg";
                            portal.studentFile = fileName;
                            if (!CacheManager.saveBitmapToFile(context.getFilesDir(), fileName, value, Bitmap.CompressFormat.JPEG, 100)) {
                                CacheManager.saveBitmapToFile(context.getFilesDir(), fileName, CacheManager.getBitmapFromURL("http://thumbnails.kamar.nz/logo.php/999999999999"), Bitmap.CompressFormat.JPEG, 100);
                            }

                            if (portal.schoolFile != null && portal.student != null)
                                callback.success(portal);
                        }

                        @Override
                        public void error(Exception e) {
                            callback.error(e);
                        }
                    }, context);
                }

                if (portal.schoolFile == null) {
                    getSettings(new ApiResponse<SettingsObject>() {
                        @Override
                        public void success(SettingsObject value) {
                            portal.schoolName = value.SettingsResults.SchoolName;
                            downloadImage(value.SettingsResults.LogoPath, new ApiResponse<Bitmap>() {
                                @Override
                                public void success(Bitmap value) {
                                    portal.schoolFile = fileName;
                                    CacheManager.saveBitmapToFile(context.getFilesDir(), fileName, value, Bitmap.CompressFormat.PNG, 100);

                                    if (portal.studentFile != null && portal.student != null)
                                        callback.success(portal);
                                }

                                @Override
                                public void error(Exception e) {
                                    callback.error(e);
                                }
                            });
                        }

                        @Override
                        public void error(Exception e) {
                            callback.error(e);
                        }
                    });
                }

                if (portal.student == null) {
                    getDetails(new ApiResponse<DetailsObject>() {
                        @Override
                        public void success(DetailsObject value) {
                            portal.student = value.StudentDetailsResults.Student.FirstName + " " + value.StudentDetailsResults.Student.LastName;

                            if (portal.studentFile != null && portal.schoolFile != null)
                                callback.success(portal);
                        }

                        @Override
                        public void error(Exception e) {
                            callback.error(e);
                        }
                    });
                }

                if (portal.studentFile != null && portal.schoolFile != null && portal.student != null)
                    callback.success(portal);
            }

            @Override
            public void error(Exception e) {
                callback.error(e);
            }
        });
    }

    /**
     * Logon to KAMAR
     *
     * @param username Username to use
     * @param password Password to use
     * @param callback Callback, returning a LogonResults object
     */
    public static void login(final String username, final String password, final ApiResponse<LoginObject> callback) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "Command=Logon&Key=vtku&Username=" + username + "&Password=" + password);
                Request request = new Request.Builder()
                        .url(URL)
                        .post(body)
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("User-Agent", "KAMAR+ 3.0")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String xml = response.body().string();

                    LoginObject loginObject = new LoginObject(xml);
//                       TOKEN = login.getKey();
                    ID = username;
                    PASSWORD = password;
                    TOKEN = loginObject.LogonResults.Key;
                    callback.success(loginObject);

                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();

    }

    public static void getSettings(final ApiResponse<SettingsObject> callback) {
        SettingsObject cache = (SettingsObject) cacheManager.get("SettingsObject" + ID, SettingsObject.class, new TypeToken<SettingsObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                    RequestBody body = RequestBody.create(mediaType, "Command=GetSettings&Key=vtku");
                    Request request = new Request.Builder()
                            .url(URL)
                            .post(body)
                            .addHeader("Content-Type", "application/x-www-form-urlencoded")
                            .addHeader("User-Agent", "KAMAR+ 3.0")
                            .build();

                    Response response = client.newCall(request).execute();
                    if (response.code() == 200) {
                        String xml = response.body().string();
                        SettingsObject settings = new SettingsObject(xml);
                        callback.success(settings);
                        cacheManager.put("SettingsObject" + ID, settings, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_MONTH.asSeconds(), false);

                        URL = request.url().toString();
                    } else {
                        callback.error(new Exceptions.InvalidServer());
                    }
                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

    public static void getGlobals(final ApiResponse<GlobalObject> callback) {
        GlobalObject cache = (GlobalObject) cacheManager.get("GlobalObject" + ID, GlobalObject.class, new TypeToken<GlobalObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetGlobals&Key=" + TOKEN);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        GlobalObject globals = new GlobalObject(xml);
                        callback.success(globals);
                        cacheManager.put("GlobalObject" + ID, globals, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_MONTH.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getEvents(final ApiResponse<EventsObject> callback, final int year) {
        EventsObject cache = (EventsObject) cacheManager.get("EventsObject" + ID, EventsObject.class, new TypeToken<EventsObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        EventsObject eventsObject = new EventsObject(xml);
                        callback.success(eventsObject);
                        cacheManager.put("EventsObject" + ID, eventsObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getNotices(final ApiResponse<NoticesObject> callback, final Date dateToShow) {
        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        SimpleDateFormat kamarDate = new SimpleDateFormat("dd/MM/yyyy");
                        String date = kamarDate.format(dateToShow.getTime());

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetNotices&Key=" + TOKEN + "&Date=" + date + "&ShowAll=YES");
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        NoticesObject noticesObject = new NoticesObject(xml);
                        callback.success(noticesObject);


                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getAbsenceStats(final ApiResponse<AbsenceObject> callback) {
        AbsenceObject cache = (AbsenceObject) cacheManager.get("AbsenceObject" + ID, AbsenceObject.class, new TypeToken<AbsenceObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        SimpleDateFormat kamarDate = new SimpleDateFormat("yyyy");
                        String date = kamarDate.format(new Date(System.currentTimeMillis())) + "TT";

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentAbsenceStats&Key=" + TOKEN + "&Grid=" + date + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        AbsenceObject absenceObject = new AbsenceObject(xml);
                        callback.success(absenceObject);
                        cacheManager.put("AbsenceObject" + ID, absenceObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getCalendar(final ApiResponse<CalendarObject> callback) {
        CalendarObject cache = (CalendarObject) cacheManager.get("CalObject" + ID, AttendanceObject.class, new TypeToken<CalendarObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        CalendarObject calendarObject = new CalendarObject(xml);
                        callback.success(calendarObject);
                        cacheManager.put("CalObject" + ID, calendarObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        }
        else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getAttendance(final ApiResponse<AttendanceObject> callback) {
        AttendanceObject cache = (AttendanceObject) cacheManager.get("AttendanceObject" + ID, AttendanceObject.class, new TypeToken<AttendanceObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        SimpleDateFormat kamarDate = new SimpleDateFormat("yyyy");
                        String date = kamarDate.format(new Date(System.currentTimeMillis())) + "TT";

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentAttendance&Key=" + TOKEN + "&Grid=" + date + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        AttendanceObject attendanceObject = new AttendanceObject(xml);
                        callback.success(attendanceObject);
                        cacheManager.put("AttendanceObject" + ID, attendanceObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_HOUR.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getNCEADetails(final ApiResponse<NCEAObject> callback) {
        NCEAObject cache = (NCEAObject) cacheManager.get("NCEAObject" + ID, NCEAObject.class, new TypeToken<NCEAObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentNCEASummary&Key=" + TOKEN + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        NCEAObject nceaObject = new NCEAObject(xml);
                        callback.success(nceaObject);
                        cacheManager.put("NCEAObject" + ID, nceaObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_DAY.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getNZQAResults(final ApiResponse<NZQAObject> callback) {
        NZQAObject cache = (NZQAObject) cacheManager.get("NZQAObject" + ID, NZQAObject.class, new TypeToken<NZQAObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentOfficialResults&Key=" + TOKEN + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        NZQAObject nzqaObject = new NZQAObject(xml);
                        callback.success(nzqaObject);
                        cacheManager.put("NZQAObject" + ID, nzqaObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_DAY.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getAllResults(final ApiResponse<ResultObject> callback) {
        ResultObject cache = (ResultObject) cacheManager.get("ResultObject" + ID, ResultObject.class, new TypeToken<ResultObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        SimpleDateFormat kamarDate = new SimpleDateFormat("yyyy");
                        String date = kamarDate.format(new Date(System.currentTimeMillis())) + "TT";

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentResults&Key=" + TOKEN + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        ResultObject resultObject = new ResultObject(xml);
                        callback.success(resultObject);
                        cacheManager.put("ResultObject" + ID, resultObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_DAY.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getTimetable(final ApiResponse<TimetableObject> callback) {
        TimetableObject cache = (TimetableObject) cacheManager.get("TimetableObject" + ID, TimetableObject.class, new TypeToken<TimetableObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
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
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        TimetableObject timetableObject = new TimetableObject(xml);
                        callback.success(timetableObject);
                        cacheManager.put("TimetableObject" + ID, timetableObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getGroupsApi(final ApiResponse<GroupObject> callback) {
        GroupObject cache = (GroupObject) cacheManager.get("GroupObject" + ID, GroupObject.class, new TypeToken<GroupObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentGroups&Key=" + TOKEN + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        GroupObject groupObject = new GroupObject(xml);
                        callback.success(groupObject);
                        cacheManager.put("GroupObject" + ID, groupObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_DAY.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getDetails(final ApiResponse<DetailsObject> callback) {
        DetailsObject cache = (DetailsObject) cacheManager.get("DetailsObject" + ID, DetailsObject.class, new TypeToken<DetailsObject>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        if (TOKEN != null) {
            Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        OkHttpClient client = new OkHttpClient();

                        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                        RequestBody body = RequestBody.create(mediaType, "Command=GetStudentDetails&Key=" + TOKEN + "&StudentID=" + ID);
                        Request request = new Request.Builder()
                                .url(URL)
                                .post(body)
                                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                                .addHeader("User-Agent", "KAMAR+ 3.0")
                                .build();

                        Response response = client.newCall(request).execute();

                        String xml = response.body().string();
                        DetailsObject detailsObject = new DetailsObject(xml);
                        callback.success(detailsObject);
                        cacheManager.put("DetailsObject" + ID, detailsObject, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void getProfilePicture(final ApiResponse<Bitmap> callback, final Context context) {
        if (TOKEN != null) {
            final Thread webThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        HttpURLConnection connection = (HttpURLConnection) new URL(URL.replace("api.php", "img.php") + "?Key=" + TOKEN + "&Stuid=" + ID).openConnection();
                        connection.setRequestProperty("User-agent", "KAMAR+ 3.0");

                        connection.connect();
                        try {
                            InputStream input = connection.getInputStream();
                            callback.success(BitmapFactory.decodeStream(input));
                        }
                        catch (FileNotFoundException e) {
                            callback.success(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_person));
                        }

                    } catch (Exception e) {
                        callback.error(e);
                    }
                }
            });
            webThread.start();
        } else {
            callback.error(new Exceptions.InvalidToken());
        }
    }

    public static void downloadImage(final String url, final ApiResponse<Bitmap> callback) {
        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestProperty("User-agent", "KAMAR+ 3.0");

                    connection.connect();
                    InputStream input = connection.getInputStream();
                    callback.success(BitmapFactory.decodeStream(input));

                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

    public static void getReports(final ApiResponse<List<ReportsObject>> callback) {
        List<ReportsObject> cache = (List<ReportsObject>) cacheManager.get("ReportsObject" + ID, ArrayList.class, new TypeToken<List<ReportsObject>>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response origCookies = Jsoup.connect(URL.replace("api/api.php", "index.php")).method(Connection.Method.GET).execute();
                    Map<String, String> sessionCookies = origCookies.cookies();

                    Connection.Response login;
                    if (sessionCookies.containsKey("csrf_kamar_cn"))
                        login = Jsoup.connect(URL.replace("api/api.php", "index.php/login")).method(Connection.Method.POST).data("username", ID, "password", PASSWORD, "csrf_kamar_sn", sessionCookies.get("csrf_kamar_cn")).cookies(sessionCookies).execute();
                    else
                        login = Jsoup.connect(URL.replace("api/api.php", "index.php/login")).method(Connection.Method.POST).data("username", ID, "password", PASSWORD).cookies(sessionCookies).execute();

                    Map<String, String> cookies = login.cookies();
                    if (cookies.containsKey("kamar_session"))
                    cookies.put("kamar_session", sessionCookies.get("kamar_session"));

                    org.jsoup.nodes.Document d = Jsoup.connect(URL.replace("api/api.php", "index.php/reports/")).cookies(cookies).get();
                    Elements groups = d.getElementsByTag("tbody").first().children();

                    List<ReportsObject> reports = new ArrayList<>();

                    for (org.jsoup.nodes.Element e : groups) {
                        ReportsObject r = new ReportsObject();
                        r.title = e.child(1).text();
                        r.date = e.child(0).text();
                        r.url = e.child(2).child(0).attr("href");
                        r.cookies = cookies;
                        reports.add(r);
                    }

                    callback.success(reports);
                    cacheManager.put("ReportsObject" + ID, reports, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);
                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

    public static void getGroupsHtml(final ApiResponse<List<GroupsObject>> callback) {
        List<GroupsObject> cache = (List<GroupsObject>) cacheManager.get("GroupsObjectHTML" + ID, ArrayList.class, new TypeToken<List<GroupsObject>>(){}.getType());
        if (cache != null) {
            callback.success(cache);
            return;
        }

        Thread webThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response login = Jsoup.connect(URL.replace("api/api.php", "/index.php/login")).method(Connection.Method.POST).data("username", ID, "password", PASSWORD).execute();
                    Map<String, String> cookies = login.cookies();

                    String lastcategory = "";
                    String lastyear = "";
                    List<String> categories = new ArrayList<>();

                    org.jsoup.nodes.Document d = Jsoup.connect(URL.replace("api/api.php", "index.php/groups/")).cookies(cookies).get();
                    Elements groups = d.getElementsByTag("table").first().children();
                    GroupsObject g = null;
                    List<GroupsObject> groupsList = new ArrayList<>();
                    for (org.jsoup.nodes.Element e : groups) {
                        if (e.tagName().equals("thead")) {
                            lastyear = e.text();
                        } else {
                            Elements tr = e.children();
                            for (org.jsoup.nodes.Element e2 : tr) {

                                if (e2.className().equalsIgnoreCase("table-active")) {
                                    lastcategory = e2.child(0).text();
                                    categories.add(lastcategory + " - " + lastyear);
                                } else if (e2.children().size() == 3) {
                                    if (g != null)
                                        groupsList.add(g);
                                    g = new GroupsObject();
                                    g.title = e2.child(1).text();
                                    g.section = lastcategory + " - " + lastyear;
                                    g.year = lastyear;
                                    g.teacher = e2.child(2).text();
                                } else if (e2.children().size() == 2) {
                                    g.comment = e2.text();
                                    groupsList.add(g);
                                    g = null;
                                }
                            }
                        }
                    }

                    callback.success(groupsList);
                    cacheManager.put("GroupsObjectHTML" + ID, groupsList, com.iainconnor.objectcache.CacheManager.ExpiryTimes.ONE_WEEK.asSeconds(), false);
                } catch (Exception e) {
                    callback.error(e);
                }
            }
        });
        webThread.start();
    }

}
