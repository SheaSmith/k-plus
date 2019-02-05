/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.pages.details;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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

public class EmergencyDetailsFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static EmergencyDetailsFragment newInstance() {
        return new EmergencyDetailsFragment();
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
        mTracker.setScreenName("Emergency Details");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
        String stringColor = sharedPreferences.getString("color", "E65100");

        final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

        LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
        mView = localInflator.inflate(R.layout.fragment_details_emergency, container, false);
        mLoader = mView.findViewById(R.id.loader);

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
       ApiManager.getDetails(new ApiResponse<DetailsObject>() {
           @Override
           public void success(final DetailsObject value) {
               if (requireActivity() == null) {
                   // TODO: Error?
                   return;
               }
               requireActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       DetailsObject.Student student = value.StudentDetailsResults.Student;

                       setText(student.EmergencyName, R.id.emergency_heading);
                       setText(student.EmergencyPhoneHome, R.id.emergency_home_phone, R.id.emergency_home_phone_heading);
                       setText(student.EmergencyPhoneCell, R.id.emergency_mobile_phone, R.id.emergency_mobile_phone_heading);
                       setText(student.EmergencyPhoneWork, R.id.emergency_work_phone, R.id.emergency_work_phone_heading);
                       setText(student.EmergencyPhoneExtn, R.id.emergency_extension, R.id.emergency_extension_heading);
                       maybeHideView(R.id.emergency, student.EmergencyName, student.EmergencyPhoneHome, student.EmergencyPhoneCell, student.EmergencyPhoneWork, student.EmergencyPhoneExtn);

                       mLoader.setVisibility(GONE);
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
        }
        else
            view.setText(text);
    }

    private void maybeHideView(int id, String... items) {
        for (String s : items) {
            if (!s.equals(""))
                return;
        }
        mView.findViewById(id).setVisibility(GONE);
        TextView none = new TextView(requireContext());
        none.setText("No emergency contact has been associated. Contact your school for more information.");
        ((LinearLayout) mView).addView(none);
    }
}
