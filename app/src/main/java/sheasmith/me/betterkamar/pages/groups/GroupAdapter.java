/*
 * Created by Shea Smith on 26/05/19 9:35 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 26/05/19 9:35 PM
 */

package sheasmith.me.betterkamar.pages.groups;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.GroupsViewModel;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    public List<GroupsViewModel> groups;
    private HashMap<String, String> groupHeadingsMap = new HashMap<>();
    private Context mContext;

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public RelativeLayout groupsLayout;
        public TextView title;
        public TextView heading;
        public TextView yearHeading;
        public TextView teacher;
        public TextView comment;
        public ImageView expandArrow;
        public LinearLayout subItems;

        public GroupViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            heading = mView.findViewById(R.id.heading);
            yearHeading = mView.findViewById(R.id.yearHeading);
            title = mView.findViewById(R.id.title);
            groupsLayout = mView.findViewById(R.id.group);
            expandArrow = mView.findViewById(R.id.expandArrow);
            subItems = mView.findViewById(R.id.subItem);
            teacher = mView.findViewById(R.id.teacher);
            comment = mView.findViewById(R.id.comment);
        }
    }

    public GroupAdapter(List<GroupsViewModel> resultLevels, Context context) {
        groups = resultLevels;
        mContext = context;
        groupHeadingsMap = new HashMap<>();
    }


    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_group, parent, false);
        GroupViewHolder vh = new GroupViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final GroupViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final GroupsViewModel group = groups.get(position);
        holder.heading.setVisibility(View.VISIBLE);

        if (!group.isYear) {
            holder.yearHeading.setVisibility(View.GONE);

            if (groupHeadingsMap.containsKey(group.section + group.year) && !groupHeadingsMap.get(group.section + group.year).equals(group.name))
                holder.heading.setVisibility(View.GONE);
            else {
                holder.heading.setVisibility(View.VISIBLE);

                if (!groupHeadingsMap.containsKey(group.section + group.year))
                    groupHeadingsMap.put(group.section + group.year, group.name);

                holder.heading.setText(group.section);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    holder.heading.setTextAppearance(R.style.TextAppearance_AppCompat_Subhead);
                } else {
                    holder.heading.setTextAppearance(mContext, R.style.TextAppearance_AppCompat_Subhead);
                }
            }
            holder.groupsLayout.setVisibility(View.VISIBLE);

            holder.title.setText(group.name);
            if (group.comment == null || group.comment.equals(""))
                holder.comment.setVisibility(View.GONE);
            else {
                holder.comment.setText(group.comment);
                holder.comment.setVisibility(View.VISIBLE);
            }
            holder.teacher.setText(group.teacher);

            boolean expanded = group.expanded;
            // Set the visibility based on state
            holder.subItems.setVisibility(expanded ? View.VISIBLE : View.GONE);

            if (!group.justRotated)
                holder.expandArrow.setRotation(group.angle);

            group.justRotated = false;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    group.angle = group.angle == 0 ? 180 : 0;  //toggle
                    holder.expandArrow.animate().rotation(group.angle).setDuration(300).start();

                    // Get the current state of the item
                    boolean expanded = group.expanded;
                    // Change the state
                    group.expanded = !expanded;
                    group.justRotated = true;
                    // Notify the adapter that item has changed
                    notifyItemChanged(position);
                }
            });
        }
        else {
           holder.groupsLayout.setVisibility(View.GONE);
            holder.heading.setVisibility(View.GONE);
            holder.yearHeading.setVisibility(View.VISIBLE);
            holder.yearHeading.setText(group.year);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return groups.size();
    }


}
