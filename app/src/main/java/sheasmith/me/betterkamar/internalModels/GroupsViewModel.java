package sheasmith.me.betterkamar.internalModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sheasmith.me.betterkamar.dataModels.GroupObject;
import sheasmith.me.betterkamar.dataModels.ResultObject;

/**
 * Created by TheDiamondPicks on 22/09/2018.
 */
public class GroupsViewModel {
    public boolean isYear = false;
    public String year;

    public String name;
    public String teacher;
    public String comment;

    public boolean expanded = false;
    public boolean justRotated = false;
    public int angle = 0;

    public GroupsViewModel(String year) {
        isYear = true;
        this.year = year;
    }

    public GroupsViewModel(GroupObject.Group group) {
        isYear = false;
        name = group.Name;
        teacher = group.Teacher;
        comment = group.Comment;
    }

    public static List<GroupsViewModel> generate(List<GroupObject.Year> years) {
        List<GroupsViewModel> groups = new ArrayList<>();
        for (GroupObject.Year year : years) {
            groups.add(new GroupsViewModel(year.Grid.replace("TT", "")));
            for (GroupObject.Group group : year.Groups) {
                groups.add(new GroupsViewModel(group));
            }
        }

        return groups;
    }
}
