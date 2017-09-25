package sheasmith.me.betterkamar.pages.timetable;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.attendance.Attendance;
import sheasmith.me.betterkamar.pages.calender.Calender;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.reports.Reports;


public class Timetable extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HashMap<String, List<TimetableObject>> items = new HashMap<>();

    boolean error;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_timetable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        s = Servers.getServersList().get(getIntent().getIntExtra("listID", -1));
        setTitle(s.title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        Connect t = new Connect();
        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
           t.execute();
        }




    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.server_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {              Intent i = new Intent(this, EditServer.class);             i.putExtra("listID", getIntent().getIntExtra("listID", -1));             startActivity(i);         }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notices) {
            Intent i = new Intent(Timetable.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Timetable.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Timetable.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Timetable.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Timetable.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Timetable.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Timetable.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Timetable.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Timetable.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        int week;
        List<String> periodList = new ArrayList<>();
        List<String> timeList = new ArrayList<>();

        HashMap<String, String> actualDates = new HashMap<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.mainArea).setVisibility(View.GONE);
            error = false;
        }
        @Override
        protected Void doInBackground(Void... params) {
            items.clear();
            try {


                    Document d = Jsoup.connect(s.hostname + "/student/index.php/timetable/").cookies(s.cookies).get();

                Elements tableItems = d.getElementById("timetable_table").child(0).children();
                Elements days = tableItems.get(0).children();
                week = Integer.parseInt(days.get(0).getElementById("week").attr("value"));

                String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                for (String s : dayNames) {
                    items.put(s, new ArrayList<TimetableObject>());
                }
                Elements times = tableItems.get(0).children();
                for (int i = 1 ; i!=6 ; i++) {
                    String[] items = times.get(i).text().split(" ");
                    actualDates.put(items[0], items[1] + " " + items[2]);
                }
                for (int i = 1 ; i != 10 ; i++) {
                    Elements periodSlots = tableItems.get(i).children();
                    String time = periodSlots.get(0).getElementsByClass("tt_time").first().text();
                    periodList.add(periodSlots.get(0).text().replace(" " + time, ""));
                    timeList.add(time);
                    for (Element e : periodSlots) {
                        if (periodSlots.indexOf(e) != 0) {
                            TimetableObject t = new TimetableObject();
                            t.periodNumber = i;
                            t.classname = e.getElementsByTag("strong").first().text();
                            String[] classInfo = e.getElementsByClass("result").first().text().split(" ");
                            t.teacher = classInfo[0];
                            if (classInfo.length == 1) {
                                t.isEmpty = true;
                            }
                            else {
                                t.room = classInfo[1];
                            }
                            t.time = time;

                            List<TimetableObject> timetableObjectList = items.get(dayNames[periodSlots.indexOf(e) - 1]);
                            timetableObjectList.add(t);
                            items.put(dayNames[periodSlots.indexOf(e) - 1], timetableObjectList);
                        }
                    }
                }




            }
            catch (IOException e) {
                Snackbar.make(findViewById(R.id.linearView),"Can't connect! Check if you are online", Snackbar.LENGTH_LONG).show();
                error = true;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!error) {
                if (!periodList.isEmpty()) {
//                String temp;
                    ((TextView) findViewById(R.id.period1)).setText(periodList.get(0).replace("Period ", "") + "\n" + timeList.get(0).replace(" ", ""));
                    ((TextView) findViewById(R.id.formTime)).setText(periodList.get(1).replace("Period ", "") + "\n" + timeList.get(1).replace(" ", ""));
                    ((TextView) findViewById(R.id.period2)).setText(periodList.get(2).replace("Period ", "") + "\n" + timeList.get(2).replace(" ", ""));
                    ((TextView) findViewById(R.id.interval)).setText(periodList.get(3).replace("Period ", "") + "\n" + timeList.get(3).replace(" ", ""));
                    ((TextView) findViewById(R.id.period3)).setText(periodList.get(4).replace("Period ", "") + "\n" + timeList.get(4).replace(" ", ""));
                    ((TextView) findViewById(R.id.period4)).setText(periodList.get(5).replace("Period ", "") + "\n" + timeList.get(5).replace(" ", ""));
                    ((TextView) findViewById(R.id.lunch)).setText(periodList.get(6).replace("Period ", "") + "\n" + timeList.get(6).replace(" ", ""));
                    ((TextView) findViewById(R.id.period5)).setText(periodList.get(7).replace("Period ", "") + "\n" + timeList.get(7).replace(" ", ""));
//                temp = "";
//                if (timeList.get(8) != null) temp = ;
                    ((TextView) findViewById(R.id.period6)).setText(periodList.get(8).replace("Period ", "") + "\n" + timeList.get(8).replace(" ", ""));

                    for (Map.Entry<String, List<TimetableObject>> entry : items.entrySet()) {
                        String key = entry.getKey();
                        List<TimetableObject> value = entry.getValue();
                        for (TimetableObject t : value) {
                            int id = t.periodNumber;
                            View root = findViewById(R.id.linearView);
                            TextView textView = (TextView) root.findViewWithTag(id + key);
                            if (t.room == null) {
                                t.room = "";
                            }
                            textView.setText(t.classname + "\n" + t.teacher + " " + t.room);

                            if (t.room != null && !t.room.equals("")) {
                                DateFormat input = new SimpleDateFormat("d MMM EEEE kk:mm a");
                                Date first = new Date();
                                int num = value.indexOf(t) + 1;
                                try {
                                    TimetableObject next = value.get(num);
                                    first = input.parse(actualDates.get(key) + " " + key + " " + t.time);
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(first);
                                    Calendar now = Calendar.getInstance();
                                    c.set(Calendar.YEAR, now.get(Calendar.YEAR));
                                    first = c.getTime();
                                    if (next.room != null) {
                                        setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "Next up you have " + next.classname + " with " + next.teacher + " in " + next.room + " at " + next.time, first.getTime());
//                                    setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "Next up you have " + next.classname + " with " + next.teacher + " in " + next.room + " at " + next.time, System.currentTimeMillis());
                                    } else {
                                        TimetableObject nextMore = value.get(num + 1);
                                        setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "Next up you have " + nextMore.classname + " with " + nextMore.teacher + " in " + nextMore.room + " at " + nextMore.time, first.getTime());
//                                    setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "Next up you have " + nextMore.classname + " with " + nextMore.teacher + " in " + nextMore.room + " at " + nextMore.time, System.currentTimeMillis());
                                    }
                                } catch (IndexOutOfBoundsException e) {
                                    setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "", first.getTime());
//                                setOneTimeAlarm("You currently have " + t.classname + " with " + t.teacher + " in " + t.room + " (" + t.time + ")", "", System.currentTimeMillis());
                                } catch (ParseException e) {
                                    Log.e("TT ParseException", "stacktrace");
                                    e.printStackTrace();
                                }

                            } else {
                                DateFormat input = new SimpleDateFormat("d MMM EEEE kk:mm a");
                                Date first;
                                int num = value.indexOf(t) + 1;
                                try {
                                    TimetableObject next = value.get(num);
                                    first = input.parse(actualDates.get(key) + " " + key + " " + next.time);
                                    Calendar c = Calendar.getInstance();
                                    c.setTime(first);
                                    Calendar now = Calendar.getInstance();
                                    c.set(Calendar.YEAR, now.get(Calendar.YEAR));
                                    first = c.getTime();
                                    if (next.room != null) {
                                        setOneTimeAlarm("Next up you have " + next.classname + " with " + next.teacher + " in " + next.room + " at " + next.time, "", first.getTime());
//                                    setOneTimeAlarm("Next up you have " + next.classname + " with " + next.teacher + " in " + next.room + " at " + next.time, "", System.currentTimeMillis());                                    setOneTimeAlarm("Next up you have " + next.classname + " with " + next.teacher + " in " + next.room + " at " + next.time, "", System.currentTimeMillis());
                                    }
                                } catch (IndexOutOfBoundsException e) {

                                } catch (ParseException e) {
                                    Log.e("TT ParseException", "stacktrace");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }


                }
                findViewById(R.id.progressBar).setVisibility(View.GONE);
                findViewById(R.id.mainArea).setVisibility(View.VISIBLE);
            }
            else {
                findViewById(R.id.progressBar).setVisibility(View.GONE);
            }
            TextView schoolName = (TextView) findViewById(R.id.schoolName);
            schoolName.setText(s.title);
            TextView studentName = (TextView) findViewById(R.id.studentName);
            studentName.setText(s.student);

        }

}
    @Override
    protected void onResume() {
        super.onResume(); if (EditServer.res != null) {             if (EditServer.res.equals("deletion"))                 finish();             else if (EditServer.res.equals("update"))                 setTitle(s.title);             EditServer.res = null;         }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);
    }
    AlarmManager am;
    public void setOneTimeAlarm(String title, String message, long timeInMillis) {
        if (timeInMillis >= System.currentTimeMillis()) {
            am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, TimeAlarm.class);
            intent.putExtra("title", title);
            intent.putExtra("message", message);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            am.set(AlarmManager.RTC, timeInMillis, pendingIntent);
        }

    }
}
