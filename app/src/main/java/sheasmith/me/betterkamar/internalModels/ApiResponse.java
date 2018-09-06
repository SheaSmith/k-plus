package sheasmith.me.betterkamar.internalModels;

/**
 * Created by TheDiamondPicks on 6/09/2018.
 */

public interface ApiResponse<T> {
    void success(T value);

    void error(Exception e);
}
