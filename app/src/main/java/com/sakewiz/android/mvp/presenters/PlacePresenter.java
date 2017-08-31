package com.sakewiz.android.mvp.presenters;

/**
 * Created by Wang Xiaoming on 08/08/17.
 */

public interface PlacePresenter extends Presenter {
    void getFavouritePlacesBars();
    void getFavouritePlacesBreweries();
    void doUnFavouritePlacesBar(String barId);
    void doUnFavouritePlacesBrewery(String breweryId);
}