package sheasmith.me.betterkamar.pages.addPortal;

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

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.PortalObject;

public class AddPortalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final EditText urlText = (EditText) findViewById(R.id.hostname);
        final EditText usernameText = (EditText) findViewById(R.id.username);
        final EditText passwordText = (EditText) findViewById(R.id.password);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                if (password.isEmpty()) {
                    passwordText.setError("Please enter a password");
                    return;
                }

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    urlText.setError("The address must either have http:// or https://");
                    return;
                }
                PortalObject portal = new PortalObject();
                portal.hostname = url;
                portal.username = username;
                portal.password = password;
                ApiManager.setVariables(portal, new ApiResponse<PortalObject>() {
                    @Override
                    public void success(PortalObject value) {
                        Intent result = new Intent();
                        result.putExtra("portal", value);
                        setResult(RESULT_OK, result);
                        finish();
                    }

                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                }, AddPortalActivity.this);
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
        setResult(RESULT_CANCELED);
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
        super.onBackPressed();
    }
}
