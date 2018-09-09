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
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.securepreferences.SecurePreferences;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sheasmith.me.betterkamar.R;

public class EditServer extends AppCompatActivity {
    EditText name;
    EditText address;
    EditText username;
    CheckBox checkbox;

    ServersObject s;

    ProgressDialog progressDialog;
    private String hostname;
    private String title;
    private String uname;
    private String password;
    private Context context;

    public static String res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_edit_server);

        s = Servers.getServersList().get(getIntent().getIntExtra("listID", -1));
        name = (EditText) findViewById(R.id.serverNameForm);
        name.setText(s.title);
        address = (EditText) findViewById(R.id.serverAddressForm);
        address.setText(s.hostname);
        username = (EditText) findViewById(R.id.usernameForm);
        username.setText(s.username);
        Button cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        Button done = (Button) findViewById(R.id.done);

        setTitle("Edit Portal: " + s.title);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean prevent = false;
                EditText serverNameForm = (EditText) findViewById(R.id.serverNameForm);
                title = serverNameForm.getText().toString().trim();
                EditText serverHostForm = (EditText) findViewById(R.id.serverAddressForm);
                hostname = serverHostForm.getText().toString().trim();
                boolean http = hostname.startsWith("http://");
                boolean https = hostname.startsWith("https://");

                if (http || https) {

                }
                else { prevent = true;
                    Snackbar.make(findViewById(R.id.scrollView), "Please ensure the URL contains http:// or https://", Snackbar.LENGTH_LONG).show(); }
                EditText usernameForm = (EditText) findViewById(R.id.usernameForm);
                uname = usernameForm.getText().toString().trim();
                EditText passwordForm = (EditText) findViewById(R.id.passwordForm);
                if (!passwordForm.getText().toString().trim().isEmpty()) {
                    password = passwordForm.getText().toString().trim();
                }
                else {
                    password = s.password;
                }
                if (!prevent) {
                    new Connect().execute();
                }


            }
        });
        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this portal?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Servers.getServersList().remove(getIntent().getIntExtra("listID", -1));
                                SharedPreferences prefs = new SecurePreferences(context);

                                List<String> jsonList = new ArrayList<>();

                                for (ServersObject s : Servers.getServersList()) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(s);
                                    jsonList.add(json);

                                }
                                Set<String> json = new HashSet<>();
                                for (String s : jsonList) {
                                    json.add(s);
                                }
                                prefs.edit().putStringSet("sheasmith.me.betterkamar.servers", json).apply();
                                res = "deletion";
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
        });
    }

    private class Connect extends AsyncTask<Void, Void, Void> {

        boolean successful;
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
                Connection.Response r = Jsoup.connect(hostname + "/index.php/login").method(Connection.Method.POST).data("username", uname, "password", password).execute();
                Document d = Jsoup.connect(hostname + "/index.php/login").data("username", uname, "password", password).post();
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


                s.hostname = hostname;
                s.title = title;
                s.student = name;
                s.username = uname;
                s.password = password;

                Servers.getServersList().set(getIntent().getIntExtra("listID", -1), s);

                SharedPreferences prefs = new SecurePreferences(context);

                Set<String> jlist = prefs.getStringSet("sheasmith.me.betterkamar.servers", new HashSet<String>());
                List<String> jsonList = new ArrayList<>();
                for (String s : jlist) {
                    jsonList.add(s);
                }


                for (ServersObject s : Servers.getServersList()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(s);
                    jsonList.set(getIntent().getIntExtra("listID", -1), json);

                }
                Set<String> json = new HashSet<>();
                for (String s : jsonList) {
                    json.add(s);
                }
                prefs.edit().putStringSet("sheasmith.me.betterkamar.servers", json).apply();
                res = "update";
                finish();


            }
            else {
                if (error != null) {
                    if (error.equalsIgnoreCase("Please enter a username & password") && !uname.isEmpty() && !password.isEmpty()) {
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
