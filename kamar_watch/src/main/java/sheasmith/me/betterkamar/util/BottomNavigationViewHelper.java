/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 14/03/19 6:41 PM
 */

package sheasmith.me.betterkamar.util;

import android.annotation.SuppressLint;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;

/**
 * Created by TheDiamondPicks on 15/09/2018.
 */
public class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        view.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//                //noinspection RestrictedApi
//                item.setShiftingMode(false);
//                // set once again checked value, so view will be updated
//                //noinspection RestrictedApi
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e("BottomNav", "Unable to get shift mode field", e);
//        } catch (IllegalAccessException e) {
//            Log.e("BottomNav", "Unable to change value of shift mode", e);
//        }
    }
}
