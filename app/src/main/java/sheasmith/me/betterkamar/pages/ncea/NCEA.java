package sheasmith.me.betterkamar.pages.ncea;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.Servers;
import sheasmith.me.betterkamar.ServersObject;
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;

public class NCEA extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<NCEAObject> groupsList = new ArrayList<>();

    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN };

    private static double[] VALUES = new double[] { 10, 11, 12, 13 };

    private static String[] NAME_LIST = new String[] { "A", "B", "C", "D" };

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private GraphicalView mChartView;


    ListAdapter customAdapter;
    ListView list;
    ProgressDialog progressDialog;
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
        //customAdapter = new NCEAAdapter(getApplicationContext(), groupsList);
        //list.setAdapter(customAdapter);
        Connect t = new Connect();
        if (!t.getStatus().equals(AsyncTask.Status.RUNNING)) {
           t.execute();
        }
        double[] d = {90, 90, 90, 90};

        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);

        for (int i = 0; i < VALUES.length; i++) {
            mSeries.add(NAME_LIST[i] + " " + VALUES[i], VALUES[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
        }

        if (mChartView != null) {
            mChartView.repaint();
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
            Intent i = new Intent(NCEA.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {

        } else if (id == R.id.nav_details) {

        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_attendance) {

        } else if (id == R.id.nav_ncea) {

        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(NCEA.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);

        } else if (id == R.id.nav_reports) {

        } else if (id == R.id.nav_back) {
            startActivity(new Intent(NCEA.this, Servers.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    public void invalidateIt() {

        list.invalidateViews();
    }
    private class Connect extends AsyncTask<Void, Void, Void> {
        String nach;
        String ach;
        String mer;
        String ex;


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


                    Document d = Jsoup.connect(s.hostname + "/student/index.php/ncea-summary/").cookies(s.cookies).get();

                Element na = d.getElementsByClass("notachieved").first();
                nach = na.text();

                Element a = d.getElementsByClass("achieved").first();
                ach = a.text();

                Element m = d.getElementsByClass("merit").first();
                mer = m.text();

                Element e = d.getElementsByClass("excellence").first();
                ex = e.text();



            }
            catch (IOException e) {
                Looper.prepare();
                Toast.makeText(NCEA.this, "Could not connect to server!", Toast.LENGTH_LONG).show();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            TextView schoolName = (TextView) findViewById(R.id.schoolName);
            schoolName.setText(s.title);
            TextView studentName = (TextView) findViewById(R.id.studentName);
            studentName.setText(s.student);

            TextView nA = (TextView) findViewById(R.id.naGrade);
            nA.setText(nach);

            TextView A = (TextView) findViewById(R.id.aGrade);
            A.setText(ach);

            TextView M = (TextView) findViewById(R.id.mGrade);
            M.setText(mer);

            TextView E = (TextView) findViewById(R.id.eGrade);
            E.setText(ex);

            int credits = Integer.parseInt(ach) + Integer.parseInt(mer) + Integer.parseInt(ex);
            int attempted = credits + Integer.parseInt(nach);

            TextView creditsView = (TextView) findViewById(R.id.totalGrade);
            creditsView.setText("" + credits + "");

            TextView attemptedView = (TextView) findViewById(R.id.totalAttemptedGrade);
            attemptedView.setText("" + attempted + "");
        }

}
    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(5).setChecked(true);
    }
}
