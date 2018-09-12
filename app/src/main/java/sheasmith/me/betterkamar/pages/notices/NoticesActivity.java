package sheasmith.me.betterkamar.pages.notices;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;

import java.util.Date;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.portals.PortalAdapter;

public class NoticesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        ApiManager.getNotices(new ApiResponse<NoticesObject>() {
            @Override
            public void success(final NoticesObject value) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, NoticesActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });

            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        }, new Date(System.currentTimeMillis()));
    }
}
