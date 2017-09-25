package sheasmith.me.betterkamar.pages.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sheasmith.me.betterkamar.R;

public class GroupsAdapter extends ArrayAdapter<GroupsObject> {

    public GroupsAdapter(Context context, List<GroupsObject> items) {
        super(context, R.layout.activity_groups_adapter, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = LayoutInflater.from(getContext()).inflate(R.layout.activity_groups_adapter, parent, false);
        }

        GroupsObject n = getItem(position);

        if (n != null) {

            TextView name = (TextView) v.findViewById(R.id.standardName);
            TextView teacher = (TextView) v.findViewById(R.id.description);
            TextView leftComment = (TextView) v.findViewById(R.id.leftComment);
            TextView rightComment = (TextView) v.findViewById(R.id.rightComment);

            name.setText(n.title);
            teacher.setText(n.teacher);
            if (n.leftComment.isEmpty() && n.rightComment.isEmpty()) {
                leftComment.setVisibility(View.GONE);
                rightComment.setVisibility(View.GONE);
            }
            else {
                leftComment.setText(n.leftComment);
                rightComment.setText(n.rightComment);
            }








        }

        return v;
    }
}