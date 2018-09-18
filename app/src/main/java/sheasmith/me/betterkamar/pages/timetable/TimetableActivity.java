package sheasmith.me.betterkamar.pages.timetable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import sheasmith.me.betterkamar.pages.notices.NoticesAdapter;

public class TimetableActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TimetableAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressBar mLoader;

    private List<GlobalObject.PeriodDefinition> periodDefinitions;
    private List<>

    Calendar current = Calendar.getInstance();
                                        current.setTime(date);

    List<TimetableObject.Class> periods = new ArrayList<>();
    Calendar monday = Calendar.getInstance();
                                        monday.setTime(date);
                                        monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    int weekNumber = 0;
    Calendar temp = Calendar.getInstance();

                                        for (AttendanceObject.Week week : attendance.StudentAttendanceResults.Weeks) {
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
        // TODO: Do something, must be a time in the future, holidays or the previous year
        return;
    }

    weekLoop:
            for (TimetableObject.Week week : timetable.StudentTimetableResults.Student.Timetable) {
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

                                        if (periods == null) {
        throw new RuntimeException();
    }

    final List<TimetableObject.Class> finalPeriods = periods;
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            mAdapter = new TimetableAdapter(cal.EventsResults.Events, finalPeriods, globals.GlobalsResults.PeriodDefinitions, TimetableActivity.this);
            mRecyclerView.setAdapter(mAdapter);
//                                                mLoader.setVisibility(View.GONE);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        PortalObject portal = (PortalObject) getIntent().getSerializableExtra("portal");
        ApiManager.setVariables(portal);

        mRecyclerView = findViewById(R.id.events);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        MaterialCalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setCurrentDate(new Date(System.currentTimeMillis()));



        if (mAdapter == null ||  mAdapter.getItemCount() == 0)
            doRequest();
    }

    private void doRequest() {
        ApiManager.getGlobals(new ApiResponse<GlobalObject>() {
            @Override
            public void success(final GlobalObject globals) {
                ApiManager.getEvents(new ApiResponse<CalendarObject>() {
                    @Override
                    public void success(final CalendarObject cal) {
                        ApiManager.getAttendance(new ApiResponse<AttendanceObject>() {
                            @Override
                            public void success(final AttendanceObject attendance) {
                                ApiManager.getTimetable(new ApiResponse<TimetableObject>() {
                                    @Override
                                    public void success(TimetableObject timetable) {

                                    }

                                    @Override
                                    public void error(Exception e) {
                                        e.printStackTrace();
                                    }
                                });
                            }

                            @Override
                            public void error(Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void error(Exception e) {
                        e.printStackTrace();
                    }
                }, new Date(System.currentTimeMillis()).getYear());
            }

            @Override
            public void error(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
