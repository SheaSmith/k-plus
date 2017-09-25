package sheasmith.me.betterkamar.pages.pathways;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.ClassFragment;
import layout.NCEAFragment;
import sheasmith.me.betterkamar.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.attendance.Attendance;
import sheasmith.me.betterkamar.pages.calender.Calender;
import sheasmith.me.betterkamar.pages.details.Details;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEA;
import sheasmith.me.betterkamar.pages.ncea.StandardObject;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.pages.reports.Reports;
import sheasmith.me.betterkamar.pages.timetable.Timetable;

public class Pathways extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean error;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_pathways);
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

        //list = (ListView) findViewById(R.id.groupsList);
        //customAdapter = new PathwaysAdapter(getApplicationContext(), groupsList);
        //list.setAdapter(customAdapter);
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
            Intent i = new Intent(Pathways.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Pathways.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(Pathways.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Pathways.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Pathways.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Pathways.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Pathways.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);

        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Pathways.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Pathways.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        List<PathwaysObject> pathways = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            findViewById(R.id.mainArea).setVisibility(View.GONE);
            error = false;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document d = Jsoup.connect(s.hostname + "/student/index.php/pathways/").cookies(s.cookies).get();
                Elements items = d.getElementsByClass("table").first().child(1).children();
                for (Element e : items) {
                    PathwaysObject p = new PathwaysObject();
                    p.standardNumber = e.child(0).text();
                    p.version = e.child(1).text();
                    p.level = e.child(2).text();
                    p.title = e.child(3).text();
                    p.credits = e.child(4).text();
                    p.result = e.child(5).text();

                    Elements dots = e.child(6).child(0).child(0).children();
                    for (Element dot : dots) {
                        if (!dot.child(0).className().contains("clear")) {
                            p.pathways.add(dot.child(0).className().split(" ")[1]);
                        }
                    }
                    pathways.add(p);
                }
            } catch (IOException e) {
                e.printStackTrace();
                error = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!error) {
                try {
                    BarChart chart = (BarChart) findViewById(R.id.chart);
                    List<BarEntry> barEntries = new ArrayList<>();
                    HashMap<String, Integer> gained = new HashMap<>();
                    HashMap<String, Integer> predicted = new HashMap<>();
                    String[] properOrder = {"orange", "red", "green", "blue", "purple", "yellow"};
                    for (PathwaysObject p : pathways) {
                        boolean hasGained = true;
                        if (p.result.contains("Not Published"))
                            hasGained = false;
                        int credits = Integer.parseInt(p.credits);
                        for (String r : p.pathways) {
                            if (hasGained) {
                                int g;
                                try {
                                    g = gained.get(r);
                                } catch (NullPointerException e) {
                                    g = 0;
                                }
                                int p1;
                                try {
                                    p1 = predicted.get(r);
                                } catch (NullPointerException e) {
                                    p1 = 0;
                                }
                                gained.put(r, g + credits);
                                predicted.put(r, p1 + credits);
                            } else {
                                int p1;
                                try {
                                    p1 = predicted.get(r);
                                } catch (NullPointerException e) {
                                    p1 = 0;
                                }
                                predicted.put(r, p1 + credits);
                            }
                        }
                    }
                    float i = 0;
                    for (String key : properOrder) {
                        int value = predicted.get(key);
                        List<Float> data = new ArrayList<>();
                        boolean foundMatch = false;
                        for (String key2 : properOrder) {
                            int value2 = gained.get(key2);
                            if (key.equals(key2)) {
                                data.add((float) value2);
                                data.add((float) value);
                                foundMatch = true;
                            }
                        }
                        if (!foundMatch) {
                            data.add(0F);
                            data.add((float) value);
                        }

                        barEntries.add(new BarEntry(i, new float[]{data.get(0), data.get(1)}));
                        i++;
                    }

                    BarDataSet dataSet = new BarDataSet(barEntries, "");
                    dataSet.setColors(new int[]{R.color.pathwaysOrange, R.color.pathwaysGrey, R.color.pathwaysRed, R.color.pathwaysGrey, R.color.pathwaysGreen, R.color.pathwaysGrey, R.color.pathwaysBlue, R.color.pathwaysGrey, R.color.pathwaysPurple, R.color.pathwaysGrey, R.color.pathwaysYellow, R.color.pathwaysGrey}, context);

                    BarData barData = new BarData(dataSet);

                    chart.setData(barData);
                    Description d = new Description();
                    d.setText("");
                    chart.setDescription(d);
                    chart.invalidate();
                    chart.getLegend().setEnabled(false);
                    chart.animateXY(1000, 1000);
                    String[] labels = new String[]{"Construction and Infrastructure", "Manufacture and Technology", "Primary Industries", "Service Industries", "Social and Community", "Creative Industries"};
                    chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
                    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    chart.getXAxis().setLabelRotationAngle(-45);
                    chart.setExtraBottomOffset(50);
                    chart.setDrawValueAboveBar(true);

                    ListView list = (ListView) findViewById(R.id.list);
                    list.setAdapter(new PathwaysAdapter(Pathways.this, pathways));
                    list.invalidate();
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    findViewById(R.id.mainArea).setVisibility(View.VISIBLE);
                }
                catch (Exception e) {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    Snackbar.make(findViewById(R.id.mainLayout), "You are not enrolled in NCEA", Snackbar.LENGTH_LONG);
                }
            } else {
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
        super.onResume(); if (EditServer.res != null) {
            if (EditServer.res.equals("deletion"))
                finish();
            else if (EditServer.res.equals("update"))
                setTitle(s.title);
            EditServer.res = null;
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(8).setChecked(true);
    }
}
