package sheasmith.me.betterkamar.pages.pathways;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;

public class PathwaysAdapter extends ArrayAdapter<PathwaysObject> {

    public PathwaysAdapter(Context context, List<PathwaysObject> items) {
        super(context, R.layout.activity_ncea_adapter, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_pathways_adapter, parent, false);
        }

        PathwaysObject n = getItem(position);

        if (n != null) {
            ((TextView) v.findViewById(R.id.standardName)).setText(n.title);
            ((TextView) v.findViewById(R.id.standardsVersionLevel)).setText("Version " + n.version + ", Level " + n.level);
            ((TextView) v.findViewById(R.id.standardNumber)).setText("#" + n.standardNumber);
            ((TextView) v.findViewById(R.id.standardCredits)).setText(n.credits + " Credits");
            ((TextView) v.findViewById(R.id.standardGrade)).setText(n.result);

            for (String s : n.pathways){
                ImageView dot = (ImageView) v.findViewWithTag(s);
                dot.setVisibility(View.VISIBLE);
            }

        }

        return v;
    }
}