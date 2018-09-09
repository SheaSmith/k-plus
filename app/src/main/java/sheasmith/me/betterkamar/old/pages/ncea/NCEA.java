package sheasmith.me.betterkamar.old.pages.ncea;

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
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import layout.ClassFragment;
import layout.NCEAFragment;
import sheasmith.me.betterkamar.old.EditServer;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.old.Servers;
import sheasmith.me.betterkamar.old.ServersObject;
import sheasmith.me.betterkamar.old.pages.attendance.Attendance;
import sheasmith.me.betterkamar.old.pages.calender.Calender;
import sheasmith.me.betterkamar.old.pages.details.Details;
import sheasmith.me.betterkamar.old.pages.groups.Groups;
import sheasmith.me.betterkamar.old.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.old.pages.pathways.Pathways;
import sheasmith.me.betterkamar.old.pages.reports.Reports;
import sheasmith.me.betterkamar.old.pages.timetable.Timetable;

public class NCEA extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    /**
     * Main progress dialog - for loading
     */
    boolean error;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_ncea);
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
            Intent i = new Intent(NCEA.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(NCEA.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {
            Intent i = new Intent(NCEA.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(NCEA.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(NCEA.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {

        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(NCEA.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);

        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(NCEA.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(NCEA.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(NCEA.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        String nach;
        String ach;
        String mer;
        String ex;

        List<StandardObject> standardsThingie = new ArrayList<>();
        List<String> ueLitNum = new ArrayList<>();

        HashMap<String, HashMap<String, List<NCEAObject>>> detailedResults = new HashMap<>();
        HashMap<String, String> totalCredits = new HashMap<>();


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


                    Document d = Jsoup.connect(s.hostname + "/index.php/ncea_summary/").cookies(s.cookies).get();

                Element na = d.getElementsByClass("notachieved").first();
                nach = na.text();

                Element a = d.getElementsByClass("achieved").first();
                ach = a.text();

                Element m = d.getElementsByClass("merit").first();
                mer = m.text();

                Element e = d.getElementsByClass("excellence").first();
                ex = e.text();

                Elements tableChildren = d.select("table.table-sm:nth-child(1) > tbody").first().children();
                int i = 0;
                for (Element t : tableChildren) {
                        StandardObject o = new StandardObject();
                        o.year = t.child(0).text();
                        o.notAchieved = t.child(1).text();
                        o.achieved = t.child(2).text();
                        o.merit = t.child(3).text();
                        o.excellence = t.child(4).text();
                        o.total = t.child(5).text();
                        o.attempted = t.child(6).text();

                        o.type = "year";

                        standardsThingie.add(o);


                    i++;
                }

                Elements standardChildren = d.select("table.table:nth-child(1)").get(2).children();
                i = 0;
                for (Element t : standardChildren) {
                    if (i != 0) {
                        StandardObject o = new StandardObject();
                        o.year = t.child(0).text();
                        o.notAchieved = t.child(1).text();
                        o.achieved = t.child(2).text();
                        o.merit = t.child(3).text();
                        o.excellence = t.child(4).text();
                        o.total = t.child(5).text();
                        o.attempted = t.child(6).text();
                        try {
                            o.qualification = t.child(7).ownText();
                        } catch (IndexOutOfBoundsException ex) {
                            o.qualification = "";
                        }

                        o.type = "level";

                        standardsThingie.add(o);
                    }

                    i++;
                }
                /*
                else if (i > 10) {
                        StandardObject o = new StandardObject();
                        o.year = t.child(0).text();
                        o.notAchieved = t.child(1).text();
                        o.achieved = t.child(2).text();
                        o.merit = t.child(3).text();
                        o.excellence = t.child(4).text();
                        o.total = t.child(5).text();
                        o.attempted = t.child(6).text();
                        try {
                            o.qualification = t.child(7).ownText();
                        }
                        catch (IndexOutOfBoundsException ex) {
                            o.qualification = "";
                        }

                        o.type = "level";

                        standardsThingie.add(o);
                    }

                 */

                Elements insertName = d.getElementsByAttributeValue("rowspan", "6").first().children();
                ueLitNum.add(insertName.get(0).getElementsByTag("strong").first().text() + ": " + insertName.get(0).ownText());
                ueLitNum.add(insertName.get(2).getElementsByTag("strong").first().text() + ": " + insertName.get(2).ownText());
                ueLitNum.add(insertName.get(4).text() + ": " + d.getElementsByAttributeValue("rowspan", "6").first().ownText());

                String lastLevel = "";
                String lastSubject = "";
                List<NCEAObject> list = new ArrayList<>();
                HashMap<String, List<NCEAObject>> ncea = new HashMap<>();

                Elements resultsTable = d.select("body > div > section > article").first().getElementsByTag("table").last().children();
                for (Element r : resultsTable) {
                    if (r.tag().equals(Tag.valueOf("thead"))) {
                        if (!list.isEmpty()) {
                            ncea.put(lastSubject, list);
                            list.clear();
                        }
                        if (!ncea.isEmpty()) {
                            detailedResults.put(lastLevel, ncea);
                            ncea.clear();
                        }
                        lastLevel = r.child(0).child(0).text();
                        totalCredits.put(lastLevel, r.child(0).child(1).text());
                    }
                    else {
                        for (Element z : r.children()) {
                            if (z.className().contains("table-active")) {
                                if (!list.isEmpty()) {
                                    ncea.put(lastSubject, list);
                                    list = new ArrayList<>();
                                }
                                lastSubject = z.text();
                            }
                            else {
                                NCEAObject n = new NCEAObject();
                                n.standard = z.child(0).text();
                                n.credits = z.child(1).text();
                                n.grade = z.child(2).text();

                                list.add(n);
                            }
                        }
                    }

                }
                if (!list.isEmpty()) {
                    ncea.put(lastSubject, list);
                }
                if (!ncea.isEmpty()) {
                    detailedResults.put(lastLevel, ncea);
                }

            }
            catch (IOException e) {
                Snackbar.make(findViewById(R.id.main_content), "Could not connect to server!", Snackbar.LENGTH_LONG);
                error = true;
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if (!error) {



                TextView nA = (TextView) findViewById(R.id.naGrade);
                nA.setText(nach);

                TextView A = (TextView) findViewById(R.id.aGrade);
                A.setText(ach);

                TextView M = (TextView) findViewById(R.id.mGrade);
                M.setText(mer);

                TextView E = (TextView) findViewById(R.id.eGrade);
                E.setText(ex);

                PieChart chart = (PieChart) findViewById(R.id.chart);

                List<Integer> values = new ArrayList<>();
                values.add(Integer.parseInt(nach));
                values.add(Integer.parseInt(ach));
                values.add(Integer.parseInt(mer));
                values.add(Integer.parseInt(ex));
                String[] names = {"Not Achieved", "Achieved", "Merit", "Excellence"};

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int z : values) {
                    if (z != 0) {
                        pieEntries.add(new PieEntry((long) z, names[values.indexOf(z)]));
                    } else {
                        pieEntries.add(new PieEntry((long) z, ""));
                    }

                }

                PieDataSet pieData = new PieDataSet(pieEntries, "");
                pieData.setColors(new int[]{R.color.red, R.color.blue, R.color.orange, R.color.green}, context);


                PieData pieData1 = new PieData(pieData);
                pieData1.setDrawValues(false);

                chart.setData(pieData1);
                Description d = new Description();
                d.setText("");
                chart.setDescription(d);
                chart.invalidate();
                chart.getLegend().setEnabled(false);
                chart.animateXY(1000, 1000);

                int credits = Integer.parseInt(ach) + Integer.parseInt(mer) + Integer.parseInt(ex);
                int attempted = credits + Integer.parseInt(nach);

                TextView creditsView = (TextView) findViewById(R.id.totalGrade);
                creditsView.setText("" + credits + "");

                TextView attemptedView = (TextView) findViewById(R.id.totalAttemptedGrade);
                attemptedView.setText("" + attempted + "");


                // Sorry...
                StandardObject o = standardsThingie.get(0);
                ((TextView) findViewById(R.id.year0)).setText(o.year);
                ((TextView) findViewById(R.id.year0Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.year0A)).setText(o.achieved);
                ((TextView) findViewById(R.id.year0M)).setText(o.merit);
                ((TextView) findViewById(R.id.year0E)).setText(o.excellence);
                ((TextView) findViewById(R.id.year0Credits)).setText(o.total);
                ((TextView) findViewById(R.id.year0Attempted)).setText(o.attempted);

                o = standardsThingie.get(1);
                ((TextView) findViewById(R.id.year1)).setText(o.year);
                ((TextView) findViewById(R.id.year1Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.year1A)).setText(o.achieved);
                ((TextView) findViewById(R.id.year1M)).setText(o.merit);
                ((TextView) findViewById(R.id.year1E)).setText(o.excellence);
                ((TextView) findViewById(R.id.year1Credits)).setText(o.total);
                ((TextView) findViewById(R.id.year1Attempted)).setText(o.attempted);

                o = standardsThingie.get(2);
                ((TextView) findViewById(R.id.year2)).setText(o.year);
                ((TextView) findViewById(R.id.year2Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.year2A)).setText(o.achieved);
                ((TextView) findViewById(R.id.year2M)).setText(o.merit);
                ((TextView) findViewById(R.id.year2E)).setText(o.excellence);
                ((TextView) findViewById(R.id.year2Credits)).setText(o.total);
                ((TextView) findViewById(R.id.year2Attempted)).setText(o.attempted);

                o = standardsThingie.get(3);
                ((TextView) findViewById(R.id.year3)).setText(o.year);
                ((TextView) findViewById(R.id.year3Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.year3A)).setText(o.achieved);
                ((TextView) findViewById(R.id.year3M)).setText(o.merit);
                ((TextView) findViewById(R.id.year3E)).setText(o.excellence);
                ((TextView) findViewById(R.id.year3Credits)).setText(o.total);
                ((TextView) findViewById(R.id.year3Attempted)).setText(o.attempted);

                o = standardsThingie.get(4);
                ((TextView) findViewById(R.id.year4)).setText(o.year);
                ((TextView) findViewById(R.id.year4Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.year4A)).setText(o.achieved);
                ((TextView) findViewById(R.id.year4M)).setText(o.merit);
                ((TextView) findViewById(R.id.year4E)).setText(o.excellence);
                ((TextView) findViewById(R.id.year4Credits)).setText(o.total);
                ((TextView) findViewById(R.id.year4Attempted)).setText(o.attempted);

                // Sets the levels table
                o = standardsThingie.get(5);
                ((TextView) findViewById(R.id.level5)).setText(o.year);
                ((TextView) findViewById(R.id.level5Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.level5A)).setText(o.achieved);
                ((TextView) findViewById(R.id.level5M)).setText(o.merit);
                ((TextView) findViewById(R.id.level5E)).setText(o.excellence);
                ((TextView) findViewById(R.id.level5Credits)).setText(o.total);
                ((TextView) findViewById(R.id.level5Attempted)).setText(o.attempted);
                ((TextView) findViewById(R.id.level5Qualification)).setText(o.qualification);

                o = standardsThingie.get(6);
                ((TextView) findViewById(R.id.level4)).setText(o.year);
                ((TextView) findViewById(R.id.level4Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.level4A)).setText(o.achieved);
                ((TextView) findViewById(R.id.level4M)).setText(o.merit);
                ((TextView) findViewById(R.id.level4E)).setText(o.excellence);
                ((TextView) findViewById(R.id.level4Credits)).setText(o.total);
                ((TextView) findViewById(R.id.level4Attempted)).setText(o.attempted);
                ((TextView) findViewById(R.id.level4Qualification)).setText(o.qualification);

                o = standardsThingie.get(7);
                ((TextView) findViewById(R.id.level3)).setText(o.year);
                ((TextView) findViewById(R.id.level3Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.level3A)).setText(o.achieved);
                ((TextView) findViewById(R.id.level3M)).setText(o.merit);
                ((TextView) findViewById(R.id.level3E)).setText(o.excellence);
                ((TextView) findViewById(R.id.level3Credits)).setText(o.total);
                ((TextView) findViewById(R.id.level3Attempted)).setText(o.attempted);
                ((TextView) findViewById(R.id.level3Qualification)).setText(o.qualification);

                o = standardsThingie.get(8);
                ((TextView) findViewById(R.id.level2)).setText(o.year);
                ((TextView) findViewById(R.id.level2Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.level2A)).setText(o.achieved);
                ((TextView) findViewById(R.id.level2M)).setText(o.merit);
                ((TextView) findViewById(R.id.level2E)).setText(o.excellence);
                ((TextView) findViewById(R.id.level2Credits)).setText(o.total);
                ((TextView) findViewById(R.id.level2Attempted)).setText(o.attempted);
                ((TextView) findViewById(R.id.level2Qualification)).setText(o.qualification);

                o = standardsThingie.get(9);
                ((TextView) findViewById(R.id.level1)).setText(o.year);
                ((TextView) findViewById(R.id.level1Na)).setText(o.notAchieved);
                ((TextView) findViewById(R.id.level1A)).setText(o.achieved);
                ((TextView) findViewById(R.id.level1M)).setText(o.merit);
                ((TextView) findViewById(R.id.level1E)).setText(o.excellence);
                ((TextView) findViewById(R.id.level1Credits)).setText(o.total);
                ((TextView) findViewById(R.id.level1Attempted)).setText(o.attempted);
                ((TextView) findViewById(R.id.level1Qualification)).setText(o.qualification);

                // still sorry
                // Sets the literacy & numeracy & university entrance
                ((TextView) findViewById(R.id.ue)).setText(ueLitNum.get(0));
                ((TextView) findViewById(R.id.literacy)).setText(ueLitNum.get(1));
                ((TextView) findViewById(R.id.numeracy)).setText(ueLitNum.get(2));


                List<Fragment> al = getSupportFragmentManager().getFragments();
                if (al != null) {
                    for (Fragment z : al) {
                        getSupportFragmentManager().beginTransaction().hide(z).commit();
                    }
                }

                for (Map.Entry<String, HashMap<String, List<NCEAObject>>> entry : detailedResults.entrySet()) {
                    String key = entry.getKey();
                    HashMap<String, List<NCEAObject>> value = entry.getValue();
                    getSupportFragmentManager()
                            .beginTransaction().add(R.id.main, new NCEAFragment(), key).commit();
                    getSupportFragmentManager().executePendingTransactions();
                    FragmentManager fm = getSupportFragmentManager();

                    Fragment main = fm.findFragmentByTag(key);
                    main.getView().setVisibility(View.VISIBLE);

                    TextView level = (TextView) main.getView().findViewById(R.id.level);
                    level.setText(key);

                    TextView creditsV = (TextView) main.getView().findViewById(R.id.credits);
                    String s = totalCredits.get(key);
                    creditsV.setText(s);

                    for (Map.Entry<String, List<NCEAObject>> entry2 : value.entrySet()) {
                        String key2 = entry2.getKey();
                        List<NCEAObject> value2 = entry2.getValue();

                        getSupportFragmentManager()
                                .beginTransaction().add(R.id.main, new ClassFragment(), key2).commit();
                        getSupportFragmentManager().executePendingTransactions();
                        FragmentManager fm2 = getSupportFragmentManager();

                        Fragment subject = fm2.findFragmentByTag(key2);
                        subject.getView().setVisibility(View.VISIBLE);

                        TextView type = (TextView) subject.getView().findViewById(R.id.subject);
                        type.setText(key2);

                        ListView standards = (ListView) subject.getView().findViewById(R.id.leList);
                        standards.setAdapter(new NCEAAdapter(NCEA.this, value2));
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
        navigationView.getMenu().getItem(5).setChecked(true);
    }
}
