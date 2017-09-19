package sheasmith.me.betterkamar.pages.notices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
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
import android.widget.Toast;

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

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.reports.Reports;

public class NoticesPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Context context;
    ListAdapter customAdapter;
    ListAdapter generalAdapter;
    ListView list;
    ListView general;
    ProgressDialog progressDialog;
    private ServersObject s;
    private String date;
    Date prevDate;

    public String getDate() {
        return date;
    }

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
        CharSequence s  = DateFormat.format("EEEE d", d.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", d.getTime());
        CharSequence t = DateFormat.format("yyyy-MM-d", d.getTime());
        date = t.toString();
        TextView date = (TextView) findViewById(R.id.todaysDate);
        date.setText(s);
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
                int day = prevDate.getDay();
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
                int day = prevDate.getDay();
                String dayStr = getDayNumberSuffix(day);
                CharSequence s  = DateFormat.format("EEEE d", prevDate.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", prevDate.getTime());
                CharSequence t = DateFormat.format("yyyy-MM-d", prevDate.getTime());
                setDate(t.toString());
                TextView date = (TextView) findViewById(R.id.todaysDate);
                date.setText(s);
                new Connect().execute();
            }
        });
        Button today = (Button) findViewById(R.id.todaysNotices);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Date d = new Date();
                prevDate = d;
                int day = d.getDay();
                String dayStr = getDayNumberSuffix(day);
                CharSequence s  = DateFormat.format("EEEE d", d.getTime()) + dayStr + DateFormat.format(", MMMM yyyy", d.getTime());
                CharSequence t = DateFormat.format("yyyy-MM-d", d.getTime());
                setDate(t.toString());
                TextView date = (TextView) findViewById(R.id.todaysDate);
                date.setText(s);
                new Connect().execute();
            }
        });

        new Connect().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
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
            return true;
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


        } else if (id == R.id.nav_details) {

        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_attendance) {

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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private class Connect extends AsyncTask<Void, Void, Void> {
        boolean successful;
        String title;
        String error;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            generalList.clear();
            notices.clear();
            try {
                if (s.cookies == null) {
                    Connection.Response r = Jsoup.connect(s.hostname + "/student/index.php/process-login").method(Connection.Method.POST).data("username", s.username).data("password", s.password).execute();
                    s.cookies = r.cookies();
                }
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
                Looper.prepare();
                Toast.makeText(NoticesPage.this, "Could not connect to server!", Toast.LENGTH_LONG).show();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            invalidateIt();
            TextView generalEmpty = (TextView) findViewById(R.id.emptyGeneral);
            TextView meetingsEmpty = (TextView) findViewById(R.id.emptyMeetings);

            ((BaseAdapter)generalAdapter).notifyDataSetChanged();
            ((BaseAdapter)customAdapter).notifyDataSetChanged();

            list.setEmptyView(meetingsEmpty);
            general.setEmptyView(generalEmpty);
            TextView schoolName = (TextView) findViewById(R.id.schoolName);
            schoolName.setText(s.title);
            TextView studentName = (TextView) findViewById(R.id.studentName);
            studentName.setText(s.student);

            Servers.getServersList().remove(getIntent().getIntExtra("listID", -1));
            Servers.getServersList().add(getIntent().getIntExtra("listID", -1), s);

        }

    }
    private String getDayNumberSuffix(int day) {

        String[] suffixes = {"st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th", "th", "st"};
        return suffixes[day - 1];
    }


}



