package sheasmith.me.betterkamar.pages.groups;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import layout.SectionFragment;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class Groups extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<GroupsObject> groupsList = new ArrayList<>();

    ListAdapter customAdapter;
    ListView list;
    ProgressDialog progressDialog;
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

        list = (ListView) findViewById(R.id.groupsList);
        customAdapter = new GroupsAdapter(getApplicationContext(), groupsList);
        list.setAdapter(customAdapter);
        Connect t = new Connect();
        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
            t.execute();
        }


    }
    @Override
    protected void onResume() {
        super.onResume();
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
            Intent i = new Intent(Groups.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {

        } else if (id == R.id.nav_details) {

        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_attendance) {

        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Groups.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Groups.this, Servers.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void invalidateIt() {

        list.invalidateViews();
    }
    private class Connect extends AsyncTask<Void, Void, Void> {
        String lastcategory;
        String lastyear;
        List<String> categories = new ArrayList<>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
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
                            categories.add(lastcategory);
                        }
                        else if (e2.child(0).className().equalsIgnoreCase("result_tile")) {
                            g = new GroupsObject();
                            g.title = e2.child(0).text();
                            g.section = lastcategory;
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
            catch (IOException e) {
                Looper.prepare();
                Toast.makeText(Groups.this, "Could not connect to server!", Toast.LENGTH_LONG).show();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            invalidateIt();


            ((BaseAdapter)customAdapter).notifyDataSetChanged();


            TextView schoolName = (TextView) findViewById(R.id.schoolName);
            schoolName.setText(s.title);
            TextView studentName = (TextView) findViewById(R.id.studentName);
            studentName.setText(s.student);

            for (int i = 0 ; i != categories.size() ; i++) {
                getSupportFragmentManager()
                        .beginTransaction().add(R.id.groupsFragment,new SectionFragment(), categories.get(i)).commit();
                getSupportFragmentManager().executePendingTransactions();
                FragmentManager fm = getSupportFragmentManager();

                Fragment testtagFragment = fm.findFragmentByTag(categories.get(i));
                TextView category = (TextView) testtagFragment.getView().findViewById(R.id.groupsSection);
                category.setText(categories.get(i));
            }


        }

    }
}
