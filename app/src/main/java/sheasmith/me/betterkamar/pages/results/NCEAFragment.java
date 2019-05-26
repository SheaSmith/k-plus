/*
 * Created by Shea Smith on 26/05/19 9:35 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 26/05/19 9:35 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.NCEAObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;

public class NCEAFragment extends Fragment {

    private View mView;
    private ProgressBar mLoader;
    private PortalObject mPortal;
    private Tracker mTracker;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static NCEAFragment newInstance() {
        return new NCEAFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final PortalObject portal = (PortalObject) requireActivity().getIntent().getSerializableExtra("portal");
        mPortal = portal;
        ApiManager.setVariables(portal, requireContext());
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 1, 0, "NCEA Information").setIcon(R.drawable.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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
            mTracker.setScreenName("NCEA Results");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            FirebaseAnalytics.getInstance(requireActivity()).setCurrentScreen(requireActivity(), "NCEA Results", null);
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
            mView = localInflator.inflate(R.layout.fragment_results_ncea, container, false);
            mLoader = mView.findViewById(R.id.loader);

            mSwipeRefreshLayout = (SwipeRefreshLayout) mView.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRequest(mPortal, true);
                }
            });
        }
        return mView;

    }

    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        ApiManager.getNCEADetails(new ApiResponse<NCEAObject>() {
            @Override
            public void success(final NCEAObject value) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NCEAObject.Student student = value.StudentNCEASummaryResults.Student;
                            ((TextView) mView.findViewById(R.id.total_notachieved)).setText(student.CreditsTotal.NotAchieved);
                            ((TextView) mView.findViewById(R.id.total_achieved)).setText(student.CreditsTotal.Achieved);
                            ((TextView) mView.findViewById(R.id.total_merit)).setText(student.CreditsTotal.Merit);
                            ((TextView) mView.findViewById(R.id.total_excellence)).setText(student.CreditsTotal.Excellence);
                            ((TextView) mView.findViewById(R.id.total_credits)).setText(student.CreditsTotal.Total);
                            ((TextView) mView.findViewById(R.id.total_attempted)).setText(student.CreditsTotal.Attempted);

                            ((TextView) mView.findViewById(R.id.internal_notachieved)).setText(student.CreditsInternal.NotAchieved);
                            ((TextView) mView.findViewById(R.id.internal_achieved)).setText(student.CreditsInternal.Achieved);
                            ((TextView) mView.findViewById(R.id.internal_merit)).setText(student.CreditsInternal.Merit);
                            ((TextView) mView.findViewById(R.id.internal_excellence)).setText(student.CreditsInternal.Excellence);
                            ((TextView) mView.findViewById(R.id.internal_credits)).setText(student.CreditsInternal.Total);
                            ((TextView) mView.findViewById(R.id.internal_attempted)).setText(student.CreditsInternal.Attempted);

                            ((TextView) mView.findViewById(R.id.external_notachieved)).setText(student.CreditsExternal.NotAchieved);
                            ((TextView) mView.findViewById(R.id.external_achieved)).setText(student.CreditsExternal.Achieved);
                            ((TextView) mView.findViewById(R.id.external_merit)).setText(student.CreditsExternal.Merit);
                            ((TextView) mView.findViewById(R.id.external_excellence)).setText(student.CreditsExternal.Excellence);
                            ((TextView) mView.findViewById(R.id.external_credits)).setText(student.CreditsExternal.Total);
                            ((TextView) mView.findViewById(R.id.external_attempted)).setText(student.CreditsExternal.Attempted);

                            NCEAObject.YearTotal latest = student.YearTotals.get(0);
                            int excellence = Integer.parseInt(latest.Excellence);
                            int merit = Integer.parseInt(latest.Merit) + excellence;
                            int achieved = Integer.parseInt(latest.Total);
                            int lastAchieved = Integer.parseInt(student.YearTotals.get(1).Total);

                            // Account for up to 20 credits gained in the previous date
                            if (lastAchieved > 20)
                                lastAchieved = 20;

                            achieved = achieved + lastAchieved;

                            if (excellence < 50)
                                excellence = 50 - excellence;
                            else
                                excellence = 0;

                            if (merit < 50)
                                merit = 50 - merit;
                            else
                                merit = 0;

                            if (achieved < 80)
                                achieved = 80 - achieved;
                            else
                                achieved = 0;

                            ((TextView) mView.findViewById(R.id.needed_excellence)).setText(Integer.toString(excellence));
                            ((TextView) mView.findViewById(R.id.needed_merit)).setText(Integer.toString(merit));
                            ((TextView) mView.findViewById(R.id.needed_pass)).setText(Integer.toString(achieved));

                            ((TextView) mView.findViewById(R.id.current_year)).setText(latest.Year);
                            ((TextView) mView.findViewById(R.id.current_year_notachieved)).setText(latest.NotAchieved);
                            ((TextView) mView.findViewById(R.id.current_year_achieved)).setText(latest.Achieved);
                            ((TextView) mView.findViewById(R.id.current_year_merit)).setText(latest.Merit);
                            ((TextView) mView.findViewById(R.id.current_year_excellence)).setText(latest.Excellence);
                            ((TextView) mView.findViewById(R.id.current_year_total)).setText(latest.Total);
                            ((TextView) mView.findViewById(R.id.current_year_attempted)).setText(latest.Attempted);

                            NCEAObject.YearTotal year = student.YearTotals.get(1);
                            ((TextView) mView.findViewById(R.id.other_year1_title)).setText(year.Year);
                            ((TextView) mView.findViewById(R.id.other_year1_notachieved)).setText(year.NotAchieved);
                            ((TextView) mView.findViewById(R.id.other_year1_achieved)).setText(year.Achieved);
                            ((TextView) mView.findViewById(R.id.other_year1_merit)).setText(year.Merit);
                            ((TextView) mView.findViewById(R.id.other_year1_excellence)).setText(year.Excellence);
                            ((TextView) mView.findViewById(R.id.other_year1_credits)).setText(year.Total);
                            ((TextView) mView.findViewById(R.id.other_year1_attempted)).setText(year.Attempted);

                            year = student.YearTotals.get(2);
                            ((TextView) mView.findViewById(R.id.other_year2_title)).setText(year.Year);
                            ((TextView) mView.findViewById(R.id.other_year2_notachieved)).setText(year.NotAchieved);
                            ((TextView) mView.findViewById(R.id.other_year2_achieved)).setText(year.Achieved);
                            ((TextView) mView.findViewById(R.id.other_year2_merit)).setText(year.Merit);
                            ((TextView) mView.findViewById(R.id.other_year2_excellence)).setText(year.Excellence);
                            ((TextView) mView.findViewById(R.id.other_year2_credits)).setText(year.Total);
                            ((TextView) mView.findViewById(R.id.other_year2_attempted)).setText(year.Attempted);

                            year = student.YearTotals.get(3);
                            ((TextView) mView.findViewById(R.id.other_year3_title)).setText(year.Year);
                            ((TextView) mView.findViewById(R.id.other_year3_notachieved)).setText(year.NotAchieved);
                            ((TextView) mView.findViewById(R.id.other_year3_achieved)).setText(year.Achieved);
                            ((TextView) mView.findViewById(R.id.other_year3_merit)).setText(year.Merit);
                            ((TextView) mView.findViewById(R.id.other_year3_excellence)).setText(year.Excellence);
                            ((TextView) mView.findViewById(R.id.other_year3_credits)).setText(year.Total);
                            ((TextView) mView.findViewById(R.id.other_year3_attempted)).setText(year.Attempted);

                            year = student.YearTotals.get(4);
                            ((TextView) mView.findViewById(R.id.other_year4_title)).setText(year.Year);
                            ((TextView) mView.findViewById(R.id.other_year4_notachieved)).setText(year.NotAchieved);
                            ((TextView) mView.findViewById(R.id.other_year4_achieved)).setText(year.Achieved);
                            ((TextView) mView.findViewById(R.id.other_year4_merit)).setText(year.Merit);
                            ((TextView) mView.findViewById(R.id.other_year4_excellence)).setText(year.Excellence);
                            ((TextView) mView.findViewById(R.id.other_year4_credits)).setText(year.Total);
                            ((TextView) mView.findViewById(R.id.other_year4_attempted)).setText(year.Attempted);

                            NCEAObject.LevelTotal level = student.LevelTotals.get(0);
                            ((TextView) mView.findViewById(R.id.level1_title)).setText(String.format("Level %s", level.Level));
                            ((TextView) mView.findViewById(R.id.level1_notachieved)).setText(level.NotAchieved);
                            ((TextView) mView.findViewById(R.id.level1_achieved)).setText(level.Achieved);
                            ((TextView) mView.findViewById(R.id.level1_merit)).setText(level.Merit);
                            ((TextView) mView.findViewById(R.id.level1_excellence)).setText(level.Excellence);
                            ((TextView) mView.findViewById(R.id.level1_credits)).setText(level.Total);
                            ((TextView) mView.findViewById(R.id.level1_attempted)).setText(level.Attempted);

                            level = student.LevelTotals.get(1);
                            ((TextView) mView.findViewById(R.id.level2_title)).setText(String.format("Level %s", level.Level));
                            ((TextView) mView.findViewById(R.id.level2_notachieved)).setText(level.NotAchieved);
                            ((TextView) mView.findViewById(R.id.level2_achieved)).setText(level.Achieved);
                            ((TextView) mView.findViewById(R.id.level2_merit)).setText(level.Merit);
                            ((TextView) mView.findViewById(R.id.level2_excellence)).setText(level.Excellence);
                            ((TextView) mView.findViewById(R.id.level2_credits)).setText(level.Total);
                            ((TextView) mView.findViewById(R.id.level2_attempted)).setText(level.Attempted);

                            level = student.LevelTotals.get(2);
                            ((TextView) mView.findViewById(R.id.level3_title)).setText(String.format("Level %s", level.Level));
                            ((TextView) mView.findViewById(R.id.level3_notachieved)).setText(level.NotAchieved);
                            ((TextView) mView.findViewById(R.id.level3_achieved)).setText(level.Achieved);
                            ((TextView) mView.findViewById(R.id.level3_merit)).setText(level.Merit);
                            ((TextView) mView.findViewById(R.id.level3_excellence)).setText(level.Excellence);
                            ((TextView) mView.findViewById(R.id.level3_credits)).setText(level.Total);
                            ((TextView) mView.findViewById(R.id.level3_attempted)).setText(level.Attempted);

                            level = student.LevelTotals.get(3);
                            ((TextView) mView.findViewById(R.id.level4_title)).setText(String.format("Level %s", level.Level));
                            ((TextView) mView.findViewById(R.id.level4_notachieved)).setText(level.NotAchieved);
                            ((TextView) mView.findViewById(R.id.level4_achieved)).setText(level.Achieved);
                            ((TextView) mView.findViewById(R.id.level4_merit)).setText(level.Merit);
                            ((TextView) mView.findViewById(R.id.level4_excellence)).setText(level.Excellence);
                            ((TextView) mView.findViewById(R.id.level4_credits)).setText(level.Total);
                            ((TextView) mView.findViewById(R.id.level4_attempted)).setText(level.Attempted);

                            level = student.LevelTotals.get(4);
                            ((TextView) mView.findViewById(R.id.level5_title)).setText(String.format("Level %s", level.Level));
                            ((TextView) mView.findViewById(R.id.level5_notachieved)).setText(level.NotAchieved);
                            ((TextView) mView.findViewById(R.id.level5_achieved)).setText(level.Achieved);
                            ((TextView) mView.findViewById(R.id.level5_merit)).setText(level.Merit);
                            ((TextView) mView.findViewById(R.id.level5_excellence)).setText(level.Excellence);
                            ((TextView) mView.findViewById(R.id.level5_credits)).setText(level.Total);
                            ((TextView) mView.findViewById(R.id.level5_attempted)).setText(level.Attempted);

                            NCEAObject.NCEA ncea = student.NCEA;
                            ((TextView) mView.findViewById(R.id.ncea_level1_result)).setText(toTitleCase(ncea.L1NCEA));
                            ((TextView) mView.findViewById(R.id.ncea_level2_result)).setText(toTitleCase(ncea.L2NCEA));
                            ((TextView) mView.findViewById(R.id.ncea_level3_result)).setText(toTitleCase(ncea.L3NCEA));
                            ((TextView) mView.findViewById(R.id.ncea_ue_lit_result)).setText(ncea.NCEAUELIT);
                            ((TextView) mView.findViewById(R.id.ncea_num_result)).setText(ncea.NCEANUM);
                            ((TextView) mView.findViewById(R.id.ncea_lit_result)).setText(ncea.NCEAL1LIT);

                            PieChart chart = mView.findViewById(R.id.chart);

                            PieEntry excellenceEntry = new PieEntry(Integer.parseInt(student.CreditsTotal.Excellence), "Excellence");
                            PieEntry notAchievedEntry = new PieEntry(Integer.parseInt(student.CreditsTotal.NotAchieved), "Not Achieved");
                            PieEntry achievedEntry = new PieEntry(Integer.parseInt(student.CreditsTotal.Achieved), "Achieved");
                            PieEntry meritEntry = new PieEntry(Integer.parseInt(student.CreditsTotal.Merit), "Merit");

                            boolean enrolledInNCEA = excellenceEntry.getValue() != 0 || notAchievedEntry.getValue() != 0 || achievedEntry.getValue() != 0 || meritEntry.getValue() != 0;


                            int color = ResourcesCompat.getColor(getResources(), R.color.white, null);
                            @ColorInt int bg = ResourcesCompat.getColor(getResources(), R.color.white, null);
                            if (isAdded()) {
                                Resources.Theme theme = requireContext().getTheme();
                                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                                    color = getContext().getResources().getColor(R.color.white);
                                } else {
                                    color = getContext().getResources().getColor(android.R.color.black);
                                }

                                TypedValue typedValue2 = new TypedValue();
                                theme.resolveAttribute(android.R.attr.colorBackground, typedValue2, true);
                                bg = typedValue2.data;
                            }

                            Description desc = new Description();
                            desc.setText("Summary of all NCEA credits gained");
                            desc.setTextColor(color);
                            chart.setDescription(desc);

                            List<PieEntry> entries = Arrays.asList(notAchievedEntry, achievedEntry, meritEntry, excellenceEntry);

                            chart.animateXY(1000, 1000);

                            PieDataSet dataSet = new PieDataSet(entries, "");
                            dataSet.setColors(new int[]{R.color.notachieved, R.color.achieved, R.color.merit, R.color.excellence}, requireContext());
                            PieData data = new PieData(dataSet);
                            chart.setDrawEntryLabels(false);
                            data.setValueTextColor(requireContext().getResources().getColor(R.color.white));
                            chart.setData(data);
                            chart.getLegend().setTextColor(color);
                            chart.setHoleColor(bg);

                            if (!enrolledInNCEA) {
                                mView.findViewById(R.id.chartLayout).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_excellence).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_merit).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_pass).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_excellence_caption).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_merit_caption).setVisibility(View.GONE);
                                mView.findViewById(R.id.needed_pass_caption).setVisibility(View.GONE);
                            }

                            mLoader.setVisibility(View.GONE);
                            mView.findViewById(R.id.scrollView).setVisibility(View.VISIBLE);
                            mView.findViewById(R.id.noInternet).setVisibility(View.GONE);
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

    public static String toTitleCase(String s) {
        String str = "";
        char a;

        for (int i = 0; i < s.length(); i++) {
            a = s.charAt(i);
            if (a == ' ') {
                str = str + Character.toLowerCase(a) + (Character.toUpperCase(s.charAt(i + 1)));
                i++; // "skip" the next element since it is now already processed
            } else if (i == 0) {
                str = str + Character.toUpperCase(s.charAt(i));
            } else {
                str = str + (Character.toLowerCase(a));
            }

        }

        return str;
    }
}
