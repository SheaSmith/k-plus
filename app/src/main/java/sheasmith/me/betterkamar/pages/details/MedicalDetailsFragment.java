/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.DetailsObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;

import static android.view.View.GONE;

public class MedicalDetailsFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static MedicalDetailsFragment newInstance() {
        return new MedicalDetailsFragment();
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

        if (isAdded()) {
            KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
            mTracker = application.getDefaultTracker();
            mTracker.setScreenName("Medical Details");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            FirebaseAnalytics.getInstance(requireActivity()).setCurrentScreen(requireActivity(), "Medical Details", null);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (isAdded()) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");
            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
            mView = localInflator.inflate(R.layout.fragment_details_medical, container, false);
            mLoader = mView.findViewById(R.id.loader);

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
        }

        return mView;
    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        ApiManager.getDetails(new ApiResponse<DetailsObject>() {
            @Override
            public void success(final DetailsObject value) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DetailsObject.Student student = value.StudentDetailsResults.Student;

                            setText(student.AllowedPanadol, R.id.general_information_allowed_panadol, R.id.general_information_allowed_panadol_heading);
                            setText(student.AllowedIbuprofen, R.id.general_information_allowed_ibuprofen, R.id.general_information_allowed_ibuprofen_heading);
                            setText(student.Medical, R.id.general_information_medical_conditions, R.id.general_information_medical_conditions_heading);
                            setText(student.Reactions, R.id.general_information_reactions, R.id.general_information_reactions_heading);
                            setText(student.Vaccinations, R.id.general_information_vaccinations, R.id.general_information_vaccinations_heading);
                            setText(student.SpecialCircumstances, R.id.general_information_special_circumstances, R.id.general_information_special_circumstances_heading);
                            setText(student.GeneralNotes, R.id.general_information_general_notes, R.id.general_information_general_notes_heading);
                            setText(student.HealthNotes, R.id.general_information_health_notes, R.id.general_information_health_notes_heading);
                            maybeHideView(R.id.general_information, student.AllowedPanadol, student.AllowedIbuprofen, student.Medical, student.MotherEmail, student.Reactions, student.Vaccinations, student.SpecialCircumstances, student.GeneralNotes, student.HealthNotes);

                            setText(student.DoctorName, R.id.doctor_name, R.id.doctor_name_heading);
                            setText(student.DoctorPhone, R.id.doctor_phone, R.id.doctor_phone_heading);
                            setText(student.DoctorAddress, R.id.doctor_address, R.id.doctor_address_heading);
                            maybeHideView(R.id.doctor, student.DoctorName, student.DoctorPhone, student.DoctorAddress);

                            setText(student.DentistName, R.id.dentist_name, R.id.dentist_name_heading);
                            setText(student.DentistPhone, R.id.dentist_phone, R.id.dentist_phone_heading);
                            setText(student.DentistAddress, R.id.dentist_address, R.id.dentist_address_heading);
                            maybeHideView(R.id.dentist, student.DentistName, student.DentistPhone, student.DentistAddress);

                            mView.findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            mView.findViewById(R.id.noInternet).setVisibility(View.GONE);
                            mLoader.setVisibility(GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
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
                                mView.findViewById(R.id.noInternet).setVisibility(View.VISIBLE);
                                mView.findViewById(R.id.scrollView).setVisibility(View.GONE);
                                mSwipeRefreshLayout.setRefreshing(false);
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

    private void setText(String text, int id) {
        TextView view = mView.findViewById(id);
        if (text.equals(""))
            view.setVisibility(GONE);
        else
            view.setText(text);
    }

    private void setText(String text, int id, int header) {
        TextView view = mView.findViewById(id);
        if (text.equals("")) {
            view.setVisibility(GONE);
            mView.findViewById(header).setVisibility(GONE);
        } else
            view.setText(text);
    }

    private void maybeHideView(int id, String... items) {
        for (String s : items) {
            if (s != null && !s.equals("") && !s.equals(" "))
                return;
        }
        mView.findViewById(id).setVisibility(GONE);
    }
}
