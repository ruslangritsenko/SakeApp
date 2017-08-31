package com.sakewiz.android.mvp.views;

import com.sakewiz.android.model.entities.response.SearchPlacesResponse;
import com.sakewiz.android.model.entities.response.SearchProductResponse;

/**
 * Created by dilshan_e on 6/10/17.
 */

public interface SearchView  extends View{
    void showSearchPlacesResponse(SearchPlacesResponse searchPlacesResponse);
    void showSearchBreweriesResponse(SearchPlacesResponse searchPlacesResponse);
    void showSearchProductsResponse(SearchProductResponse searchProductResponse);
    void showSuggestPlacesResponse(Object response);
    void showSuggestBreweriesResponse(Object response);
    void showSuggestProductsResponse(Object response);
}
