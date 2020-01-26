/*
 * Created by Shea Smith on 26/01/20 6:49 PM
 * Copyright (c) 2016 -  2020 Shea Smith. All rights reserved.
 * Last modified 3/06/19 12:42 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.htmlModels.ReportsObject;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    public List<ReportsObject> reports;
    private Context mContext;
    private PortalObject mPortal;

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView title;
        public TextView date;

        public ReportViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            title = mView.findViewById(R.id.title);
            date = mView.findViewById(R.id.date);
        }
    }

    public ReportAdapter(List<ReportsObject> reports, Context context, PortalObject portalObject) {
        this.reports = reports;
        mContext = context;
        mPortal = portalObject;
    }


    @Override
    public ReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reports, parent, false);
        ReportViewHolder vh = new ReportViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ReportViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final ReportsObject report = reports.get(position);
        holder.title.setText(report.title);
        holder.date.setText(report.date);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, PDFViewer.class);
                i.putExtra("portal", mPortal);
                i.putExtra("report", report);
                mContext.startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return reports.size();
    }


}
