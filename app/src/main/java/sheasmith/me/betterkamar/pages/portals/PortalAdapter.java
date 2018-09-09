package sheasmith.me.betterkamar.pages.portals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;
import sheasmith.me.betterkamar.internalModels.PortalObject;

/**
 * Created by TheDiamondPicks on 9/09/2018.
 */

public class PortalAdapter extends RecyclerView.Adapter<PortalAdapter.PortalViewHolder> {

    private List<PortalObject> mDataset;
    private Context mContext;

    public static class PortalViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout mView;
        public ImageView studentPhoto;
        public ImageView schoolPhoto;
        public TextView schoolName;
        public TextView studentName;

        public PortalViewHolder(RelativeLayout v) {
            super(v);
            mView = v;

            studentPhoto = (ImageView) mView.findViewById(R.id.studentImage);
            schoolPhoto = (ImageView) mView.findViewById(R.id.schoolLogo);
            schoolName = (TextView) mView.findViewById(R.id.schoolName);
            studentName = (TextView) mView.findViewById(R.id.studentName);
        }
    }

    public PortalAdapter(List<PortalObject> servers, Context context) {
        mDataset = servers;
        mContext = context;
    }


    @Override
    public PortalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout v = (RelativeLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_portal, parent, false);
        PortalViewHolder vh = new PortalViewHolder(v);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PortalViewHolder holder, int position) {
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
        return mDataset.size();
    }

}
