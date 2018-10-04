package sheasmith.me.betterkamar.pages.addPortal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.net.UnknownHostException;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

public class AddPortalActivity extends AppCompatActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portal);

        KamarPlusApplication application = (KamarPlusApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Add Portal");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText urlText = (EditText) findViewById(R.id.hostname);
        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest(usernameText, passwordText, urlText);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Unsaved Changes")
                .setMessage("Are you sure you want to delete unsaved changes?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).create().show();
    }

    private void doRequest(final EditText usernameText, final EditText passwordText, final EditText hostnameText) {
        String url = hostnameText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (url.isEmpty()) {
            hostnameText.setError("Please enter a portal address");
            return;
        }

        if (username.isEmpty()) {
            usernameText.setError("Please enter a username");
            return;
        }

        if (password.isEmpty()) {
            passwordText.setError("Please enter a password");
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            hostnameText.setError("The address must either have http:// or https://");
            return;
        }
        PortalObject portal = new PortalObject();
        portal.hostname = url;
        portal.username = username;
        portal.password = password;

        final ProgressDialog dialog = new ProgressDialog(AddPortalActivity.this);
        dialog.setTitle("Adding Portal");
        dialog.setMessage("Validating portal...");
        dialog.show();

        ApiManager.setVariables(portal, new ApiResponse<PortalObject>() {
            @Override
            public void success(PortalObject value) {
                Intent result = new Intent();
                result.putExtra("portal", value);
                setResult(RESULT_OK, result);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                });
                finish();
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                });

                if (e instanceof Exceptions.InvalidUsernamePassword) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usernameText.setError("Either the username or password is incorrect. Please check and try again");
                            passwordText.setError("Either the username or password is incorrect. Please check and try again");
                        }
                    });
                }

                else if (e instanceof NullPointerException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostnameText.setError("Incorrect prefix. Try using http or https instead.");
                        }
                    });
                }

                else if (e instanceof Exceptions.InvalidServer || e instanceof UnknownHostException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hostnameText.setError("This website is not a valid KAMAR portal");
                        }
                    });
                }
                else if (e instanceof IOException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(AddPortalActivity.this)
                                    .setTitle("No Internet")
                                    .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            doRequest(usernameText, passwordText, hostnameText);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });


                }
            }
        }, AddPortalActivity.this);
    }
}
