package com.sakewiz.android.mvp.presenters;

import com.sakewiz.android.model.entities.request.SearchPlacesRequest;
import com.sakewiz.android.model.entities.request.SearchProductsRequest;

/**
 * Created by dilshan_e on 6/10/17.
 */
public interface SearchPresenter extends Presenter {
    void doSearchPlaces(int page, int size, SearchPlacesRequest searchPlacesRequest);
    void doSearchBreweries(int page, int size, SearchPlacesRequest searchPlacesRequest);
    void doSearchProducts(int page, int size, SearchProductsRequest searchProductsRequest);
    void getSuggestPlaces(String text, int count);
    void getSuggestBreweries(String text, int count);
    void getSuggestProducts(String text, int count);
}
