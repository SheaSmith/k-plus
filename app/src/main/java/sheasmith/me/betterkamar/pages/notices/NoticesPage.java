package sheasmith.me.betterkamar.pages.notices;

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
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.attendance.Attendance;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.timetable.Timetable;
import sheasmith.me.betterkamar.pages.reports.Reports;

public class NoticesPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    ListAdapter customAdapter;
    ListAdapter generalAdapter;
    ListView list;
    ListView general;
    private ServersObject s;
    private String date;
    Date prevDate;

    boolean error;

    public void setDate(String date) {
        this.date = date;
    }

    private List<NoticesObject> notices = new ArrayList<>();
    private List<NoticesObject> generalList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        int listID = getIntent().getIntExtra("listID", -1);
        ServersObject server = Servers.getServersList().get(listID);
        s = server;
        setTitle(server.title);

        setContentView(R.layout.activity_notices_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         list = (ListView) findViewById(R.id.meetingsList);
        customAdapter = new NoticesAdapter(getApplicationContext(), notices);
        list.setAdapter(customAdapter);
        general = (ListView) findViewById(R.id.generalList);
        generalAdapter = new NoticesAdapter(getApplicationContext(), generalList);
        general.setAdapter(generalAdapter);

        final Date d = new Date();
        int day = d.getDay();
        String dayStr = getDayNumberSuffix(day);
        CharSequence d2  = DateFormat.format("EEEE d", d.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", d.getTime());
        CharSequence t = DateFormat.format("yyyy-MM-d", d.getTime());
        date = t.toString();
        TextView date = (TextView) findViewById(R.id.todaysDate);
        date.setText(d2);
        Button prev = (Button) findViewById(R.id.prevDay);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                if (prevDate != null) {
                    cal.setTime(prevDate);
                }
                else {
                    cal.setTime(d);
                }

                cal.add(Calendar.DATE, -1);
                prevDate = cal.getTime();
                Calendar c = Calendar.getInstance();
                c.setTime(prevDate);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String dayStr = getDayNumberSuffix(day);
                CharSequence s  = DateFormat.format("EEEE d", prevDate.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", prevDate.getTime());
                CharSequence t = DateFormat.format("yyyy-MM-d", prevDate.getTime());
                setDate(t.toString());
                TextView date = (TextView) findViewById(R.id.todaysDate);
                date.setText(s);
                new Connect().execute();


            }
        });
        Button nextDay = (Button) findViewById(R.id.nextDay);
        nextDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                if (prevDate != null) {
                    cal.setTime(prevDate);
                }
                else {
                    cal.setTime(d);
                }

                cal.add(Calendar.DATE, 1);
                prevDate = cal.getTime();
                Calendar c = Calendar.getInstance();
                c.setTime(prevDate);
                int day = c.get(Calendar.DAY_OF_MONTH);
                String dayStr = getDayNumberSuffix(day);
                CharSequence d1  = DateFormat.format("EEEE d", prevDate.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", prevDate.getTime());
                CharSequence t = DateFormat.format("yyyy-MM-d", prevDate.getTime());
                setDate(t.toString());
                TextView date = (TextView) findViewById(R.id.todaysDate);
                date.setText(d1);
                new Connect().execute();
            }
        });
        Button today = (Button) findViewById(R.id.todaysNotices);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Date d = new Date();
                prevDate = d;
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                int day = c.get(Calendar.DAY_OF_MONTH);

                String dayStr = getDayNumberSuffix(day);
                CharSequence s  = DateFormat.format("EEEE d", d.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", d.getTime());
                CharSequence t = DateFormat.format("yyyy-MM-d", d.getTime());
                setDate(t.toString());
                TextView date = (TextView) findViewById(R.id.todaysDate);
                date.setText(s);
                new Connect().execute();
            }
        });
        Connect c = new Connect();
        if (c.getStatus() != AsyncTask.Status.RUNNING) {
            new Connect().execute();
        }




    }

    @Override
    protected void onResume() {
        super.onResume(); if (EditServer.res != null) {             if (EditServer.res.equals("deletion"))                 finish();             else if (EditServer.res.equals("update"))                 setTitle(s.title);             EditServer.res = null;         }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        if (EditServer.res != null) {
            if (EditServer.res.equals("deletion"))
                finish();
            else if (EditServer.res.equals("update"))
                setTitle(s.title);
        }
    }

    public void invalidateIt() {

        list.invalidateViews();
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

                    Intent i = new Intent(NoticesPage.this, EditServer.class);
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

        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(NoticesPage.this, sheasmith.me.betterkamar.pages.calender.Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(NoticesPage.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(NoticesPage.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(NoticesPage.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(NoticesPage.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(NoticesPage.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(NoticesPage.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);

        } else if (id == R.id.nav_back) {
            startActivity(new Intent(NoticesPage.this, Servers.class));
        } else if (id == R.id.nav_pathways) {
            Intent i = new Intent(NoticesPage.this, Pathways.class);
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
            generalList.clear();
            notices.clear();
            try {
//                if (s.cookies == null) {
                    Connection.Response r = Jsoup.connect(s.hostname + "/student/index.php/process-login").method(Connection.Method.POST).data("username", s.username).data("password", s.password).execute();
                    s.cookies = r.cookies();
//                }
                Document d = Jsoup.connect(s.hostname + "/student/index.php/notices/" + date).get();
                Elements meetings = d.getElementsByClass("meeting-notice");
                for (Element e : meetings) {
                    Elements title = e.getElementsByClass("subject");
                    Elements teacher = e.getElementsByClass("teacher");
                    Elements meeting = e.getElementsByClass("meet");
                    Elements level = e.getElementsByClass("level");
                    Elements body = e.getElementsByClass("body");
                    NoticesObject n = new NoticesObject();
                    n.subject = title.text();
                    n.teacher = teacher.text();
                    n.level = level.text();
                    n.meet = meeting.text();
                    n.body = body.text();
                    notices.add(n);

                }

                Elements general = d.getElementsByClass("general-notice");
                for (Element e : general) {
                    Elements title = e.getElementsByClass("subject");

                    Elements teacher = e.getElementsByClass("teacher");
                    Elements level = e.getElementsByClass("level");
                    Elements body = e.getElementsByClass("body");
                    NoticesObject n = new NoticesObject();
                    n.subject = title.text();

                    n.teacher = teacher.text();
                    n.level = level.text();
                    n.body = body.text();
                    generalList.add(n);

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
                invalidateIt();
                TextView generalEmpty = (TextView) findViewById(R.id.emptyGeneral);
                TextView meetingsEmpty = (TextView) findViewById(R.id.emptyMeetings);

                ((BaseAdapter) generalAdapter).notifyDataSetChanged();
                ((BaseAdapter) customAdapter).notifyDataSetChanged();

                list.setEmptyView(meetingsEmpty);
                general.setEmptyView(generalEmpty);

                Servers.getServersList().remove(getIntent().getIntExtra("listID", -1));
                Servers.getServersList().add(getIntent().getIntExtra("listID", -1), s);



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
    private String getDayNumberSuffix(int day) {

        String[] suffixes = {"st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "st"};
        return suffixes[day - 1];
    }


}



