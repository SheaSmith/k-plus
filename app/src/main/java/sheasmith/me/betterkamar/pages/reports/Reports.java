package sheasmith.me.betterkamar.pages.reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import layout.SectionFragment;
import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.attendance.Attendance;
import sheasmith.me.betterkamar.pages.calender.Calender;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.timetable.Timetable;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class Reports extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<ReportsObject> groupsList = new ArrayList<>();

    boolean error = false;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_reports);
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

//        Intent k = new Intent(Reports.this, PDFViewer.class);
//        k.putExtra("listID", getIntent().getIntExtra("listID", -1));
//        startActivity(k);
        if (groupsList.isEmpty()) {
            Connect t = new Connect();
            if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
                t.execute();
            }
        }


    }
    @Override
    protected void onResume() {
        super.onResume(); if (EditServer.res != null) {             if (EditServer.res.equals("deletion"))                 finish();             else if (EditServer.res.equals("update"))                 setTitle(s.title);             EditServer.res = null;         }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(7).setChecked(true);
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
            Intent i = new Intent(Reports.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Reports.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Reports.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Reports.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Reports.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Reports.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Reports.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Reports.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Reports.this, Pathways.class);
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
            groupsList.clear();
            try {
                Document d = Jsoup.connect(s.hostname + "/index.php/reports/").cookies(s.cookies).get();
                Elements groups = d.getElementsByTag("tbody").first().children();

                for (Element e : groups) {
                    ReportsObject r = new ReportsObject();
                    r.title = e.child(1).text();
                    r.url = e.child(2).child(0).attr("href");
                    groupsList.add(r);
                }
            }
            catch (Exception e) {
                Snackbar.make(findViewById(R.id.reportsLinearView),"Can't connect! Check if you are online", Snackbar.LENGTH_LONG).show();
                error = true;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {

            if (!error) {
                List<String> prettyNames = new ArrayList<>();
                for (ReportsObject r : groupsList) {
                    prettyNames.add(r.title);
                }
                    ListView listView = (ListView) findViewById(R.id.reportsList);
                    listView.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, prettyNames));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                            Intent k = new Intent(Reports.this, PDFViewer.class);
                            k.putExtra("listID", getIntent().getIntExtra("listID", -1));
                            k.putExtra("name", groupsList.get(pos).title);
                            k.putExtra("url", groupsList.get(pos).url);
                            startActivity(k);
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
