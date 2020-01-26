/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

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

import static android.view.View.GONE;

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
        if (mAdapter == null || mAdapter.getItemCount() == 0)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest(mPortal, false);
                }
            }).start();

        if (isAdded()) {
            KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName("Reports");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            FirebaseAnalytics.getInstance(requireActivity()).setCurrentScreen(requireActivity(), "Reports", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isAdded()) {
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
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRequest(mPortal, true);
                }
            };
            mSwipeRefreshLayout.setOnRefreshListener(listener);

            Button noInternetRetry = mView.findViewById(R.id.no_internet_retry);
            noInternetRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSwipeRefreshLayout.setRefreshing(true);
                    listener.onRefresh();
                    mView.findViewById(R.id.noInternet).setVisibility(GONE);
                }
            });

            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest(mPortal, false);
                }
            }).start();
        }
        return mView;
    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        ApiManager.getReports(new ApiResponse<List<ReportsObject>>() {
            @Override
            public void success(final List<ReportsObject> value) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isAdded()) {
                                mAdapter = new ReportAdapter(value, requireContext(), mPortal);
                                mRecyclerView.setAdapter(mAdapter);
                                mLoader.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mView.findViewById(R.id.noInternet).setVisibility(View.GONE);
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }
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
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                                mView.findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);
                            }
                        });
                    }
                } else {
                    if (isAdded()) {
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
                                                if (isAdded())
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
