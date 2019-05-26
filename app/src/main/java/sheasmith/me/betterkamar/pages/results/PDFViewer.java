/*
 * Created by Shea Smith on 26/05/19 9:35 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 26/05/19 9:35 PM
 */

package sheasmith.me.betterkamar.pages.results;

/**
 * Created by TheDiamondPicks on 23/09/2018.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by Shea on 2/03/2017.
 */

public class PDFViewer extends Activity {
    ProgressDialog mProgressDialog;
    ReportsObject report;
    PortalObject mPortal;
    private Tracker mTracker;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        setTheme(getResources().getIdentifier("T_" + stringColor, "style", getPackageName()));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_viewer);
        mPortal = (PortalObject) getIntent().getSerializableExtra("portal");

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.noInternet).setVisibility(View.GONE);
        findViewById(R.id.webView).setVisibility(View.VISIBLE);

        SwipeRefreshLayout layout = findViewById(R.id.swipe_container);
        layout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
            }
        });

        KamarPlusApplication application = (KamarPlusApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Report Viewer");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        report = (ReportsObject) getIntent().getSerializableExtra("report");
        ((TextView) findViewById(R.id.reportName)).setText(report.title);
        File f = new File(getCacheDir() + "/betterkamar/" + mPortal.studentFile.replace(".jpg", ""), report.title.replace(" ", "_") + ".pdf");
        if (!f.exists()) {

            mProgressDialog = new ProgressDialog(PDFViewer.this);
            mProgressDialog.setTitle("Downloading Report...");
            mProgressDialog.setMessage("Downloading Report " + report.title);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);

            final DownloadTask downloadTask = new DownloadTask(PDFViewer.this);
            downloadTask.execute(report.url);

            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    downloadTask.cancel(true);
                }
            });

            //PDFView p = (PDFView) findViewById(R.id.webView);
            //p.fromUri(Uri.parse(Uri.decode("http//async5.org/moz/pdfjs.pdf"))).load();
        }
        else {
            PDFView p = (PDFView) findViewById(R.id.webView);
            if (f.length() != 0)
                p.fromFile(f).load();
            else {
                f.delete();
                recreate();
            }
        }

    }



    private class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                Connection.Response origCookies = Jsoup.connect(mPortal.hostname + "/index.php").method(Connection.Method.GET).execute();
                Map<String, String> sessionCookies = origCookies.cookies();

                Connection.Response login;
                if (sessionCookies.containsKey("csrf_kamar_cn"))
                    login = Jsoup.connect(mPortal.hostname + "/index.php/login").method(Connection.Method.POST).data("username", mPortal.username, "password", mPortal.password, "csrf_kamar_sn", sessionCookies.get("csrf_kamar_cn")).cookies(sessionCookies).execute();
                else
                    login = Jsoup.connect(mPortal + "/index.php/login").method(Connection.Method.POST).data("username", mPortal.username, "password", mPortal.password).cookies(sessionCookies).execute();

                Map<String, String> loginCookies = login.cookies();
//                    if (sessionCookies.containsKey("kamar_session"))
                loginCookies.put("kamar_session", sessionCookies.get("kamar_session"));

                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                StringBuilder cookies = new StringBuilder();
                for (Map.Entry<String, String> entry : loginCookies.entrySet()) {

                    cookies.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
                }
                connection.setRequestProperty("Cookie", cookies.toString());
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                String sname = mPortal.studentFile.replace(".jpg", "");
                String name = report.title.replace(" ", "_");
                File t = new File(getCacheDir(), "/betterkamar/" + sname);
                t.mkdirs();
                File fina = new File(t, name + ".pdf");

                output = new FileOutputStream(fina);

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            mProgressDialog.dismiss();
            if (result != null)
                if (result.contains("java.net.UnknownHostException") || result.contains("java.net.SocketTimeoutException")) {
                    findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
                    findViewById(R.id.webView).setVisibility(View.GONE);
                }
                else {
                    findViewById(R.id.noInternet).setVisibility(View.GONE);
                    findViewById(R.id.webView).setVisibility(View.VISIBLE);
                    Snackbar.make(findViewById(R.id.linearView),"Undefined error. Please report this.", Snackbar.LENGTH_LONG).show();
                    Log.e("PDFViewer Error", result);
                }
            else {
                PDFView p = (PDFView) findViewById(R.id.webView);
                String sname = mPortal.studentFile.replace(".jpg", "");
                String name = report.title.replace(" ", "_");
                File f = new File(getCacheDir() + "/betterkamar/" + sname, name + ".pdf");
                if (f.length() != 0)
                    p.fromFile(f).load();
                else {
                    f.delete();
                    recreate();
                }
            }
        }


    }
}