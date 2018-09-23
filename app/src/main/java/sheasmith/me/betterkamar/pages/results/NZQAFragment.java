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
import android.widget.TextView;

import java.io.IOException;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NZQAObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;

public class NZQAFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private RecyclerView mRecyclerView;
    private NZQAAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static NZQAFragment newInstance() {
        return new NZQAFragment();
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
        mView = inflater.inflate(R.layout.fragment_results_nzqa, container, false);
        mLoader = mView.findViewById(R.id.loader);

        mRecyclerView = mView.findViewById(R.id.results);

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
        ApiManager.getNZQAResults(new ApiResponse<NZQAObject>() {
            @Override
            public void success(final NZQAObject value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView noResults = mView.findViewById(R.id.noResults);
                        if (value.StudentOfficialResultsResults.Types.size() == 3) {
                            mAdapter = new NZQAAdapter(value.StudentOfficialResultsResults.Types.get(0).Qualifications, value.StudentOfficialResultsResults.Types.get(1).Qualifications, value.StudentOfficialResultsResults.Types.get(2).Qualifications, getContext());
                            mRecyclerView.setAdapter(mAdapter);
                            noResults.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        else {
                            mRecyclerView.setVisibility(View.GONE);
                            noResults.setVisibility(View.VISIBLE);

                        }
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
