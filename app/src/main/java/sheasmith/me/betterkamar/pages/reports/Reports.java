package sheasmith.me.betterkamar.pages.reports;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.List;

import layout.SectionFragment;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class Reports extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    List<ReportsObject> groupsList = new ArrayList<>();

    ListAdapter customAdapter;
    ListView list;
    ProgressDialog progressDialog;
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
            Intent i = new Intent(Reports.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {

        } else if (id == R.id.nav_details) {

        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_attendance) {

        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Reports.this, NCEA.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Reports.this, Servers.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        String title;
        String url;
        String lastyear;
        List<String> years = new ArrayList<>();
        List<ReportsObject> reports = new ArrayList<>();
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
                Document d = Jsoup.connect(s.hostname + "/student/index.php/reports/").cookies(s.cookies).get();
                Elements groups = d.getElementById("results_table").children();

                for (Element e : groups) {

                    Elements tr = e.getElementsByTag("tr");
                    for (Element e2 : tr) {

                        if (e2.child(0).className().equalsIgnoreCase("result_subject")) {
                            lastyear = e2.child(0).text();
                            years.add(lastyear);
                        }
                        else if (e2.child(0).className().equalsIgnoreCase("result_increase")) {
                            title = e2.child(0).text();
                            url = e2.child(0).child(0).attr("href");
                            ReportsObject g = new ReportsObject();
                            g.title = title;
                            g.url = url;
                            g.year = lastyear;
                            reports.add(g);
                        }
                    }
                }
            }
            catch (IOException e) {
                Looper.prepare();
                Toast.makeText(Reports.this, "Could not connect to server!", Toast.LENGTH_LONG).show();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            final HashMap<String, List<ReportsObject>> hashMap = new HashMap<>();
            progressDialog.dismiss();

            TextView schoolName = (TextView) findViewById(R.id.schoolName);
            schoolName.setText(s.title);
            TextView studentName = (TextView) findViewById(R.id.studentName);
            studentName.setText(s.student);

            Fragment empty = getSupportFragmentManager().findFragmentByTag("empty");
            int t = empty.getView().getVisibility();
            empty.getView().setVisibility(View.GONE);


            for (int i = 0 ; i != years.size() ; i++) {
                List<ReportsObject> c = new ArrayList<>();
                getSupportFragmentManager()
                        .beginTransaction().add(R.id.container,new SectionFragment(), years.get(i)).commit();
                getSupportFragmentManager().executePendingTransactions();
                FragmentManager fm = getSupportFragmentManager();

                Fragment testtagFragment = fm.findFragmentByTag(years.get(i));
                testtagFragment.getView().setVisibility(t);

                TextView category = (TextView) testtagFragment.getView().findViewById(R.id.groupsSection);
                List<String> list = new ArrayList<>();
                for (ReportsObject r : reports) {
                    if (r.year.equals(years.get(i))) {
                        String title = r.title;
                        list.add(title);
                        c.add(r);
                    }
                }
                hashMap.put(years.get(i), c);
                c.clear();

                if (list.isEmpty()) {
                    testtagFragment.getView().setVisibility(View.INVISIBLE);
                }
                final String y = years.get(i);
                ListView listView = (ListView) testtagFragment.getView().findViewById(R.id.groupsList);
                listView.setAdapter(new ArrayAdapter(context, android.R.layout.simple_list_item_1, list));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                        Intent k = new Intent(Reports.this, PDFViewer.class);
                        k.putExtra("listID", getIntent().getIntExtra("listID", -1));
                        k.putExtra("name", hashMap.get(y).get(pos).title);
                        k.putExtra("year", hashMap.get(y).get(pos).year);
                        k.putExtra("url", hashMap.get(y).get(pos).url);
                        startActivity(k);
                    }
                });
                category.setText(years.get(i));



            }


        }

    }
}
