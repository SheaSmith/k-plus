package sheasmith.me.betterkamar.old.pages.calender;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sheasmith.me.betterkamar.old.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.old.Servers;
import sheasmith.me.betterkamar.old.ServersObject;
import sheasmith.me.betterkamar.old.pages.attendance.Attendance;
import sheasmith.me.betterkamar.old.pages.details.Details;
import sheasmith.me.betterkamar.old.pages.groups.Groups;
import sheasmith.me.betterkamar.old.pages.ncea.NCEA;
import sheasmith.me.betterkamar.old.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.old.pages.pathways.Pathways;
import sheasmith.me.betterkamar.old.pages.reports.Reports;
import sheasmith.me.betterkamar.old.pages.timetable.Timetable;

public class Calender extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<CalenderObject> events = new ArrayList<>();
    Date future;
    String viewBy = "day";

    boolean error;

    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_calender);
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

        MaterialCalendarView calender = (MaterialCalendarView) findViewById(R.id.calendar);
        calender.state().edit().setCalendarDisplayMode(CalendarMode.WEEKS).commit();
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        findViewById(R.id.mainArea).setVisibility(View.VISIBLE);


//        Connect t = new Connect();
//        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
//            t.execute();
//        }


    }
    @Override
    protected void onResume() {
        super.onResume(); if (EditServer.res != null) {             if (EditServer.res.equals("deletion"))                 finish();             else if (EditServer.res.equals("update"))                 setTitle(s.title);             EditServer.res = null;         }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);

        if (EditServer.res != null) {
            if (EditServer.res.equals("deletion"))
                finish();
            else if (EditServer.res.equals("update"))
                setTitle(s.title);
            EditServer.res = null;
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
            Intent i = new Intent(Calender.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {

        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Calender.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Calender.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Calender.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Calender.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Calender.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Calender.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Calender.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Calender.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.mainArea).setVisibility(View.GONE);
            error = false;
        }
        @Override
        protected Void doInBackground(Void... params) {
            events.clear();
            try {
                String date;
                if (future == null) {
                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM/dd", Locale.ENGLISH);
                    Date da = new Date(System.currentTimeMillis());
                    date = dateFormat.format(da);
                }
                else {
                    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM/dd");
                    date = dateFormat.format(future);
                }

                Document d = Jsoup.connect(s.hostname + "/index.php/calendar/" + date + "/" + viewBy).cookies(s.cookies).get();
                Elements groups = d.getElementById("calendar_table").child(2).children();
                CalenderObject g = new CalenderObject();
                boolean tile = false;
                for (Element e : groups) {
                    if (e.hasClass("table-active")) {
                        if(g.date != null)
                        events.add(g);
                        tile = false;
                        g = new CalenderObject();
                        SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMMM");
                        g.date = format.parse(e.text().replaceAll("(?<=\\d)(st|nd|rd|th)", ""));
                        g.dateRaw = e.text();
                    }
                    else {
                        if (tile) {
                            g.location = e.child(2).text();
                        }
                        else {
                            g.description = e.child(1).text();
                            tile = true;
                        }
                    }


                }


            }
            catch (IOException e) {
                Snackbar.make(findViewById(R.id.linearView),"Can't connect! Check if you are online", Snackbar.LENGTH_LONG).show();
                error = true;
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!error) {

                ListView listView = (ListView) findViewById(R.id.eventsList);
                if (events.size() != 0) {
                    listView.setAdapter(new CalenderAdapter(Calender.this, events));
                    (findViewById(R.id.empty)).setVisibility(View.GONE);
                }
                else
                    (findViewById(R.id.empty)).setVisibility(View.VISIBLE);

                listView.invalidate();

                MaterialCalendarView calender = (MaterialCalendarView) findViewById(R.id.calendar);
                if (future == null) {
                    calender.setSelectedDate(Calendar.getInstance());
                } else {
                    Calendar c = Calendar.getInstance();
                    c.setTime(future);
                    calender.setSelectedDate(c);
                }

                calender.setOnDateChangedListener(new OnDateSelectedListener() {
                    @Override
                    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                        future = date.getDate();
                        Connect t = new Connect();
                        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
                            t.execute();
                        }
                    }
                });
                final Spinner selector = (Spinner) findViewById(R.id.spinner);
                selector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (selector.getSelectedItem().toString().equals("Day")) {
                            viewBy = "day";
                        } else if (selector.getSelectedItem().toString().equals("Month")) {
                            viewBy = "";
                        } else if (selector.getSelectedItem().toString().equals("Week")) {
                            viewBy = "week";
                        }
                        Connect t = new Connect();
                        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
                            t.execute();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
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


    }


