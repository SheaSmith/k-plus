package sheasmith.me.betterkamar.old.pages.calender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;

import static android.view.View.GONE;

public class CalenderAdapter extends ArrayAdapter<CalenderObject> {

    public CalenderAdapter(Context context, List<CalenderObject> items) {
        super(context, R.layout.activity_groups_adapter, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_calender_adapter, parent, false);
        }

        CalenderObject n = getItem(position);

        if (n != null) {
            ((TextView) v.findViewById(R.id.standardName)).setText(n.dateRaw);
            ((TextView) v.findViewById(R.id.description)).setText(n.description);
            if (n.location != null)
            ((TextView) v.findViewById(R.id.location)).setText(n.location);
            else
                v.findViewById(R.id.location).setVisibility(GONE);
        }

        return v;
    }
}