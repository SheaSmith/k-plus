/*
 * Created by Shea Smith on 18/05/19 9:45 AM
 * Copyright (c) 2016 -  2019 Shea Smith. All rights reserved.
 * Last modified 6/09/18 6:45 PM
 */

package sheasmith.me.betterkamar.internalModels;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public interface ApiResponse<T> {
    void success(T value);

    void error(Exception e);
}
