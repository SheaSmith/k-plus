package sheasmith.me.betterkamar.pages.groups;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import layout.SectionFragment;
import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.pages.attendance.Attendance;
import sheasmith.me.betterkamar.pages.calender.Calender;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.reports.Reports;
import sheasmith.me.betterkamar.pages.timetable.Timetable;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class Groups extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<GroupsObject> groupsList = new ArrayList<>();

    ListAdapter customAdapter;
    ListView list;
    boolean error;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_groups);
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
    protected void onResume() {
        super.onResume(); if (EditServer.res != null) {             if (EditServer.res.equals("deletion"))                 finish();             else if (EditServer.res.equals("update"))                 setTitle(s.title);             EditServer.res = null;         }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(6).setChecked(true);
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
            Intent i = new Intent(Groups.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Groups.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Groups.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Groups.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Groups.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Groups.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {

        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Groups.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Groups.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Groups.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void invalidateIt() {
        try {
            list.invalidateViews();
        }
        catch (Exception e) {

        }
    }
    private class Connect extends AsyncTask<Void, Void, Void> {
        String lastcategory;
        String lastyear;
        List<String> categories = new ArrayList<>();
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
                Document d = Jsoup.connect(s.hostname + "/student/index.php/groups/").cookies(s.cookies).get();
                Elements groups = d.getElementById("groups_table").children();
                GroupsObject g = new GroupsObject();
                for (Element e : groups) {

                    Elements tr = e.getElementsByTag("tr");
                    for (Element e2 : tr) {

                        if (e2.child(0).className().equalsIgnoreCase("result_subject")) {
                            lastyear = e2.child(0).text();
                        }
                        else if (e2.child(0).className().equalsIgnoreCase("result_increase")) {
                            lastcategory = e2.child(0).text();
                            categories.add(lastcategory + " - " + lastyear);
                        }
                        else if (e2.child(0).className().equalsIgnoreCase("result_tile")) {
                            g = new GroupsObject();
                            g.title = e2.child(0).text();
                            g.section = lastcategory + " - " + lastyear;
                            g.year = lastyear;
                            g.teacher = e2.child(1).text();
                        }
                        else if (e2.child(0).className().equalsIgnoreCase("result_comment_left")) {
                            g.leftComment = e2.child(0).text();
                            g.rightComment = e2.child(1).text();
                            groupsList.add(g);
                        }
                    }
                }
            }
            catch (Exception e) {
                Snackbar.make(findViewById(R.id.linearView),"Can't connect! Check if you are online", Snackbar.LENGTH_LONG).show();
                error = true;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!error) {
                invalidateIt();

                try {

                    ((BaseAdapter) customAdapter).notifyDataSetChanged();
                } catch (Exception e) {

                }


                List<Fragment> al = getSupportFragmentManager().getFragments();
                if (al != null) {
                    for (Fragment t : al) {
                        getSupportFragmentManager().beginTransaction().hide(t).commit();
                    }
                }

                for (int i = 0; i != categories.size(); i++) {
                    getSupportFragmentManager()
                            .beginTransaction().add(R.id.groups, new SectionFragment(), categories.get(i)).commit();
                    getSupportFragmentManager().executePendingTransactions();
                    FragmentManager fm = getSupportFragmentManager();

                    Fragment testtagFragment = fm.findFragmentByTag(categories.get(i));
                    TextView category = (TextView) testtagFragment.getView().findViewById(R.id.groupsSection);
                    category.setText(categories.get(i));

                    List<GroupsObject> temp = new ArrayList<>();
                    for (GroupsObject g : groupsList) {
                        if (g.section.equals(categories.get(i))) {
                            temp.add(g);
                        }
                    }
                    ListView groupsList = (ListView) testtagFragment.getView().findViewById(R.id.groupsList);
                    groupsList.setAdapter(new GroupsAdapter(Groups.this, temp));

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
}
