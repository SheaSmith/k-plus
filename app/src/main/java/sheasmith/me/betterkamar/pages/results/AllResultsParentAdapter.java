/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.ResultsViewModel;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class AllResultsParentAdapter extends RecyclerView.Adapter<AllResultsParentAdapter.ParentResultViewHolder> {

    public List<ResultsViewModel> results;
    private Context mContext;

    public static class ParentResultViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public RelativeLayout resultLayout;
        public TextView title;
        public TextView heading;
        public ImageView expandArrow;
        public LinearLayout subItems;
        public RecyclerView items;

        public ParentResultViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            heading = mView.findViewById(R.id.heading);
            title = mView.findViewById(R.id.title);
            resultLayout = mView.findViewById(R.id.results);
            expandArrow = mView.findViewById(R.id.expandArrow);
            subItems = mView.findViewById(R.id.subItem);
            items = mView.findViewById(R.id.items);
        }
    }

    public AllResultsParentAdapter(List<ResultsViewModel> resultLevels, Context context) {
        results = resultLevels;
        mContext = context;
    }


    @Override
    public ParentResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_result_parent, parent, false);
        ParentResultViewHolder vh = new ParentResultViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ParentResultViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final ResultsViewModel result = results.get(position);

        if (!result.isLevel) {
            holder.heading.setVisibility(View.GONE);
            holder.resultLayout.setVisibility(View.VISIBLE);

            if (result.title.equals(""))
                holder.title.setText("Other Results");
            else
                holder.title.setText(result.title);

            boolean expanded = result.expanded;
            // Set the visibility based on state
            holder.subItems.setVisibility(expanded ? View.VISIBLE : View.GONE);

            if (!result.justRotated)
                holder.expandArrow.setRotation(result.angle);

            result.justRotated = false;

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    result.angle = result.angle == 0 ? 180 : 0;  //toggle
                    holder.expandArrow.animate().rotation(result.angle).setDuration(300).start();

                    // Get the current state of the item
                    boolean expanded = result.expanded;
                    // Change the state
                    result.expanded = !expanded;
                    result.justRotated = true;
                    // Notify the adapter that item has changed
                    notifyItemChanged(position);
                }
            });

            holder.items.setHasFixedSize(false);

            // use a linear layout manager
            LinearLayoutManager lm = new LinearLayoutManager(mContext);
            holder.items.setLayoutManager(lm);

            holder.items.setAdapter(new AllResultsChildAdapter(result.results, mContext));
        }
        else {
           holder.resultLayout.setVisibility(View.GONE);
           holder.heading.setVisibility(View.VISIBLE);

           if (result.level.equals("0"))
               holder.heading.setText("School Based Assessments");
           else
               holder.heading.setText(String.format("Level %s", result.level));
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return results.size();
    }


}
