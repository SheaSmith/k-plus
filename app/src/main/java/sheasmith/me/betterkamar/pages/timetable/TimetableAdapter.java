package sheasmith.me.betterkamar.pages.timetable;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.EventsObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public List<EventsObject.Event> mEvents;
    public List<TimetableObject.Class> mClasses;
    public List<GlobalObject.PeriodDefinition> mPeriodDefinitions;
    private Context mContext;

    public static class TimetableViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mView;
        public TextView title;
        public TextView details;
        public TextView periodName;
        public TextView attendance;
        public RelativeLayout item;

        public TimetableViewHolder(RelativeLayout v) {
            super(v);
            mView = v;

            title = mView.findViewById(R.id.title);
            details = mView.findViewById(R.id.details);
            periodName = mView.findViewById(R.id.time);
            attendance = mView.findViewById(R.id.attendance);
            item = mView.findViewById(R.id.item);
        }
    }

    public TimetableAdapter(List<EventsObject.Event> events, List<TimetableObject.Class> classes, List<GlobalObject.PeriodDefinition> periodDefinitions, Context context) {
        mEvents = events;
        mClasses = classes;
        mPeriodDefinitions = periodDefinitions;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_timetable, parent, false);
        return new TimetableViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position < mEvents.size()) {
            final EventsObject.Event event = mEvents.get(position);

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            ((TimetableViewHolder) holder).title.setText(event.Title);
            String time = "All day";
            if (!event.DateTimeStart.equals("") && !event.DateTimeFinish.equals("")) {
                try {
                    String start = timeFormat.format(format.parse(event.DateTimeStart));
                    String end = timeFormat.format(format.parse(event.DateTimeFinish));
                    time = start + " - " + end;

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            } else if (!event.DateTimeStart.equals("")) {
                try {
                    time = timeFormat.format(format.parse(event.DateTimeStart));

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            ((TimetableViewHolder) holder).details.setText(time);
            ((TimetableViewHolder) holder).item.getBackground().mutate().setColorFilter(Color.parseColor("#5677fc"), PorterDuff.Mode.SRC_ATOP);
            ((TimetableViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                    View titleView = inflater.inflate(R.layout.timetable_title, null);
                    TextView title = titleView.findViewById(R.id.title);
                    title.setText(event.Title);
                    titleView.findViewById(R.id.bg).setBackgroundColor(Color.parseColor("#5677fc"));
                    titleView.findViewById(R.id.attendance).setVisibility(View.GONE);

                    new AlertDialog.Builder(mContext)
                            .setCustomTitle(titleView)
                            .setMessage(event.Details + "\n\nLocation: " + event.Location + "\n " + event.DateTimeInfo.replace("   ", " "))
                            .setPositiveButton("Close", null)
                            .create()
                            .show();
                }
            });

            if (position == 0)
                ((TimetableViewHolder) holder).periodName.setText("Events");
            else
                ((TimetableViewHolder) holder).periodName.setText("");

            ((TimetableViewHolder) holder).attendance.setVisibility(View.GONE);

        } else {
            final int pos = position - mEvents.size();

            final TimetableObject.Class period = mClasses.get(pos);
            final GlobalObject.PeriodDefinition periodDefinition = mPeriodDefinitions.get(pos);
            ((TimetableViewHolder) holder).periodName.setText(periodDefinition.PeriodName);

            if (!period.SubjectCode.equals("")) {
                ((TimetableViewHolder) holder).title.setText(period.SubjectCode);
                ((TimetableViewHolder) holder).details.setText(String.format("%s • %s • %s", periodDefinition.PeriodTime, period.Teacher, period.Room));
                final TypedArray colors = mContext.getResources().obtainTypedArray(R.array.mdcolor_500);
                final int number = Math.abs(period.SubjectCode.hashCode()) % colors.length();
                ((TimetableViewHolder) holder).item.getBackground().mutate().setColorFilter(colors.getColor(number, Color.BLACK), PorterDuff.Mode.SRC_ATOP);

                ((TimetableViewHolder) holder).item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                        View titleView = inflater.inflate(R.layout.timetable_title, null);
                        TextView title = titleView.findViewById(R.id.title);
                        title.setText(period.SubjectCode);
                        titleView.findViewById(R.id.bg).setBackgroundColor(colors.getColor(number, Color.BLACK));

                        TextView attendance = titleView.findViewById(R.id.attendance);

                        if (period.attendance != '.') {
                            ((TimetableViewHolder) holder).attendance.setVisibility(View.VISIBLE);
                            switch (period.attendance) {
                                case 'P':
                                    attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_present));
                                    attendance.setText("Present");
                                    break;
                                case 'U':
                                    attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_unjustified));
                                    attendance.setText("Unjustified");
                                    break;
                                case 'L':
                                    attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_late));
                                    attendance.setText("Late");
                                    break;
                                case 'O':
                                    attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_overseas));
                                    attendance.setText("Overseas");
                                    break;
                                case 'J':
                                    attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_justified));
                                    attendance.setText("Justified");
                                    break;
                                default:
                                    ((TimetableViewHolder) holder).attendance.setVisibility(View.GONE);
                            }

                        }
                        else {
                            ((TimetableViewHolder) holder).attendance.setVisibility(View.GONE);
                        }
                        String endTime = "";
                        if (mPeriodDefinitions.size() != pos) {
                            if (!mPeriodDefinitions.get(pos + 1).PeriodTime.equals(""))
                                endTime = " - " + mPeriodDefinitions.get(pos + 1).PeriodTime;
                        }
                        new AlertDialog.Builder(mContext)
                                .setCustomTitle(titleView)
                                .setMessage(periodDefinition.PeriodTime + endTime + "\nTeacher: " + period.Teacher + "\nRoom: " + period.Room)
                                .setPositiveButton("Close", null)
                                .create()
                                .show();
                    }
                });
            } else {
                ((TimetableViewHolder) holder).item.setVisibility(View.GONE);
            }

            if (period.attendance != '.') {
                ((TimetableViewHolder) holder).attendance.setVisibility(View.VISIBLE);
                switch (period.attendance) {
                    case 'P':
                        ((TimetableViewHolder) holder).attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_present));
                        break;
                    case 'U':
                        ((TimetableViewHolder) holder).attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_unjustified));
                        break;
                    case 'L':
                        ((TimetableViewHolder) holder).attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_late));
                        break;
                    case 'O':
                        ((TimetableViewHolder) holder).attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_overseas));
                        break;
                    case 'J':
                        ((TimetableViewHolder) holder).attendance.setBackground(mContext.getResources().getDrawable(R.drawable.attendence_justified));
                        break;
                    default:
                        ((TimetableViewHolder) holder).attendance.setVisibility(View.GONE);
                }

                ((TimetableViewHolder) holder).attendance.setText(period.attendance + "");
            }
            else {
                ((TimetableViewHolder) holder).attendance.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {

        return TYPE_ITEM;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEvents.size() + mClasses.size();
    }

}
