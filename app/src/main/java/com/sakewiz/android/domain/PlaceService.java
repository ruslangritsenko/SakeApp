package com.sakewiz.android.domain;

import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;

import rx.Observable;

/**
 * Created by dilshan_e on 08/08/17.
 */

public interface PlaceService extends Service {
    Observable<FavouritePlacesResponse> getFavouritePlacesBars(String authorizationKey);
    Observable<FavouritePlacesResponse> getFavouritePlacesBreweries(String authorizationKey);
    Observable<ReviewForPlaceResponse> doUnfavourPlaceBar(String authorizationKey, String barId);
    Observable<ReviewForPlaceResponse> doUnfavourPlaceBrewery(String authorizationKey, String breweryId);
}
