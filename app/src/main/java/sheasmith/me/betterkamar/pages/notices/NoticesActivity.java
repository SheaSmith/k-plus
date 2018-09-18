package sheasmith.me.betterkamar.pages.notices;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.BottomNavigationViewHelper;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.timetable.TimetableActivity;

public class NoticesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private NoticesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;
    private PortalObject mPortal;

    private List<String> groups;
    private Set<String> mEnabled;
    private Set<String> mTempEnabled;

    private HashMap<Date, NoticesObject> notices = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notices);

        final PortalObject portal = (PortalObject) getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal);

        mRecyclerView = findViewById(R.id.notices);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLoader = findViewById(R.id.loader);

        BottomNavigationView nav = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.removeShiftMode(nav);
        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//            case R.id.action_filter:
//                createFilterDiag(mPortal);
//                break;
                    case R.id.nav_calender:
                        Intent i = new Intent(NoticesActivity.this, TimetableActivity.class);
                        i.putExtra("portal", portal);
                        startActivity(i);
                        break;
                }

                return false;
            }
        });

        SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
        mEnabled = preferences.getStringSet(portal.schoolFile.replace(".jpg", ""), new HashSet<String>());

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            doRequest(portal, new Date(System.currentTimeMillis()));
    }

    private void doRequest(final PortalObject portal, final Date date) {

        if (!notices.containsKey(date))
            ApiManager.getNotices(new ApiResponse<NoticesObject>() {
                @Override
                public void success(final NoticesObject value) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, NoticesActivity.this, mEnabled);
                            mRecyclerView.setAdapter(mAdapter);
                            mLoader.setVisibility(View.GONE);
                        }
                    });

                    notices.put(date, value);

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
                                doRequest(portal, date);
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
            }, date);
        else {
            NoticesObject value = notices.get(date);

            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, NoticesActivity.this, mEnabled);
            mRecyclerView.setAdapter(mAdapter);
            mLoader.setVisibility(View.GONE);

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
//            case R.id.action_filter:
//                createFilterDiag(mPortal);
//                break;
            case R.id.nav_calender:
                startActivity(new Intent(NoticesActivity.this, TimetableActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createFilterDiag(PortalObject portal) {
        final AlertDialog.Builder diag = new AlertDialog.Builder(NoticesActivity.this)
                .setTitle("Include Notices From");

        if (groups == null) {
            diag.setMessage("The notices are still loading. Please wait");
        }
        else {
            SharedPreferences preferences = this.getPreferences(MODE_PRIVATE);
            Set<String> enabled = preferences.getStringSet(portal.schoolFile.replace(".jpg", ""), new HashSet<String>());
            boolean[] enabledBoolean = new boolean[groups.size()];
            for (String group : groups) {
                enabledBoolean[groups.indexOf(group)] = enabled.contains(group) || enabled.size() == 0;
            }
            mTempEnabled = new HashSet<>();
            diag.setMultiChoiceItems(groups.toArray(new CharSequence[groups.size()]), enabledBoolean, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    mTempEnabled.add(groups.get(i));
                }
            });

            diag.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mEnabled = mTempEnabled;
                    mAdapter = new NoticesAdapter(mAdapter.meetingNotices, mAdapter.generalNotices, NoticesActivity.this, mEnabled);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        diag.show();
    }
}
