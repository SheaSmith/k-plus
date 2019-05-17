/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/02/19 12:54 PM
 */

package sheasmith.me.betterkamar.widgets;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by TheDiamondPicks on 18/01/2019.
 */
public class TimetableWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new TimetableWidgetRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}