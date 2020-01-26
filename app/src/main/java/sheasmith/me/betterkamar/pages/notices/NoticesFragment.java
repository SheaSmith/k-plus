/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.notices;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;
import sheasmith.me.betterkamar.util.OnSwipeTouchListener;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;

public class NoticesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NoticesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private View mView;

    private ArrayList<String> groups;
    private HashSet<String> mDisabled;
    private HashSet<String> mTempEnabled;

    private Date lastDate;

    private HashMap<Date, NoticesObject> notices = new HashMap<>();
    private Tracker mTracker;
    private TextView mCurrentDate;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static NoticesFragment newInstance() {
        return new NoticesFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            requireActivity().setTheme(R.style.NoActionBarShadow);
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setElevation(0);
            requireActivity().setTitle("Notices");

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");

            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            if (mAdapter == null || mAdapter.getItemCount() == 0)
                doRequest(mPortal, new Date(System.currentTimeMillis()), false, contextThemeWrapper);

            KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName("Notices");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            FirebaseAnalytics.getInstance(requireActivity()).setCurrentScreen(requireActivity(), "Notices", null);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 1, 0, "Filter Notices").setIcon(R.drawable.ic_filter).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1)
            createFilterDiag(mPortal);

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            final PortalObject portal = (PortalObject) requireActivity().getIntent().getSerializableExtra("portal");
            mPortal = portal;
            ApiManager.setVariables(portal, requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isAdded()) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");

            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
            View view = localInflator.inflate(R.layout.fragment_notices, container, false);
            mRecyclerView = view.findViewById(R.id.notices);
            setHasOptionsMenu(true);

            mView = view;

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(false);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(requireContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerView.setOnTouchListener(new OnSwipeTouchListener(requireContext()) {
                @Override
                public void onSwipeRight() {
                    super.onSwipeRight();
                    Calendar c = Calendar.getInstance();
                    c.setTime(lastDate);
                    c.add(Calendar.DAY_OF_YEAR, -1);
                    Date newDate = c.getTime();
                    mLoader.setVisibility(View.VISIBLE);
                    doRequest(mPortal, newDate, false, contextThemeWrapper);
                }

                @Override
                public void onSwipeLeft() {
                    super.onSwipeLeft();
                    Calendar c = Calendar.getInstance();
                    c.setTime(lastDate);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    Date newDate = c.getTime();
                    mLoader.setVisibility(View.VISIBLE);
                    doRequest(mPortal, newDate, false, contextThemeWrapper);
                }
            });

            mLoader = view.findViewById(R.id.loader);
            mCurrentDate = view.findViewById(R.id.current_date);

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRequest(mPortal, lastDate, true, contextThemeWrapper);
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

            view.findViewById(R.id.previous).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(lastDate);
                    c.add(Calendar.DAY_OF_YEAR, -1);
                    Date newDate = c.getTime();
                    mLoader.setVisibility(View.VISIBLE);
                    doRequest(mPortal, newDate, false, contextThemeWrapper);
                }
            });

            view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(lastDate);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    Date newDate = c.getTime();
                    mLoader.setVisibility(View.VISIBLE);
                    doRequest(mPortal, newDate, false, contextThemeWrapper);
                }
            });

            SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
            mDisabled = (HashSet<String>) preferences.getStringSet(mPortal.studentFile.replace(".jpg", "_notices_disabled"), new HashSet<String>());

            ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

            if (lastDate != null) {
                NoticesObject value = notices.get(lastDate);

                if (value != null) {
                    mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, contextThemeWrapper, mDisabled);
                    mRecyclerView.setAdapter(mAdapter);
                    mLoader.setVisibility(View.GONE);
                }
            }
            return view;
        }
        return inflater.inflate(R.layout.fragment_notices, container, false);

    }

    private void doRequest(final PortalObject portal, final Date date, final boolean ignoreCache, final Context contextThemeWrapper) {
        lastDate = date;
        SimpleDateFormat titleFormat = new SimpleDateFormat("EEEE, d'[p]' MMMM yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String title = titleFormat.format(date).replace("[p]", getDayOfMonthSuffix(day));
        mCurrentDate.setText(title);
        if (!notices.containsKey(date) || ignoreCache)
            ApiManager.getNotices(new ApiResponse<NoticesObject>() {
                @Override
                public void success(final NoticesObject value) {
                    if (isAdded()) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, contextThemeWrapper, mDisabled);
                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mView.findViewById(R.id.noInternet).setVisibility(View.GONE);
                                mLoader.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mSwipeRefreshLayout.setRefreshing(false);
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

                }

                @Override
                public void error(Exception e) {
                    if (e instanceof Exceptions.ExpiredToken) {
                        ApiManager.login(portal.username, portal.password, new ApiResponse<LoginObject>() {
                            @Override
                            public void success(LoginObject value) {
                                doRequest(portal, date, ignoreCache, contextThemeWrapper);
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
                    } else if (e instanceof Exceptions.AccessDenied) {
                        if (isAdded()) {
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new AlertDialog.Builder(requireActivity())
                                            .setTitle("Access Denied")
                                            .setMessage("Your school has disabled access to this section. You may still be able to view it via the web portal.")
                                            .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    doRequest(portal, date, ignoreCache, contextThemeWrapper);
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // Since notices is the first section we aren't going to go back to portals activity. This isn't great, but we don't want to brick the rest of the app
                                                }
                                            })
                                            .create()
                                            .show();
                                }
                            });
                        }
                    }
                    e.printStackTrace();
                }
            }, date, ignoreCache);
        else {
            NoticesObject value = notices.get(date);

            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, contextThemeWrapper, mDisabled);
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


    private void createFilterDiag(final PortalObject portal) {
        if (isAdded()) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");
            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            final AlertDialog.Builder diag = new AlertDialog.Builder(contextThemeWrapper)
                    .setTitle("Include Notices From");

            if (groups == null) {
                diag.setMessage("The notices are still loading. Please wait");
            } else {
                if (isAdded()) {
                    final SharedPreferences preferences = requireActivity().getPreferences(MODE_PRIVATE);
                    HashSet<String> disabled = (HashSet<String>) preferences.getStringSet(portal.studentFile.replace(".jpg", "_notices_disabled"), new HashSet<>());

                    List<String> groupsToRender = new ArrayList<>();
                    List<Boolean> whetherToEnable = new ArrayList<>();

                    for (String group : groups) {
                        groupsToRender.add(group);
                        whetherToEnable.add(!disabled.contains(group));
                    }
                    mTempEnabled = disabled;
                    diag.setMultiChoiceItems(groupsToRender.toArray(new CharSequence[groupsToRender.size()]), toPrimitiveArray(whetherToEnable), new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                            if (!isChecked)
                                mTempEnabled.add(groupsToRender.get(i));
                            else
                                mTempEnabled.remove(groupsToRender.get(i));
                        }
                    });

                    diag.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (isAdded()) {
                                mDisabled = mTempEnabled;
                                preferences.edit().putStringSet(portal.studentFile.replace(".jpg", "_notices_disabled"), mDisabled).apply();
                                mAdapter = new NoticesAdapter(mAdapter.meetingNotices, mAdapter.generalNotices, contextThemeWrapper, mDisabled);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                }
            }

            diag.show();
        }
    }

    String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    private boolean[] toPrimitiveArray(final List<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean object : booleanList) {
            primitives[index++] = object;
        }
        return primitives;
    }
}
