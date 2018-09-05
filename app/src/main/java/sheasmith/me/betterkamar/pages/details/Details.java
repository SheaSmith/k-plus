package sheasmith.me.betterkamar.pages.details;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
import sheasmith.me.betterkamar.pages.groups.Groups;
import sheasmith.me.betterkamar.pages.ncea.NCEAAdapter;
import sheasmith.me.betterkamar.pages.ncea.NCEAObject;
import sheasmith.me.betterkamar.pages.ncea.StandardObject;
import sheasmith.me.betterkamar.pages.notices.NoticesPage;
import sheasmith.me.betterkamar.pages.pathways.Pathways;
import sheasmith.me.betterkamar.pages.reports.Reports;
import sheasmith.me.betterkamar.pages.timetable.Timetable;

public class Details extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    boolean error = false;
    ServersObject s;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        setContentView(R.layout.activity_details);
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
            Intent i = new Intent(Details.this, NoticesPage.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_calender) {
            Intent i = new Intent(Details.this, Calender.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_details) {

        } else if (id == R.id.nav_timetable) {
            Intent i = new Intent(Details.this, Timetable.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_attendance) {
            Intent i = new Intent(Details.this, Attendance.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_ncea) {
            Intent i = new Intent(Details.this, Details.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }  else if (id == R.id.nav_groups) {
            Intent i = new Intent(Details.this, Groups.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);

        } else if (id == R.id.nav_reports) {
            Intent i = new Intent(Details.this, Reports.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        } else if (id == R.id.nav_back) {
            startActivity(new Intent(Details.this, Servers.class));
        }
        else if (id == R.id.nav_pathways) {
            Intent i = new Intent(Details.this, Pathways.class);
            i.putExtra("listID", getIntent().getIntExtra("listID", -1));
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private class Connect extends AsyncTask<Void, Void, Void> {
        String token;
        String suid;
        Drawable proPic;

        StudentDetailsObject studentDetailsObject = new StudentDetailsObject();
        CaregiversDetailsObject caregiversDetailsObject =  new CaregiversDetailsObject();
        MedicalDetailsObject medicalDetailsObject = new MedicalDetailsObject();
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
                URL url = new URL(s.hostname + "/api/api.php");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("UserAgent", "KAMAR+ App [Android] - Student Profile Picture Retrieval (Logon)");
                byte[] postData = ("Key=vtku&Command=Logon&Username=" + s.username + "&Password=" + s.password).getBytes();
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( postData.length ));
                urlConnection.setUseCaches( false );
                DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream());
                wr.write( postData );
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    try {
                        JSONObject json = (XML.toJSONObject(stringBuilder.toString()));

                        if (json.getJSONObject("LogonResults").getString("Success").equals("YES")) {
                            token = json.getJSONObject("LogonResults").getString("Key");
                            suid = json.getJSONObject("LogonResults").getString("CurrentStudent");
                            URL image = new URL(s.hostname + "/api/img.php?Key=" + token + "&Stuid=" + suid);

                            proPic = Drawable.createFromStream(image.openStream(), "src");
                            InputStream t = image.openStream();
                            t.close();
                        }
                        else
                            Log.e("K+ Invalid API Response", stringBuilder.toString());

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                finally{
                    urlConnection.disconnect();
                }

            }
            catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Document d = Jsoup.connect(s.hostname + "/index.php/details").cookies(s.cookies).get();

                Elements details = d.getElementById("StudentDetails").child(0).child(0).child(0).child(1).children();
                int i = 0;
                for (Element e : details) {
                    if (i==0) {
                        studentDetailsObject.legalFirstName = e.child(1).text();
                        studentDetailsObject.legalForeName = e.child(2).text();
                        studentDetailsObject.legalLastName = e.child(3).text();
                    }
                    else if (i==1) {
                        studentDetailsObject.preferredFirstName = e.child(1).text();
                        studentDetailsObject.preferredForeName = e.child(2).text();
                        studentDetailsObject.preferredLastName = e.child(3).text();
                    }
                    else if (i==2) {
                        studentDetailsObject.dateOfBirth = e.child(1).text();
                    }
                    else if (i==3) {
                        studentDetailsObject.nsn = e.child(1).text();
                    }
                    i++;
                }

                Elements addressDetails = d.getElementById("student_details").child(1).child(0).child(1).children();
                i = 0;
                // TODO handle br properly
                for (Element e : addressDetails) {
                    if (i==1)
                        studentDetailsObject.phone = e.child(0).html().replace("<br>", "\n");
                    else if (i==3)
                        studentDetailsObject.physicalAddress = e.child(0).html().replace("<br>", "\n");
                    else if (i==5)
                        studentDetailsObject.postalAddress = e.child(0).html().replace("<br>", "\n");
                    i++;
                }
                Elements phoneDetails = d.getElementById("student_details").child(2).child(0).child(0).children();
                i = 0;
                for (Element e : phoneDetails) {
                    if (i==1)
                        studentDetailsObject.homePhone = e.child(1).text();

                    else if (i==2)
                        studentDetailsObject.mobile = e.child(1).text();

                    i++;
                }

                Elements caregiverDetails = d.getElementById("CaregiversDetails").child(1).child(0).child(0).children();
                i = 0;
                for (Element e : caregiverDetails) {
                    if (i==1) {
                        caregiversDetailsObject.caregiver1Title = e.child(1).text();
                        caregiversDetailsObject.caregiver2Title = e.child(2).text();
                    }
                    else if (i==2) {
                        caregiversDetailsObject.caregiver1Name = e.child(1).text();
                        caregiversDetailsObject.caregiver2Name = e.child(2).text();
                    }
                    else if (i==3) {
                        caregiversDetailsObject.caregiver1Address = e.child(1).html().replace("<br>", "\n");
                        caregiversDetailsObject.caregiver2Address = e.child(2).html().replace("<br>", "\n");
                    }
                    else if (i==4) {
                        caregiversDetailsObject.caregiver1Home = e.child(1).text();
                        caregiversDetailsObject.caregiver2Home = e.child(2).text();
                    }
                    else if (i==5) {
                        caregiversDetailsObject.caregiver1Mobile = e.child(1).text();
                        caregiversDetailsObject.caregiver2Mobile = e.child(2).text();
                    }
                    else if (i==6) {
                        caregiversDetailsObject.caregiver1Occupation = e.child(1).text();
                        caregiversDetailsObject.caregiver2Occupation = e.child(2).text();
                    }
                    else if (i==7) {
                        caregiversDetailsObject.caregiver1Work = e.child(1).text();
                        caregiversDetailsObject.caregiver2Work = e.child(2).text();
                    }
                    else if (i==8) {
                        caregiversDetailsObject.caregiver1Email = e.child(1).text();
                        caregiversDetailsObject.caregiver2Email = e.child(2).text();
                    }
                    i++;
                }

                Elements emergencyContact = d.getElementById("ContactDetails").child(1).child(0).child(0).children();
                i = 0;
                for (Element e : emergencyContact) {
                    if (i==1)
                        caregiversDetailsObject.emergencyTitle = e.child(1).text();

                    else if (i==2)
                        caregiversDetailsObject.emergencyName = e.child(1).text();

                    else if (i==3)
                        caregiversDetailsObject.emergencyAddress = e.child(1).html().replace("<br>", "\n");

                    else if (i==4)
                        caregiversDetailsObject.emergencyHome = e.child(1).text();

                    else if (i==5)
                        caregiversDetailsObject.emergencyMobile = e.child(1).text();

                    else if (i==6)
                        caregiversDetailsObject.emergencyOccupation = e.child(1).text();

                    else if (i==7)
                        caregiversDetailsObject.emergencyWork = e.child(1).text();
                    i++;
                }

                Elements medicalDetails = d.getElementById("MedicalDetails").child(1).child(0).child(0).children();
                i = 0;
                for (Element e : medicalDetails) {
                    if (i == 0)
                        medicalDetailsObject.doctor = e.child(1).text();
                    if (i == 1)
                        medicalDetailsObject.dentist = e.child(1).text();
                    if (i == 2)
                        medicalDetailsObject.allowedPanadol = e.child(1).text();
                    if (i == 3)
                        medicalDetailsObject.allowedIbuprofen = e.child(1).text();
                    if (i == 5)
                        medicalDetailsObject.medicalConditions = e.child(0).html();
                    if (i == 7)
                        medicalDetailsObject.allergies = e.child(0).html();
                    if (i == 9)
                        medicalDetailsObject.medicalNotes = e.child(0).html();
                    i++;
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


                ImageView picture = (ImageView) findViewById(R.id.profilePicture);
                picture.setImageDrawable(proPic);

                ((TextView) findViewById(R.id.firstNameLegal)).setText(studentDetailsObject.legalFirstName);
                ((TextView) findViewById(R.id.foreNameLegal)).setText(studentDetailsObject.legalForeName);
                ((TextView) findViewById(R.id.lastNameLegal)).setText(studentDetailsObject.legalLastName);

                ((TextView) findViewById(R.id.firstNamePreferred)).setText(studentDetailsObject.preferredFirstName);
                ((TextView) findViewById(R.id.foreNamePreferred)).setText(studentDetailsObject.preferredForeName);
                ((TextView) findViewById(R.id.lastNamePreferred)).setText(studentDetailsObject.preferredLastName);

                ((TextView) findViewById(R.id.dateOfBirth)).setText(studentDetailsObject.dateOfBirth);
                ((TextView) findViewById(R.id.nsn)).setText(studentDetailsObject.nsn);
                ((TextView) findViewById(R.id.physicalAddress)).setText(studentDetailsObject.physicalAddress);
                ((TextView) findViewById(R.id.postalAddress)).setText(studentDetailsObject.postalAddress);
                ((TextView) findViewById(R.id.phone)).setText(studentDetailsObject.phone);
                ((TextView) findViewById(R.id.home)).setText(studentDetailsObject.homePhone);
                ((TextView) findViewById(R.id.mobile)).setText(studentDetailsObject.mobile);

                ((TextView) findViewById(R.id.mum)).setText(caregiversDetailsObject.caregiver1Title);
                ((TextView) findViewById(R.id.dad)).setText(caregiversDetailsObject.caregiver2Title);
                ((TextView) findViewById(R.id.mumName)).setText(caregiversDetailsObject.caregiver1Name);
                ((TextView) findViewById(R.id.dadName)).setText(caregiversDetailsObject.caregiver2Name);
                ((TextView) findViewById(R.id.mumAddress)).setText(caregiversDetailsObject.caregiver1Address);
                ((TextView) findViewById(R.id.dadAddress)).setText(caregiversDetailsObject.caregiver2Address);
                ((TextView) findViewById(R.id.mumHome)).setText(caregiversDetailsObject.caregiver1Home);
                ((TextView) findViewById(R.id.dadHome)).setText(caregiversDetailsObject.caregiver2Home);
                ((TextView) findViewById(R.id.mumMobile)).setText(caregiversDetailsObject.caregiver1Mobile);
                ((TextView) findViewById(R.id.dadMobile)).setText(caregiversDetailsObject.caregiver2Mobile);
                ((TextView) findViewById(R.id.mumOccupation)).setText(caregiversDetailsObject.caregiver1Occupation);
                ((TextView) findViewById(R.id.dadOccupation)).setText(caregiversDetailsObject.caregiver2Occupation);
                ((TextView) findViewById(R.id.mumWork)).setText(caregiversDetailsObject.caregiver1Work);
                ((TextView) findViewById(R.id.dadWork)).setText(caregiversDetailsObject.caregiver2Work);
                ((TextView) findViewById(R.id.mumEmail)).setText(caregiversDetailsObject.caregiver1Email);
                ((TextView) findViewById(R.id.dadEmail)).setText(caregiversDetailsObject.caregiver2Email);

                ((TextView) findViewById(R.id.grandma)).setText(caregiversDetailsObject.emergencyTitle);
                ((TextView) findViewById(R.id.emergencyName)).setText(caregiversDetailsObject.emergencyName);
                ((TextView) findViewById(R.id.emergencyAddress)).setText(caregiversDetailsObject.emergencyAddress);
                ((TextView) findViewById(R.id.emergencyHome)).setText(caregiversDetailsObject.emergencyHome);
                ((TextView) findViewById(R.id.emergencyMobile)).setText(caregiversDetailsObject.emergencyMobile);
                ((TextView) findViewById(R.id.emergencyOccupation)).setText(caregiversDetailsObject.emergencyOccupation);
                ((TextView) findViewById(R.id.emergencyWork)).setText(caregiversDetailsObject.emergencyWork);

                ((TextView) findViewById(R.id.medicalDoctor)).setText(medicalDetailsObject.doctor);
                ((TextView) findViewById(R.id.medicalDentist)).setText(medicalDetailsObject.dentist);
                ((TextView) findViewById(R.id.medicalPanadol)).setText(medicalDetailsObject.allowedPanadol);
                ((TextView) findViewById(R.id.medicalIbuprofen)).setText(medicalDetailsObject.allowedIbuprofen);
                ((TextView) findViewById(R.id.medicalConditions)).setText(medicalDetailsObject.medicalConditions);
                ((TextView) findViewById(R.id.medicalReactions)).setText(medicalDetailsObject.allergies);
                ((TextView) findViewById(R.id.medicalNotes)).setText(medicalDetailsObject.medicalNotes);

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
        navigationView.getMenu().getItem(2).setChecked(true);
    }
}
