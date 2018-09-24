package sheasmith.me.betterkamar.pages.groups;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.List;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.GroupObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.GroupsViewModel;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.pages.results.ReportAdapter;

public class GroupFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private GroupAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static GroupFragment newInstance() {
        return new GroupFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(10);
        getActivity().setTitle("Groups");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, getContext());
        new Thread(new Runnable() {
            @Override
            public void run() {
                doRequest(mPortal);
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_group, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.groups);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);


        return mView;
    }

    private void doRequest(final PortalObject portal) {
        ApiManager.getGroupsApi(new ApiResponse<GroupObject>() {
            @Override
            public void success(final GroupObject value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new GroupAdapter(GroupsViewModel.generate(value.StudentGroupsResults.Years), getContext());
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
        });
    }
}
