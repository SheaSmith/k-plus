package sheasmith.me.betterkamar;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddServer extends  AppCompatActivity {
    ProgressDialog progressDialog;
    private String hostname;
    private String username;
    private String password;
    private boolean keep;
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
                EditText serverNameForm = (EditText) findViewById(R.id.serverAddressForm);
                hostname = serverNameForm.getText().toString().trim();
                boolean http = hostname.startsWith("http://");
                boolean https = hostname.startsWith("https://");

                if (http || https) {

                }
                else { prevent = true;
                    Toast.makeText(AddServer.this, "Please ensure the URL contains http:// or https://", Toast.LENGTH_LONG).show(); }
                EditText usernameForm = (EditText) findViewById(R.id.usernameForm);
                username = usernameForm.getText().toString().trim();
                EditText passwordForm = (EditText) findViewById(R.id.passwordForm);
                password = passwordForm.getText().toString().trim();
                CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
                keep = checkBox.isEnabled();
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
            progressDialog.setTitle("Connecting to server");
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document d = Jsoup.connect(hostname + "/student/index.php/process-login").data("username", username, "password", password).post();
                Elements span = d.select("div[class=success]");
                //Elements auth = d.select("span[class=auth]");
                if (span.size() != 0) {
                    successful = true;
                    Elements title = d.select("div[id=header] > h1");
                    this.title = title.text();
                    Elements student = d.select("span[id=auth] > p > strong > a");
                    name = student.text();

                }
                else {
                    successful = false;
                    Elements error = d.select("div[class=error]");
                    if (error.size() != 0) {
                        this.error = error.text();
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
                    server.keep = keep;
                    server.title = title;
                    server.student = name;
                    if (keep) {
                        server.username = username;
                        server.password = password;


                    }




                    Servers.getServersList().add(server);
                    Set<String> jsonList = new HashSet<>();
                    SharedPreferences prefs = getSharedPreferences(
                            "sheasmith.me.betterkamar", Context.MODE_PRIVATE);
                    for (ServersObject s : Servers.getServersList()) {

                        Gson gson = new Gson();
                        String json = gson.toJson(s);

                        jsonList.add(json);

                    }
                    prefs.edit().putStringSet("sheasmith.me.betterkamar.servers", jsonList).apply();
                    finish();

                }
                else {
                    Toast.makeText(AddServer.this, "This server already exists!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if (error != null) {
                    if (error.equalsIgnoreCase("Please enter a username & password") && !username.isEmpty() && !password.isEmpty()) {
                        Toast.makeText(AddServer.this, "Please ensure the http prefix is correct (it may need changed to https)", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(AddServer.this, error, Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(AddServer.this, "Invalid URL", Toast.LENGTH_LONG).show();
                }
            }

        }

    }


}
