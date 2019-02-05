/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.pages.groups;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.GroupObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.GroupsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.GroupsViewModel;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;

public class GroupFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setElevation(10);
        requireActivity().setTitle("Groups");

        KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Groups");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) requireActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, requireContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal, false, true);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

        LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
        mView = localInflator.inflate(R.layout.fragment_group, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.groups);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRequest(mPortal, true, true);
            }
        });

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        return mView;
    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache, final boolean useHtml) {
        if (useHtml) {
            ApiManager.getGroupsHtml(new ApiResponse<List<GroupsObject>>() {
                @Override
                public void success(final List<GroupsObject> value) {
                    if (requireActivity() == null)
                        return;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new GroupAdapter(GroupsViewModel.generate((ArrayList<GroupsObject>) value), requireContext());
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
                                doRequest(portal, ignoreCache, useHtml);
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
                                                    doRequest(portal, ignoreCache, useHtml);
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
                        }
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
                                                    doRequest(portal, ignoreCache, useHtml);
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
                        doRequest(portal, ignoreCache, false);
                    }
                }
            }, ignoreCache);
        } else {
            ApiManager.getGroupsApi(new ApiResponse<GroupObject>() {
                @Override
                public void success(final GroupObject value) {
                    if (requireActivity() == null)
                        return;
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new GroupAdapter(GroupsViewModel.generate(value.StudentGroupsResults.Years), requireContext());
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
                                doRequest(portal, ignoreCache, useHtml);
                            }

                            @Override
                            public void error(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    } else if (e instanceof IOException) {
                        if (requireActivity() != null) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(requireActivity())
                                            .setTitle("No Internet")
                                            .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    doRequest(portal, ignoreCache, useHtml);
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
                        }
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
                                                    doRequest(portal, ignoreCache, useHtml);
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
}
