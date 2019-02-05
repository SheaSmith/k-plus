/*
 * Created by Shea Smith on 6/02/19 12:54 PM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:53 PM
 */

package sheasmith.me.betterkamar.pages.results;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.dataModels.NZQAObject;

import static android.view.View.GONE;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class NZQAAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public List<NZQAObject.Qualification> codeQ;
    public List<NZQAObject.Qualification> codeO;
    public List<NZQAObject.Qualification> codeC;
    private Context mContext;

    public static class QualificationViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView title;
        public TextView endorsement;
        public TextView year;
        public TextView level;

        public QualificationViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            title = mView.findViewById(R.id.title);
            endorsement = mView.findViewById(R.id.endorsement);
            year = mView.findViewById(R.id.year);
            level = mView.findViewById(R.id.level);
        }
    }

    public static class HeadingViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public LinearLayout mView;
        public TextView heading;

        public HeadingViewHolder(LinearLayout v) {
            super(v);
            mView = v;

            heading = mView.findViewById(R.id.heading);
        }
    }

    public NZQAAdapter(List<NZQAObject.Qualification> q, List<NZQAObject.Qualification> o, List<NZQAObject.Qualification> c, Context context) {
        codeQ = q;
        codeO = o;
        codeC = c;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.requireContext())
                    .inflate(R.layout.adapter_nzqa, parent, false);
            return new QualificationViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.requireContext())
                    .inflate(R.layout.adapter_nzqa_heading, parent, false);
            return new HeadingViewHolder(v);
        }
        return null;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if (holder instanceof QualificationViewHolder) {
            NZQAObject.Qualification qualification;
            try {
                if (position <= codeQ.size())
                    qualification = codeQ.get(position - 1);
                else if (position <= codeO.size() + 3)
                    qualification = codeO.get(position - codeQ.size() - 2);
                else
                    qualification = codeC.get(position - codeQ.size() - codeO.size() - 3);

                ((QualificationViewHolder) holder).title.setText(qualification.Title);
                ((QualificationViewHolder) holder).year.setText(qualification.Year);

                if (qualification.Level.equals(""))
                    ((QualificationViewHolder) holder).level.setVisibility(GONE);
                else {
                    ((QualificationViewHolder) holder).level.setVisibility(View.VISIBLE);
                    ((QualificationViewHolder) holder).level.setText(String.format("Level %s", qualification.Level));
                }

                if (qualification.Endorse.equals(""))
                    ((QualificationViewHolder) holder).endorsement.setVisibility(GONE);
                else {
                    ((QualificationViewHolder) holder).endorsement.setVisibility(View.VISIBLE);
                    ((QualificationViewHolder) holder).endorsement.setText(String.format("Endorsed with %s", qualification.Endorse));
                }
            }
            catch (IndexOutOfBoundsException e) {
                Crashlytics.logException(e);
                Crashlytics.setInt("q", codeQ.size());
                Crashlytics.setInt("o", codeO.size());
                Crashlytics.setInt("c", codeC.size());
                Crashlytics.setInt("pos", position);
            }


        }
        else if (holder instanceof HeadingViewHolder) {
            if (position == 0) {
                ((HeadingViewHolder) holder).heading.setText("National Certificates");
            }
            else if (position == codeQ.size() + 1) {
                ((HeadingViewHolder) holder).heading.setText("Other Qualifications");
            }
            else {
                ((HeadingViewHolder) holder).heading.setText("Course Endorsements");
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
        return codeQ.size() + codeO.size()+ codeC.size() + 3;
    }

    private boolean isHeader(int position) {
        return position == 0 || position == codeQ.size() + 1 || position == codeQ.size() + codeO.size() + 2;
    }

}
