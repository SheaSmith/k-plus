package sheasmith.me.betterkamar.pages.timetable;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sheasmith.me.betterkamar.ApiManager;
import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.AttendanceObject;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;
import sheasmith.me.betterkamar.internalModels.ApiResponse;
import sheasmith.me.betterkamar.internalModels.PortalObject;

public class TimetableFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TimetableAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;
    private TextView noEvents;
    private MaterialCalendarView mCalendarView;

    private ArrayList<GlobalObject.PeriodDefinition> periodDefinitions;
    private ArrayList<AttendanceObject.Week> attendanceResults;
    private ArrayList<CalendarObject.Event> events;
    private ArrayList<TimetableObject.Week> timetable;
    private Date lastDate;

    public static TimetableFragment newInstance() {
        return new TimetableFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("periodDefinitions", periodDefinitions);
        outState.putSerializable("attendanceResults", attendanceResults);
        outState.putSerializable("events", events);
        outState.putSerializable("timetable", timetable);
        outState.putSerializable("selectedDate", mCalendarView.getSelectedDate().getDate());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            periodDefinitions = (ArrayList<GlobalObject.PeriodDefinition>) savedInstanceState.getSerializable("periodDefinitions");
            attendanceResults = (ArrayList<AttendanceObject.Week>) savedInstanceState.getSerializable("attendanceResults");
            events = (ArrayList<CalendarObject.Event>) savedInstanceState.getSerializable("events");
            timetable = (ArrayList<TimetableObject.Week>) savedInstanceState.getSerializable("timetable");

            lastDate = (Date) savedInstanceState.getSerializable("selectedDate");
        }

        PortalObject portal = (PortalObject) getActivity().getIntent().getSerializableExtra("portal");
        ApiManager.setVariables(portal, getContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timetable, container, false);
        mLoader = view.findViewById(R.id.progressBar);
        noEvents = view.findViewById(R.id.noEvents);

        mRecyclerView = view.findViewById(R.id.events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
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
        mCalendarView.state().edit().setMaximumDate(max).setMinimumDate(min).commit();

        mCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay calDate, boolean selected) {
                final Date date = calDate.getDate();
                updateList(date);
            }
        });

        if (lastDate != null) {
            mCalendarView.setSelectedDate(lastDate);
            updateList(lastDate);
        }

        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    doRequest();
                }
            }).start();

        return view;
    }


    private void doRequest() {
        final boolean[] finished = new boolean[]{false, false, false, false};

        ApiManager.getGlobals(new ApiResponse<GlobalObject>() {
            @Override
            public void success(GlobalObject value) {
                periodDefinitions = value.GlobalsResults.PeriodDefinitions;
                finished[0] = true;
                hideLoader(finished);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[0] = true;
                hideLoader(finished);
            }

        });

        ApiManager.getAttendance(new ApiResponse<AttendanceObject>() {
            @Override
            public void success(AttendanceObject value) {
                attendanceResults = value.StudentAttendanceResults.Weeks;
                finished[1] = true;
                hideLoader(finished);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[1] = true;
                hideLoader(finished);
            }
        });

        ApiManager.getEvents(new ApiResponse<CalendarObject>() {
            @Override
            public void success(CalendarObject value) {
                events = value.EventsResults.Events;
                finished[2] = true;
                hideLoader(finished);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[2] = true;
                hideLoader(finished);
            }
        }, new Date(System.currentTimeMillis()).getYear());

        ApiManager.getTimetable(new ApiResponse<TimetableObject>() {
            @Override
            public void success(TimetableObject value) {
                timetable = value.StudentTimetableResults.Student.Timetable;
                finished[3] = true;
                hideLoader(finished);
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
                finished[3] = true;
                hideLoader(finished);
            }
        });
    }

    private void hideLoader(boolean[] finished) {
        for (boolean f : finished) {
            if (!f)
                return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mLoader.setVisibility(View.GONE);
                mCalendarView.setSelectedDate(new Date(System.currentTimeMillis()));
                updateList(new Date(System.currentTimeMillis()));
            }
        });
    }

    private void updateList(Date date) {
        Calendar current = Calendar.getInstance();
        current.setTime(date);

        List<TimetableObject.Class> periods = new ArrayList<>();
        Calendar monday = Calendar.getInstance();
        monday.setTime(date);
        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        int weekNumber = 0;
        Calendar temp = Calendar.getInstance();

        for (AttendanceObject.Week week : attendanceResults) {
            try {
                Date start = format.parse(week.WeekStart);
                temp.setTime(start);
                if (monday.get(Calendar.DAY_OF_YEAR) == temp.get(Calendar.DAY_OF_YEAR)) {
                    weekNumber = Integer.parseInt(week.index);
                    break;
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (weekNumber == 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setVisibility(View.GONE);
                    noEvents.setVisibility(View.VISIBLE);
                }
            });
            return;
        }
        else {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    noEvents.setVisibility(View.GONE);
                }
            });
        }

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
                        break weekLoop;
                    }
                }
            }
        }

        final List<CalendarObject.Event> dayEvents = new ArrayList<>();

        for (CalendarObject.Event event : events) {
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

        if (periods == null) {
            throw new RuntimeException();
        }

        if (dayEvents.size() == 0 && periods.size() == 0) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.setVisibility(View.GONE);
                    noEvents.setVisibility(View.VISIBLE);
                }
            });
            return;
        }

        final List<TimetableObject.Class> finalPeriods = periods;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter = new TimetableAdapter(dayEvents, finalPeriods, periodDefinitions, getContext());
                mRecyclerView.setAdapter(mAdapter);
//                                                mLoader.setVisibility(View.GONE);
            }
        });
    }
}