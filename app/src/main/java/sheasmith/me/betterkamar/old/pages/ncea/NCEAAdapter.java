package sheasmith.me.betterkamar.old.pages.ncea;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;

public class NCEAAdapter extends ArrayAdapter<NCEAObject> {

    public NCEAAdapter(Context context, List<NCEAObject> items) {
        super(context, R.layout.activity_ncea_adapter, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_ncea_adapter, parent, false);
        }

        NCEAObject n = getItem(position);

        if (n != null) {

            ((TextView) v.findViewById(R.id.standardName)).setText(n.standard);
            ((TextView) v.findViewById(R.id.grade)).setText(n.grade);
            ((TextView) v.findViewById(R.id.credits)).setText(n.credits);

        }

        return v;
    }
}