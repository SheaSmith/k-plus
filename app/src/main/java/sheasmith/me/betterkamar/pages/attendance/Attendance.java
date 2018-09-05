package sheasmith.me.betterkamar.pages.attendance;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.calender.Calender;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.reports.Reports;
import sheasmith.me.betterkamar.pages.timetable.Timetable;


public class Attendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HashMap<String, List<AttendanceObject>> items = new HashMap<>();

    boolean error;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_attendance);
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
        if (id == R.id.action_settings) {

            Intent i = new Intent(Attendance.this, EditServer.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notices) {
            Intent i = new Intent(Attendance.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Attendance.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Attendance.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Attendance.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {

        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Attendance.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Attendance.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Attendance.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Attendance.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Attendance.this, Pathways.class);
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


                Document d = Jsoup.connect(s.hostname + "/index.php/attendance/").cookies(s.cookies).get();

                Elements tableItems = d.select("body > div > section > article > div.table-responsive > table > tbody").first().children();
                week = Integer.parseInt(d.getElementsByAttributeValue("name", "week").first().attr("value"));

                String[] dayNames = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
                for (String s : dayNames) {
                    items.put(s, new ArrayList<AttendanceObject>());
                }
                int pNum = 1;
                for (Element t : tableItems) {
                    String period = t.child(0).getElementsByTag("h4").first().ownText();
                    String tim = t.child(0).getElementsByTag("small").text();

                    timeList.add(tim);
                    periodList.add(period);

                    List<AttendanceObject> times = new ArrayList<>();

                    for (int i = 1 ; i < 6 ; i++) {
                        t.child(i);
                        AttendanceObject time = new AttendanceObject();
                        time.day = i;
                        try {
                            time.classname = t.child(i).child(0).child(1).text();
                            time.attendance = t.child(i).child(0).child(0).text();

                            time.isEmpty = false;
                        }
                        catch (IndexOutOfBoundsException | NullPointerException e) {
                            time.isEmpty = true;
                        }
                        times.add(time);
                    }
                    items.put(Integer.toString(pNum), times);
                    pNum++;
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

                    for (Map.Entry<String, List<AttendanceObject>> entry : items.entrySet()) {
                        String key = entry.getKey();
                        List<AttendanceObject> value = entry.getValue();
                        for (AttendanceObject t : value) {
                            int id = t.day;
                            View root = findViewById(R.id.linearView);
                            TextView textView = (TextView) root.findViewWithTag(key + ";" + (id - 1));

                            if (t.isEmpty) {
                                textView.setBackgroundColor(getColor(R.color.dim_foreground_disabled_material_dark));
                            }
                            else {
                                textView.setText(t.attendance + "\n" + t.classname);

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
        navigationView.getMenu().getItem(4).setChecked(true);

        if (EditServer.res != null) {
            if (EditServer.res.equals("deletion"))
                finish();
            else if (EditServer.res.equals("update"))
                setTitle(s.title);
            EditServer.res = null;
        }
    }

}
