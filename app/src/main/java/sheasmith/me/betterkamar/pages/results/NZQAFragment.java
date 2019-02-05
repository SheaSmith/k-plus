/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NZQAObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;

public class NZQAFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private NZQAAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static NZQAFragment newInstance() {
        return new NZQAFragment();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal, false);
            }
        }).start();

        KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("NZQA Results");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("NZQA Qualifications Information")
                    .setMessage("These qualifications and endorsements have been officially awarded by NZQA. They should properly reflect official records.")
                    .setPositiveButton("Close", null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

        LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
        mView = localInflator.inflate(R.layout.fragment_results_nzqa, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.results);

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

        return mView;
    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        ApiManager.getNZQAResults(new ApiResponse<NZQAObject>() {
            @Override
            public void success(final NZQAObject value) {
                if (requireActivity() == null)
                    return;
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView noResults = mView.findViewById(R.id.noResults);
                        if (value.StudentOfficialResultsResults.Types.size() == 3) {
                            mAdapter = new NZQAAdapter(value.StudentOfficialResultsResults.Types.get(0).Qualifications, value.StudentOfficialResultsResults.Types.get(1).Qualifications, value.StudentOfficialResultsResults.Types.get(2).Qualifications, requireContext());
                            mRecyclerView.setAdapter(mAdapter);
                            noResults.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            mRecyclerView.setVisibility(View.GONE);
                            noResults.setVisibility(View.VISIBLE);

                        }
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
                                            requireActivity().finish();
                                        }
                                    })
                                    .create()
                                    .show();
                        }
                    });
                } else if (e instanceof Exceptions.AccessDenied) {
                    if (requireActivity() != null) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(requireActivity())
                                        .setTitle("Access Denied")
                                        .setMessage("Your school has disabled access to this section. You may still be able to view it via the web portal.")
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
