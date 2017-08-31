package com.sakewiz.android.domain;


import com.sakewiz.android.model.entities.response.FavouritePlacesResponse;
import com.sakewiz.android.model.entities.response.ReviewForPlaceResponse;
import com.sakewiz.android.model.rest.SakeWizService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Wang Xiaoming on 08/08/17.
 */

public class PlaceServiceImpl implements PlaceService {

    private SakeWizService sakeWizService;

    public PlaceServiceImpl(SakeWizService nzsService) {
        super();
        this.sakeWizService = nzsService;
    }

    @Override
    public Observable<FavouritePlacesResponse> getFavouritePlacesBars(String authorizationKey) {
        return sakeWizService.getApi()
                .getFavouritePlaceBarsAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FavouritePlacesResponse> getFavouritePlacesBreweries(String authorizationKey) {
        return sakeWizService.getApi()
                .getFavouritePlaceBreweriesAPI(authorizationKey)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForPlaceResponse> doUnfavourPlaceBar(String authorizationKey, String barId) {
        return sakeWizService.getApi()
                .doUnFavourPlaceBarAPI(authorizationKey, barId)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ReviewForPlaceResponse> doUnfavourPlaceBrewery(String authorizationKey, String breweryId) {
        return sakeWizService.getApi()
                .doUnFavourPlaceBreweryAPI(authorizationKey, breweryId)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
