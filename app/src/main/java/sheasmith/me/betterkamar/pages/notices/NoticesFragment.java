package sheasmith.me.betterkamar.pages.notices;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.editPortal.EditPortalActivity;

import static android.content.Context.MODE_PRIVATE;

public class NoticesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NoticesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;
    private PortalObject mPortal;

    private ArrayList<String> groups;
    private HashSet<String> mEnabled;
    private HashSet<String> mTempEnabled;

    private Date lastDate;

    private HashMap<Date, NoticesObject> notices = new HashMap<>();

    public static NoticesFragment newInstance() {
        return new NoticesFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("notices", notices);
        outState.putSerializable("groups", groups);
        outState.putSerializable("enabled", mEnabled);
        outState.putSerializable("lastDate", lastDate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(10);
        getActivity().setTitle("Notices");

        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            doRequest(mPortal, new Date(System.currentTimeMillis()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, getContext());

        if (savedInstanceState != null) {
            notices = (HashMap<Date, NoticesObject>) savedInstanceState.getSerializable("notices");
            groups = (ArrayList<String>) savedInstanceState.getSerializable("groups");
            mEnabled = (HashSet<String>) savedInstanceState.getSerializable("enabled");
            lastDate = (Date) savedInstanceState.getSerializable("lastDate");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notices, container, false);
        mRecyclerView = view.findViewById(R.id.notices);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mLoader = view.findViewById(R.id.loader);

        SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
        mEnabled = (HashSet<String>) preferences.getStringSet(mPortal.schoolFile.replace(".jpg", ""), new HashSet<String>());

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        if (lastDate != null) {
            NoticesObject value = notices.get(lastDate);

            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, getContext(), mEnabled);
            mRecyclerView.setAdapter(mAdapter);
            mLoader.setVisibility(View.GONE);
        }

        return view;
    }

    private void doRequest(final PortalObject portal, final Date date) {
        lastDate = date;
        if (!notices.containsKey(date))
            ApiManager.getNotices(new ApiResponse<NoticesObject>() {
                @Override
                public void success(final NoticesObject value) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, getContext(), mEnabled);
                            mRecyclerView.setAdapter(mAdapter);
                            mLoader.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
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

                    else if (e instanceof IOException) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("No Internet")
                                        .setMessage("You do not appear to be connected to the internet. Please check your connection and try again.")
                                        .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                doRequest(portal, date);
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
                    e.printStackTrace();
                }
            }, date);
        else {
            NoticesObject value = notices.get(date);

            mAdapter = new NoticesAdapter(value.NoticesResults.MeetingNotices.Meeting, value.NoticesResults.GeneralNotices.General, getContext(), mEnabled);
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



    private void createFilterDiag(PortalObject portal) {
        final AlertDialog.Builder diag = new AlertDialog.Builder(getContext())
                .setTitle("Include Notices From");

        if (groups == null) {
            diag.setMessage("The notices are still loading. Please wait");
        }
        else {
            SharedPreferences preferences = getActivity().getPreferences(MODE_PRIVATE);
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
                    mAdapter = new NoticesAdapter(mAdapter.meetingNotices, mAdapter.generalNotices, getContext(), mEnabled);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }

        diag.show();
    }
}
