/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:50 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.util.List;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;

public class ReportFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private ReportAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) requireActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, requireContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest(mPortal, false);
                }
            }).start();

        KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Reports");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

        LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
        mView = localInflator.inflate(R.layout.fragment_results_report, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.reports);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRequest(mPortal, true);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal, false);
            }
        }).start();
        return mView;
    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        ApiManager.getReports(new ApiResponse<List<ReportsObject>>() {
            @Override
            public void success(final List<ReportsObject> value) {
                if (requireActivity() == null)
                    return;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new ReportAdapter(value, requireContext(), mPortal);
                        mRecyclerView.setAdapter(mAdapter);
                        mLoader.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
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
                            doRequest(portal, ignoreCache);
                        }

                        @Override
                        public void error(Exception e) {
                            e.printStackTrace();
                        }
                    });
                    return;
                } else if (e instanceof IOException) {
                    if (requireActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("No Internet")
                                        .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                doRequest(portal, ignoreCache);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (requireActivity() != null)
                                                    requireActivity().finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        });
                    }
                } else {
                    if (requireActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(requireContext())
                                        .setTitle("Reports not supported")
                                        .setMessage("Your school's portal does not seem to support reports. You will have to view them via the web portal.")
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                doRequest(portal, ignoreCache);
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                if (requireActivity() != null)
                                                    requireActivity().finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        });
                    }
                }

            }
        }, ignoreCache);
    }
}
