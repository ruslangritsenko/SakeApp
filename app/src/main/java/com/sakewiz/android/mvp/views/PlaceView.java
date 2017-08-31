package com.sakewiz.android.mvp.views;

import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.FavouriteSakeResponse;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;

/**
 * Created by Wang Xiaoming on 08/08/17.
 */

public interface PlaceView extends View {
    void showGetFavouritePlacesBarsResponse(FavouritePlacesResponse favouritePlacesResponse);
    void showGetFavouritePlacesBreweriesResponse(FavouritePlacesResponse favouritePlacesResponse);
    void showDoUnFavourPlaceBarResponse(ReviewForPlaceResponse reviewForPlaceResponse);
    void showDoUnFavourPlaceBreweryResponse(ReviewForPlaceResponse reviewForPlaceResponse);
}
