package sheasmith.me.betterkamar.pages.notices;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class NoticesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<NoticesObject.GeneralNotices> generalNotices;
    private List<NoticesObject.MeetingNotices> meetingNotices;
    private Context mContext;

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mView;
        public TextView teacher;
        public TextView title;
        public TextView group;
        public TextView details;
        public TextView description;
        public LinearLayout subItems;

        public NoticeViewHolder(RelativeLayout v) {
            super(v);
            mView = v;

            teacher = (TextView) mView.findViewById(R.id.teacher);
            title = (TextView) mView.findViewById(R.id.title);
            group = (TextView) mView.findViewById(R.id.group);
            details = (TextView) mView.findViewById(R.id.details);
            description = (TextView) mView.findViewById(R.id.description);
            subItems = (LinearLayout) mView.findViewById(R.id.subItem);
        }
    }

    public NoticesAdapter(List<NoticesObject.MeetingNotices> meetings, List<NoticesObject.GeneralNotices> general, Context context) {
        meetingNotices = meetings;
        generalNotices = general;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_portal, parent, false);
            NoticeViewHolder vh = new NoticeViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            return new VHHeader(null);
        }

        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        PortalObject portal = mDataset.get(position);
        String studentPathName = mContext.getFilesDir().toString() + "/" + portal.studentFile;
        String schoolPathName = mContext.getFilesDir().toString() + "/" + portal.schoolFile;
        holder.studentPhoto.setImageDrawable(Drawable.createFromPath(studentPathName));
        holder.schoolPhoto.setImageDrawable(Drawable.createFromPath(schoolPathName));

        holder.schoolName.setText(portal.schoolName);
        holder.studentName.setText(portal.student);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return meetingNotices.size() + generalNotices.size() + 2;
    }

    private boolean isHeader(int position) {
        return position == 0 || position == meetingNotices.size();
    }

    private boolean isGeneral(int position) {
        return position > meetingNotices.size();
    }

}
