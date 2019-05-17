/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 17/05/19 10:29 PM
 */

package sheasmith.me.betterkamar.pages.timetable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sheasmith.me.betterkamar.KamarPlusApplication;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.AbsenceObject;
import sheasmith.me.betterkamar.dataModels.AttendanceObject;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.EventsObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.LoginObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.Exceptions;
import sheasmith.me.betterkamar.internalModels.PortalObject;
import sheasmith.me.betterkamar.util.ApiManager;
import sheasmith.me.betterkamar.util.OnSwipeTouchListener;

public class TimetableFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TimetableAdapter mAdapter;
    private ProgressBar mLoader;
    private TextView noEvents;
    private MaterialCalendarView mCalendarView;

    private ArrayList<GlobalObject.PeriodDefinition> periodDefinitions;
    private ArrayList<AttendanceObject.Week> attendanceResults;
    private ArrayList<EventsObject.Event> events;
    private ArrayList<TimetableObject.Week> timetable;
    private ArrayList<CalendarObject.Day> days;
    private AbsenceObject absenceStats;
    private Date lastDate;
    private PortalObject mPortal;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isAdded()) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setElevation(10);
            requireActivity().setTitle("Timetable");
            if (mAdapter == null || mAdapter.getItemCount() == 0)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        doRequest(mPortal, false);
                    }
                }).start();
            requireActivity().invalidateOptionsMenu();

            KamarPlusApplication application = (KamarPlusApplication) requireActivity().getApplication();
            Tracker mTracker = application.getDefaultTracker();
            mTracker.setScreenName("Timetable");
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    public static TimetableFragment newInstance() {
        return new TimetableFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("periodDefinitions", periodDefinitions);
        if (attendanceResults != null)
            outState.putSerializable("attendanceResults", attendanceResults);
        outState.putSerializable("events", events);
        outState.putSerializable("timetable", timetable);
        if (mCalendarView.getSelectedDate() != null)
            outState.putSerializable("selectedDate", new Date(mCalendarView.getSelectedDate().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
        outState.putSerializable("days", days);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            periodDefinitions = (ArrayList<GlobalObject.PeriodDefinition>) savedInstanceState.getSerializable("periodDefinitions");
            attendanceResults = (ArrayList<AttendanceObject.Week>) savedInstanceState.getSerializable("attendanceResults");
            events = (ArrayList<EventsObject.Event>) savedInstanceState.getSerializable("events");
            timetable = (ArrayList<TimetableObject.Week>) savedInstanceState.getSerializable("timetable");
            days = (ArrayList<CalendarObject.Day>) savedInstanceState.getSerializable("days");

            if (savedInstanceState.containsKey("selectedDate"))
                lastDate = (Date) savedInstanceState.getSerializable("selectedDate");
        }

        if (isAdded()) {
            mPortal = (PortalObject) requireActivity().getIntent().getSerializableExtra("portal");
            ApiManager.setVariables(mPortal, requireContext());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (isAdded()) {
            if (item.getItemId() == 1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Attendance Statistics").setPositiveButton("close", null);
                if (absenceStats == null) {
                    builder.setMessage("Please wait for the timetable to load");
                } else {
                    AbsenceObject.Student student = absenceStats.StudentAbsenceStatsResults.Student;
                    builder.setMessage(String.format("Unjustified: %s%%\nJustified: %s%%\nOverseas: %s%%\nTotal Absences: %s%%\nTotal Present: %s%%", student.PctgeU, student.PctgeJ, student.PctgeO, student.PctgeT, student.PctgeP));
                }
                builder.create().show();
            } else if (item.getItemId() == 2) {
                LayoutInflater inflater = getLayoutInflater();
                View contentView = inflater.inflate(R.layout.timetable_info, null);

                new AlertDialog.Builder(requireContext())
                        .setTitle("Attendance Codes (based on MoE rules)")
                        .setView(contentView)
                        .setPositiveButton("Close", null)
                        .create()
                        .show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        menu.add(0, 1, 0, "Attendance Stats").setIcon(R.drawable.ic_chart).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0, 2, 0, "Attendance Info").setIcon(R.drawable.ic_info).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (isAdded()) {
            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ThemeColours", Context.MODE_PRIVATE);
            String stringColor = sharedPreferences.getString("color", "E65100");

            final Context contextThemeWrapper = new ContextThemeWrapper(requireActivity(), getResources().getIdentifier("T_" + stringColor, "style", requireContext().getPackageName()));

            LayoutInflater localInflator = inflater.cloneInContext(contextThemeWrapper);
            View view = localInflator.inflate(R.layout.fragment_timetable, container, false);
            mLoader = view.findViewById(R.id.progressBar);
            noEvents = view.findViewById(R.id.noEvents);
            setHasOptionsMenu(true);

            mRecyclerView = view.findViewById(R.id.events);
            mRecyclerView.setOnTouchListener(new OnSwipeTouchListener(requireContext()) {
                @Override
                public void onSwipeRight() {
                    mCalendarView.setCurrentDate(mCalendarView.getSelectedDate().getDate().minusDays(1));
                    mCalendarView.setSelectedDate(mCalendarView.getSelectedDate().getDate().minusDays(1));
                    final Date date = new Date(mCalendarView.getSelectedDate().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    updateList(date);
                }

                @Override
                public void onSwipeLeft() {
                    mCalendarView.setCurrentDate(mCalendarView.getSelectedDate().getDate().plusDays(1));
                    mCalendarView.setSelectedDate(mCalendarView.getSelectedDate().getDate().plusDays(1));
                    final Date date = new Date(mCalendarView.getSelectedDate().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    updateList(date);
                }
            });

            noEvents.setOnTouchListener(new OnSwipeTouchListener(requireContext()) {
                @Override
                public void onSwipeRight() {
                    mCalendarView.setCurrentDate(mCalendarView.getSelectedDate().getDate().minusDays(1));
                    mCalendarView.setSelectedDate(mCalendarView.getSelectedDate().getDate().minusDays(1));
                    final Date date = new Date(mCalendarView.getSelectedDate().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    updateList(date);
                }

                @Override
                public void onSwipeLeft() {
                    mCalendarView.setCurrentDate(mCalendarView.getSelectedDate().getDate().plusDays(1));
                    mCalendarView.setSelectedDate(mCalendarView.getSelectedDate().getDate().plusDays(1));
                    final Date date = new Date(mCalendarView.getSelectedDate().getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    updateList(date);
                }
            });

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(false);

            // use a linear layout manager
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            Date now = new Date(System.currentTimeMillis());
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);

            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MONTH, 0);
            cal.set(Calendar.DAY_OF_MONTH, 1);

            Date min = cal.getTime();

            cal.set(Calendar.MONTH, 11);
            cal.set(Calendar.DAY_OF_MONTH, 31);

            Date max = cal.getTime();

            mCalendarView = view.findViewById(R.id.calendarView);
//        mCalendarView.setCurrentDate(new Date(System.currentTimeMillis()));
            mCalendarView.state().edit().setMaximumDate(Instant.ofEpochMilli(max.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()).setMinimumDate(Instant.ofEpochMilli(min.getTime()).atZone(ZoneId.systemDefault()).toLocalDate()).commit();

            mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
                @Override
                public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calDate, boolean selected) {
                    final Date date = new Date(calDate.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli());
                    updateList(date);
                }
            });

            if (lastDate != null) {
                mCalendarView.setSelectedDate(Instant.ofEpochMilli(lastDate.getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                updateList(lastDate);
            }

            mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
            mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    doRequest(mPortal, true);
                }
            });


            return view;
        }
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }


    private void doRequest(final PortalObject portal, final boolean ignoreCache) {
        final boolean[] finished = new boolean[]{false, false, false, false, false, false};

        ApiManager.getGlobals(new ApiResponse<GlobalObject>() {
            @Override
            public void success(GlobalObject value) {
                if (isAdded()) {
                    periodDefinitions = value.GlobalsResults.PeriodDefinitions;
                    finished[0] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[0] = true;
                handleError(portal, e, ignoreCache);
            }

        }, ignoreCache);

        ApiManager.getAttendance(new ApiResponse<AttendanceObject>() {
            @Override
            public void success(AttendanceObject value) {
                if (isAdded()) {
                    attendanceResults = value.StudentAttendanceResults.Weeks;
                    finished[1] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[1] = true;
                handleError(portal, e, ignoreCache);
            }
        }, ignoreCache);

        ApiManager.getEvents(new ApiResponse<EventsObject>() {
            @Override
            public void success(EventsObject value) {
                if (isAdded()) {
                    events = value.EventsResults.Events;
                    finished[2] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[2] = true;
                handleError(portal, e, ignoreCache);
            }
        }, Calendar.getInstance().get(Calendar.YEAR), ignoreCache);

        ApiManager.getTimetable(new ApiResponse<TimetableObject>() {
            @Override
            public void success(TimetableObject value) {
                if (isAdded()) {
                    timetable = value.StudentTimetableResults.Student.Timetable;
                    finished[3] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[3] = true;

                handleError(portal, e, ignoreCache);
            }
        }, ignoreCache);

        ApiManager.getCalendar(new ApiResponse<CalendarObject>() {
            @Override
            public void success(CalendarObject value) {
                if (isAdded()) {
                    days = value.CalendarResults.Days;
                    finished[4] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[4] = true;

                handleError(portal, e, ignoreCache);
            }
        }, ignoreCache);

        ApiManager.getAbsenceStats(new ApiResponse<AbsenceObject>() {
            @Override
            public void success(AbsenceObject value) {
                if (isAdded()) {
                    absenceStats = value;
                    finished[5] = true;
                    hideLoader(finished);
                }
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[5] = true;

                handleError(portal, e, ignoreCache);
            }
        }, ignoreCache);
    }

    private void handleError(final PortalObject portal, Exception e, final boolean ignoreCache) {
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
            if (isAdded())
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
                                        if (isAdded())
                                            requireActivity().finish();
                                    }
                                })
                                .create()
                                .show();
                    }
                });
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

    private void hideLoader(boolean[] finished) {
        for (boolean f : finished) {
            if (!f)
                return;
        }
        if (isAdded()) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoader.setVisibility(View.GONE);
                    mSwipeRefreshLayout.setRefreshing(false);
                    // So we get the weeks to show up
                    mCalendarView.setCurrentDate(mCalendarView.getCurrentDate().getDate().minusWeeks(1));
                    mCalendarView.setCurrentDate(mCalendarView.getCurrentDate().getDate().plusWeeks(1));
                    mCalendarView.setSelectedDate(Instant.ofEpochMilli(System.currentTimeMillis()).atZone(ZoneId.systemDefault()).toLocalDate());
                    updateList(new Date(System.currentTimeMillis()));
                }
            });
        }
    }

    private void updateList(final Date date) {
        Calendar current = Calendar.getInstance();
        current.setTime(date);

        List<TimetableObject.Class> periods = new ArrayList<>();
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        int weekNumber = 0;
        CalendarObject.Day thisDay = null;
        final Calendar temp = Calendar.getInstance();

        AttendanceObject.Week attendanceWeek = null;

        HashMap<String, List<CalendarDay>> dotsSortedByType = new HashMap<>();

        if (days != null) {
            for (CalendarObject.Day day : days) {
                try {

                    Date start = format.parse(day.Date);
                    temp.setTime(start);

                    List<CalendarDay> tempList = new ArrayList<>();
                    if (dotsSortedByType.containsKey(day.Status))
                        tempList = dotsSortedByType.get(day.Status);

                    tempList.add(CalendarDay.from(temp.get(Calendar.YEAR), temp.get(Calendar.MONTH) + 1, temp.get(Calendar.DAY_OF_MONTH)));

                    dotsSortedByType.put(day.Status, tempList);

                    if (current.get(Calendar.DAY_OF_YEAR) == temp.get(Calendar.DAY_OF_YEAR) && !day.WeekYear.equals("") && !day.Status.equals("Holidays")) {
                        weekNumber = Integer.parseInt(day.WeekYear);
                        if (attendanceResults != null) {
                            for (AttendanceObject.Week aw : attendanceResults) {
                                Date weekStart = format.parse(aw.WeekStart);
                                Calendar weekCal = Calendar.getInstance();
                                weekCal.setTime(weekStart);
                                if (weekCal.get(Calendar.DAY_OF_YEAR) == monday.get(Calendar.DAY_OF_YEAR) && weekCal.get(Calendar.YEAR) == monday.get(Calendar.YEAR)) {
                                    attendanceWeek = aw;
                                    break;
                                }
                            }
                        }
                        thisDay = day;
                        break;
                    } else if (current.get(Calendar.DAY_OF_YEAR) == temp.get(Calendar.DAY_OF_YEAR)) {
                        thisDay = day;
                        break;
                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        for (Map.Entry<String, List<CalendarDay>> entry : dotsSortedByType.entrySet()) {
            int color;
            switch (entry.getKey()) {
                case "Holidays":
                    color = ResourcesCompat.getColor(getResources(), R.color.merit, null);
                    break;
                case "Holiday":
                    color = ResourcesCompat.getColor(getResources(), R.color.merit, null);
                    break;
                case "Open":
                    color = ResourcesCompat.getColor(getResources(), R.color.achieved, null);
                    break;
                case "Teacher Only Day":
                    color = ResourcesCompat.getColor(getResources(), R.color.excellence, null);
                    break;
                case "School Closed":
                    color = ResourcesCompat.getColor(getResources(), R.color.notachieved, null);
                    break;
                default:
                    color = -1;
                    break;
            }

            if (color != -1)
                mCalendarView.addDecorator(new DayTypeDecorator(color, entry.getValue()));
        }

        if (thisDay != null) {
            final CalendarObject.Day finalThisDay = thisDay;
            if (isAdded()) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                    if (finalThisDay.Week.equals("") || finalThisDay.DayTT.equals("")) {
//                    }
//                    else {
//                        status.setText(String.format("School Status: %s. Term %s Week %s", finalThisDay.Status, finalThisDay.Term, finalThisDay.Week));
//                    }
                        mCalendarView.setTitleFormatter(new TitleFormatter() {
                            @Override
                            public CharSequence format(CalendarDay calendarDay) {
                                SimpleDateFormat form = new SimpleDateFormat("MMMM, yyyy");
                                CalendarObject.Day weekDay = null;
                                int dayOfYear = mCalendarView.getCurrentDate().getDate().getDayOfYear();
                                for (CalendarObject.Day day : days) {
                                    try {
                                        Date start = format.parse(day.Date);
                                        temp.setTime(start);
                                        if (dayOfYear == temp.get(Calendar.DAY_OF_YEAR)) {
                                            weekDay = day;
                                        }

                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                if (weekDay.Status.equals("Holidays"))
                                    return form.format(new Date(calendarDay.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()));
                                else
                                    return form.format(new Date(calendarDay.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli())) + "\nTerm " + weekDay.TermA + " Week " + weekDay.WeekA;
                            }
                        });
                        mCalendarView.invalidate();
                    }
                });
            }
        }

        if (weekNumber == 0) {
            if (isAdded()) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setVisibility(View.GONE);
                        noEvents.setVisibility(View.VISIBLE);
                    }
                });
            }
            return;
        } else {
            if (isAdded()) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        noEvents.setVisibility(View.GONE);
                    }
                });
            }
        }

        AttendanceObject.Day attendanceDay = null;

        if (timetable != null) {
            weekLoop:
            for (TimetableObject.Week week : timetable) {
                if (week.WeekNumber == weekNumber) {
                    for (Integer day : week.Classes.keySet()) {
                        Calendar cal = Calendar.getInstance();
                        cal.setFirstDayOfWeek(Calendar.SUNDAY);
                        cal.setTime(date);
                        cal.set(Calendar.HOUR_OF_DAY, 0);
                        cal.set(Calendar.MINUTE, 0);
                        cal.set(Calendar.SECOND, 0);
                        cal.set(Calendar.MILLISECOND, 0);
                        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                        cal.add(Calendar.DAY_OF_WEEK, day);

                        if (cal.get(Calendar.DAY_OF_YEAR) == current.get(Calendar.DAY_OF_YEAR)) {
                            periods = week.Classes.get(day);
                            if (attendanceWeek != null) {
                                for (AttendanceObject.Day ad : attendanceWeek.Days) {
                                    if (ad.index.equals(day.toString())) {
                                        attendanceDay = ad;
                                        break;
                                    }
                                }
                            }
                            break weekLoop;
                        }
                    }
                }
            }
        }

        if (periods != null) {
            for (TimetableObject.Class c : periods) {
                if (attendanceDay != null) {
                    try {
                        c.attendance = attendanceDay.content.charAt(periods.indexOf(c));
                    } catch (IndexOutOfBoundsException e) {
                        c.attendance = '.';
                    }
                } else
                    break;
            }
        }

        final List<EventsObject.Event> dayEvents = new ArrayList<>();

        if (events != null) {
            for (EventsObject.Event event : events) {
                try {
                    Date start = format.parse(event.Start);
                    Date end = format.parse(event.Finish);

                    Calendar c = Calendar.getInstance();
                    c.setTime(start);
                    int startDay = c.get(Calendar.DAY_OF_YEAR);

                    c.setTime(end);
                    int endDay = c.get(Calendar.DAY_OF_YEAR);

                    c.setTime(date);
                    int currentDay = c.get(Calendar.DAY_OF_YEAR);

                    if (startDay >= currentDay && endDay <= currentDay) {
                        dayEvents.add(event);
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (periods == null) {
            throw new RuntimeException();
        }

        if (dayEvents.size() == 0 && periods.size() == 0) {
            if (isAdded()) {
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.setVisibility(View.GONE);
                        noEvents.setVisibility(View.VISIBLE);
                    }
                });
            }
            return;
        }

        final List<TimetableObject.Class> finalPeriods = periods;
        if (isAdded()) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        mAdapter = new TimetableAdapter(dayEvents, finalPeriods, periodDefinitions, requireContext());
                        mRecyclerView.setAdapter(mAdapter);
//                                                mLoader.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
