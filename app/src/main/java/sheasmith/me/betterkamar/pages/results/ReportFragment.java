package sheasmith.me.betterkamar.pages.results;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.util.List;

import sheasmith.me.betterkamar.util.ApiManager;
import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

public class ReportFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Tracker mTracker;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest(mPortal);
                }
            }).start();

        KamarPlusApplication application = (KamarPlusApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Reports");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_results_report, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.reports);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal);
            }
        }).start();
        return mView;
    }

    private void doRequest(final PortalObject portal) {
        ApiManager.getReports(new ApiResponse<List<ReportsObject>>() {
            @Override
            public void success(final List<ReportsObject> value) {
                if (getActivity() == null)
                    return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ReportAdapter(value, getContext(), mPortal);
                        mRecyclerView.setAdapter(mAdapter);
                        mLoader.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();

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
                } else if (e instanceof IOException) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("No Internet")
                                        .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                doRequest(portal);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                getActivity().finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        });
                    }
                }
            }
        });
    }
}
