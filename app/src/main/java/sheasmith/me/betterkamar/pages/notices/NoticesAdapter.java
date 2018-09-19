package sheasmith.me.betterkamar.pages.notices;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Set;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.NoticesObject;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class NoticesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public List<NoticesObject.General> generalNotices;
    public List<NoticesObject.Meeting> meetingNotices;
    private Context mContext;
    public Set<String> enabled;

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mView;
        public TextView teacher;
        public TextView title;
        public TextView group;
        public TextView details;
        public TextView description;
        public ImageView expandArrow;
        public LinearLayout subItems;

        public NoticeViewHolder(RelativeLayout v) {
            super(v);
            mView = v;

            teacher = (TextView) mView.findViewById(R.id.teacher);
            title = (TextView) mView.findViewById(R.id.title);
            group = (TextView) mView.findViewById(R.id.group);
            details = (TextView) mView.findViewById(R.id.details);
            description = (TextView) mView.findViewById(R.id.description);
            expandArrow = (ImageView) mView.findViewById(R.id.expandArrow);
            subItems = (LinearLayout) mView.findViewById(R.id.subItem);
        }
    }

    public static class HeadingViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView heading;

        public HeadingViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            heading = (TextView) mView.findViewById(R.id.heading);
        }
    }

    public NoticesAdapter(List<NoticesObject.Meeting> meetings, List<NoticesObject.General> general, Context context, Set<String> shownGroups) {
        meetingNotices = meetings;
        generalNotices = general;
        mContext = context;
        enabled = shownGroups;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_notice, parent, false);
            NoticeViewHolder vh = new NoticeViewHolder(v);
            return vh;
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_heading, parent, false);
            HeadingViewHolder vh = new HeadingViewHolder(v);
            return vh;
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder instanceof NoticeViewHolder) {
            if (position <= meetingNotices.size()) {
                final NoticesObject.Meeting notice = meetingNotices.get(position - 1);

//                if (!enabled.contains(notice.Level)) {
//                    ((NoticeViewHolder) holder).mView.setVisibility(View.GONE);
//                    return;
//                }

                ((NoticeViewHolder) holder).title.setText(notice.Subject);
                ((NoticeViewHolder) holder).group.setText(notice.Level);
                ((NoticeViewHolder) holder).teacher.setText(notice.Teacher);
                ((NoticeViewHolder) holder).description.setText(notice.Body);
                ((NoticeViewHolder) holder).details.setText(String.format("Location: %s • When: %s", notice.PlaceMeet, notice.DateMeet));
                ((NoticeViewHolder) holder).details.setVisibility(View.VISIBLE);

                boolean expanded = notice.expanded;
                // Set the visibility based on state
                ((NoticeViewHolder) holder).subItems.setVisibility(expanded ? View.VISIBLE : View.GONE);

                if (!notice.justRotated)
                    ((NoticeViewHolder) holder).expandArrow.setRotation(notice.angle);

                notice.justRotated = false;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notice.angle = notice.angle == 0 ? 180 : 0;  //toggle
                        ((NoticeViewHolder) holder).expandArrow.animate().rotation(notice.angle).setDuration(300).start();

                        // Get the current state of the item
                        boolean expanded = notice.expanded;
                        // Change the state
                        notice.expanded = !expanded;
                        notice.justRotated = true;
                        // Notify the adapter that item has changed
                        notifyItemChanged(position);
                    }
                });
            }
            else {
                final NoticesObject.General notice = generalNotices.get(position - meetingNotices.size() - 1);

//                if (!enabled.contains(notice.Level)) {
//                    ((NoticeViewHolder) holder).mView.setVisibility(View.GONE);
//                    return;
//                }

                ((NoticeViewHolder) holder).title.setText(notice.Subject);
                ((NoticeViewHolder) holder).group.setText(notice.Level);
                ((NoticeViewHolder) holder).teacher.setText(notice.Teacher);
                ((NoticeViewHolder) holder).description.setText(notice.Body);
                ((NoticeViewHolder) holder).details.setVisibility(View.GONE);

                boolean expanded = notice.expanded;
                // Set the visibility based on state
                ((NoticeViewHolder) holder).subItems.setVisibility(expanded ? View.VISIBLE : View.GONE);

                if (!notice.justRotated)
                    ((NoticeViewHolder) holder).expandArrow.setRotation(notice.angle);

                notice.justRotated = false;

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notice.angle = notice.angle == 0 ? 180 : 0;  //toggle
                        ((NoticeViewHolder) holder).expandArrow.animate().rotation(notice.angle).setDuration(300).start();

                        // Get the current state of the item
                        boolean expanded = notice.expanded;
                        // Change the state
                        notice.expanded = !expanded;
                        notice.justRotated = true;
                        // Notify the adapter that item has changed
                        notifyItemChanged(position);
                    }
                });
            }
        }
        else if (holder instanceof HeadingViewHolder) {
            if (position == 0) {
                ((HeadingViewHolder) holder).heading.setText("Meeting Notices");
            }
            else {
                ((HeadingViewHolder) holder).heading.setText("General Notices");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return meetingNotices.size() + generalNotices.size() + 1;
    }

    private boolean isHeader(int position) {
        return position == 0 || position == meetingNotices.size();
    }

}
