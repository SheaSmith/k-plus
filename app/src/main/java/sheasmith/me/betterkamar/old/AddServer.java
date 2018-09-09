package sheasmith.me.betterkamar.old;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sheasmith.me.betterkamar.R;

public class AddServer extends  AppCompatActivity {
    ProgressDialog progressDialog;
    private String hostname;
    private String username;
    private String password;
    private Context context;

    ServersObject server = new ServersObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setTitle("Add a Server");
        setContentView(R.layout.activity_add_server);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean prevent = false;
                EditText serverNameForm = (EditText) findViewById(R.id.serverNameForm);
                hostname = serverNameForm.getText().toString().trim();
                boolean http = hostname.startsWith("http://");
                boolean https = hostname.startsWith("https://");

                if (http || https) {

                }
                else { prevent = true;
                    Snackbar.make(findViewById(R.id.scrollView), "Please ensure the URL contains http:// or https://", Snackbar.LENGTH_LONG).show(); }
                EditText usernameForm = (EditText) findViewById(R.id.usernameForm);
                username = usernameForm.getText().toString().trim();
                EditText passwordForm = (EditText) findViewById(R.id.passwordForm);
                password = passwordForm.getText().toString().trim();
                if (!prevent) {
                    new Connect().execute();
                }


            }
        });
    }

    private class Connect extends AsyncTask<Void, Void, Void> {

        boolean successful;
        String title;
        String name;
        String error;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Verifying Credentials");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Connection.Response r = Jsoup.connect(hostname + "/index.php/login").method(Connection.Method.POST).data("username", username, "password", password).execute();
                Document d = Jsoup.connect(hostname + "/index.php/login").data("username", username, "password", password).post();
                Elements error = d.getElementsByClass("alert-danger");
                if (error.size() != 0) {
                    successful = false;
                    this.error = error.text();
                }
                else {
                    if (d.html().equals("<html>\n" +
                            " <head></head>\n" +
                            " <body></body>\n" +
                            "</html>")) {
                        Map<String, String> cookies = r.cookies();
                        Document notices = Jsoup.connect(hostname + "/index.php/notices").cookies(cookies).get();
                        Element title = notices.getElementsByClass("jumbotron").first().getElementsByClass("col-sm-7").first().child(0);
                        successful = true;
                        this.title = title.text();
                        Elements student = notices.select("div[id=auth] > h4");
                        name = student.text();
                    }
                    else {
                        // not a valid KAMAR install
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
                successful = false;
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            if (successful) {
                if (!Servers.getTitleList().contains(title)) {

                    server.hostname = hostname;
                    server.title = title;
                    server.student = name;
                        server.username = username;
                        server.password = password;

                    Servers.getServersList().add(server);
                    Set<String> jsonList = new HashSet<>();
                    SharedPreferences prefs = new SecurePreferences(context);

                    for (ServersObject s : Servers.getServersList()) {

                        Gson gson = new Gson();
                        String json = gson.toJson(s);

                        jsonList.add(json);

                    }
                    prefs.edit().putStringSet("sheasmith.me.betterkamar.servers", jsonList).apply();
                    finish();

                }
                else {
                    new AlertDialog.Builder(context)
                            .setTitle("Duplicate Portal")
                            .setMessage("A portal by that name already exists! Do you want to create another?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    server.hostname = hostname;
                                    server.title = title;
                                    server.student = name;
                                        server.username = username;
                                        server.password = password;

                                    Servers.getServersList().add(server);
                                    Set<String> jsonList = new HashSet<>();
                                    SharedPreferences prefs = new SecurePreferences(context);

                                    for (ServersObject s : Servers.getServersList()) {

                                        Gson gson = new Gson();
                                        String json = gson.toJson(s);

                                        jsonList.add(json);

                                    }
                                    prefs.edit().putStringSet("sheasmith.me.betterkamar.servers", jsonList).apply();
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.ic_warning_black_24dp)
                            .show();
                }
            }
            else {
                if (error != null) {
                    if (error.equalsIgnoreCase("Please enter a username & password") && !username.isEmpty() && !password.isEmpty()) {
                        Snackbar.make(findViewById(R.id.scrollView), "Please ensure the http prefix is correct (it may need changed to https)", Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        Snackbar.make(findViewById(R.id.scrollView), error, Snackbar.LENGTH_LONG).show();
                    }

                }
                else {
                    Snackbar.make(findViewById(R.id.scrollView), "Invalid URL", Snackbar.LENGTH_LONG).show();
                }
            }

        }

    }


}
