package sheasmith.me.betterkamar.pages.notices;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;

public class NoticesAdapter extends ArrayAdapter<NoticesObject> {

    public NoticesAdapter(Context context, List<NoticesObject> items) {
        super(context, R.layout.activity_notices_adapter, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_notices_adapter, parent, false);
        }

        NoticesObject n = getItem(position);

        if (n != null) {

            TextView body = (TextView) v.findViewById(R.id.Body);
            TextView subject = (TextView) v.findViewById(R.id.standardName);
            TextView meet = (TextView) v.findViewById(R.id.meet);
            TextView teacher = (TextView) v.findViewById(R.id.description);
            TextView group = (TextView) v.findViewById(R.id.Group);

            body.setText(n.body);
            subject.setText(n.subject);
            if (n.meet == null) {
                meet.setVisibility(View.GONE);
            }
            else {
                meet.setText(n.meet);
            }
            teacher.setText(n.teacher);
            group.setText(n.level);



        }

        return v;
    }
}
