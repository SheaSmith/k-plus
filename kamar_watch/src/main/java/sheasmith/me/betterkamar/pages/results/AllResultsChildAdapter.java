/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 1:04 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.ResultObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class AllResultsChildAdapter extends RecyclerView.Adapter<AllResultsChildAdapter.ChildResultViewHolder> {

    public List<ResultObject.Result> results;
    private Context mContext;

    public static class ChildResultViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView title;
        public TextView version;
        public TextView date;
        public TextView result;
        public TextView credits;

        public ChildResultViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            title = mView.findViewById(R.id.title);
            version = mView.findViewById(R.id.version);
            result = mView.findViewById(R.id.result);
            date = mView.findViewById(R.id.date);
            credits = mView.findViewById(R.id.credits);
        }
    }

    public AllResultsChildAdapter(List<ResultObject.Result> result, Context context) {
        results = result;
        mContext = context;
    }


    @Override
    public ChildResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_result_child, parent, false);
        ChildResultViewHolder vh = new ChildResultViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ChildResultViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


        final ResultObject.Result result = results.get(position);
        holder.title.setText(result.Title);
        holder.version.setText(String.format("%s ver. %s", result.Number, result.Version));
        if (!result.ResultPublished.equals("")) {
            holder.date.setVisibility(View.VISIBLE);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(result.ResultPublished);
                Calendar c = Calendar.getInstance();
                c.setTime(date);

                String dateString = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);
                holder.date.setText(String.format("Date Added: %s", dateString));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            holder.date.setVisibility(View.GONE);
        }

        if (!result.Grade.equals("")) {
            holder.result.setVisibility(View.VISIBLE);
            holder.result.setText(result.Grade.substring(0, 1).toUpperCase() + result.Grade.substring(1));
            if (result.Grade.equals("Achieved with Excellence"))
                holder.result.setBackgroundColor(mContext.getResources().getColor(R.color.excellence));
            else if (result.Grade.equals("Achieved with Merit"))
                holder.result.setBackgroundColor(mContext.getResources().getColor(R.color.merit));
            else if (result.Grade.equals("Achieved"))
                holder.result.setBackgroundColor(mContext.getResources().getColor(R.color.achieved));
            else if (result.Grade.equals("Not Achieved"))
                holder.result.setBackgroundColor(mContext.getResources().getColor(R.color.notachieved));
        }
        else {
            holder.result.setVisibility(View.GONE);
        }

        if (!result.Credits.equals("")) {
            holder.credits.setVisibility(View.VISIBLE);
            holder.credits.setText(String.format("%s credits", result.Credits));
        }
        else
            holder.credits.setVisibility(View.GONE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return results.size();
    }


}
