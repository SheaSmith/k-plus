package sheasmith.me.betterkamar.pages.editPortal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
import sheasmith.me.betterkamar.pages.addPortal.AddPortalActivity;

public class EditPortalActivity extends AppCompatActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_portal);

        KamarPlusApplication application = (KamarPlusApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Edit Portal");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText urlText = (EditText) findViewById(R.id.hostname);
        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        final int index = getIntent().getIntExtra("index", -1);
        final PortalObject portal = (PortalObject) getIntent().getSerializableExtra("portal");

        urlText.setText(portal.hostname);
        usernameText.setText(portal.username);
        passwordText.setHint("[unchanged]");

        findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditPortalActivity.this)
                        .setTitle("Confirm Deletion")
                        .setMessage("Are you sure you want to delete this portal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent result = new Intent();
                                result.putExtra("isDeleted", true);
                                result.putExtra("index", index);
                                setResult(RESULT_OK, result);
                                finish();
                            }
                        })
                        .setNegativeButton("No", null)
                        .create()
                        .show();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRequest(usernameText, passwordText, urlText, index, portal);
            }
        });
    }

    private void doRequest(final EditText usernameText, final EditText passwordText, final EditText urlText, final int index, final PortalObject portal) {
        String url = urlText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if (url.isEmpty()) {
            urlText.setError("Please enter a portal address");
            return;
        }

        if (username.isEmpty()) {
            usernameText.setError("Please enter a username");
            return;
        }


        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            urlText.setError("The address must either have http:// or https://");
            return;
        }
        portal.hostname = url;
        portal.username = username;
        if (!password.isEmpty())
            portal.password = password;

        final ProgressDialog dialog = new ProgressDialog(EditPortalActivity.this);
        dialog.setTitle("Updating Portal");
        dialog.setMessage("Validating portal...");
        dialog.show();

        ApiManager.setVariables(portal, new ApiResponse<PortalObject>() {
            @Override
            public void success(PortalObject value) {
                Intent result = new Intent();
                result.putExtra("portal", value);
                result.putExtra("index", index);
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
                } else if (e instanceof NullPointerException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            urlText.setError("Incorrect prefix. Try using http or https instead.");
                        }
                    });
                } else if (e instanceof Exceptions.InvalidServer || e instanceof UnknownHostException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            urlText.setError("This website is not a valid KAMAR portal");
                        }
                    });
                } else if (e instanceof IOException) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(EditPortalActivity.this)
                                    .setTitle("No Internet")
                                    .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                    .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            doRequest(usernameText, passwordText, urlText, index, portal);
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
            }, EditPortalActivity.this);
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
}
