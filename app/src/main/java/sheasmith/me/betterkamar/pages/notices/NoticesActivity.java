package sheasmith.me.betterkamar.pages.notices;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.portals.PortalAdapter;

public class NoticesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;

    private List<String> groups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        PortalObject portal = (PortalObject) getIntent().getSerializableExtra("portal");
        ApiManager.setVariables(portal);

        mRecyclerView = (RecyclerView) findViewById(R.id.notices);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLoader = findViewById(R.id.loader);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            doRequest(portal);
    }

    private void doRequest(final PortalObject portal) {
        ApiManager.getNotices(new ApiResponse<NoticesObject>() {
            @Override
            public void success(final NoticesObject value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, NoticesActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mLoader.setVisibility(View.GONE);
                    }
                });

                groups = new ArrayList<>();
                for (NoticesObject.General notice : value.NoticesResults.GeneralNotices.General) {
                    if (!groups.contains(notice.Level))
                        groups.add(notice.Level);
                }

                for (NoticesObject.Meeting notice : value.NoticesResults.MeetingNotices.Meeting) {
                    if (!groups.contains(notice.Level))
                        groups.add(notice.Level);
                }

            }

            @Override
            public void error(Exception e) {
                if (e instanceof Exceptions.ExpiredToken) {
                    ApiManager.login(portal.username, portal.password, new ApiResponse<LoginObject>() {
                        @Override
                        public void success(LoginObject value) {
                            doRequest(portal);
                        }

                        @Override
                        public void error(Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return;
                }
                e.printStackTrace();
            }
        }, new Date(System.currentTimeMillis()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notices, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:

        }
        return super.onOptionsItemSelected(item);
    }

    private void createFilterDiag() {
        AlertDialog.Builder diag = new AlertDialog.Builder(NoticesActivity.this)
                .setTitle("Include Notices From");

        if (groups == null) {
            diag.setMessage("The notices are still loading. Please wait");
        }
        else {
            diag.setMultiChoiceItems(, new boolean[] {true, false}, null)
        }
    }
}
