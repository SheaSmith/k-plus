package sheasmith.me.betterkamar.pages.timetable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.CalendarObject;
import sheasmith.me.betterkamar.dataModels.GlobalObject;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.dataModels.TimetableObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class TimetableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public List<CalendarObject.Event> mEvents;
    public List<TimetableObject.Class> mClasses;
    public List<GlobalObject.PeriodDefinition> mPeriodDefinitions;
    private Context mContext;

    public static class TimetableViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mView;
        public TextView title;
        public TextView details;
        public TextView periodName;
        public LinearLayout item;

        public TimetableViewHolder(RelativeLayout v) {
            super(v);
            mView = v;

            title = mView.findViewById(R.id.title);
            details = mView.findViewById(R.id.details);
            periodName = mView.findViewById(R.id.time);
            item = mView.findViewById(R.id.item);
        }
    }

    public TimetableAdapter(List<CalendarObject.Event> events, List<TimetableObject.Class> classes, List<GlobalObject.PeriodDefinition> periodDefinitions, Context context) {
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
            final CalendarObject.Event event = mEvents.get(position);

            DateFormat timeFormat = DateFormat.getTimeInstance();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

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

            }
            else if (!event.DateTimeStart.equals("")) {
                try {
                    time = timeFormat.format(format.parse(event.DateTimeStart));

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            ((TimetableViewHolder) holder).details.setText(time);
            ((TimetableViewHolder) holder).item.getBackground().mutate().setColorFilter(Color.parseColor("#5677fc"), PorterDuff.Mode.SRC_ATOP);

            if (position == 0)
                ((TimetableViewHolder) holder).periodName.setText("Events");
            else
                ((TimetableViewHolder) holder).periodName.setText("");
        } else {
            int pos = position - mEvents.size();

            final TimetableObject.Class period = mClasses.get(pos);
            GlobalObject.PeriodDefinition periodDefinition = mPeriodDefinitions.get(pos);
            ((TimetableViewHolder) holder).periodName.setText(periodDefinition.PeriodName);

            if (!period.SubjectCode.equals("")) {
                ((TimetableViewHolder) holder).title.setText(period.SubjectCode);
                ((TimetableViewHolder) holder).details.setText(String.format("%s • %s • %s", periodDefinition.PeriodTime, period.Teacher, period.Room));
                TypedArray colors = mContext.getResources().obtainTypedArray(R.array.mdcolor_500);
                int number = Math.abs(period.SubjectCode.hashCode()) % colors.length();
                ((TimetableViewHolder) holder).item.getBackground().mutate().setColorFilter(colors.getColor(number, Color.BLACK), PorterDuff.Mode.SRC_ATOP);
            }
            else {
                ((TimetableViewHolder) holder).item.setVisibility(View.GONE);
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
